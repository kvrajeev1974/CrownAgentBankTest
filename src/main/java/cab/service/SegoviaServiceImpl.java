package cab.service;

import cab.entity.PaymentRequest;
import cab.entity.PaymentResponse;
import cab.external.client.SegoviaWebClient;
import cab.response.RequestStatus;
import cab.response.SimpleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

@Service
@Slf4j
public class SegoviaServiceImpl implements SegoviaService {

    @Autowired
    CSVService csvService;

    @Autowired
    Executor taskExecutor;

    @Autowired
    private SegoviaWebClient webclient;

    @Autowired
    protected AsyncJobsManager asyncJobsManager;

    @Override
    public List<String> callService(MultipartFile file) {
        List<String> responseList = new ArrayList<>();
        List<PaymentRequest> request = csvService.parseCsvToCase(file);
        request.forEach(lst -> {
            String jobId = UUID.randomUUID().toString();
            log.info("Generated job-id {} for request {}", jobId, lst.getMsisdn());
            responseList.add(jobId);
            var currentRequestTime = OffsetDateTime.now().toLocalDateTime();
            CompletableFuture
                    .supplyAsync(() -> {
                        var response = webclient.makePayment(lst);
                        response.setTimestamp(currentRequestTime.toString());
                        try {
                            var simpleResponse = new SimpleResponse(jobId, RequestStatus.IN_PROGRESS, response);
                            asyncJobsManager.putJob(jobId, simpleResponse);
                            return simpleResponse;
                        } catch (ExecutionException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }, taskExecutor)
                    .exceptionally(throwable -> {
                        log.error("inside exceptionally block");
                        SimpleResponse simpleResponse = null;
                        try {
                            PaymentResponse response = PaymentResponse.builder().message(throwable.getMessage()).build();
                            response.setTimestamp(currentRequestTime.toString());
                            simpleResponse = new SimpleResponse(jobId, RequestStatus.ERROR, response);
                            asyncJobsManager.putJob(jobId, simpleResponse);
                        } catch (ExecutionException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        return simpleResponse;
                    })
                    .thenApply(response -> {
                        try {
                            var simpleResponse = response;
                            if (response.getRequestStatus().value() != RequestStatus.ERROR.value()) {
                                simpleResponse = new SimpleResponse(jobId, RequestStatus.COMPLETE, response.getResponse());
                                asyncJobsManager.putJob(jobId, simpleResponse);
                            }
                            return simpleResponse;
                        } catch (ExecutionException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
        });
        return responseList;
    }

    @Override
    public SimpleResponse getJobStatus(String jobId) {
        return asyncJobsManager.getJob(jobId);
    }
}

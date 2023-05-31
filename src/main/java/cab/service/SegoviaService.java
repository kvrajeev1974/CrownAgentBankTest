package cab.service;

import cab.entity.PaymentRequest;
import cab.entity.PaymentResponse;
import cab.response.SimpleResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SegoviaService {
    public List<String> callService(MultipartFile file);

    public SimpleResponse getJobStatus(String jobId);
}

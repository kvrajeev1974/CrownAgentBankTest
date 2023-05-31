package cab.response;

import cab.entity.PaymentResponse;
import lombok.*;

import java.util.concurrent.ExecutionException;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@AllArgsConstructor
public class SimpleResponse {

    private static final long serialVersionUID = 6248820308968669361L;

    private String jobId;

    private PaymentResponse response;

    private RequestStatus requestStatus;

    public SimpleResponse(String jobId, RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
        this.jobId = jobId;
    }

    public SimpleResponse(String jobId, RequestStatus requestStatus, PaymentResponse response) throws ExecutionException, InterruptedException {
        this.requestStatus = requestStatus;
        this.response = response;
        this.jobId = jobId;
    }
}


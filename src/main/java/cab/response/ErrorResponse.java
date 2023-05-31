package cab.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ErrorResponse {

    private static final long serialVersionUID = -7788642488158291220L;

    private String errorMessage;
    private String requestUri;

    private RequestStatus requestStatus;

    public ErrorResponse() {
    }

    public ErrorResponse(String errorMessage, String requestUri, RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
        this.errorMessage = errorMessage;
        this.requestUri = requestUri;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ErrorResponse [errorMessage=").append(errorMessage).append(", requestUri=").append(requestUri)
                .append("]");
        return builder.toString();
    }
}

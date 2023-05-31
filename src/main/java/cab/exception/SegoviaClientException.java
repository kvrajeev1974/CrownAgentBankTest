package cab.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SegoviaClientException extends RuntimeException {
    public SegoviaClientException(String errorBody) {
        log.debug(errorBody);
    }
}

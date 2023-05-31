package cab.api;

import cab.response.SimpleResponse;
import cab.service.SegoviaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@Slf4j
public class CABPaymentController {
    @Autowired
    private SegoviaService segoviaService;

    @PostMapping(value="/pay", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity handlePayments(@RequestParam("file") MultipartFile file)
            throws Throwable {
        log.info("Received request for asynchronous file processing.");
        if (file.isEmpty()) {
            throw new Exception("invalid csv file");
        }
        var responseList = segoviaService.callService(file);
        return ResponseEntity.status(HttpStatus.OK).body("Payment file processed successfully. \nJob IDs\n" + new ObjectMapper().writeValueAsString(responseList));
    }

    @GetMapping(path = "/status/{id}", produces = "application/json")
    public SimpleResponse getJobStatus(@PathVariable(name = "id") String jobId) {
        log.debug("Received request to fetch status of job-id: {}", jobId);
        return segoviaService.getJobStatus(jobId);
    }
}

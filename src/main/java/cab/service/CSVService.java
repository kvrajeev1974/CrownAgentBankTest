package cab.service;

import cab.entity.PaymentRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CSVService {
    public List<PaymentRequest> parseCsvToCase(MultipartFile file);
}

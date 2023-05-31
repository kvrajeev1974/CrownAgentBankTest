package cab.service;

import cab.entity.PaymentRequest;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Service
public class CSVServiceImpl implements CSVService {

    @Autowired
    private HeaderColumnNameTranslateMappingStrategy strategy;

    @Override
    public List<PaymentRequest> parseCsvToCase(MultipartFile file) {
        List<PaymentRequest> requestBeans = new CsvToBeanBuilder(getReader(file))
                .withType(PaymentRequest.class)
                .build()
                .parse();
        return requestBeans;
    }

    private static Reader getReader(MultipartFile file) {
        try {
            return new BufferedReader(new InputStreamReader(file.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

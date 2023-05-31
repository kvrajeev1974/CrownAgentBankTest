package cab.integration;

import cab.service.SegoviaService;
import org.awaitility.Duration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles(value = "test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CABPaymentIT {

    @Autowired
    private SegoviaService segoviaService;

    @Test
    public void testPaymentService() throws IOException, InterruptedException {
        Resource resource = new ClassPathResource("payments.csv");
        assertNotNull(resource);

        MockMultipartFile file = new MockMultipartFile("file", resource.getFilename(), "text/csv", resource.getInputStream());
        var list = segoviaService.callService(file);
        assertNotNull(list);

        await().atMost(Duration.ONE_MINUTE).untilAsserted(() -> Assertions.assertNotNull(segoviaService.getJobStatus(list.get(0))));
    }

}

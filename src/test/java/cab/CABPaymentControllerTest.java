package cab;

import cab.response.SimpleResponse;
import cab.service.SegoviaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(excludeAutoConfiguration = SecurityAutoConfiguration.class)
class CABPaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SegoviaService segoviaService;

    @Test
    void shouldReturnJobStatusForJobId() throws Exception {
        SimpleResponse response = SimpleResponse.builder().jobId("722b1d3e").build();

        when(segoviaService.getJobStatus("722b1d3e"))
                .thenReturn(response);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/status/722b1d3e"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.jobId").value("722b1d3e"));
    }

    @Test
    void submitPaymentRequest() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "payment.csv", "text/csv", "12125551000,Rec1,10,KES".getBytes());

        List<String> list = Lists.list();

        when(segoviaService.callService(file))
                .thenReturn(list);

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/api/pay")
                        .file("file", file.getBytes())
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment file processed successfully. \nJob IDs\n" + new ObjectMapper().writeValueAsString(list)));

    }
}
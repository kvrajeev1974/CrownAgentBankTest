package cab.entity;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    @CsvBindByName(column = "ID")
    private String conversationID;

    @CsvBindByName(column = "Server-generated ID")
    private String serverId;

    @CsvBindByName(column = "Status")
    private String status;

    @CsvBindByName(column = "Fee")
    private int fee;

    @CsvBindByName(column = "Details")
    private String message;

    @CsvBindByName(column = "TimeStamp")
    private String timestamp;

    @CsvBindByName(column = "Reference")
    private String reference;

    @CsvBindByName(column = "Customer Reference")
    private String customerReference;

}

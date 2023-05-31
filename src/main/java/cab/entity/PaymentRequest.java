package cab.entity;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Builder(toBuilder = true)
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    @CsvBindByName(column = "ID", required=true)
    private String msisdn;

    @CsvBindByName(column = "Recipient", required=true)
    private String recipient;

    @CsvBindByName(column = "Amount", required=true)
    private Double amount;

    @CsvBindByName(column = "Currency", required=true)
    private String currency;
}

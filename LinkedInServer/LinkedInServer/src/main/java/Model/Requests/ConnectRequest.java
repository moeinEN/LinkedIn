package Model.Requests;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConnectRequest {
    private int identificationCode;
    private String note;
}

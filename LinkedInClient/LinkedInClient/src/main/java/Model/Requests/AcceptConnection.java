package Model.Requests;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AcceptConnection {
    private Boolean acceptOrDecline;
    private int connectionIdentification;
}

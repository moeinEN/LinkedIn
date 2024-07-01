package Model.Requests;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WatchPendingConnectionListRequest {
    private int myProfileId;
}

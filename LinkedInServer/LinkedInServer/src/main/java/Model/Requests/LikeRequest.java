package Model.Requests;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LikeRequest {
    private int postIdentification;
    private Boolean likeOrDislike = false;
}

package Model.Requests;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentRequest {
    private String comment;
    private String postIdentification;
}

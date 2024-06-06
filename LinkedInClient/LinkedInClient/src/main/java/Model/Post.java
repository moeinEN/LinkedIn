package Model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Post {
    private String text;
    private Like likes;
    private Comment comments;
    private int identification;
}

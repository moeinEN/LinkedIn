package Model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    List<String> hashtags = new ArrayList<>();

    //TODO edit post queries
}

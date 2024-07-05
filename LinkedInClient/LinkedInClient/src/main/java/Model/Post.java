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
    private String mediaName;
    private int identification;
    private MiniProfile miniProfile;
    List<String> hashtags = new ArrayList<>();
}

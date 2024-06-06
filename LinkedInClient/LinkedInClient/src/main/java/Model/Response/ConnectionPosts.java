package Model.Response;

import Model.Post;
import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConnectionPosts {
    private List<Post> posts;
}

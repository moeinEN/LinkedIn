package Model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Feed {
    private List<Post> posts = new ArrayList<Post>();
}

package Model;

import lombok.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Like {
    private Set<MiniProfile> likedUsers = new HashSet<>();
}

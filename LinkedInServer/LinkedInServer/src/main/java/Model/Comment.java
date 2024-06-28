package Model;

import lombok.*;

import java.util.HashMap;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Comment {
    private HashMap<MiniProfile, HashMap<Integer, String>> commentedUsers = new HashMap<>();
}
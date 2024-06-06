package Model.Response;

import Model.MiniProfile;
import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WatchProfileSearchResults {
    private List<MiniProfile> miniProfiles;
}

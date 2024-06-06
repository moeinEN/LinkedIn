package Model.Response;

import Model.MiniProfile;
import lombok.*;

import java.util.HashMap;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WatchConnectionPendingLists {
    HashMap<MiniProfile, String> pendingLists;
}

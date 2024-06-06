package Model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MiniProfile {
    private String firstName;
    private String lastName;
    private String imageURL;
    private int identification;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MiniProfile that = (MiniProfile) o;
        return identification == that.identification;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(identification);
    }
}

package classes;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode
public class Type {
    private String typeName;
    private double multiplier;
    private Map<String, MatchUp> combatList;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private byte[] icon;
}

enum MatchUp {
    weak, neutral, strong
}




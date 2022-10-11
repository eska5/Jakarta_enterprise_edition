package classes;

import java.util.List;
import java.util.Map;

public class Type {
    private String typeName;
    private List<Pokemon> pokemonList;
    private byte[] icon;
    private double multiplier;
    private Map<Type, MatchUp> combatList;
}

enum MatchUp {
    weak, neutral, strong
}




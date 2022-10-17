package classes;

import java.util.Date;

public class Pokemon {
    private String name;
    private Date captureDate;
    private Double power;
    private Type type;
    private byte[] photo;
}

enum Rarity {
    common, uncommon, rare, epic, legendary
}
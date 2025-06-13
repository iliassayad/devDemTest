package net.ayad.devdemtest.model;

public enum Formule {
    ECONOMIC("Économique"),
    ECONOMIC_PLUS("Économique plus"),
    SOLO("Solo"),
    CONFORT("Confort"),
    LUXE("Clé en main");

    private String label;
    private Formule(String label) {
        this.label = label;
    }

    public String getLibelle() {
        return label;
    }

}

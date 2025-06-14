package net.ayad.devdemtest.model;

public enum DevisStatus {
    BROUILLON("Brouillon"),
    ENVOYE("Envoyé"),
    ACCEPTE("Accepté"),
    REFUSE("Refusé"),
    EXPIRE("Expiré");

    private final String label;

    DevisStatus(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }

    public String getLabel() {
        return label;
    }
}

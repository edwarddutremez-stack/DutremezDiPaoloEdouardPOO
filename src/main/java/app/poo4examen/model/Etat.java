package app.poo4examen.model;

public enum Etat {
    TO_DO("To do"),
    DOING("Doing"),
    DONE("Done");

    private final String label;

    Etat(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Etat fromString(String text) {
        for (Etat e : Etat.values()) {
            if (e.label.equalsIgnoreCase(text)) {
                return e;
            }
        }
        return TO_DO;
    }
}
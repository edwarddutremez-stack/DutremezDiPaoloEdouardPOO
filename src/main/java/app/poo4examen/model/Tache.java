package app.poo4examen.model;

public class Tache {
    private String nom;
    private String personne;
    private Etat etat;
    private int priorite;

    public Tache() {}

    public Tache(String nom, String personne, Etat etat, int priorite) {
        this.nom = nom;
        this.personne = personne;
        this.etat = etat;
        this.priorite = priorite;
    }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPersonne() { return personne; }
    public void setPersonne(String personne) { this.personne = personne; }

    public Etat getEtat() { return etat; }
    public void setEtat(Etat etat) { this.etat = etat; }

    public int getPriorite() { return priorite; }
    public void setPriorite(int priorite) { this.priorite = priorite; }
}
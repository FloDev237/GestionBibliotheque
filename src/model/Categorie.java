package model;

public class Categorie {
    private int idCategorie;
    private String nom;
    private String description;

    // Constructeur vide
    public Categorie() {
        this.idCategorie = 0;
        this.nom = "";
        this.description = "";
    }

    // Constructeur paramétré
    public Categorie(int idCategorie, String nom, String description) {
        this.idCategorie = idCategorie;
        this.nom = nom;
        this.description = description;
    }

    // Getters
    public int getIdCategorie() { return idCategorie; }
    public String getNom() { return nom; }
    public String getDescription() { return description; }

    // Setters
    public void setIdCategorie(int idCategorie) { this.idCategorie = idCategorie; }
    public void setNom(String nom) { this.nom = nom; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "Categorie{id=" + idCategorie 
             + ", nom='" + nom 
             + "', description='" + description + "'}";
    }
}


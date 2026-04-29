package model;

public class Categorie{
    private int idCategorie;
    private String nom;
    private String description;

    //constructeurs
    public Categorie(){
        this.idCategorie = idCategorie;
        this.nom = nom;
        this.description = description;
    }

    public Categorie(int idCategorie, String nom, String description){
        this.idCategorie = idCategorie;
        this.nom = nom;
        this.description = description;
    }

    //getters
    public int getIdCategorie(){ return idCategorie; }
    public String getNom(){ return nom; }
    public String getDescription(){ return description; }

    //setters
    public void setIdCategorie(int idCat){ this.idCategorie = idCat; }
    public void setNom(String name){ this.nom = name; }
    public void setDescription (String descript){ this.description = descript; }
}

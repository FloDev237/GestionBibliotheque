package model;

public class Livre {
    private int idLivre;
    private String titre;
    private String auteur;
    private int quantite;
    private String isbn;
    private int anneePublication;
    public Livre(int idLivre, String titre, String auteur, int quantite, String isbn, int anneePublication) {
        this.idLivre = idLivre;
        this.titre = titre;
        this.auteur = auteur;
        this.quantite = quantite;
        this.anneePublication = anneePublication;
        this.isbn = isbn;
    }
    public int getId(){
        return idLivre;
    }
    public String getTitre(){
        return titre;
    }
    public String getAuteur(){
        return auteur;
    }
    public int getQuantite(){
        return quantite;
    }
    public int getAnneepublication(){
        return anneePublication;
    }
    public String getIsbn(){
        return isbn;
    }
    public boolean isDisponible(){
        return quantite > 0;
    }
    public void emprunter(){
        if (isDisponible()) {
            quantite--;
        } else {
            System.out.println("Livre non disponible pour emprunt.");
        }
    }
    public void retourner(){
        quantite++;
    }
    @Override
    public String toString(){
        return "Livre{" +
                "idLivre=" + idLivre +
                ", titre='" + titre + '\'' +
                ", auteur='" + auteur + '\'' +
                ", quantite=" + quantite +
                ", anneePublication=" + anneePublication +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}

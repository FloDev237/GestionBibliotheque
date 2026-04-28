package model;

public class Livre {
    private int id;
    private String titre;
    private String auteur;
    private int quantite;
    private String isbn;
    public Livre(int id, String titre, String auteur, int quantite, String isbn) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.quantite = quantite;
        this.isbn = isbn;
    }
    public int getId(){
        return id;
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
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", auteur='" + auteur + '\'' +
                ", quantite=" + quantite +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}

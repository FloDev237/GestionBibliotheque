/*cd C:\Users\JERRY DON\GestionBibliotheque
javac -d bin src\model\*.java src\service\*.java
java -cp bin model.Livre */
package model;

import service.BibliothequeService;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Livre {
    private int idLivre;
    private String titre;
    private String auteur;
    private int annee;
    private int quantite;
    private boolean disponibilite;
    private String isbn;

    //constructeur
    public Livre(int idLivre, String titre, String auteur, int annee, int quantite, boolean disponibilite, String isbn) {
        this.idLivre = idLivre;
        this.titre = titre;
        this.auteur = auteur;
        this.annee = annee;
        this.quantite = quantite;
        this.disponibilite = disponibilite;
        this.isbn = isbn;
    }

    //getters
    public int getId(){ return idLivre; }
    public String getTitre(){ return titre; }
    public String getAuteur(){ return auteur; }
    public int getQuantite(){ return quantite; }
    public int getAnnee(){ return annee; }
    public String getIsbn(){ return isbn; }
    public boolean getDisponibilite(){ return disponibilite; }

    //setters
    public void setIdLivre(int idLivre) { this.idLivre = idLivre; }
    public void setTitre(String titre) { this.titre = titre; }
    public void setAuteur(String nomAuteur) {  this.auteur = nomAuteur; }
    public void setAnnee(int anneePublication) { this.annee = anneePublication; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public void setDisponibilite(boolean disponibilite) { this.disponibilite = disponibilite; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public void emprunter(){
        if (getDisponibilite()) {
            quantite--;
            disponibilite = false;
        } else {
            System.out.println("Livre non disponible pour emprunt.");
        }
    }
    public void retourner(){
        quantite++;
        disponibilite = true;
    }
     
    @Override
    public String toString() {
        return "Livre{" +
                "idLivre=" + idLivre +
                ", titre='" + titre + '\'' +
                ", nomAuteur='" + auteur + '\'' +
                ", anneePublication=" + annee +
                ", nombreExemplaire=" + quantite +
                ", disponibilite=" + disponibilite +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}

/*cd C:\Users\JERRY DON\GestionBibliotheque
javac -d bin src\model\*.java src\service\*.java
java -cp bin model.Livre */
/*package model;

public class Livre {
    
}
*/
// DONNEE DE TEST

/*package model;
// package service;
import service.BibliothequeService;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Livre {
    private static Scanner sc = new Scanner(System.in);
    // ===================== ATTRIBUTS =====================
    private int idLivre;
    private String titre;
    private String auteur;
    private int annee;
    private int quantite;
    private boolean disponibilite;
    private String isbn;

    // ===================== CONSTRUCTEURS =====================

    public Livre() {
    }

    public Livre(int idLivre, String titre, String auteur, int annee,
                 int quantite, boolean disponibilite, String isbn) {
        this.idLivre = idLivre;
        this.titre = titre;
        this.auteur = auteur;
        this.annee = annee;
        this.quantite = quantite;
        this.disponibilite = disponibilite;
        this.isbn = isbn;
    }

    // ===================== GETTERS =====================

    public int getIdLivre() { return idLivre; }
    public String getTitre() { return titre; }
    public String getAuteur() { return auteur; }
    public int getAnnee() { return annee; }
    public int getQuantite() { return quantite; }
    public boolean getDisponibilite() { return disponibilite; }
    public String getIsbn() { return isbn; }

    // ===================== SETTERS =====================

    public void setIdLivre(int idLivre) { this.idLivre = idLivre; }
    public void setTitre(String titre) { this.titre = titre; }
    public void setAuteur(String nomAuteur) {  this.auteur = nomAuteur; }
    public void setAnnee(int anneePublication) { this.annee = anneePublication; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public void setDisponibilite(boolean disponibilite) { this.disponibilite = disponibilite; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    // ===================== TOSTRING =====================

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

    //main
    public static void main(String[] args) {
        BibliothequeService bservice = new BibliothequeService();
        int ch = 1;
        while (ch == 1){
            System.out.println("\t\tGestion des Livre");
            System.out.println("1-Afficher tout leslivres diponibles");
            System.out.println("2-modifier un livre");
            System.out.println("3-supprimer un livre");
            System.out.println("4-ajouter un livre");
            System.out.println("5-Quitter");
            int choix = sc.nextInt();
            switch (choix) {
                case 1:
                    bservice.afficherTousLesLivres();
                    break;
                case 2:
                    System.out.println("Entrer l\'identifiant du livre a modifier: ");
                    int idlivrem = sc.nextInt();
                    bservice.modifierLivre(idlivrem);
                    break;
                case 3:
                    System.out.println("Entrer l\'identifiant du livre a supprimer: ");
                    int idlivres = sc.nextInt();
                    bservice.supprimerLivre(idlivres);
                    break;
                case 4:
                    Livre livre;
                    bservice.ajouterLivre();
                    break;
                case 5:
                    ch = 0;
                    break;
                default:
                    System.out.println("Choix incorrect, veuillez a faire un autre choix");
                    break;
            }      
        }
    }
}*/


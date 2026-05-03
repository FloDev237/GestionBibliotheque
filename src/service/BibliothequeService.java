package service;

import model.Livre;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class BibliothequeService{

    private Scanner sc;
    private List<Livre> livres = new ArrayList<>();
    private CategorieService categorieService;

    // Constructeur
    public BibliothequeService(CategorieService categorieService, Scanner sc) {
        this.categorieService = categorieService;
        this.sc = sc;
    }

    //getters 
    public List<Livre> getLivres() { return livres; }

    // Méthode pour initialisation directe (sans Scanner)
    public void ajouterLivreDirecte(Livre livre) {
        livres.add(livre);
    }

    //afficher tout les livres
    public void afficherTousLesLivres() {
        if (livres.isEmpty()) {
            System.out.println("La bibliotheque ne contient aucun livre.");
            return;
        }
        System.out.println("\n======= LISTE DE TOUS LES LIVRES =======");
        for (Livre l : livres) {
            System.out.println("------------------------------------------");
            System.out.println("ID         : " + l.getIdLivre());
            System.out.println("Titre      : " + l.getTitre());
            System.out.println("Auteur     : " + l.getAuteur());
            System.out.println("Année      : " + l.getAnnee());
            System.out.println("Quantite   : " + l.getQuantite());
            System.out.println("Disponible : " + (l.getDisponibilite() ? "Oui" : "Non"));
            System.out.println("ISBN       : " + l.getIsbn());
            System.out.println("Categorie  : " + categorieService.getNomCategorie(l.getIdCategorie()));
        }
        System.out.println("==========================================");
    }

    //ajouter un livre
    public void ajouterLivre() {
        int idLivre = genererIdLivre();

        System.out.print("Titre: ");
        String titre = sc.nextLine();

        System.out.print("Auteur: ");
        String auteur = sc.nextLine();

        int annee;
        int anneeActuelle = java.time.Year.now().getValue();

        do {
            System.out.print("Année de publication : ");
            annee = sc.nextInt();

            if (annee <= 0 || annee > anneeActuelle) {
                System.out.println("Annee invalide. Elle doit etre comprise entre 1 et " + anneeActuelle + ".");
            }
        } while (annee <= 0 || annee > anneeActuelle);

        int quantite;
        do{
            System.out.print("Nombre d\'exemplaire: ");
            quantite = sc.nextInt();
            if(quantite < 0)
                System.out.println("La quantite doit etre positive");
        } while(quantite < 0);
        
        boolean disponibilite = quantite > 0;
        
        sc.nextLine();
        String isbn;
        while(true){
            System.out.print("Code ISBN: ");
            isbn = sc.nextLine();
            if (!isbnExiste(isbn)) {
                break;
            }
            System.out.println("Code isbn non disponible, car deja affecte");
        }
        
        categorieService.afficherCategories();
        int idCategorie;
        while (true) {
            System.out.print("ID de la categorie : ");
            idCategorie = sc.nextInt();
            sc.nextLine();
            if (categorieService.rechercherParId(idCategorie) != null) break;
            System.out.println("Categorie introuvable, choisissez un ID valide.");
        }

        Livre livre = new Livre(idLivre, titre, auteur, annee, quantite, disponibilite, isbn, idCategorie);
        livres.add(livre);
        System.out.println("Livre ajoute avec succes. ");
    }

    //modifier un livre
    public void modifierLivre(int idLivre) {
        Livre livre = rechercherParId(idLivre);
        if (livre == null) {
            System.out.println("Aucun livre trouve avec cet identifiant.");
            return;
        }
        sc.nextLine();

        System.out.print("Titre (" + livre.getTitre() + ") : ");
        String titre = sc.nextLine();
        if (!titre.isEmpty()) livre.setTitre(titre);

        System.out.print("Auteur (" + livre.getAuteur() + ") : ");
        String auteur = sc.nextLine();
        if (!auteur.isEmpty()) livre.setAuteur(auteur);

        int annee;
        int anneeActuelle = java.time.Year.now().getValue();
        do {
            System.out.print("Annee (" + livre.getAnnee() + ") : ");
            annee = sc.nextInt();
            if (annee <= 0 || annee > anneeActuelle)
                System.out.println("Annee invalide.");
        } while (annee <= 0 || annee > anneeActuelle);
        livre.setAnnee(annee);

        int quantite;
        do {
            System.out.print("Quantite (" + livre.getQuantite() + ") : ");
            quantite = sc.nextInt();
            if (quantite < 0)
                System.out.println("Quantite invalide.");
        } while (quantite < 0);
        livre.setQuantite(quantite);
        livre.setDisponibilite(quantite > 0);
        sc.nextLine();

        while (true) {
            System.out.print("ISBN (" + livre.getIsbn() + ") : ");
            String nouvelIsbn = sc.nextLine();
            if (nouvelIsbn.isEmpty()
                    || nouvelIsbn.equalsIgnoreCase(livre.getIsbn())
                    || !isbnExiste(nouvelIsbn)) {
                if (!nouvelIsbn.isEmpty()) livre.setIsbn(nouvelIsbn);
                break;
            }
            System.out.println("ISBN deja utilise.");
        }

        System.out.println("Categorie actuelle : "
            + categorieService.getNomCategorie(livre.getIdCategorie()));
        categorieService.afficherCategories();
        System.out.print("Nouvel ID categorie (Entree pour garder) : ");
        String ligneId = sc.nextLine();
        if (!ligneId.isEmpty()) {
            int nouvelId = Integer.parseInt(ligneId);
            if (categorieService.rechercherParId(nouvelId) != null) {
                livre.setIdCategorie(nouvelId);
            } else {
                System.out.println("Categorie introuvable, categorie inchangee.");
            }
        }

        System.out.println("Livre modifie avec succes.");
        
    }

    //supprimer un livre
    public void supprimerLivre(int idLivre) {
        Livre livre = rechercherParId(idLivre);

        if (livre == null) {
            System.out.println("Aucun livre trouve avec cet identifiant.");
            return;
        }

        livres.remove(livre);
        System.out.println("Livre supprime avec succes.");
  
    }

    //rechercher un livre

    /*recherche par titre*/
    public List<Livre> rechercherParTitre(String titre) {
        List<Livre> resultats = new ArrayList<>();
        for (Livre livre : livres) {
            if (livre.getTitre().toLowerCase().contains(titre.toLowerCase())) {
                resultats.add(livre);
            }
        }
        if (resultats.isEmpty()) {
            System.out.println("Aucun livre trouve avec le titre : " + titre);
        }
        return resultats;
    }

    /* recherche par nom auteur*/
    public List<Livre> rechercherParAuteur(String auteur) {
        List<Livre> resultats = new ArrayList<>();
        for (Livre livre : livres) {
            if (livre.getAuteur().toLowerCase().contains(auteur.toLowerCase())) {
                resultats.add(livre);
            }
        }
        if (resultats.isEmpty()) {
            System.out.println("Aucun livre trouve pour l\'auteur : " + auteur);
        }
        return resultats;
    }

    /* recherche par annee de publication*/
    public List<Livre> rechercherParAnnee(int annee) {
        List<Livre> resultats = new ArrayList<>();
        for (Livre livre : livres) {
            if (livre.getAnnee() == annee) {
                resultats.add(livre);
            }
        }
        if (resultats.isEmpty()) {
            System.out.println("Aucun livre trouve pour l\'annee : " + annee);
        }
        return resultats;
    }

    /*//verification existe id
    private boolean idExiste(int idLivre) {
        for (Livre livre : livres) {
            if (livre.getIdLivre() == idLivre) {
                return true;
            }
        }
        return false;
    }*/

    //verification exixte isbn
    private boolean isbnExiste(String isbn) {
        for (Livre livre : livres) {
            if (livre.getIsbn().equalsIgnoreCase(isbn)) {
                return true;
            }
        }
        return false;
    }

    //verification exixte livre
    public Livre rechercherParId(int idLivre) {
        for (Livre livre : livres) {
            if (livre.getIdLivre() == idLivre) {
                return livre;
            }
        }
        return null;
    }
    
    // Generation idLivre
    private int genererIdLivre() {
        int max = 0;
        for (Livre l : livres) {
            if (l.getIdLivre() > max) max = l.getIdLivre();
        }
        return max + 1;
    }
}
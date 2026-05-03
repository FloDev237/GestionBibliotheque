package service;

import model.Categorie;
import model.Livre;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CategorieService {

    // La liste principale de toutes les catégories
    private List<Categorie> categories;
    private Scanner scanner;

    //constructeur
    public CategorieService(Scanner scanner) {
        this.categories = new ArrayList<>();
        this.scanner = scanner;
    }

    // Méthode pour initialisation directe (sans Scanner)
    public void ajouterCategorieDirecte(int id, String nom, String description) {
        categories.add(new Categorie(id, nom, description));    
    }

    // determiner le nom d'une categorie en fonction de l'idCategorie dans Livre
    public String getNomCategorie(int idCategorie) {
        Categorie cat = rechercherParId(idCategorie);
        return (cat != null) ? cat.getNom() : "Categorie inconnue";
    }

    // obtenir tout les livres d'une categorie
    public List<Livre> getLivresDeCategorie(int idCategorie, List<Livre> tousLesLivres) {
        List<Livre> resultat = new ArrayList<>();
        for (Livre l : tousLesLivres) {
            if (l.getIdCategorie() == idCategorie) {
                resultat.add(l);
            }
        }
        return resultat;
    }

    // afficher toutes les categories
    public void afficherCategories() {
        if (categories.isEmpty()) {
            System.out.println("Aucune categorie disponible.");
            return;
        }
        System.out.println("\n===== LISTE DES CATEGORIES =====");
        for (Categorie c : categories) {
            System.out.println(
                "ID: "          + c.getIdCategorie()  +
                " | Nom: "      + c.getNom()           +
                " | Description: "     + c.getDescription()
            );
        }
    }

    // afficher toutes les categorie et les livres qui sont dans chaque categorie
    public void afficherCategoriesAvecLivres(List<Livre> tousLesLivres) {
        if (categories.isEmpty()) {
            System.out.println("Aucune categorie disponible.");
            return;
        }
        System.out.println("\n===== CATEGORIES ET LEURS LIVRES =====");
        for (Categorie c : categories) {
            System.out.println("\n-> " + c.getNom() + " (ID: " + c.getIdCategorie() + ")" + " — " + c.getDescription());
            List<Livre> livresDeCat = getLivresDeCategorie(c.getIdCategorie(), tousLesLivres);
            if (livresDeCat.isEmpty()) {
                System.out.println("   Aucun livre dans cette categorie.");
            } else {
                for (Livre l : livresDeCat) {
                    System.out.println("   - " + l.getTitre() 
                                     + " | Auteur : " + l.getAuteur());
                }
            }
        }
    }

    // ajouter une categorie
    public void ajouterCategorie() {
        System.out.println("\n===== AJOUTER UNE CATEGORIE =====");

        System.out.print("Nom : ");
        String nom = scanner.nextLine().trim();

        if (rechercherParNom(nom) != null) {
            System.out.println("Cette categorie existe deja.");
            return;
        }

        System.out.print("Description : ");
        String description = scanner.nextLine().trim();

        int id = genererIdCategorie();

        categories.add(new Categorie(id, nom, description));
        System.out.println("Categorie '" + nom + "' ajoutee (ID: " + id + ").");
    }

    // modifier une categorie
    public void modifierCategorie() {
        afficherCategories();
        if (categories.isEmpty()) return;

        System.out.print("ID de la categorie à modifier : ");
        int id = lireEntier();

        Categorie cat = rechercherParId(id);
        if (cat == null) {
            System.out.println("Categorie introuvable.");
            return;
        }

        System.out.print("Nouveau nom (actuel : " + cat.getNom() + ") : ");
        String nom = scanner.nextLine().trim();
        if (!nom.isEmpty()) cat.setNom(nom);

        System.out.print("Nouvelle description (actuelle : " + cat.getDescription() + ") : ");
        String desc = scanner.nextLine().trim();
        if (!desc.isEmpty()) cat.setDescription(desc);

        System.out.println("Categorie modifiee avec succes.");
    }

    // supprimer une categorie
    public void supprimerCategorie(List<Livre> tousLesLivres) {
        afficherCategories();
        if (categories.isEmpty()) return;

        System.out.print("ID de la categorie a supprimer : ");
        int id = lireEntier();

        Categorie cat = rechercherParId(id);
        if (cat == null) {
            System.out.println("Categorie introuvable.");
            return;
        }

        // Vérification : des livres sont-ils encore liés à cette catégorie ?
        List<Livre> livresLies = getLivresDeCategorie(id, tousLesLivres);
        if (!livresLies.isEmpty()) {
            System.out.println("Oups  " + livresLies.size() 
                + " livre(s) appartiennent a cette categorie.");
            System.out.print("Supprimer quand meme ? (oui/non) : ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (!confirm.equals("oui")) {
                System.out.println("Suppression annulee.");
                return;
            }
        }

        categories.remove(cat);
        System.out.println("Categorie supprimee.");
    }

    // Recherche par id
    public Categorie rechercherParId(int id) {
        for (Categorie c : categories) {
            if (c.getIdCategorie() == id) return c;
        }
        return null;
    }

    // Recherche par nom
    public Categorie rechercherParNom(String nom) {
        for (Categorie c : categories) {
            if (c.getNom().equalsIgnoreCase(nom)) return c;
        }
        return null;
    }

    // Getter pour acces depuis BibliothequeService
    public List<Categorie> getCategories() { return categories; }

    // Generation idCategorie automatique
    private int genererIdCategorie() {
        int max = 0;
        for (Categorie c : categories) {
            if (c.getIdCategorie() > max) max = c.getIdCategorie();
        }
        return max + 1;
    }

    // Lecture des entiers 
    private int lireEntier() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Nombre invalide, reessayez : ");
            }
        }
    }
}

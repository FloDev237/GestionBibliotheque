// package util;

import model.*;
import service.*;

import java.util.*;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static CategorieService categorieService = new CategorieService(scanner);
    static BibliothequeService bibliothequeService = new BibliothequeService(categorieService, scanner);
    static GestionMembre gestionMembre = new GestionMembre();
    static EmpruntService empruntService = new EmpruntService(bibliothequeService, gestionMembre);

    public static void main(String[] args) {

        initialiserDonnees();

        // ─── ÉCRAN DE CONNEXION ───────────────────────────────────────────
        effacerEcran();
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║         BIENVENUE À LA BIBLIOTHÈQUE NUMÉRIQUE        ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();

        Membre membreConnecte = null;

        while (membreConnecte == null) {
            System.out.print("  Nom d'utilisateur : ");
            String nom = scanner.nextLine().trim();
            System.out.print("  Adresse email     : ");
            String email = scanner.nextLine().trim();

            membreConnecte = gestionMembre.chercherMembreParEmail(email);

            if (membreConnecte == null || !membreConnecte.getNom().equalsIgnoreCase(nom)) {
                membreConnecte = null;
                System.out.println();
                System.out.println("  ✗ Identifiants incorrects. Réessayez.");
                System.out.println();
            }
        }

        System.out.println();
        System.out.println("  ✓ Connexion réussie ! Bienvenue, " + membreConnecte.getNom() + ".");
        pause();

        // ─── ROUTAGE SELON LE RÔLE ───────────────────────────────────────
        boolean estAdmin = membreConnecte.getEmail().equalsIgnoreCase("admin@gmail.com");

        if (estAdmin) {
            menuAdmin();
        } else {
            menuMembre(membreConnecte);
        }

        System.out.println("\n  À bientôt !");
        scanner.close();
    }

    // ══════════════════════════════════════════════════════════════════════
    //  MENU ADMIN — PRINCIPAL
    // ══════════════════════════════════════════════════════════════════════

    static void menuAdmin() {
        boolean continuer = true;
        while (continuer) {
            effacerEcran();
            System.out.println("╔══════════════════════════════════════════════════════╗");
            System.out.println("║                  ESPACE ADMINISTRATEUR               ║");
            System.out.println("╠══════════════════════════════════════════════════════╣");
            System.out.println("║   1. Gestion des membres                             ║");
            System.out.println("║   2. Gestion des livres                              ║");
            System.out.println("║   3. Gestion des catégories                          ║");
            System.out.println("║   4. Gestion des emprunts                            ║");
            System.out.println("║   0. Quitter                                         ║");
            System.out.println("╚══════════════════════════════════════════════════════╝");
            System.out.print("  Votre choix : ");

            int choix = lireEntier();

            switch (choix) {
                case 1 -> menuGestionMembres();
                case 2 -> menuGestionLivres();
                case 3 -> menuGestionCategories();
                case 4 -> menuGestionEmprunts();
                case 0 -> continuer = false;
                default -> { System.out.println("  Choix invalide."); pause(); }
            }
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  SOUS-MENU — GESTION DES MEMBRES
    // ══════════════════════════════════════════════════════════════════════

    static void menuGestionMembres() {
        boolean continuer = true;
        while (continuer) {
            effacerEcran();
            System.out.println("╔══════════════════════════════════════════════════════╗");
            System.out.println("║                  GESTION DES MEMBRES                 ║");
            System.out.println("╠══════════════════════════════════════════════════════╣");
            System.out.println("║   1. Afficher tous les membres                       ║");
            System.out.println("║   2. Ajouter un membre                               ║");
            System.out.println("║   3. Modifier un membre                              ║");
            System.out.println("║   4. Supprimer un membre                             ║");
            System.out.println("║   0. Retour                                          ║");
            System.out.println("╚══════════════════════════════════════════════════════╝");
            System.out.print("  Votre choix : ");

            int choix = lireEntier();

            switch (choix) {
                case 1 -> gestionMembre.afficherTousLesMembres();
                case 2 -> {
                    System.out.print("  Nom : ");     String nom    = scanner.nextLine().trim();
                    System.out.print("  Email : ");   String email  = scanner.nextLine().trim();
                    System.out.print("  Adresse : "); String adresse = scanner.nextLine().trim();
                    System.out.print("  Téléphone : "); int tel = lireEntier();
                    gestionMembre.inscrireMembre(nom, email, adresse, tel);
                }
                case 3 -> {
                    gestionMembre.afficherTousLesMembres();
                    System.out.print("  ID du membre à modifier : "); int id = lireEntier();
                    System.out.print("  Nouveau nom : ");      String nom    = scanner.nextLine().trim();
                    System.out.print("  Nouvel email : ");     String email  = scanner.nextLine().trim();
                    System.out.print("  Nouvelle adresse : "); String adresse = scanner.nextLine().trim();
                    System.out.print("  Nouveau téléphone : "); int tel = lireEntier();
                    gestionMembre.modifierMembre(id, nom, email, adresse, tel);
                }
                case 4 -> {
                    gestionMembre.afficherTousLesMembres();
                    System.out.print("  ID du membre à supprimer : "); int id = lireEntier();
                    gestionMembre.supprimerMembre(id);
                }
                case 0 -> continuer = false;
                default -> System.out.println("  Choix invalide.");
            }

            if (continuer && choix != 0) pause();
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  SOUS-MENU — GESTION DES LIVRES
    // ══════════════════════════════════════════════════════════════════════

    static void menuGestionLivres() {
        boolean continuer = true;
        while (continuer) {
            effacerEcran();
            System.out.println("╔══════════════════════════════════════════════════════╗");
            System.out.println("║                  GESTION DES LIVRES                  ║");
            System.out.println("╠══════════════════════════════════════════════════════╣");
            System.out.println("║   1. Afficher tous les livres                        ║");
            System.out.println("║   2. Ajouter un livre                                ║");
            System.out.println("║   3. Modifier un livre                               ║");
            System.out.println("║   4. Supprimer un livre                              ║");
            System.out.println("║   0. Retour                                          ║");
            System.out.println("╚══════════════════════════════════════════════════════╝");
            System.out.print("  Votre choix : ");

            int choix = lireEntier();

            switch (choix) {
                case 1 -> bibliothequeService.afficherTousLesLivres();
                case 2 -> bibliothequeService.ajouterLivre();
                case 3 -> {
                    bibliothequeService.afficherTousLesLivres();
                    System.out.print("  ID du livre à modifier : "); int id = lireEntier();
                    bibliothequeService.modifierLivre(id);
                }
                case 4 -> {
                    bibliothequeService.afficherTousLesLivres();
                    System.out.print("  ID du livre à supprimer : "); int id = lireEntier();
                    bibliothequeService.supprimerLivre(id);
                }
                case 0 -> continuer = false;
                default -> System.out.println("  Choix invalide.");
            }

            if (continuer && choix != 0) pause();
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  SOUS-MENU — GESTION DES CATÉGORIES
    // ══════════════════════════════════════════════════════════════════════

    static void menuGestionCategories() {
        boolean continuer = true;
        while (continuer) {
            effacerEcran();
            System.out.println("╔══════════════════════════════════════════════════════╗");
            System.out.println("║                GESTION DES CATÉGORIES                ║");
            System.out.println("╠══════════════════════════════════════════════════════╣");
            System.out.println("║   1. Afficher les catégories                         ║");
            System.out.println("║   2. Afficher catégories avec leurs livres           ║");
            System.out.println("║   3. Ajouter une catégorie                           ║");
            System.out.println("║   4. Modifier une catégorie                          ║");
            System.out.println("║   5. Supprimer une catégorie                         ║");
            System.out.println("║   0. Retour                                          ║");
            System.out.println("╚══════════════════════════════════════════════════════╝");
            System.out.print("  Votre choix : ");

            int choix = lireEntier();

            switch (choix) {
                case 1 -> categorieService.afficherCategories();
                case 2 -> categorieService.afficherCategoriesAvecLivres(bibliothequeService.getLivres());
                case 3 -> categorieService.ajouterCategorie();
                case 4 -> categorieService.modifierCategorie();
                case 5 -> categorieService.supprimerCategorie(bibliothequeService.getLivres());
                case 0 -> continuer = false;
                default -> System.out.println("  Choix invalide.");
            }

            if (continuer && choix != 0) pause();
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  SOUS-MENU — GESTION DES EMPRUNTS
    // ══════════════════════════════════════════════════════════════════════

    static void menuGestionEmprunts() {
        boolean continuer = true;
        while (continuer) {
            effacerEcran();
            System.out.println("╔══════════════════════════════════════════════════════╗");
            System.out.println("║                GESTION DES EMPRUNTS                  ║");
            System.out.println("╠══════════════════════════════════════════════════════╣");
            System.out.println("║   1. Emprunts d'un membre spécifique                 ║");
            System.out.println("║   2. Emprunts de tous les membres                    ║");
            System.out.println("║   3. Emprunts en retard                              ║");
            System.out.println("║   4. Statistiques des emprunts                       ║");
            System.out.println("║   0. Retour                                          ║");
            System.out.println("╚══════════════════════════════════════════════════════╝");
            System.out.print("  Votre choix : ");

            int choix = lireEntier();

            switch (choix) {
                case 1 -> afficherEmpruntsMembreSpecifique();
                case 2 -> afficherEmpruntsTosMembres();
                case 3 -> empruntService.afficherEmpruntsEnRetard();
                case 4 -> empruntService.afficherStatistiques();
                case 0 -> continuer = false;
                default -> System.out.println("  Choix invalide.");
            }

            if (continuer && choix != 0) pause();
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  MENU MEMBRE
    // ══════════════════════════════════════════════════════════════════════

    static void menuMembre(Membre membre) {
        boolean continuer = true;
        while (continuer) {
            effacerEcran();
            System.out.println("╔══════════════════════════════════════════════════════╗");
            System.out.println("║              ESPACE MEMBRE — " + padDroit(membre.getNom(), 22) + "║");
            System.out.println("╠══════════════════════════════════════════════════════╣");
            System.out.println("║   1. Consulter les catégories                        ║");
            System.out.println("║   2. Consulter les catégories avec leurs livres      ║");
            System.out.println("║   3. Rechercher un livre                             ║");
            System.out.println("║   4. Emprunter un livre                              ║");
            System.out.println("║   5. Rendre un livre                                 ║");
            System.out.println("║   0. Quitter                                         ║");
            System.out.println("╚══════════════════════════════════════════════════════╝");
            System.out.print("  Votre choix : ");

            int choix = lireEntier();

            switch (choix) {
                case 1 -> categorieService.afficherCategories();
                case 2 -> categorieService.afficherCategoriesAvecLivres(bibliothequeService.getLivres());
                case 3 -> {
                    System.out.println("  Rechercher par : 1. Titre  2. Auteur  3. Année");
                    System.out.print("  Choix : ");
                    int type = lireEntier();
                    switch (type) {
                        case 1 -> {
                            System.out.print("  Titre : "); String t = scanner.nextLine().trim();
                            afficherListeLivres(bibliothequeService.rechercherParTitre(t));
                        }
                        case 2 -> {
                            System.out.print("  Auteur : "); String a = scanner.nextLine().trim();
                            afficherListeLivres(bibliothequeService.rechercherParAuteur(a));
                        }
                        case 3 -> {
                            System.out.print("  Année : "); int an = lireEntier();
                            afficherListeLivres(bibliothequeService.rechercherParAnnee(an));
                        }
                        default -> System.out.println("  Choix invalide.");
                    }
                }
                case 4 -> {
                    // Vérifications avant emprunt
                    if (!empruntService.peutEmprunter(membre.getIdMembre())) {
                        System.out.println("  ✗ Vous ne pouvez pas effectuer d'emprunt.");
                    } else {
                        bibliothequeService.afficherTousLesLivres();
                        System.out.print("  ID du livre à emprunter : ");
                        int idLivre = lireEntier();
                        empruntService.emprunterLivre(idLivre, membre.getIdMembre());
                    }
                }
                case 5 -> {
                    List<Livre> livresEmpruntes = membre.getLivresEmpruntes();
                    if (livresEmpruntes.isEmpty()) {
                        System.out.println("  Vous n'avez aucun livre emprunté.");
                    } else {
                        System.out.println("\n  Vos livres empruntés :");
                        for (Livre l : livresEmpruntes) {
                            System.out.println("   [" + l.getIdLivre() + "] " + l.getTitre());
                        }
                        System.out.print("  ID du livre à rendre : ");
                        int idLivre = lireEntier();
                        // Vérifier que le livre appartient bien à ce membre
                        boolean possedeLelivre = livresEmpruntes.stream()
                            .anyMatch(l -> l.getIdLivre() == idLivre);
                        if (!possedeLelivre) {
                            System.out.println("  ✗ Ce livre ne figure pas dans vos emprunts.");
                        } else {
                            empruntService.retournerLivre(idLivre, membre.getIdMembre());
                        }
                    }
                }
                case 0 -> continuer = false;
                default -> System.out.println("  Choix invalide.");
            }

            if (continuer && choix != 0) pause();
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  EMPRUNTS — VUE ADMIN
    // ══════════════════════════════════════════════════════════════════════

    static void afficherEmpruntsMembreSpecifique() {
        gestionMembre.afficherTousLesMembres();
        System.out.print("  ID du membre : ");
        int idMembre = lireEntier();

        Membre membre = gestionMembre.chercherMembreParId(idMembre);
        if (membre == null) {
            System.out.println("  ✗ Membre introuvable.");
            return;
        }

        List<Emprunt> emprunts = empruntService.listerEmpruntsParMembre(idMembre);
        long actifs = emprunts.stream().filter(e -> e.getStatut().equals("EN_COURS")).count();

        System.out.println("\n  ═══ Emprunts de " + membre.getNom() + " ═══");
        if (emprunts.isEmpty()) {
            System.out.println("  Aucun emprunt.");
        } else {
            for (Emprunt e : emprunts) {
                String statut = e.getStatut().equals("EN_COURS")
                    ? (e.estEnRetard() ? "⚠ EN RETARD" : "✓ EN COURS")
                    : "✓ RETOURNÉ";
                System.out.printf("   #%-3d | Livre ID: %-3d | %s | Emprunté le: %s%n",
                    e.getIdEmprunt(), e.getLivreId(), statut, e.getDateEmprunt());
            }
        }
        System.out.println("  ─────────────────────────────────────────────");
        System.out.println("  Total emprunts       : " + emprunts.size());
        System.out.println("  Total emprunts actifs: " + actifs);
    }

    static void afficherEmpruntsTosMembres() {
        List<Membre> membres = gestionMembre.listerTousLesMembres();
        int totalEmprunts = 0;
        int totalActifs = 0;

        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║           EMPRUNTS DE TOUS LES MEMBRES               ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        for (Membre membre : membres) {
            List<Emprunt> emprunts = empruntService.listerEmpruntsParMembre(membre.getIdMembre());
            long actifs = emprunts.stream().filter(e -> e.getStatut().equals("EN_COURS")).count();
            totalEmprunts += emprunts.size();
            totalActifs += actifs;

            System.out.println("\n  ── " + membre.getNom() + " (ID: " + membre.getIdMembre() + ") ──");
            if (emprunts.isEmpty()) {
                System.out.println("     Aucun emprunt.");
            } else {
                for (Emprunt e : emprunts) {
                    String statut = e.getStatut().equals("EN_COURS")
                        ? (e.estEnRetard() ? "⚠ EN RETARD" : "✓ EN COURS")
                        : "✓ RETOURNÉ";
                    System.out.printf("     #%-3d | Livre ID: %-3d | %-13s | %s%n",
                        e.getIdEmprunt(), e.getLivreId(), statut, e.getDateEmprunt());
                }
            }
            System.out.println("     Emprunts: " + emprunts.size() + " | Actifs: " + actifs);
        }

        System.out.println("\n══════════════════════════════════════════════════════");
        System.out.println("  TOTAL EMPRUNTS       : " + totalEmprunts);
        System.out.println("  TOTAL ACTIFS         : " + totalActifs);
        System.out.println("══════════════════════════════════════════════════════");
    }

    // ══════════════════════════════════════════════════════════════════════
    //  INITIALISATION DES DONNÉES
    // ══════════════════════════════════════════════════════════════════════

    static void initialiserDonnees() {

        // ── 4 CATÉGORIES ──────────────────────────────────────────────────
        categorieService.ajouterCategorieDirecte(1, "Informatique",  "Livres sur la programmation et les systèmes");
        categorieService.ajouterCategorieDirecte(2, "Littérature",   "Romans, nouvelles et poésie");
        categorieService.ajouterCategorieDirecte(3, "Sciences",      "Physique, chimie, biologie");
        categorieService.ajouterCategorieDirecte(4, "Histoire",      "Histoire mondiale et civilisations");

        // ── 12 LIVRES (3 par catégorie) ───────────────────────────────────
        bibliothequeService.ajouterLivreDirecte(new Livre(1,  "Clean Code",                    "Robert C. Martin",  2008, 3, true,  "978-0132350884", 1));
        bibliothequeService.ajouterLivreDirecte(new Livre(2,  "Design Patterns",               "Gang of Four",      1994, 2, true,  "978-0201633610", 1));
        bibliothequeService.ajouterLivreDirecte(new Livre(3,  "The Pragmatic Programmer",      "Andrew Hunt",       1999, 4, true,  "978-0201616224", 1));
        bibliothequeService.ajouterLivreDirecte(new Livre(4,  "Les Misérables",                "Victor Hugo",       1862, 5, true,  "978-2070409228", 2));
        bibliothequeService.ajouterLivreDirecte(new Livre(5,  "L'Étranger",                    "Albert Camus",      1942, 3, true,  "978-2070360024", 2));
        bibliothequeService.ajouterLivreDirecte(new Livre(6,  "Le Petit Prince",               "Antoine de Saint-Exupéry", 1943, 6, true, "978-2070612758", 2));
        bibliothequeService.ajouterLivreDirecte(new Livre(7,  "Une brève histoire du temps",   "Stephen Hawking",   1988, 2, true,  "978-2081227460", 3));
        bibliothequeService.ajouterLivreDirecte(new Livre(8,  "Le Gène égoïste",               "Richard Dawkins",   1976, 3, true,  "978-2221109847", 3));
        bibliothequeService.ajouterLivreDirecte(new Livre(9,  "La Structure des révolutions",  "Thomas Kuhn",       1962, 2, true,  "978-2080811028", 3));
        bibliothequeService.ajouterLivreDirecte(new Livre(10, "Sapiens",                       "Yuval Noah Harari", 2011, 4, true,  "978-2226257017", 4));
        bibliothequeService.ajouterLivreDirecte(new Livre(11, "Civilisations",                 "Laurent Binet",     2019, 3, true,  "978-2246815679", 4));
        bibliothequeService.ajouterLivreDirecte(new Livre(12, "Le Monde d'hier",               "Stefan Zweig",      1942, 2, true,  "978-2253006220", 4));

        // ── 7 MEMBRES (dont admin) ─────────────────────────────────────────
        gestionMembre.inscrireMembreDirecte(new Membre(1, "Admin",          "admin@gmail.com",    "Bibliothèque centrale", 699000001, "2024-01-01"));
        gestionMembre.inscrireMembreDirecte(new Membre(2, "Marie Dupont",   "marie@gmail.com",    "12 rue des Fleurs",     699000002, "2024-03-15"));
        gestionMembre.inscrireMembreDirecte(new Membre(3, "Jean Martin",    "jean@gmail.com",     "45 avenue Victor Hugo", 699000003, "2024-04-01"));
        gestionMembre.inscrireMembreDirecte(new Membre(4, "Sophie Bernard", "sophie@gmail.com",   "8 rue de la Paix",      699000004, "2024-05-10"));
        gestionMembre.inscrireMembreDirecte(new Membre(5, "Paul Leroy",     "paul@gmail.com",     "3 impasse du Moulin",   699000005, "2024-06-20"));
        gestionMembre.inscrireMembreDirecte(new Membre(6, "Emma Moreau",    "emma@gmail.com",     "27 boulevard Haussmann",699000006, "2024-07-05"));
        gestionMembre.inscrireMembreDirecte(new Membre(7, "Lucas Petit",    "lucas@gmail.com",    "19 rue Nationale",      699000007, "2024-08-12"));

        // ── 3 EMPRUNTS ────────────────────────────────────────────────────
        empruntService.emprunterLivre(1, 2);  // Marie emprunte Clean Code
        empruntService.emprunterLivre(4, 3);  // Jean emprunte Les Misérables
        empruntService.emprunterLivre(7, 4);  // Sophie emprunte Une brève histoire du temps
    }

    // ══════════════════════════════════════════════════════════════════════
    //  UTILITAIRES
    // ══════════════════════════════════════════════════════════════════════

    static void afficherListeLivres(List<Livre> livres) {
        if (livres == null || livres.isEmpty()) return;
        for (Livre l : livres) {
            System.out.println("  [" + l.getIdLivre() + "] " + l.getTitre()
                + " — " + l.getAuteur()
                + " (" + l.getAnnee() + ")"
                + " | Dispo: " + (l.getDisponibilite() ? "Oui" : "Non"));
        }
    }

    static int lireEntier() {
        while (true) {
            try {
                String ligne = scanner.nextLine().trim();
                return Integer.parseInt(ligne);
            } catch (NumberFormatException e) {
                System.out.print("  Nombre invalide, réessayez : ");
            }
        }
    }

    static void pause() {
        System.out.println("\n  Appuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }

    static void effacerEcran() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static String padDroit(String texte, int longueur) {
        if (texte.length() >= longueur) return texte.substring(0, longueur);
        return texte + " ".repeat(longueur - texte.length());
    }
}
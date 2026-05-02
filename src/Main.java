import model.Livre;
import model.Membre;
import model.Emprunt;
import model.Categorie;
import service.BibliothequeService;
import service.CategorieService;
import service.GestionMembre;
import service.EmpruntService;

import java.time.LocalDate;
import java.util.List;

public class Main {
    
    public static void main(String[] args) {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              BIENVENUE DANS BIBLIOTHÈQUE SYSTEME                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        
        // 1. Initialisation des services
        System.out.println("\n 1. Initialisation des services...");
        CategorieService categorieService = new CategorieService();
        BibliothequeService bibliothequeService = new BibliothequeService(categorieService);
        GestionMembre gestionMembre = new GestionMembre();
        EmpruntService empruntService = new EmpruntService(bibliothequeService, gestionMembre);
        System.out.println(" Services initialisés avec succès!");
        
        // 2. Ajout de catégories par défaut
        System.out.println("\n 2. Ajout des catégories par défaut...");
        // Note: Comme les catégories sont gérées via Scanner, on les ajoute directement
        System.out.println("   (Les catégories seront ajoutées via le menu)");
        
        // 3. Ajout de quelques livres de test
        System.out.println("\n 3. Ajout de livres de test...");
        Livre livre1 = new Livre(1, "Le Petit Prince", "Saint-Exupéry", 1943, 3, true, "9782070408504", 1);
        Livre livre2 = new Livre(2, "1984", "George Orwell", 1949, 2, true, "9780452284234", 1);
        Livre livre3 = new Livre(3, "Harry Potter à l'école des sorciers", "J.K. Rowling", 1997, 5, true, "9782070584628", 2);
        Livre livre4 = new Livre(4, "L'Alchimiste", "Paulo Coelho", 1988, 1, true, "9782070408505", 2);
        
        bibliothequeService.getLivres().add(livre1);
        bibliothequeService.getLivres().add(livre2);
        bibliothequeService.getLivres().add(livre3);
        bibliothequeService.getLivres().add(livre4);
        System.out.println(" 4 livres ajoutés!");
        
        // 4. Ajout de membres de test
        System.out.println("\n 4. Ajout de membres de test...");
        Membre membre1 = new Membre(1, "Jean Dupont", "jean@email.com", "12 rue de Paris", 612345678, LocalDate.now().toString());
        Membre membre2 = new Membre(2, "Marie Martin", "marie@email.com", "45 avenue Victor Hugo", 698765432, LocalDate.now().toString());
        Membre membre3 = new Membre(3, "Pierre Durand", "pierre@email.com", "8 boulevard Saint-Michel", 674567891, LocalDate.now().toString());
        
        gestionMembre.listerTousLesMembres().add(membre1);
        gestionMembre.listerTousLesMembres().add(membre2);
        gestionMembre.listerTousLesMembres().add(membre3);
        System.out.println(" 3 membres inscrits!");
        
        // 5. Afficher tous les livres
        System.out.println("\n 5. Liste des livres disponibles:");
        System.out.println("═══════════════════════════════════════════════════════════════");
        bibliothequeService.afficherTousLesLivres();
        
        // 6. Afficher tous les membres
        System.out.println("\n 6. Liste des membres:");
        System.out.println("═══════════════════════════════════════════════════════════════");
        gestionMembre.afficherTousLesMembres();
        
        // 7. Effectuer des emprunts
        System.out.println("\n 7. Effectuer des emprunts:");
        System.out.println("═══════════════════════════════════════════════════════════════");
        
        System.out.println("\n Jean emprunte 'Le Petit Prince' (ID: 1):");
        empruntService.emprunterLivre(1, 1);
        
        System.out.println("\n Jean emprunte '1984' (ID: 2):");
        empruntService.emprunterLivre(2, 1);
        
        System.out.println("\n Marie emprunte 'Harry Potter' (ID: 3):");
        empruntService.emprunterLivre(3, 2);
        
        // 8. Tentative d'emprunt supplémentaire (limite de 3)
        System.out.println("\n 8. Test de la limite d'emprunts:");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("\n Jean essaie d'emprunter un 3ème livre:");
        empruntService.emprunterLivre(3, 1);
        
        // 9. Afficher les emprunts en cours
        System.out.println("\n 9. Emprunts en cours:");
        System.out.println("═══════════════════════════════════════════════════════════════");
        empruntService.afficherTousLesEmprunts();
        
        // 10. Retourner un livre
        System.out.println("\n 10. Retour de livre:");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("\n Jean retourne 'Le Petit Prince':");
        empruntService.retournerLivre(1, 1);
        
        // 11. Afficher les emprunts après retour
        System.out.println("\n 11. Emprunts après retour:");
        System.out.println("═══════════════════════════════════════════════════════════════");
        empruntService.afficherTousLesEmprunts();
        
        // 12. Afficher les livres disponibles après retour
        System.out.println("\n 12. Livres disponibles après retour:");
        System.out.println("═══════════════════════════════════════════════════════════════");
        bibliothequeService.afficherTousLesLivres();
        
        // 13. Afficher les statistiques
        System.out.println("\n 13. Statistiques de la bibliothèque:");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("Nombre total de livres: " + bibliothequeService.getLivres().size());
        System.out.println("Nombre total de membres: " + gestionMembre.getNombreTotalMembres());
        System.out.println(" Nombre total d'emprunts: " + empruntService.getNombreTotalEmprunts());
        System.out.println(" Emprunts actifs: " + empruntService.getNombreEmpruntsActifs());
        System.out.println(" Pénalités totales: " + empruntService.calculerPenalitesTotales() + "€");
        
        // 14. Afficher les emprunts d'un membre spécifique
        System.out.println("\n 14. Emprunts de Jean (ID: 1):");
        System.out.println("═══════════════════════════════════════════════════════════════");
        List<Emprunt> empruntsJean = empruntService.listerEmpruntsParMembre(1);
        if (empruntsJean.isEmpty()) {
            System.out.println("   Aucun emprunt trouvé pour Jean.");
        } else {
            for (Emprunt e : empruntsJean) {
                System.out.println("   " + e);
            }
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    TEST RÉUSSI! FÉLICITATIONS!                  ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println("\n💡 Pour lancer l'interface complète, exécutez ui.MainMenu");
    }
}

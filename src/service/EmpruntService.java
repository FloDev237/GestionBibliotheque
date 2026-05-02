package service;

import model.Emprunt;
import model.Livre;
import model.Membre;
import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

public class EmpruntService {
    
    // Liste pour stocker tous les emprunts
    private List<Emprunt> emprunts;
    private int prochainId;
    
    // Références vers les autres services
    private BibliothequeService bibliothequeService;
    private GestionMembre gestionMembre;
    
    /**
     * Constructeur avec les services nécessaires
     */
    public EmpruntService(BibliothequeService bibliothequeService, 
                          GestionMembre gestionMembre) {
        this.emprunts = new ArrayList<>();
        this.prochainId = 1;
        this.bibliothequeService = bibliothequeService;
        this.gestionMembre = gestionMembre;
    }
    
    /**
     * Constructeur par défaut (pour les tests)
     */
    public EmpruntService() {
        this.emprunts = new ArrayList<>();
        this.prochainId = 1;
    }
    
    /**
     * Emprunter un livre
     */
    public boolean emprunterLivre(int livreId, int membreId) {
        // Vérifier si le livre existe
        Livre livre = bibliothequeService.rechercherParId(livreId);
        if (livre == null) {
            System.out.println(" Erreur: Livre avec ID " + livreId + " non trouvé!");
            return false;
        }
        
        // Vérifier si le livre est disponible (quantité > 0)
        if (!livre.getDisponibilite() || livre.getQuantite() <= 0) {
            System.out.println("Erreur: Le livre '" + livre.getTitre() + "' n'est pas disponible!");
            System.out.println("   Quantité disponible: " + livre.getQuantite());
            return false;
        }
        
        // Vérifier si le membre existe
        Membre membre = gestionMembre.chercherMembreParId(membreId);
        if (membre == null) {
            System.out.println("Erreur: Membre avec ID " + membreId + " non trouvé!");
            return false;
        }
        
        // Vérifier si le membre n'a pas déjà 3 emprunts en cours
        int empruntsActifs = getNombreEmpruntsActifsParMembre(membreId);
        if (empruntsActifs >= 3) {
            System.out.println(" Erreur: Le membre '" + membre.getNom() + 
                             "' a déjà " + empruntsActifs + " emprunts en cours (maximum 3)!");
            return false;
        }
        
        // Vérifier si le membre a déjà emprunté ce même livre
        boolean aDejaEmprunte = aDejaEmprunteCeLivre(membreId, livreId);
        if (aDejaEmprunte) {
            System.out.println(" Erreur: Le membre '" + membre.getNom() + 
                             "' a déjà emprunté ce livre et ne l'a pas encore retourné!");
            return false;
        }
        
        // Créer l'emprunt
        Emprunt emprunt = new Emprunt(prochainId++, livreId, membreId);
        emprunts.add(emprunt);
        
        // Emprunter le livre (décrémente la quantité via la méthode de Membre)
        membre.emprunterLivre(livre);
        
        System.out.println("\n EMPRUNT EFFECTUÉ AVEC SUCCÈS!");
        System.out.println("┌─────────────────────────────────────────────────┐");
        System.out.println("│ ID Emprunt: " + emprunt.getIdEmprunt());
        System.out.println("│ Livre: '" + livre.getTitre() + "'");
        System.out.println("│ Auteur: " + livre.getAuteur());
        System.out.println("│ Membre: " + membre.getNom());
        System.out.println("│ Date emprunt: " + emprunt.getDateEmprunt());
        System.out.println("│ Date retour prévue: " + emprunt.getDateRetourPrevue());
        System.out.println("│ Quantité restante: " + livre.getQuantite());
        System.out.println("└─────────────────────────────────────────────────┘");
        
        return true;
    }
    
    /**
     * Retourner un livre
     */
    public boolean retournerLivre(int livreId, int membreId) {
        // Vérifier si le livre existe
        Livre livre = bibliothequeService.rechercherParId(livreId);
        if (livre == null) {
            System.out.println(" Erreur: Livre avec ID " + livreId + " non trouvé!");
            return false;
        }
        
        // Vérifier si le membre existe
        Membre membre = gestionMembre.chercherMembreParId(membreId);
        if (membre == null) {
            System.out.println(" Erreur: Membre avec ID " + membreId + " non trouvé!");
            return false;
        }
        
        // Vérifier si le membre a vraiment ce livre emprunté
        boolean aLeLivre = membre.getLivresEmpruntes().stream()
            .anyMatch(l -> l.getIdLivre() == livreId);
        
        if (!aLeLivre) {
            System.out.println(" Erreur: Le membre '" + membre.getNom() + 
                             "' n'a pas emprunté ce livre!");
            return false;
        }
        
        // Trouver l'emprunt actif
        Emprunt empruntActif = trouverEmpruntActif(livreId, membreId);
        if (empruntActif == null) {
            System.out.println(" Erreur: Aucun emprunt actif trouvé pour ce livre et ce membre!");
            return false;
        }
        
        // Enregistrer le retour
        LocalDate dateRetour = LocalDate.now();
        empruntActif.retourner();
        
        // Retourner le livre (via la méthode de Membre)
        membre.retournerLivre(livre);
        
        // Afficher le résultat
        System.out.println("\n RETOUR EFFECTUÉ AVEC SUCCÈS!");
        System.out.println("┌─────────────────────────────────────────────────┐");
        System.out.println("│ Livre: '" + livre.getTitre() + "'");
        System.out.println("│ Membre: " + membre.getNom());
        System.out.println("│ Date retour: " + dateRetour);
        System.out.println("│ Quantité disponible: " + livre.getQuantite());
        
        // Afficher les pénalités si nécessaire
        double penalites = empruntActif.getPenalitesCalculees();
        if (penalites > 0) {
            long joursRetard = ChronoUnit.DAYS.between(empruntActif.getDateRetourPrevue(), dateRetour);
            System.out.println("│    PÉNALITÉS: " + penalites + "€");
            System.out.println("│    Retard de " + joursRetard + " jours");
            System.out.println("│    Taux: " + Emprunt.PENALITE_PAR_JOUR + "€/jour");
        } else {
            System.out.println("│    Aucune pénalité - Retour dans les délais");
        }
        System.out.println("└─────────────────────────────────────────────────┘");
        
        return true;
    }
    
    /**
     * Vérifier si un membre a déjà emprunté un livre spécifique
     */
    public boolean aDejaEmprunteCeLivre(int membreId, int livreId) {
        return emprunts.stream()
            .anyMatch(e -> e.getMembreId() == membreId && 
                          e.getLivreId() == livreId &&
                          e.getStatut().equals("EN_COURS"));
    }
    
    /**
     * Trouver un emprunt actif par livre et membre
     */
    public Emprunt trouverEmpruntActif(int livreId, int membreId) {
        return emprunts.stream()
            .filter(e -> e.getLivreId() == livreId && 
                        e.getMembreId() == membreId &&
                        e.getStatut().equals("EN_COURS"))
            .findFirst()
            .orElse(null);
    }
    
    //Compter les emprunts actifs d'un membre
    public int getNombreEmpruntsActifsParMembre(int membreId) {
        return (int) emprunts.stream()
            .filter(e -> e.getMembreId() == membreId && 
                        e.getStatut().equals("EN_COURS"))
            .count();
    }
    
    /**
     * Lister tous les emprunts actifs
     */
    public List<Emprunt> listerEmpruntsActifs() {
        return emprunts.stream()
            .filter(e -> e.getStatut().equals("EN_COURS"))
            .collect(Collectors.toList());
    }
    
    /**
     * Lister les emprunts d'un membre spécifique
     */
    public List<Emprunt> listerEmpruntsParMembre(int membreId) {
        return emprunts.stream()
            .filter(e -> e.getMembreId() == membreId)
            .sorted((e1, e2) -> e2.getDateEmprunt().compareTo(e1.getDateEmprunt()))
            .collect(Collectors.toList());
    }
    
    /**
     * Lister les emprunts en retard
     */
    public List<Emprunt> listerEmpruntsEnRetard() {
        return emprunts.stream()
            .filter(e -> e.getStatut().equals("EN_COURS") && e.estEnRetard())
            .collect(Collectors.toList());
    }
    
    /**
     * Afficher tous les emprunts
     */
    public void afficherTousLesEmprunts() {
        if (emprunts.isEmpty()) {
            System.out.println("    Aucun emprunt dans l'historique.");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                           HISTORIQUE COMPLET DES EMPRUNTS                        ║");
        System.out.println("╠═════╦════════════════════════╦════════════════════════╦══════════════╦═════════════╣");
        System.out.println("║ ID  ║ Livre                   ║ Membre                 ║ Date emprunt ║ Statut      ║");
        System.out.println("╠═════╬════════════════════════╬════════════════════════╬══════════════╬═════════════╣");
        
        for (Emprunt emprunt : emprunts) {
            Livre livre = bibliothequeService != null ? 
                bibliothequeService.rechercherParId(emprunt.getLivreId()) : null;
            Membre membre = gestionMembre != null ? 
                gestionMembre.chercherMembreParId(emprunt.getMembreId()) : null;
            
            String titreLivre = (livre != null) ? tronquer(livre.getTitre(), 20) : "ID:" + emprunt.getLivreId();
            String nomMembre = (membre != null) ? tronquer(membre.getNom(), 20) : "ID:" + emprunt.getMembreId();
            String statut = emprunt.getStatut();
            
            if (emprunt.getStatut().equals("EN_COURS") && emprunt.estEnRetard()) {
                statut = "   EN RETARD";
            } else if (emprunt.getStatut().equals("EN_COURS")) {
                statut = "EN COURS";
            } else {
                statut = "   RETOURNÉ";
            }
            
            System.out.printf("║ %-3d ║ %-20s ║ %-20s ║ %-12s ║ %-11s ║\n",
                emprunt.getIdEmprunt(), titreLivre, nomMembre, 
                emprunt.getDateEmprunt(), statut);
        }
        System.out.println("╚═════╩════════════════════════╩════════════════════════╩══════════════╩═════════════╝");
    }
    
    /**
     * Afficher les emprunts actifs d'un membre
     */
    public void afficherEmpruntsActifsMembre(int membreId) {
        Membre membre = gestionMembre != null ? 
            gestionMembre.chercherMembreParId(membreId) : null;
        
        if (membre == null) {
            System.out.println("   Membre non trouvé!");
            return;
        }
        
        List<Emprunt> empruntsActifs = emprunts.stream()
            .filter(e -> e.getMembreId() == membreId && e.getStatut().equals("EN_COURS"))
            .collect(Collectors.toList());
        
        if (empruntsActifs.isEmpty()) {
            System.out.println("\n   " + membre.getNom() + " n'a aucun emprunt en cours.");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    EMPRUNTS EN COURS DE " + tronquer(membre.getNom().toUpperCase(), 30) + "║");
        System.out.println("╠═════╦════════════════════════╦══════════════╦══════════════╦════════════════╣");
        System.out.println("║ ID  ║ Livre                   ║ Date emprunt ║ Retour prévu ║ Retard         ║");
        System.out.println("╠═════╬════════════════════════╬══════════════╬══════════════╬════════════════╣");
        
        for (Emprunt emprunt : empruntsActifs) {
            Livre livre = bibliothequeService.rechercherParId(emprunt.getLivreId());
            String retard = emprunt.estEnRetard() ? 
                "-> " + emprunt.getJoursRetard() + " jours" : "  Dans les délais";
            
            System.out.printf("║ %-3d ║ %-20s ║ %-12s ║ %-12s ║ %-14s ║\n",
                emprunt.getIdEmprunt(),
                tronquer(livre.getTitre(), 20),
                emprunt.getDateEmprunt(),
                emprunt.getDateRetourPrevue(),
                retard);
        }
        System.out.println("╚═════╩════════════════════════╩══════════════╩══════════════╩════════════════╝");
    }
    
    /**
     * Afficher la liste des emprunts en retard
     */
    public void afficherEmpruntsEnRetard() {
        List<Emprunt> empruntsRetard = listerEmpruntsEnRetard();
        
        if (empruntsRetard.isEmpty()) {
            System.out.println("\n  Aucun emprunt en retard! Tous les retours sont dans les délais.");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                               LISTE DES EMPRUNTS EN RETARD                         ║");
        System.out.println("╠═════╦════════════════════════╦════════════════════════╦══════════════╦═════════════╣");
        System.out.println("║ ID  ║ Livre                   ║ Membre                 ║ Jours retard ║ Pénalités   ║");
        System.out.println("╠═════╬════════════════════════╬════════════════════════╬══════════════╬═════════════╣");
        
        for (Emprunt emprunt : empruntsRetard) {
            Livre livre = bibliothequeService.rechercherParId(emprunt.getLivreId());
            Membre membre = gestionMembre.chercherMembreParId(emprunt.getMembreId());
            long joursRetard = emprunt.getJoursRetard();
            double penalites = joursRetard * Emprunt.PENALITE_PAR_JOUR;
            
            System.out.printf("║ %-3d ║ %-20s ║ %-20s ║ %-12d ║ %-9.2f€ ║\n",
                emprunt.getIdEmprunt(),
                tronquer(livre.getTitre(), 20),
                tronquer(membre.getNom(), 20),
                joursRetard, penalites);
        }
        System.out.println("╚═════╩════════════════════════╩════════════════════════╩══════════════╩═════════════╝");
        
        // Afficher le total des pénalités
        double totalPenalites = empruntsRetard.stream()
            .mapToDouble(e -> e.getJoursRetard() * Emprunt.PENALITE_PAR_JOUR)
            .sum();
        System.out.println("\n  Total des pénalités dues: " + totalPenalites + "€");
    }
    
    /**
     * Calculer les pénalités totales de tous les emprunts
     */
    public double calculerPenalitesTotales() {
        return emprunts.stream()
            .mapToDouble(Emprunt::getPenalitesCalculees)
            .sum();
    }
    
    /**
     * Obtenir le nombre total d'emprunts
     */
    public int getNombreTotalEmprunts() {
        return emprunts.size();
    }
    
    /**
     * Obtenir le nombre d'emprunts actifs
     */
    public int getNombreEmpruntsActifs() {
        return (int) emprunts.stream()
            .filter(e -> e.getStatut().equals("EN_COURS"))
            .count();
    }
    
    /**
     * Obtenir le nombre d'emprunts terminés
     */
    public int getNombreEmpruntsTermines() {
        return (int) emprunts.stream()
            .filter(e -> e.getStatut().equals("RETOURNE"))
            .count();
    }
    
    /**
     * Afficher les statistiques des emprunts
     */
    public void afficherStatistiques() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    STATISTIQUES DES EMPRUNTS                    ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Total emprunts : %-47d ║\n", getNombreTotalEmprunts());
        System.out.printf("║ Emprunts actifs : %-46d ║\n", getNombreEmpruntsActifs());
        System.out.printf("║ Emprunts terminés : %-45d ║\n", getNombreEmpruntsTermines());
        System.out.printf("║ Emprunts en retard : %-44d ║\n", listerEmpruntsEnRetard().size());
        System.out.printf("║ Pénalités totales : %-44.2f€ ║\n", calculerPenalitesTotales());
        
        // Taux de retour dans les délais
        if (getNombreEmpruntsTermines() > 0) {
            int retards = (int) emprunts.stream()
                .filter(e -> e.getStatut().equals("RETOURNE") && e.getPenalitesCalculees() > 0)
                .count();
            int sansRetard = getNombreEmpruntsTermines() - retards;
            double taux = (sansRetard * 100.0) / getNombreEmpruntsTermines();
            System.out.printf("║ Retours sans pénalité : %-42.1f%% ║\n", taux);
        }
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
    }
    
    /**
     * Vérifier si un membre peut emprunter
     */
    public boolean peutEmprunter(int membreId) {
        Membre membre = gestionMembre.chercherMembreParId(membreId);
        if (membre == null) return false;
        
        int empruntsActifs = getNombreEmpruntsActifsParMembre(membreId);
        boolean pasDeRetard = !aDesEmpruntsEnRetard(membreId);
        boolean pasTropEmprunts = empruntsActifs < 3;
        
        if (!pasTropEmprunts) {
            System.out.println("-> " + membre.getNom() + " a déjà " + empruntsActifs + " emprunts (max 3)");
        }
        if (!pasDeRetard) {
            System.out.println("-> " + membre.getNom() + " a des emprunts en retard!");
        }
        
        return pasTropEmprunts && pasDeRetard;
    }
    
    /**
     * Vérifier si un membre a des emprunts en retard
     */
    public boolean aDesEmpruntsEnRetard(int membreId) {
        return emprunts.stream()
            .anyMatch(e -> e.getMembreId() == membreId && 
                          e.getStatut().equals("EN_COURS") && 
                          e.estEnRetard());
    }
    
    /**
     * Obtenir la liste de tous les emprunts
     */
    public List<Emprunt> getTousLesEmprunts() {
        return new ArrayList<>(emprunts);
    }
    
    /**
     * Supprimer un emprunt (admin seulement)
     */
    public boolean supprimerEmprunt(int idEmprunt) {
        Emprunt emprunt = emprunts.stream()
            .filter(e -> e.getIdEmprunt() == idEmprunt)
            .findFirst()
            .orElse(null);
        
        if (emprunt == null) {
            System.out.println("  Emprunt non trouvé!");
            return false;
        }
        
        if (emprunt.getStatut().equals("EN_COURS")) {
            System.out.println("  Impossible de supprimer un emprunt en cours!");
            return false;
        }
        
        emprunts.remove(emprunt);
        System.out.println("  Emprunt #" + idEmprunt + " supprimé de l'historique!");
        return true;
    }
    
    // Méthode utilitaire pour tronquer les textes
    private String tronquer(String texte, int longueur) {
        if (texte == null) return "";
        if (texte.length() <= longueur) return texte;
        return texte.substring(0, longueur - 3) + "...";
    }
}

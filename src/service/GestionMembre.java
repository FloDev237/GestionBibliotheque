package service;

import model.Membre;
import model.Livre;
import java.util.*;
import java.util.stream.Collectors;

public class GestionMembre {
    
    // Liste pour stocker tous les membres
    private List<Membre> membres;
    private int prochainId;
    
    // Constructeur
    public GestionMembre() {
        this.membres = new ArrayList<>();
        this.prochainId = 1;
    }
    
    // Méthode pour initialisation directe (sans Scanner)
    public void inscrireMembreDirecte(Membre membre) {
        membres.add(membre);
        prochainId = membre.getIdMembre() + 1;
    }

    /**
     * Inscrire un nouveau membre
     */
    public boolean inscrireMembre(String nom, String email, String adresse, int telephone) {

        // Vérifier si l'email existe déjà
        if (chercherMembreParEmail(email) != null) {
            System.out.println(" Erreur: Un membre avec cet email existe déjà!");
            return false;
        }
        
        // Valider les données
        
        
        if (!validerEmail(email)) {
            System.out.println(" Erreur: Email invalide!");
            return false;
        }
        
        if (!validerTelephone(telephone)) {
            System.out.println(" Erreur: Téléphone invalide!");
            return false;
        }
        
        // Créer le membre
        String dateInscription = java.time.LocalDate.now().toString();
        Membre membre = new Membre(prochainId++, nom, email, adresse, telephone, dateInscription);
        membres.add(membre);
        
        System.out.println("✅ Membre inscrit avec succès!");
        System.out.println("   ID: " + membre.getIdMembre());
        System.out.println("   Nom: " + membre.getNom());
        System.out.println("   Date d'inscription: " + dateInscription);
        
        return true;
    }
    
    /**
     * Supprimer un membre
     */
    public boolean supprimerMembre(int idMembre) {
        Membre membre = chercherMembreParId(idMembre);
        
        if (membre == null) {
            System.out.println(" Erreur: Membre non trouvé!");
            return false;
        }
        
        // Vérifier si le membre a des emprunts en cours
        if (!membre.getLivresEmpruntes().isEmpty()) {
            System.out.println(" Erreur: Impossible de supprimer le membre car il a " + 
                             membre.getLivresEmpruntes().size() + " livre(s) emprunté(s)!");
            return false;
        }
        
        membres.remove(membre);
        System.out.println("✅ Membre '" + membre.getNom() + "' supprimé avec succès!");
        return true;
    }
    
    /**
     * Modifier les informations d'un membre
     */
    public boolean modifierMembre(int idMembre, String nom, String email, String adresse, int telephone) {
        Membre membre = chercherMembreParId(idMembre);
    
        if (membre == null) {
            System.out.println(" Erreur: Membre non trouvé!");
            return false;
        }
    
        // Vérifier si le nouvel email n'est pas déjà utilisé par un autre membre
        Membre membreExistant = chercherMembreParEmail(email);
        if (membreExistant != null && membreExistant.getIdMembre() != idMembre) {
            System.out.println(" Erreur: Cet email est déjà utilisé par un autre membre!");
            return false;
        }
    
        // Mettre à jour les informations
        if (nom != null && !nom.trim().isEmpty()) {
            membre.setNom(nom);           // ← corrigé
        }
        if (email != null && !email.trim().isEmpty()) {
            membre.setEmail(email);       // ← ajouté
        }
        if (adresse != null && !adresse.trim().isEmpty()) {
            membre.setAdresse(adresse);   // ← ajouté
        }
        if (validerTelephone(telephone)) {
            membre.setTel(telephone);     // ← ajouté
        }
    
        System.out.println("Membre modifié avec succès!");
        return true;
    }
    
    /**
     * Chercher un membre par son ID
     */
    public Membre chercherMembreParId(int idMembre) {
        return membres.stream()
            .filter(m -> m.getIdMembre() == idMembre)
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Chercher un membre par son email
     */
    public Membre chercherMembreParEmail(String email) {
        return membres.stream()
            .filter(m -> m.getEmail().equalsIgnoreCase(email))
            .findFirst()
            .orElse(null);
    }
    
    
    /**
     * Lister tous les membres
     */
    public List<Membre> listerTousLesMembres() {
        return new ArrayList<>(membres);
    }
    
    /**
     * Afficher tous les membres
     */
    public void afficherTousLesMembres() {
        if (membres.isEmpty()) {
            System.out.println(" Aucun membre inscrit dans la bibliothèque.");
            return;
        }
        
        System.out.println("\n=== LISTE DES MEMBRES (" + membres.size() + " inscrits) ===");
        System.out.println("┌─────┬────────────────────────┬──────────────────────────┬─────────────┐");
        System.out.println("│ ID  │ Nom                    │ Email                    │ Téléphone   │");
        System.out.println("├─────┼────────────────────────┼──────────────────────────┼─────────────┤");
        
        for (Membre membre : membres) {
            System.out.printf("│ %-3d │ %-22s │ %-24s │ %-11d │\n",
                membre.getIdMembre(),
                membre.getNom(),
                membre.getEmail(),
                membre.getTel()
            );
        }
        
        System.out.println("└─────┴────────────────────────┴──────────────────────────┴─────────────┘");
    }
    
    /**
     * Afficher les détails d'un membre spécifique
     */
    public void afficherDetailsMembre(int idMembre) {
        Membre membre = chercherMembreParId(idMembre);
        
        if (membre == null) {
            System.out.println("Membre non trouvé!");
            return;
        }
        
        System.out.println("\n=== DÉTAILS DU MEMBRE ===");
        System.out.println("┌─────────────────────────────────────────┐");
        System.out.println("│ ID: " + membre.getIdMembre());
        System.out.println("│ Nom: " + membre.getNom());
        System.out.println("│ Email: " + membre.getEmail());
        System.out.println("│ Adresse: " + membre.getAdresse());
        System.out.println("│ Téléphone: " + membre.getTel());
        System.out.println("│ Date d'inscription: " + membre.getDateInscription());
        System.out.println("│ Livres empruntés: " + membre.getLivresEmpruntes().size());
        
        if (!membre.getLivresEmpruntes().isEmpty()) {
            System.out.println("│");
            System.out.println("│ Livres actuellement empruntés:");
            for (Livre livre : membre.getLivresEmpruntes()) {
                System.out.println("│   📖 " + livre.getTitre());
            }
        }
        System.out.println("└─────────────────────────────────────────┘");
    }
    
    /**
     * Obtenir le nombre total de membres
     */
    public int getNombreTotalMembres() {
        return membres.size();
    }
    
    /**
     * Obtenir les membres actifs (ceux qui ont des emprunts)
     */
    public List<Membre> getMembresActifs() {
        return membres.stream()
            .filter(m -> !m.getLivresEmpruntes().isEmpty())
            .collect(Collectors.toList());
    }
    
    /**
     * Obtenir les membres inactifs (sans emprunts)
     */
    public List<Membre> getMembresInactifs() {
        return membres.stream()
            .filter(m -> m.getLivresEmpruntes().isEmpty())
            .collect(Collectors.toList());
    }
    
    
    // Méthodes de validation
    
    
    private boolean validerEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    private boolean validerTelephone(int telephone) {
        String telStr = String.valueOf(telephone);
        return telStr.length() >= 8 && telStr.length() <= 15;
    }
    
    
    
    /**
     * Sauvegarder les membres dans un fichier (optionnel)
     */
    public void sauvegarderDansFichier(String nomFichier) {
        // À implémenter avec FileManager
        System.out.println(" Sauvegarde des membres dans " + nomFichier);
    }
    
    /**
     * Charger les membres depuis un fichier (optionnel)
     */
    public void chargerDepuisFichier(String nomFichier) {
        // À implémenter avec FileManager
        System.out.println(" Chargement des membres depuis " + nomFichier);
    }
}

public class EmpruntService {

    private static final double PENALITE_PAR_JOUR = 500;

    // 1. Emprunter un livre
    public void emprunterLivre(Livre livre, Utilisateur utilisateur) {
        if (livre.isDisponible()) {
            livre.setDisponible(false);
            System.out.println(utilisateur.getNom() + " a emprunté : " + livre.getTitre());
        } else {
            System.out.println("Livre non disponible !");
        }
    }

    // 2. Retourner un livre
    public void retournerLivre(Livre livre) {
        livre.setDisponible(true);
        System.out.println("Livre retourné : " + livre.getTitre());
    }

    // 3. Vérifier disponibilité
    public boolean verifierDisponibilite(Livre livre) {
        return livre.isDisponible();
    }

    // 4. Calculer pénalité de retard
    public double calculerPenalite(int joursRetard) {
        if (joursRetard <= 0) {
            return 0;
        }
        return joursRetard * PENALITE_PAR_JOUR;
    }
}

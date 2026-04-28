package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprunt {
    //attributs
    private int idEmprunt;
    private int livreId;
    private int membreId;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourReelle;
    private double penalitesCalculees;
    private String statut;

    // Constantes
    public static final int DUREE_EMPRUNT_JOURS = 15;
    public static final double PENALITE_PAR_JOUR = 0.50;

    // Constructeur
    public Emprunt(int idEmprunt, int livreId, int membreId){
        this.idEmprunt = idEmprunt;
        this.livreId = livreId;
        this.membreId = membreId;
        this.dateEmprunt = LocalDate.now();
        this.dateRetourPrevue = dateEmprunt.plusDays(DUREE_EMPRUNT_JOURS);
        this.penalitesCalculees = 0.0;
        this.statut = "EN_COURS";

    }

    // Getters
    public int getIdEmprunt() { return idEmprunt; }
    public int getLivreId() { return livreId; }
    public int getMembreId() { return membreId; }
    public LocalDate getDateEmprunt() { return dateEmprunt; }
    public LocalDate getDateRetourPrevue() { return dateRetourPrevue; }
    public LocalDate getDateRetourReelle() { return dateRetourReelle; }
    public double getPenalitesCalculees() { return penalitesCalculees; }
    public String getStatut() { return statut;}
     

    //Methodes
    public void retourner() {
        this.dateRetourReelle = LocalDate.now();
        calculerPenalites(); 
        this.statut = "RETOURNE";
    }


    public boolean estEnRetard() {
        return statut.equals("EN_COURS") && LocalDate.now().isAfter(dateRetourPrevue);
    }



    private void calculerPenalites() {
        if (dateRetourReelle != null && dateRetourReelle.isAfter(dateRetourPrevue)) {
            long joursRetard = ChronoUnit.DAYS.between(dateRetourPrevue, dateRetourReelle);
            this.penalitesCalculees = joursRetard * PENALITE_PAR_JOUR;
        }
    }

      public long getJoursRetard() {
        if (!estEnRetard()) return 0;
        return ChronoUnit.DAYS.between(dateRetourPrevue, LocalDate.now());
    }

      @Override
    public String toString() {
        return String.format(
            "Emprunt #%d | Livre: %d | Membre: %d | Date: %s | Retour prévu: %s | %s",
            idEmprunt, livreId, membreId, dateEmprunt, dateRetourPrevue, statut
        );
    }

}

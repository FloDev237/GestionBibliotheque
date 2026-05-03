/*javac -d bin src/model/*.java src/service/*.java src/ui/*.java src/Main.java
 java -cp bin Main*/
package model;

import java.util.ArrayList;
import java.util.List;

public class Membre {
    private int idMembre;
    private String nom;
    private String email;
    private String adresse;
    private int tel;
    private String dateInscription;
    private List<Livre> livresEmpruntes;
    
    public Membre(int idMembre, String nom, String email, String adresse, int tel, String dateInscription){
        this.idMembre = idMembre;
        this.nom = nom;
        this.email = email;
        this.adresse = adresse;
        this.tel = tel;
        this.dateInscription = dateInscription;
        this.livresEmpruntes = new ArrayList<>();
    }
    public int getIdMembre(){
        return idMembre;
    }
    public String getNom(){
        return nom;
    }
    public String getEmail(){
        return email;
    }

    public String getAdresse(){
        return adresse;
    }
    public int getTel(){
        return tel;
    }
    public String getDateInscription(){
        return dateInscription;
    }
    public void setNom(String nom){
        this.nom = nom;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setAdresse(String adresse){
        this.adresse = adresse;
    }
    public void setTel(int tel){
        this.tel = tel;
    }
    public void setDateInscription(String dateInscription){
        this.dateInscription = dateInscription;
    }
    public void setLivresEmpruntes(List<Livre> livresEmpruntes){
        this.livresEmpruntes = livresEmpruntes;
    }
    public List<Livre> getLivresEmpruntes(){
        return livresEmpruntes;
    }
    public void emprunterLivre(Livre livre){
        if (livre.getDisponibilite()) {
            livre.emprunter();
            livresEmpruntes.add(livre);
        } else {
            System.out.println("Livre non disponible pour emprunt.");
        }
    }
    public void retournerLivre(Livre livre){
        if (livresEmpruntes.remove(livre)) {
            livre.retourner();
        } else {
            System.out.println("Ce livre n'est pas emprunté par ce membre.");
        }
    }
    @Override
    public String toString(){
        return "Membre{" +
                "idMembre=" + idMembre +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", adresse='" + adresse + '\'' +
                ", tel=" + tel +
                ", dateInscription='" + dateInscription + '\'' +
                '}';
    }
}

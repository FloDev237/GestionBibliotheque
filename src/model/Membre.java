package model;

import java.util.ArrayList;
import java.util.List;

public class Membre {
    private int idMembre;
    private String nom;
    private String email;
    private String adresse;
    private int tel;
    private List<Livre> livresEmpruntes;
    
    public Membre(int idMembre, string nom, String email, String adresse, int tel){
        this.idMembre = idMembre;
        this.nom = nom;
        this.email = email;
        this.adresse = adresse;
        this.tel = tel;
        this.livresEmpruntes = new Arraylist<>();
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
    public List<Livre> getLivresEmpruntes(){
        return livresEmpruntes;
    }
    public void emprunterLivre(Livre livre){
        if (livre.isDisponible()) {
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
                '}';
    }
}

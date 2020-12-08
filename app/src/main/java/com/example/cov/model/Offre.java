package com.example.cov.model;

public class Offre {

    private String full_name;
    private Integer prix;
    private String heure_depart;
    private String description;
    private Integer telephone;
    private Integer nombre_place;
    private String adresse_depart;
    private String adresse_destination;
    private String email;

    public Offre() {
    }

    public Offre(String fullName, Integer prix, String heure_depart, String description, Integer telephone, Integer nombre_place, String adresse_depart, String adresse_destination, String email) {
        this.full_name = fullName;
        this.prix = prix;
        this.heure_depart = heure_depart;
        this.description = description;
        this.telephone = telephone;
        this.nombre_place = nombre_place;
        this.adresse_depart = adresse_depart;
        this.adresse_destination = adresse_destination;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public Integer getTelephone() {
        return telephone;
    }

    public void setTelephone(Integer telephone) {
        this.telephone = telephone;
    }

    public String getAdresse_destination() {
        return adresse_destination;
    }

    public void setAdresse_destination(String adresse_destination) {
        this.adresse_destination = adresse_destination;
    }

    public String getAdresse_depart() {
        return adresse_depart;
    }

    public void setAdresse_depart(String adresse_depart) {
        this.adresse_depart = adresse_depart;
    }

    public String getHeure_depart() {
        return heure_depart;
    }

    public void setHeure_depart(String heure_depart) {
        this.heure_depart = heure_depart;
    }

    public Integer getNombre_place() {
        return nombre_place;
    }

    public void setNombre_place(Integer nombre_place) {
        this.nombre_place = nombre_place;
    }

    public Integer getPrix() {
        return prix;
    }

    public void setPrix(Integer prix) {
        this.prix = prix;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

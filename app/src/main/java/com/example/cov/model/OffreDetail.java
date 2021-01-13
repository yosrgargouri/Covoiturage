package com.example.cov.model;

public class OffreDetail extends Offre {

    private String key;

    public OffreDetail(String fullName, Integer prix, String heureDepart, String description, Integer telephone, Integer nombrePlace, String adresseDepart, String adresseDestination, String email) {
        this.setAdresse_depart(adresseDepart);
        this.setAdresse_destination(adresseDestination);
        this.setDescription(description);
        this.setEmail(email);
        this.setFull_name(fullName);
        this.setHeure_depart(heureDepart);
        this.setNombre_place(nombrePlace);
        this.setPrix(prix);
        this.setTelephone(telephone);
    }

    public OffreDetail(Offre offre) {
        this.setAdresse_depart(offre.getAdresse_depart());
        this.setAdresse_destination(offre.getAdresse_destination());
        this.setDescription(offre.getDescription());
        this.setEmail(offre.getEmail());
        this.setFull_name(offre.getFull_name());
        this.setHeure_depart(offre.getHeure_depart());
        this.setNombre_place(offre.getNombre_place());
        this.setPrix(offre.getPrix());
        this.setTelephone(offre.getTelephone());
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

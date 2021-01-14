package com.example.cov.model;

public class Request {

    private String email_request;
    private String offre_key;
    private Integer nombre_place;

    public Request(String email_request, String offre_key, Integer nombre_place) {
        this.email_request = email_request;
        this.offre_key = offre_key;
        this.nombre_place = nombre_place;
    }

    public Request() {
    }

    public String getEmail_request() {
        return email_request;
    }

    public void setEmail_request(String email_request) {
        this.email_request = email_request;
    }

    public String getOffre_key() {
        return offre_key;
    }

    public void setOffre_key(String offre_key) {
        this.offre_key = offre_key;
    }

    public Integer getNombre_place() {
        return nombre_place;
    }

    public void setNombre_place(Integer nombre_place) {
        this.nombre_place = nombre_place;
    }
}

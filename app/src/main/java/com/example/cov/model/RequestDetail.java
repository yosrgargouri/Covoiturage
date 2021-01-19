package com.example.cov.model;

public class RequestDetail {

    private String email_request;
    private String request_key;
    private String offre_key;
    private String titleOffre;
    private String status;
    private Integer nombre_place;
    private Offre offre;

    public RequestDetail(String titleOffre, String email_request, Integer nombre_place) {
        this.email_request = email_request;
        this.titleOffre = titleOffre;
        this.nombre_place = nombre_place;
    }
    public RequestDetail(String titleOffre, String email_request, Integer nombre_place, String status) {
        this.email_request = email_request;
        this.titleOffre = titleOffre;
        this.nombre_place = nombre_place;
        this.status = status;
    }

    public RequestDetail(Request request) {
        this.setOffre_key(request.getOffre_key());
        this.setEmail_request(request.getEmail_request());
        this.setNombre_place(request.getNombre_place());
        this.setStatus(request.getStatus());
    }

    public String getRequest_key() {
        return request_key;
    }

    public void setRequest_key(String request_key) {
        this.request_key = request_key;
    }

    public String getTitleOffre() {
        return titleOffre;
    }

    public void setTitleOffre(String titleOffre) {
        this.titleOffre = titleOffre;
    }

    public RequestDetail(String titleOffre) {
        this.titleOffre = titleOffre;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Offre getOffre() {
        return offre;
    }

    public void setOffre(Offre offre) {
        this.offre = offre;
    }
}

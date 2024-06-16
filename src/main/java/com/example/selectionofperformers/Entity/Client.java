package com.example.selectionofperformers.Entity;


public class Client {
    private String idClient;
    private String clientName;
    private String clientEmail;
    private String clientPhone;

    public Client(String idClient, String clientName, String clientEmail, String clientPhone) {
        this.idClient = idClient;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientPhone = clientPhone;
    }

    public String getClientId() {
        return idClient ;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientId() {
        this.idClient = idClient;
    }

    public void setClientName() {
        this.clientName = clientName;
    }

    public void setClientEmail() {
        this.clientEmail = clientEmail;
    }

    public void setClientPhone() {
        this.clientPhone = clientPhone;
    }
}

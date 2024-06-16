package com.example.selectionofperformers.Entity;

public class Developer {
    private String idDeveloper;
    private String developerName;
    private String developerEmail;
    private String developerPhone;
    private Float developerRating;
    private String developerStatus;

    public Developer(String idDeveloper, String developerName, String developerEmail, String developerPhone, Float developerRating, String developerStatus) {
        this.idDeveloper = idDeveloper;
        this.developerName = developerName;
        this.developerEmail = developerEmail;
        this.developerPhone = developerPhone;
        this.developerRating = developerRating;
        this.developerStatus = developerStatus;
    }

    public String getDeveloperId() {
        return idDeveloper ;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public String getDeveloperEmail() {
        return developerEmail;
    }

    public String getDeveloperPhone() {
        return developerPhone;
    }
    public Float getDeveloperRating() {
        return developerRating;
    }
    public String getDeveloperStatus() {
        return developerStatus;
    }
}

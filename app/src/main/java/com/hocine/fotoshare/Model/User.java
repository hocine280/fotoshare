package com.hocine.fotoshare.Model;

public class User {
    private String id;
    private String nom;
    private String prenom;
    private String imageurl;
    private String bio;

    public User(String id, String nom, String prenom, String imageurl, String bio){
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.imageurl = imageurl;
        this.bio = bio;
    }

    public User(){
    }

    /**
     * Getter
     */
    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getBio() {
        return bio;
    }

    /**
     * Setter
     */
    public void setId(String id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}

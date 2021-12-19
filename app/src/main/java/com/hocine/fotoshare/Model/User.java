package com.hocine.fotoshare.Model;

/**
 * Modèle de la table Users, cette classe permet de gérer les utilisateurs
 *
 * @author Hocine
 * @version 1.0
 */
public class User {
    /**
     * Attribut de la classe
     */
    private String id;
    private String nom;
    private String prenom;
    private String imageurl;
    private String bio;

    /**
     * Constructeur par initialisation
     *
     * @param id
     * @param nom
     * @param prenom
     * @param imageurl
     * @param bio
     */
    public User(String id, String nom, String prenom, String imageurl, String bio) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.imageurl = imageurl;
        this.bio = bio;
    }

    /**
     * Constructeur par défaut
     */
    public User() {
    }

    /**
     * Getter id
     */
    public String getId() {
        return id;
    }

    /**
     * Getter nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Getter prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Getter imageurl
     */
    public String getImageurl() {
        return imageurl;
    }

    /**
     * Getter bio
     */
    public String getBio() {
        return bio;
    }

    /**
     * Setter id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Setter nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Setter prenom
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Setter imageurl
     */
    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    /**
     * Setter bio
     */
    public void setBio(String bio) {
        this.bio = bio;
    }
}

package com.hocine.fotoshare.Model;

/**
 * Modèle de la table Posts, cette classe permet de gérer les posts/publications
 *
 * @author Hocine
 * @version 1.0
 */
public class Post {
    /**
     * Attribut de la classe
     */
    private String postid;
    private String postimage;
    private String description;
    private String publisher;

    /**
     * Constructeur par initialisation
     */
    public Post(String postid, String postimage, String description, String publisher) {
        this.postid = postid;
        this.postimage = postimage;
        this.description = description;
        this.publisher = publisher;
    }

    /**
     * Constructeur par défaut
     */
    public Post() {
    }

    /**
     * Getter postid
     *
     * @return
     */
    public String getPostid() {
        return postid;
    }

    /**
     * Setter postid
     *
     * @param postid
     */
    public void setPostid(String postid) {
        this.postid = postid;
    }

    /**
     * Getter postimage
     *
     * @return
     */
    public String getPostimage() {
        return postimage;
    }

    /**
     * Setter postimage
     *
     * @param postimage
     */
    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    /**
     * Getter description
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter description
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter publisher
     *
     * @return
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Setter publisher
     *
     * @param publisher
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}

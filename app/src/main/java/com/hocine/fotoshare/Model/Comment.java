package com.hocine.fotoshare.Model;

/**
 * Modèle de la table Comments, cette classe permet de gérer les commentaires
 *
 * @author Hocine
 * @version 1.0
 */
public class Comment {
    /**
     * Attribut de la classe
     */
    private String comment;
    private String publisher;
    private String commentid;

    /**
     * Constructeur par initialisation
     *
     * @param comment
     * @param publisher
     * @param commentid
     */
    public Comment(String comment, String publisher, String commentid) {
        this.comment = comment;
        this.publisher = publisher;
        this.commentid = commentid;
    }

    /**
     * Constructeur par défaut
     */
    public Comment() {
    }

    /**
     * Getter comment
     *
     * @return
     */
    public String getComment() {
        return comment;
    }

    /**
     * Setter comment
     *
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
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

    /**
     * Getter commentid
     *
     * @return
     */
    public String getCommentid() {
        return commentid;
    }

    /**
     * Setter commentid
     *
     * @param commentid
     */
    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }
}

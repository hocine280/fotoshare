package com.hocine.fotoshare.Model;

/**
 * Modèle de la table Notifications, cette classe permet de gérer les notifications
 *
 * @author Hocine
 * @version 1.0
 */
public class Notification {

    /**
     * Attribut de la classe
     */
    private String userid;
    private String text;
    private String postid;
    private boolean ispost;

    /**
     * Constructeur par initialisation
     *
     * @param userid
     * @param text
     * @param postid
     * @param ispost
     */
    public Notification(String userid, String text, String postid, boolean ispost) {
        this.userid = userid;
        this.text = text;
        this.postid = postid;
        this.ispost = ispost;
    }

    /**
     * Constructeur par défaut
     */
    public Notification() {

    }

    /**
     * Getter userid
     *
     * @return
     */
    public String getUserid() {
        return userid;
    }

    /**
     * Setter userid
     *
     * @param userid
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * Getter text
     *
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * Getter text
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
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
     * Getter ispost
     *
     * @return
     */
    public boolean isIspost() {
        return ispost;
    }

    /**
     * Setter ispost
     *
     * @param ispost
     */
    public void setIspost(boolean ispost) {
        this.ispost = ispost;
    }
}

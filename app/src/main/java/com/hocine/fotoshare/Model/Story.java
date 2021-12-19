package com.hocine.fotoshare.Model;

/**
 * Modèle de la table Story, cette classe permet de gérer les stories
 *
 * @author Hocine
 * @version 1.0
 */
public class Story {

    /**
     * Attribut de la classe
     */
    private String imageurl;
    private long timestart;
    private long timeend;
    private String storyid;
    private String userid;

    /**
     * Constructeur par initialisation
     *
     * @param imageurl
     * @param timestart
     * @param timeend
     * @param storyid
     * @param userid
     */
    public Story(String imageurl, long timestart, long timeend, String storyid, String userid) {
        this.imageurl = imageurl;
        this.timestart = timestart;
        this.timeend = timeend;
        this.storyid = storyid;
        this.userid = userid;
    }

    /**
     * Constructeur par défaut
     */
    public Story() {
    }

    /**
     * Getter imageurl
     *
     * @return
     */
    public String getImageurl() {
        return imageurl;
    }

    /**
     * Setter imageurl
     *
     * @param imageurl
     */
    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    /**
     * Getter timestart
     *
     * @return
     */
    public long getTimestart() {
        return timestart;
    }

    /**
     * Setter timestart
     *
     * @param timestart
     */
    public void setTimestart(long timestart) {
        this.timestart = timestart;
    }

    /**
     * Getter timeend
     *
     * @return
     */
    public long getTimeend() {
        return timeend;
    }

    /**
     * Setter timeend
     *
     * @param timeend
     */
    public void setTimeend(long timeend) {
        this.timeend = timeend;
    }

    /**
     * Getter storyid
     *
     * @return
     */
    public String getStoryid() {
        return storyid;
    }

    /**
     * Setter storyid
     *
     * @param storyid
     */
    public void setStoryid(String storyid) {
        this.storyid = storyid;
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
}

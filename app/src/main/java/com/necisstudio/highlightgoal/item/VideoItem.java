package com.necisstudio.highlightgoal.item;

/**
 * Created by Jarod on 2015-10-23.
 */
public class VideoItem {
    String title;
    String urlimage;
    String idvideo;
    String date;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {

        return date;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {

        return author;
    }

    String author;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrlimage(String urlimage) {
        this.urlimage = urlimage;
    }

    public void setIdvideo(String idvideo) {
        this.idvideo = idvideo;
    }

    public String getTitle() {

        return title;
    }

    public String getUrlimage() {
        return urlimage;
    }

    public String getIdvideo() {
        return idvideo;
    }
}

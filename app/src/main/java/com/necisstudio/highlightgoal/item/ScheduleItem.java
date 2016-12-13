package com.necisstudio.highlightgoal.item;

import io.realm.RealmObject;

/**
 * Created by Jarod on 2015-10-21.
 */
public class ScheduleItem extends RealmObject{
    String id;
    String hometeam;
    String awayteam;
    String idhome;
    String idaway;
    String homescore;
    String awayscore;
    String urlhome;
    String date;
    String clubcategory;
    String latest;

    public String getLatest() {
        return latest;
    }

    public void setLatest(String latest) {
        this.latest = latest;
    }

    int logo;

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getClubcategory() {
        return clubcategory;
    }

    public void setClubcategory(String clubcategory) {
        this.clubcategory = clubcategory;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {

        return date;
    }

    public void setUrlhome(String urlhome) {
        this.urlhome = urlhome;
    }

    public void setUrlaway(String urlaway) {
        this.urlaway = urlaway;
    }

    public String getUrlaway() {

        return urlaway;
    }

    public String getUrlhome() {
        return urlhome;
    }

    String urlaway;

    public void setId(String id) {
        this.id = id;
    }

    public void setHometeam(String hometeam) {
        this.hometeam = hometeam;
    }

    public void setAwayteam(String awayteam) {
        this.awayteam = awayteam;
    }

    public void setIdhome(String idhome) {
        this.idhome = idhome;
    }

    public void setIdaway(String idaway) {
        this.idaway = idaway;
    }

    public void setHomescore(String homescore) {
        this.homescore = homescore;
    }

    public void setAwayscore(String awayscore) {
        this.awayscore = awayscore;
    }

    public String getId() {

        return id;
    }

    public String getHometeam() {
        return hometeam;
    }

    public String getAwayteam() {
        return awayteam;
    }

    public String getIdhome() {
        return idhome;
    }

    public String getIdaway() {
        return idaway;
    }

    public String getHomescore() {
        return homescore;
    }

    public String getAwayscore() {
        return awayscore;
    }
}

package com.necisstudio.highlightgoal.item;

/**
 * Created by Jarod on 2015-10-22.
 */
public class KlasementItem {
    String team;
    String different;
    String player;
    String point;
    String idtema;

    public void setIdtema(String idtema) {
        this.idtema = idtema;
     }
    public String getIdtema() {
        return idtema;
    }

    public void setPosisi(String posisi) {
        this.posisi = posisi;
    }

    public String getPosisi() {

        return posisi;
    }

    String posisi;

    public void setTeam(String team) {
        this.team = team;
    }

    public void setDifferent(String different) {
        this.different = different;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getTeam() {

        return team;
    }

    public String getDifferent() {
        return different;
    }

    public String getPlayer() {
        return player;
    }

    public String getPoint() {
        return point;
    }
}

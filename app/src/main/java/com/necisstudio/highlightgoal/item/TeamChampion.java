package com.necisstudio.highlightgoal.item;

/**
 * Created by Jarod on 2015-10-22.
 */
public class TeamChampion {
    String jumlah;
    String team;
    String urlteam;
    String idteam;

    public void setIdteam(String idteam) {
        this.idteam = idteam;
    }

    public String getIdteam() {

        return idteam;
    }

    public void setKeuangan(String keuangan) {
        this.keuangan = keuangan;
    }

    public String getKeuangan() {

        return keuangan;
    }

    String keuangan;

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setUrlteam(String urlteam) {
        this.urlteam = urlteam;
    }

    public String getJumlah() {

        return jumlah;
    }

    public String getTeam() {
        return team;
    }

    public String getUrlteam() {
        return urlteam;
    }
}

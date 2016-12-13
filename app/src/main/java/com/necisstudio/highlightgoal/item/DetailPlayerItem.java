package com.necisstudio.highlightgoal.item;

/**
 * Created by Jarod on 2015-10-22.
 */
public class DetailPlayerItem {
    String jumlah;
    String team;
    String urlteam;
    String number;

    public void setNegara(String negara) {
        this.negara = negara;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getNegara() {

        return negara;
    }

    public String getDob() {
        return dob;
    }

    public String getContract() {
        return contract;
    }

    String position;
    String negara;
    String dob;
    String contract;
    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {

        return position;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {

        return number;
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

package com.queenieeq.mymoney.notification;


import androidx.annotation.Keep;
import androidx.annotation.NonNull;

@Keep
public class cards {
    private String DUE;
    private String TITLE;
    private String DESC;



    private String NAME;

    @NonNull
    @Override
    public String toString() {
        return "cards{" +
                ", DUE='" + DUE + '\'' +
                ", TITLE='" + TITLE + '\'' +
                ", DESC='" + DESC + '\'' +
                ", NAME='" + NAME + '\'' +
                '}';
    }

    public cards(String DUE, String TITLE, String DESC, String NAME) {
        this.DUE = DUE;
        this.TITLE = TITLE;
        this.DESC = DESC;
        this.NAME = NAME;
    }

    public String getName() { return NAME; }

    public void setName(String NAME) { this.NAME = NAME; }

    public String getDESC() {
        return DESC;
    }

    public void setDESC(String DESC) {
        this.DESC = DESC;
    }


    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getDUE() {
        return DUE;
    }

    public void setDUE(String DUE) {
        this.DUE = DUE;
    }

    public cards() {

    }

}

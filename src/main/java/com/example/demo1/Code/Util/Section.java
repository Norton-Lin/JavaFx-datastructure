package com.example.demo1.Code.Util;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Section {
    private final StringProperty Sec = new SimpleStringProperty();
    private final StringProperty Mon_Cour = new SimpleStringProperty();
    private final StringProperty Tue_Cour = new SimpleStringProperty();
    private final StringProperty Wed_Cour = new SimpleStringProperty();
    private final StringProperty Thu_Cour = new SimpleStringProperty();
    private final StringProperty Fri_Cour = new SimpleStringProperty();
    private final StringProperty Sat_Cour = new SimpleStringProperty();
    private final StringProperty Sun_Cour = new SimpleStringProperty();

    public String getSec() {
        return Sec.get();
    }

    public StringProperty secProperty() {
        return Sec;
    }

    public void setSec(String sec) {
        this.Sec.set(sec);
    }

    public String getMon_Cour() {
        return Mon_Cour.get();
    }

    public StringProperty mon_CourProperty() {
        return Mon_Cour;
    }

    public void setMon_Cour(String mon_Cour) {
        this.Mon_Cour.set(mon_Cour);
    }

    public String getTue_Cour() {
        return Tue_Cour.get();
    }

    public StringProperty tue_CourProperty() {
        return Tue_Cour;
    }

    public void setTue_Cour(String tue_Cour) {
        this.Tue_Cour.set(tue_Cour);
    }

    public String getWed_Cour() {
        return Wed_Cour.get();
    }

    public StringProperty wed_CourProperty() {
        return Wed_Cour;
    }

    public void setWed_Cour(String wed_Cour) {
        this.Wed_Cour.set(wed_Cour);
    }

    public String getThu_Cour() {
        return Thu_Cour.get();
    }

    public StringProperty thu_CourProperty() {
        return Thu_Cour;
    }

    public void setThu_Cour(String thu_Cour) {
        this.Thu_Cour.set(thu_Cour);
    }

    public String getFri_Cour() {
        return Fri_Cour.get();
    }

    public StringProperty fri_CourProperty() {
        return Fri_Cour;
    }

    public void setFri_Cour(String fri_Cour) {
        this.Fri_Cour.set(fri_Cour);
    }

    public String getSat_Cour() {
        return Sat_Cour.get();
    }

    public StringProperty sat_CourProperty() {
        return Sat_Cour;
    }

    public void setSat_Cour(String sat_Cour) {
        this.Sat_Cour.set(sat_Cour);
    }

    public String getSun_Cour() {
        return Sun_Cour.get();
    }

    public StringProperty sun_CourProperty() {
        return Sun_Cour;
    }

    public void setSun_Cour(String sun_Cour) {
        this.Sun_Cour.set(sun_Cour);
    }
}
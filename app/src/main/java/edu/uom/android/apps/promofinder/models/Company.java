package edu.uom.android.apps.promofinder.models;

import com.google.firebase.firestore.GeoPoint;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Company implements Serializable{

    @SerializedName("name")
    private String name;
    @SerializedName("companyDescription")
    private String companyDescription;
    @SerializedName("location")
    private GeoPoint location;
    @SerializedName("openTimeSatDay")
    private String openTimeSatDay;
    @SerializedName("closeTimeSatDay")
    private String closeTimeSatDay;
    @SerializedName("openTimeSunday")
    private String openTimeSunday;
    @SerializedName("closeTimeSunDay")
    private String closeTimeSunDay;
    @SerializedName("openTimeWeek")
    private String openTimeWeek;
    @SerializedName("closeTimeWeek")
    private String closeTimeWeek;
    @SerializedName("imageUrl")
    private String imageUrl;

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public String getOpenTimeSatDay() {
        return openTimeSatDay;
    }

    public void setOpenTimeSatDay(String openTimeSatDay) {
        this.openTimeSatDay = openTimeSatDay;
    }

    public String getCloseTimeSatDay() {
        return closeTimeSatDay;
    }

    public void setCloseTimeSatDay(String closeTimeSatDay) {
        this.closeTimeSatDay = closeTimeSatDay;
    }

    public String getOpenTimeSunday() {
        return openTimeSunday;
    }

    public void setOpenTimeSunday(String openTimeSunday) {
        this.openTimeSunday = openTimeSunday;
    }

    public String getCloseTimeSunDay() {
        return closeTimeSunDay;
    }

    public void setCloseTimeSunDay(String closeTimeSunDay) {
        this.closeTimeSunDay = closeTimeSunDay;
    }

    public String getOpenTimeWeek() {
        return openTimeWeek;
    }

    public void setOpenTimeWeek(String openTimeWeek) {
        this.openTimeWeek = openTimeWeek;
    }

    public String getCloseTimeWeek() {
        return closeTimeWeek;
    }

    public void setCloseTimeWeek(String closeTimeWeek) {
        this.closeTimeWeek = closeTimeWeek;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", companyDescription='" + companyDescription + '\'' +
                ", location=" + location +
                ", openTimeSatDay='" + openTimeSatDay + '\'' +
                ", closeTimeSatDay='" + closeTimeSatDay + '\'' +
                ", openTimeSunday='" + openTimeSunday + '\'' +
                ", closeTimeSunDay='" + closeTimeSunDay + '\'' +
                ", openTimeWeek='" + openTimeWeek + '\'' +
                ", closeTimeWeek='" + closeTimeWeek + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}

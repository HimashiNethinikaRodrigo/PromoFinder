package edu.uom.android.apps.promofinder.models;

import com.google.firebase.firestore.GeoPoint;
import com.google.gson.annotations.SerializedName;
import com.koalap.geofirestore.GeoLocation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class Promo implements Serializable{

    @SerializedName("shortDescription")
    private String shortDescription;
    @SerializedName("longDescription")
    private String longDescription;
    @SerializedName("startDate")
    private Date startDate;
    @SerializedName("endDate")
    private Date endDate;
    @SerializedName("location")
    private GeoLocation location;
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("companyId")
    private String companyId;
    @SerializedName("company")
    private Company company;


    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public GeoLocation getLocation() {
        return location;
    }

    public void setLocation(GeoLocation location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Promo promo = (Promo) o;
        return Objects.equals(shortDescription, promo.shortDescription) &&
                Objects.equals(longDescription, promo.longDescription) &&
                Objects.equals(startDate, promo.startDate) &&
                Objects.equals(endDate, promo.endDate) &&
                Objects.equals(location, promo.location);
    }

    @Override
    public int hashCode() {

        return Objects.hash(shortDescription, longDescription, startDate, endDate, location);
    }

    @Override
    public String toString() {
        return "Promo{" +
                "shortDescription='" + shortDescription + '\'' +
                ", longDescription='" + longDescription + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", location=" + location +
                ", imageUrl='" + imageUrl + '\'' +
                ", companyId='" + companyId + '\'' +
                ", company=" + company +
                '}';
    }
}

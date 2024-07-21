package vk.my.code.foodie.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// This class represents a 'User' object that can be stored in a SQLite database
// using the Room persistence library.
@Entity(tableName = "users")
public class User {

    // The primary key for the 'User' object. It will be auto-generated.
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int uid;

    private String countryLocation = "";
    private String diet = "";
    private boolean firstTimeOpeningTheApp;
    private int numberOfRecipes;

    public int getNumberOfRecipes() {
        return numberOfRecipes;
    }

    public void setNumberOfRecipes(int numberOfRecipes) {
        this.numberOfRecipes = numberOfRecipes;
    }

    @NonNull
    public int getUid() {
        return uid;
    }

    public void setUid(@NonNull int uid) {
        this.uid = uid;
    }

    public String getCountryLocation() {
        return countryLocation;
    }

    public void setCountryLocation(String countryLocation) {
        this.countryLocation = countryLocation;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public boolean isFirstTimeOpeningTheApp() {
        return firstTimeOpeningTheApp;
    }

    public void setFirstTimeOpeningTheApp(boolean firstTimeOpeningTheApp) {
        this.firstTimeOpeningTheApp = firstTimeOpeningTheApp;
    }
}
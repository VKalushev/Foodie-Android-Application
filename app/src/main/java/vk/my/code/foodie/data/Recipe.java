package vk.my.code.foodie.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

// This class represents a 'Recipe' object that can be stored in a SQLite database
// using the Room persistence library.
@Entity(tableName = "recipeInfo", indices = {
        @Index(value = "name"),
        @Index(value = "category"),
        @Index(value = "isAddedToMyRecipes")
})
public class Recipe {

    // The primary key for the 'Recipe' object. It is set to be non-null and not auto-generated.
    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String recipeId;

    private String name;

    private String cookingTime;

    private String preparationTime;

    private String servings;

    private String imageURL;

    private String instructions;

    private String ingredients;

    private boolean isAddedToMyRecipes = false;

    private boolean wasVisitedPreviously = false;

    private String category;

    public boolean wasVisitedPreviously() {
        return wasVisitedPreviously;
    }

    public void setWasVisitedPreviously(boolean wasVisitedPreviously) {
        this.wasVisitedPreviously = wasVisitedPreviously;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAddedToMyRecipes() {
        return isAddedToMyRecipes;
    }

    public void setAddedToMyRecipes(boolean addedToMyRecipes) {
        isAddedToMyRecipes = addedToMyRecipes;
    }


    @NonNull
    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(@NonNull String recipeId) {
        this.recipeId = recipeId;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "recipeId='" + recipeId + '\'' +
                ", name='" + name + '\'' +
                ", cookingTime=" + cookingTime +
                ", preparationTime=" + preparationTime +
                ", imageURL='" + imageURL + '\'' +
                ", instructions='" + instructions + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", isAddedToMyRecipes=" + isAddedToMyRecipes +
                ", category='" + category + '\'' +
                '}';
    }
}




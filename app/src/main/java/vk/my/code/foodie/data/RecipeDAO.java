package vk.my.code.foodie.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Query;

import java.util.List;

// This is a DAO (Data Access Object) interface that defines
// methods to access the 'recipeInfo' table in a SQLite database.
@Dao
public interface RecipeDAO {

    @Insert
    void insert(Recipe recipe);

    @Update
    void update(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

    // Get a 'Recipe' object from the 'recipeInfo' table by its ID.
    @Query("SELECT * FROM recipeInfo WHERE recipeId = :id")
    Recipe getById(String id);

    // Get all 'Recipe' objects from the 'recipeInfo' table.
    @Query("SELECT * FROM recipeInfo")
    List<Recipe> getAll();

    // Get all 'Recipe' objects from the 'recipeInfo' table with a given category.
    @Query("SELECT * FROM recipeInfo WHERE category = :category ORDER BY recipeId")
    public List<Recipe> findRecipesByCategory(String category);

    // Check if a 'Recipe' object with a given ID exists in the 'recipeInfo' table.
    @Query("SELECT EXISTS (SELECT 1 FROM recipeInfo WHERE recipeId = :id)")
    public boolean recipeIdExists(String id);

}
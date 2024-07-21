package vk.my.code.foodie.data;

import android.content.Context;

import java.util.List;

// This class provides methods to manage 'Recipe' and 'User' data
// stored in a SQLite database using the 'RecipeDAO' and 'UserDAO' classes.
public class RecipeRepository {

    private RecipeDAO mRecipeDao;
    private UserDAO mUserDao;

    // Initialize the 'RecipeRepository' class with the given context.
    // This will initialize the 'RecipeDAO' and 'UserDAO' objects for accessing the database.
    public RecipeRepository(Context context){
        super();
        mRecipeDao = FoodieDatabase.getInstance(context).recipeDAO();
        mUserDao = FoodieDatabase.getInstance(context).userDAO();
    }

    // Store a new 'User' object in the database.
    public void storeUser(User user){
        this.mUserDao.insert(user);
    }

    // Update an existing 'User' object in the database.
    public void updateUser(User user){
        this.mUserDao.update(user);
    }

    // Get a 'User' object from the database by its ID.
    public User getUserById(int id){
        return this.mUserDao.getById(id);
    }

    // Store a new 'Recipe' object in the database.
    public void storeRecipe(Recipe recipe){
        this.mRecipeDao.insert(recipe);
    }

    // Update an existing 'Recipe' object in the database.
    public void updateRecipe(Recipe recipe){
        this.mRecipeDao.update(recipe);
    }

    // Delete a 'Recipe' object from the database.
    public void deleteRecipes(Recipe recipe){
        this.mRecipeDao.delete(recipe);
    }

    // Delete a 'User' object from the database.
    public void deleteUser(User user){
        this.mUserDao.delete(user);
    }

    // Get a 'Recipe' object from the database by its ID.
    public Recipe getRecipeById(String id){
        return this.mRecipeDao.getById(id);
    }

    // Get all 'Recipe' objects from the database.
    public List<Recipe> getAllRecipes(){
        return this.mRecipeDao.getAll();
    }

    // Get all 'User' objects from the database.
    public List<User> getAllUsers(){
        return this.mUserDao.getAllUsers();
    }

    // Get all 'Recipe' objects from the database with a given category.
    public List<Recipe> findRecipesByCategory(String category){
        return this.mRecipeDao.findRecipesByCategory(category);
    }

    // Check if a 'Recipe' object with a given ID exists in the database.
    public boolean recipeIdExists(String id){
        return this.mRecipeDao.recipeIdExists(id);
    }
}
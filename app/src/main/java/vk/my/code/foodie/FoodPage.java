package vk.my.code.foodie;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import vk.my.code.foodie.data.Recipe;
import vk.my.code.foodie.data.RecipeRepository;

public class FoodPage extends Fragment {

    private Drawable fullHeart;
    private Drawable borderHeart;

    // for the data to be shown to the user
    private Recipe recipe;

    // repo for managing data locally
    private RecipeRepository mrepository;


    public FoodPage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mrepository = new RecipeRepository(getContext());
        fullHeart = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_favorite_25, null);
        borderHeart = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_favourite_border_25, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_page, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the recipe ID, title and ImageURL from the arguments passed to this fragment
        String recipeID = "";
        String recipeTitle = "";
        String recipeImageURL = "";
        if (getArguments() != null) {
            recipeID = getArguments().getString("recipeID");
            recipeTitle = getArguments().getString("recipeTitle");
            recipeImageURL = getArguments().getString("recipeImage");
        }

        // Get references to the text and image views that will be used to display the recipe information
        TextView recipeName = view.findViewById(R.id.food_name);
        ImageView recipeImage = view.findViewById(R.id.food_image);
        TextView ingredients = view.findViewById((R.id.food_ingredients_text));
        TextView instructions = view.findViewById(R.id.food_instructions_text);
        TextView tvCookingTime = view.findViewById(R.id.food_cook_time);
        TextView tvPreparationTime = view.findViewById(R.id.food_prep_time);
        TextView tvServings = view.findViewById(R.id.food_servings);


        Button button = view.findViewById(R.id.food_page_button);

//            Used to Check if the user is connected to the internet to choose if to use the API to load the recipe
//        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
//
//        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        this.recipe = mrepository.getRecipeById(recipeID);

        //Check if the recipe is saved in the ROOM database already
        if (recipe.wasVisitedPreviously()) {
            // Get the recipe information from the ROOM database using the recipe
            getRecipeByIDFromFoodieDatabase(recipe, recipeName, recipeImage,
                    ingredients, instructions, tvCookingTime, tvPreparationTime, tvServings);
        } else {
            // Get the recipe information from the HTTP database using the recipe ID
            getRecipeByIDFromAPI(recipeID, recipeName, recipeImage,
                    ingredients, instructions, tvCookingTime, tvPreparationTime, tvServings);

            // Check the database and update the button appearance using firebase instead of ROOM
//            checkDatabaseAndUpdateButtonAppearanceWithHTTPDatabase(recipeID, button);
        }

        // Check the database and update the button appearance
        checkDatabaseAndUpdateButtonAppearanceWithFoodieDatabase(recipe, button);

        // Set up a click listener for the button
        likeRecipeClickListener(recipe, button, recipeID, recipeImageURL, recipeTitle);
        System.out.println("Test: " + recipe.isAddedToMyRecipes());

    }

    //    Checks if the recipe is liked (saved in "My Recipes")
    private void checkDatabaseAndUpdateButtonAppearanceWithFoodieDatabase(Recipe recipe, Button button) {
        if (recipe.isAddedToMyRecipes()) {
            button.setBackground(fullHeart);
        } else {
            button.setBackground(borderHeart);
        }
    }

    //    Setting the TextViews and the Image from the ROOM Database recipe once it is saved in the DB
    private void getRecipeByIDFromFoodieDatabase(Recipe recipe, TextView recipeName, ImageView recipeImage, TextView ingredients,
                                                 TextView instructions, TextView tvCookingTime, TextView tvPreparationTime,
                                                 TextView tvServings) {

        recipeName.setText(recipe.getName());
        setImageByURL(recipe.getImageURL(), recipeImage);
        ingredients.setText(recipe.getIngredients());
        instructions.setText(recipe.getInstructions());
        tvCookingTime.setText(recipe.getCookingTime());
        tvPreparationTime.setText(recipe.getPreparationTime());
        tvServings.setText(recipe.getServings());

    }

    //    Check the Button appearance on Click and Changes the boolean variable in the DB to saved/unsaved
    private void likeRecipeClickListener(Recipe recipe, Button button, String recipeID, String recipeImageURL, String recipeTitle) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!recipe.isAddedToMyRecipes()) {
                    button.setBackground(fullHeart);
                    recipe.setAddedToMyRecipes(true);
                    mrepository.updateRecipe(recipe);
                    Toast.makeText(getContext(),"Recipe is Saved",Toast. LENGTH_SHORT).show();
//                    addNewRecipe(recipeID, recipeTitle, recipeImageURL);
                } else {
                    button.setBackground(borderHeart);
                    recipe.setAddedToMyRecipes(false);
                    mrepository.updateRecipe(recipe);
                    Toast.makeText(getContext(),"Recipe is Removed from My Recipes",Toast. LENGTH_SHORT).show();
//                    removeRecipe(recipeID);
                }
            }
        });
    }

    //    Get the information for the Recipe from the API (the first time it is loaded) and save the data in the ROOM database
    private void getRecipeByIDFromAPI(String id, TextView recipeName, ImageView recipeImage, TextView ingredients,
                                      TextView instructions, TextView tvCookingTime, TextView tvPreparationTime,
                                      TextView tvServings) {

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = String.format("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/%s/information", id);

        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    Converting the response to a JSONObject
                    JSONObject rootObj = new JSONObject(response);

//                    Getting the Data from the JSONObject
                    String imageURL = rootObj.getString("image");
                    String recipeTitle = rootObj.getString("title");
                    String cookingTime = "Preparation time: " + rootObj.getString("cookingMinutes");
                    String preparationTime = "Cooking time: " + rootObj.getString("preparationMinutes");
                    String servings = "Serving size: " + rootObj.getString("servings");

//                    Setting the ImageView Image
                    setImageByURL(imageURL, recipeImage);

//                  Making a JSONArray in which all the ingredients data is stored (Extracting it from the API)
                    JSONArray ingredientsArray = rootObj.getJSONArray("extendedIngredients");
                    String allIngredients = "";

//                  For loop Setting up the Ingredients Text
                    for (int i = 0; i < ingredientsArray.length(); i++) {
                        JSONObject ingredient = ingredientsArray.getJSONObject(i);
                        allIngredients += ingredient.getString("name") + ": " +
                                ingredient.getString("amount") + " " + ingredient.getString("unit") + "/s\n";
                    }

//                  Making a JSONArray in which all the instructions data is stored (Extracting it from the API)
                    JSONArray instructionsArray = rootObj.getJSONArray("analyzedInstructions");
                    String allInstructions = "";

//                  For loop Setting up the Instructions Text
                    for (int i = 0; i < instructionsArray.length(); i++) {
                        JSONObject currentInstructions = instructionsArray.getJSONObject(i);
                        JSONArray steps = currentInstructions.getJSONArray("steps");

                        for (int j = 0; j < steps.length(); j++) {
                            JSONObject currentStep = steps.getJSONObject(j);
                            allInstructions += currentStep.getString("step") + "\n";
                        }

                        allInstructions += "\n";
                    }

                    allInstructions += "\n";

//                  Setting Text to all the TextViews on the Page
                    instructions.setText(allInstructions);
                    ingredients.setText(allIngredients);
                    recipeName.setText(recipeTitle);
                    tvCookingTime.setText(cookingTime);
                    tvPreparationTime.setText(preparationTime);
                    tvServings.setText(servings);

//                  Saving the Recipe in the ROOM Database
                    recipe.setInstructions(allInstructions);
                    recipe.setIngredients(allIngredients);
                    recipe.setCookingTime(cookingTime);
                    recipe.setPreparationTime(preparationTime);
                    recipe.setServings(servings);
                    recipe.setWasVisitedPreviously(true);
                    mrepository.updateRecipe(recipe);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.d("ERROR", "error => " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-RapidAPI-Key", "ee6ec09df9msh5c1f2e9b69eee75p16e391jsn04da41a39ab5");
                params.put("X-RapidAPI-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com");
                return params;
            }
        };

        queue.add(postRequest);
    }

    //    Set the ImageView to the URL
    public void setImageByURL(String URL, ImageView imageView) {
        ImageRequest imageRequest = new ImageRequest(URL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(final Bitmap responses) {
                imageView.setImageBitmap(responses);

                System.out.println("Look here" + responses);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_INSIDE, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(imageRequest);
    }

//    At the beginning used this function to save the Recipes in the Firebase DB
//    private void addNewRecipe(String recipeID, String recipeName, String recipeImageURL) {
//        // Create a RequestQueue instance
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//
//        // Set the URL for the first request
//        String url = "https://cm3110-6e80b-default-rtdb.firebaseio.com/vkalushev.json";
//
//        // Create the first request
//        StringRequest firstRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    // Parse the response data into a JSONObject
//                    final JSONObject jsonResponse = new JSONObject(response);
//
//                    // Check if the "recipes" key exists in the JSON object
//                    if (!jsonResponse.has("recipes")) {
//                        // Create a new JSON array for the "recipes" array
//                        JSONArray recipes = new JSONArray();
//
//                        // Add the "recipes" array to the jsonResponse object
//                        jsonResponse.put("recipes", recipes);
//                    }
//
//                    // Get the "recipes" array from the JSON object
//                    JSONArray recipes = jsonResponse.optJSONArray("recipes");
//
//                    // If the "recipes" array doesn't exist, create it
//                    if (recipes == null) {
//                        recipes = new JSONArray();
//                        jsonResponse.put("recipes", recipes);
//                    }
//
//                    // Create a new JSON object for the new recipe
//                    JSONObject newRecipe = new JSONObject();
//                    newRecipe.put("id", recipeID);
//                    newRecipe.put("title", recipeName);
//                    newRecipe.put("image", recipeImageURL);
//
//                    // Add the new recipe to the "recipes" array
//                    recipes.put(newRecipe);
//
//                    // Update the "recipes" array in the jsonResponse object
//                    jsonResponse.put("recipes", recipes);
//
//                    // Create the second request
//                    StringRequest secondRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            // Handle the response from the server here
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            // Handle any errors here
//                        }
//                    }) {
//                        @Override
//                        public String getBodyContentType() {
//                            return "application/json; charset=utf-8";
//                        }
//
//                        @Override
//                        public byte[] getBody() {
//                            return jsonResponse.toString().getBytes(StandardCharsets.UTF_8);
//                        }
//                    };
//
//                    // Add the second request to the RequestQueue and execute it
//                    queue.add(secondRequest);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // Handle any errors here
//            }
//        });
//
//        // Add the first request to the RequestQueue and execute it
//        queue.add(firstRequest);
//    }
//
    //    At the beginning used this function to remove the Recipes in the Firebase DB
//    private void removeRecipe(String recipeID) {
//        // Create a RequestQueue instance
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//
//        // Set the URL for the first request
//        String url = "https://cm3110-6e80b-default-rtdb.firebaseio.com/vkalushev.json";
//
//        // Create the first request
//        StringRequest firstRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    // Parse the response data into a JSONObject
//                    final JSONObject jsonResponse = new JSONObject(response);
//
//                    // Check if the "recipes" key exists in the JSON object
//                    if (!jsonResponse.has("recipes")) {
//                        // If the "recipes" key doesn't exist, there are no recipes to remove
//                        return;
//                    }
//
//                    // Get the "recipes" array from the JSON object
//                    JSONArray recipes = jsonResponse.optJSONArray("recipes");
//
//                    // If the "recipes" array doesn't exist, there are no recipes to remove
//                    if (recipes == null) {
//                        return;
//                    }
//
//                    // Loop through the "recipes" array and find the recipe with the specified ID
//                    for (int i = 0; i < recipes.length(); i++) {
//                        JSONObject recipe = recipes.optJSONObject(i);
//
//                        // Check if the recipe has an "id" field
//                        if (recipe != null && recipe.has("id")) {
//                            // If the recipe has an "id" field, check if it matches the recipeID
//                            if (recipe.optString("id").equals(recipeID)) {
//                                // If the recipe's ID matches the recipeID, remove it from the array
//                                recipes.remove(i);
//                                break;
//                            }
//                        }
//                    }
//
//                    // Update the "recipes" array in the jsonResponse object
//                    jsonResponse.put("recipes", recipes);
//
//                    // Create the second request
//                    StringRequest secondRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            // Handle the response from the server here
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            // Handle any errors here
//                        }
//                    }) {
//                        @Override
//                        public String getBodyContentType() {
//                            return "application/json; charset=utf-8";
//                        }
//
//                        @Override
//                        public byte[] getBody() {
//                            return jsonResponse.toString().getBytes(StandardCharsets.UTF_8);
//                        }
//                    };
//
//                    // Add the second request to the RequestQueue and execute it
//                    queue.add(secondRequest);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // Handle any errors here
//            }
//        });
//
//        // Add the first request to the RequestQueue and execute it
//        queue.add(firstRequest);
//    }


    //    public void checkDatabaseAndUpdateButtonAppearanceWithHTTPDatabase(String recipeID, Button button) {
//
//        RequestQueue queue = Volley.newRequestQueue(getContext());
//
//        String url = String.format("https://cm3110-6e80b-default-rtdb.firebaseio.com/vkalushev.json");
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            final JSONObject jsonResponse = new JSONObject(response);
//
//                            if (!jsonResponse.has("recipes")) {
//                                // If the "recipes" key doesn't exist, there are no recipes to remove
//                                return;
//                            }
//
//                            // Get the "recipes" array from the JSON object
//                            JSONArray recipes = jsonResponse.optJSONArray("recipes");
//
//                            // If the "recipes" array doesn't exist, there are no recipes to remove
//                            if (recipes == null) {
//                                return;
//                            }
//
//
//                            // Loop through the "recipes" array and find the recipe with the specified ID
//                            for (int i = 0; i < recipes.length(); i++) {
//                                JSONObject recipe = recipes.optJSONObject(i);
//
//                                // Check if the recipe has an "id" field
//                                if (recipe != null && recipe.has("id")) {
//                                    // If the recipe has an "id" field, check if it matches the recipeID
//                                    if (recipe.optString("id").equals(recipeID)) {
//                                        // If the recipe's ID matches the recipeID, remove it from the array
//                                        setISADDEDALREADY(true);
//                                        break;
//                                    }
//                                }
//                            }
//
//                            if (getISADDEDALREADY()) {
//                                button.setBackground(fullHearth);
//                            } else {
//                                button.setBackground(borderHearth);
//                            }
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO Auto-generated method stub
//                        Log.d("ERROR", "error => " + error.toString());
//                    }
//                }
//        );
//
//        queue.add(stringRequest);
//    }
}
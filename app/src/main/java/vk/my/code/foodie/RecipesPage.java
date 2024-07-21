package vk.my.code.foodie;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vk.my.code.foodie.data.Recipe;
import vk.my.code.foodie.data.RecipeRepository;
import vk.my.code.foodie.data.User;
import vk.my.code.foodie.placeholder.Item;
import vk.my.code.foodie.placeholder.MyAdapter;

public class RecipesPage extends Fragment {

    private String category = "";

    private User user;
    // repo for managing data locally
    private RecipeRepository mrepository;

    public RecipesPage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mrepository = new RecipeRepository(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipes_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_myrecipes);
        user = mrepository.getAllUsers().get(0);

//        Get The Category name from the Categories Page
        if (getArguments() != null) {
            category = getArguments().getString("CategoryName");
        }

        TextView tvCatName = view.findViewById(R.id.food_cat_name);
        tvCatName.setText(category);

        List<Item> items = new ArrayList<>();

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        // for the data to be shown to the user
        List<Recipe> recipeList = mrepository.findRecipesByCategory(category);

//        Check if there is no internet connection or if the recipeList that is saved in the Database is not bigger than the requested recipes
        if (!isConnected) {
            getRecipesFromDatabase(recipeList, items, recyclerView);
        } else {
            getRecipes(category, items, recyclerView);
        }


    }

    //Using the recycler view to fill the page using the ROOM database
    private void getRecipesFromDatabase(List<Recipe> recipeList, List<Item> items, RecyclerView recyclerView) {
        int counter = 0;
        for (Recipe recipe : recipeList) {
            if(counter < user.getNumberOfRecipes()) {
                String recipeTitle = recipe.getName();
                String recipeImageURL = recipe.getImageURL();
                String recipeID = recipe.getRecipeId();
                items.add(new Item(recipeImageURL, recipeTitle, recipeID, "RecipesPage"));
                counter++;
            }
        }

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MyAdapter(getContext(), items));
    }


    //    Function that gets the Recipes but an API requested is used in order to do that
    private void getRecipes(String category, List<Item> items, RecyclerView recyclerView) {
        String numberOfRecipes = user.getNumberOfRecipes() + "";
        System.out.print(numberOfRecipes);


//        Setting up the URL since for the API the query and any additional information is being added in the URL
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = String.format("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/search?query=%s&number=%s", category, numberOfRecipes);

        if (!user.getDiet().equals("")) {
            url = String.format("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/search?query=%s&diet=%s&number=%s", category, user.getDiet(), numberOfRecipes);
        }

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
//                          Converting the response to a JSONObject
                            JSONObject rootObj = new JSONObject(response);
//                          Getting a base URL that is used by the API to store images
                            String baseUrl = rootObj.getString("baseUri");

//                            Getting the "results" in which all the recipes are being stored
                            JSONArray results = rootObj.getJSONArray("results");

//                            Looping through every single recipe that is received and saving the information about it in the ROOM database if it doesn't exist already
//                            And at the same time filling the RecyclerView with the Recipes
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject firstRecipe = results.getJSONObject(i);

                                String firstRecipeTitle = firstRecipe.getString("title");
                                String firstRecipeImageURL = baseUrl + firstRecipe.optString("image");
                                String firstRecipeID = firstRecipe.getString("id");

                                items.add(new Item(firstRecipeImageURL, firstRecipeTitle, firstRecipeID, "RecipesPage"));

                                if (!mrepository.recipeIdExists(firstRecipeID)) {
                                    Recipe recipe = new Recipe();
                                    recipe.setName(firstRecipeTitle);
                                    recipe.setRecipeId(firstRecipeID);
                                    recipe.setImageURL(firstRecipeImageURL);
                                    recipe.setCategory(category);

                                    mrepository.storeRecipe(recipe);
                                }

                            }

                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
                            recyclerView.setAdapter(new MyAdapter(getContext(), items));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {
            //            The API expects the KEY to be in the Headers
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


}
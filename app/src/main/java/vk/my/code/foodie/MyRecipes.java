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
import android.widget.TextView;

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
import java.util.List;

import vk.my.code.foodie.data.Recipe;
import vk.my.code.foodie.data.RecipeRepository;
import vk.my.code.foodie.placeholder.Item;
import vk.my.code.foodie.placeholder.MyAdapter;

public class MyRecipes extends Fragment {

    // for the data to be shown to the user
    private List<Recipe> recipeList;

    // repo for managing data locally
    private RecipeRepository mrepository;

    public MyRecipes() {
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
        return inflater.inflate(R.layout.fragment_my_recipes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_myrecipes);

        List<Item> items = new ArrayList<>();

        this.recipeList = mrepository.getAllRecipes();

        getRecipesFromFoodieDatabase(items, recyclerView);


//        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

//        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

//        if (isConnected) {
//            getRecipesFromHTTPDatabase(items, recyclerView);
//        } else {
//        getRecipesFromFoodieDatabase(items, recyclerView);
//        }
    }

//    Function that fills the Recycler View with the Recipes that are added to My Recipes in the DB
    private void getRecipesFromFoodieDatabase(List<Item> items, RecyclerView recyclerView) {

        for (Recipe recipe : recipeList) {
            if (recipe.isAddedToMyRecipes()) {
                String recipeTitle = recipe.getName();
                String recipeImageURL = recipe.getImageURL();
                String recipeID = recipe.getRecipeId();
                items.add(new Item(recipeImageURL, recipeTitle, recipeID, "MyRecipes"));
            }
        }

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MyAdapter(getContext(), items));
    }


//    Used to sue this code get the Recipes that are added to My Recipes but by using the Firebase Database
    //    private void getRecipesFromHTTPDatabase(List<Item> items, RecyclerView recyclerView) {
//
//        RequestQueue queue = Volley.newRequestQueue(getContext());
//        String url = "https://cm3110-6e80b-default-rtdb.firebaseio.com/vkalushev.json";
//
//        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//
//                        try {
//                            final JSONObject jsonResponse = new JSONObject(response);
//
//                            if (jsonResponse.has("recipes")) {
//
//                                JSONArray recipes = jsonResponse.getJSONArray("recipes");
//
//                                for (int i = 0; i < recipes.length(); i++) {
//                                    JSONObject firstRecipe = recipes.getJSONObject(i);
//
//                                    String firstRecipeTitle = firstRecipe.optString("title");
//                                    String firstRecipeImageURL = firstRecipe.optString("image");
//                                    String firstRecipeID = firstRecipe.optString("id");
//
//                                    items.add(new Item(firstRecipeImageURL, firstRecipeTitle, firstRecipeID, "MyRecipes"));
//                                }
//
//                                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
//                                recyclerView.setAdapter(new MyAdapter(getContext(), items));
//                            }
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
//        queue.add(postRequest);
//    }
}
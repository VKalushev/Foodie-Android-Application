package vk.my.code.foodie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import android.graphics.Bitmap;

import vk.my.code.foodie.data.RecipeRepository;
import vk.my.code.foodie.data.User;

public class CategoriesPage extends Fragment {

    private RecipeRepository mrepository;

    public CategoriesPage() {
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
        return inflater.inflate(R.layout.fragment_categories_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Creating the Variables for all the ImageViews
        ImageView imageView_one = view.findViewById(R.id.cat_image_1);
        ImageView imageView_two = view.findViewById(R.id.cat_image_2);
        ImageView imageView_three = view.findViewById(R.id.cat_image_3);
        ImageView imageView_four = view.findViewById(R.id.cat_image_4);
        ImageView imageView_five = view.findViewById(R.id.cat_image_5);
        ImageView imageView_six = view.findViewById(R.id.cat_image_6);

        //Setting the Images
        setCategoryImages(imageView_one, imageView_two, imageView_three, imageView_four, imageView_five, imageView_six);

        //Setting the ImageViews to work as buttons
        setCategoryImageButtons(imageView_one, imageView_two, imageView_three, imageView_four, imageView_five, imageView_six);
    }

    //Function which sets the ImageView by URL
    public void setImageByURL(String URL, ImageView imageView) {
        ImageRequest imageRequest = new ImageRequest(URL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(final Bitmap responses) {
                imageView.setImageBitmap(responses);
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

    //Function in which I'm setting images for the 6 different Categories
    public void setCategoryImages(ImageView imageView_one, ImageView imageView_two, ImageView imageView_three,
                                  ImageView imageView_four, ImageView imageView_five, ImageView imageView_six) {

        //Function for the URLs of the Images
        String URL_image_one = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a3/Eq_it-na_pizza-margherita_sep2005_sml.jpg/800px-Eq_it-na_pizza-margherita_sep2005_sml.jpg";
        String URL_image_two = "https://t4.ftcdn.net/jpg/02/44/61/19/360_F_244611927_yrh0ZIYwOGTDurVnCMAt7Cq8DR4sBkB0.jpg";
        String URL_image_three = "https://cdn.apartmenttherapy.info/image/upload/f_jpg,q_auto:eco,c_fill,g_auto,w_1500,ar_1:1/k%2FEdit%2F2021-05-Assassins-Spaghetti%2FIMG_3873";
        String URL_image_four = "https://static.standard.co.uk/s3fs-public/thumbnails/image/2019/05/14/15/vegetarian-meal-14-05-19-0.jpg?width=1200";
        String URL_image_five = "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/black-pepper-chicken2-1647635684.jpeg?crop=0.696xw:1.00xh;0.135xw,0&resize=640:*";
        String URL_image_six = "https://henpicked.net/wp-content/uploads/2019/03/iStock-618201406.jpg";


        //Setting the Images
        setImageByURL(URL_image_one, imageView_one);
        setImageByURL(URL_image_two, imageView_two);
        setImageByURL(URL_image_three, imageView_three);
        setImageByURL(URL_image_four, imageView_four);
        setImageByURL(URL_image_five, imageView_five);
        setImageByURL(URL_image_six, imageView_six);
    }

    private void setCategoryImageButtons(ImageView imageView_one, ImageView imageView_two, ImageView imageView_three,
                                         ImageView imageView_four, ImageView imageView_five, ImageView imageView_six) {

        //Getting the User and the Country he has chosen
        User user = mrepository.getAllUsers().get(0);
        String cuisine = user.getCountryLocation();

        //Since in the API an actually cuisine name is needed I change the Country Name to the cuisine (have for most countries)
        switch (cuisine) {
            case "United Kingdom":
                cuisine = "british";
                break;
            case "Italy":
                cuisine = "italian";
                break;
            case "Africa":
                cuisine = "african";
                break;
            case "China":
                cuisine = "chinese";
                break;
            case "Japan":
                cuisine = "japanese";
                break;
            case "Korea":
                cuisine = "korean";
                break;
            case "Vietnam":
                cuisine = "vietnamese";
                break;
            case "India":
                cuisine = "indian";
                break;
            case "France":
                cuisine = "french";
                break;
            case "Mexico":
                cuisine = "mexican";
                break;
            case "United States":
                cuisine = "american";
                break;
            case "Germany":
                cuisine = "german";
                break;
            default:
                cuisine = "eastern european";
                break;
        }

        //Sends the string to the RecipesPage so it can be used to search for the specific category
        setImageOnClickListener(imageView_one, "Pizza");
        setImageOnClickListener(imageView_two, "Burgers");
        setImageOnClickListener(imageView_three, "Pasta");
        setImageOnClickListener(imageView_four, "Vegetarian");
        setImageOnClickListener(imageView_five, "Chinese");
        setImageOnClickListener(imageView_six, cuisine);
    }

    //This function makes  the ImageViews clickable
    public void setImageOnClickListener(ImageView image, String str) {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("CategoryName", str);
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_categoriesPage_to_recipesPage, bundle);
            }
        });
    }

}
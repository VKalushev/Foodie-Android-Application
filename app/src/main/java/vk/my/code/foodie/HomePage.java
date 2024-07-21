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
import android.widget.Button;

import java.util.List;

import vk.my.code.foodie.data.FoodieDatabase;
import vk.my.code.foodie.data.RecipeRepository;
import vk.my.code.foodie.data.User;

public class HomePage extends Fragment implements View.OnClickListener {

    RecipeRepository mrepository;

    public HomePage() {
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
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // for navigating to the CategoriesPage or the Settings Page
        Button goMainPageButton = view.findViewById(R.id.button_go_categories);
        goMainPageButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        // get the nav contoller
        NavController navController = Navigation.findNavController(view);

        List<User> users = mrepository.getAllUsers();

//        If statement that check if there is a user already, if there is it sends us to the Categories page
//        If not we create a new user and it sends us to the Settings page
        if (users.size() == 0) {
            User user = new User();
            mrepository.storeUser(user);

            if (view.getId() == R.id.button_go_categories) {// navigate to the LocationConfirmationFragment
                navController.navigate(R.id.action_homePage_to_settingsPage);
            }
        } else {
            if (view.getId() == R.id.button_go_categories) {// navigate to the LocationConfirmationFragment
                navController.navigate(R.id.action_homePage_to_categoriesPage);
            }
        }

    }


}
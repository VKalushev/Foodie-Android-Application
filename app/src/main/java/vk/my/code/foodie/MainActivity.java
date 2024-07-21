package vk.my.code.foodie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);
        bnv.setOnItemSelectedListener(this);

        // Get the current page/fragment
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.homePage);

        if (currentFragment instanceof HomePage) {
            // If the current fragment is MyFragment, hide the bottom navigation view
            bnv.setVisibility(View.INVISIBLE);
        } else {
            // Otherwise, show the bottom navigation view
            bnv.setVisibility(View.VISIBLE);
        }


        // Get the current fragment being displayed
    }

//    Code for the Bottom Navigation Bar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(findViewById(R.id.fragmentContainerView));
        // work out where the user currently is
        int currentFragmentId = navController.getCurrentDestination().getId();


//        Using this if statement to make sure that the nav bar does not work in the Home Page but it all other pages
//        And setting up the Links from clicking the specific buttons
        if(currentFragmentId != R.id.homePage){
            switch (item.getItemId()){
                case R.id.button_home:
                    // navigate "home" to the location select fragment
                    if (currentFragmentId != R.id.categoriesPage){
                        navController.navigate(R.id.categoriesPage);
                        return true;
                    }
                    break;
                case R.id.button_mySettings:
                    // navigate to settings fragment
                    if (currentFragmentId != R.id.settingsPage){
                        navController.navigate(R.id.settingsPage);
                        return true;
                    }
                    break;
                case R.id.button_myRecipes:
                    // navigate to settings fragment
                    if (currentFragmentId != R.id.myRecipes){
                        navController.navigate(R.id.myRecipes);
                        return true;
                    }
                    break;
            }
        }

        return false;
    }


}
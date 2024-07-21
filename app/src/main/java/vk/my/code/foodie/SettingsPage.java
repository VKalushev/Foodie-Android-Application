package vk.my.code.foodie;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import vk.my.code.foodie.data.Recipe;
import vk.my.code.foodie.data.RecipeRepository;
import vk.my.code.foodie.data.User;


public class SettingsPage extends Fragment implements LocationListener {

    // repo for managing data locally
    private RecipeRepository mrepository;

    User user;
    LocationManager locationManager;
    EditText etLocation;

    public SettingsPage() {
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
        return inflater.inflate(R.layout.fragment_settings_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etLocation = getView().findViewById(R.id.settings_set_location);


//        Just in case checing if there is no user already and if there isn't creating one
        if (mrepository.getAllUsers().size() == 0) {
            user = new User();
            mrepository.storeUser(user);
        } else {
            user = mrepository.getAllUsers().get(0);
        }

//      Function that takes care of the ClearData button
        clearDataFunctionality(view);

//      Function that takes care of the numberOfRecipes EditText
        enterNumberOfRecipesPerPage(view);

//      Function that Takes Care of the Location extracting, permissions and etc...
        setLocationFunctionality(view);

//      Function that takes care of the Radio Group/Buttons
        dietFunctionality(view);

//      Function that Saves all the Data by Clicking the Button
        saveAllData(view);
    }

    private void saveAllData(View view) {
        Button btnSaveAll = view.findViewById(R.id.settings_save_all);

        btnSaveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etNumberOfRecipes = getView().findViewById(R.id.settings_num_of_recipes);

                int numberOfRecipes = Integer.parseInt(etNumberOfRecipes.getText().toString());
                String locationToSave = etLocation.getText().toString();

                user.setNumberOfRecipes(numberOfRecipes);
                user.setCountryLocation(locationToSave);
                mrepository.updateUser(user);
                Toast.makeText(getContext(),"Data is Saved",Toast. LENGTH_SHORT).show();
            }
        });
    }

    //      Function that Takes Care of the Location extracting, permissions and etc...
    private void setLocationFunctionality(View view) {

        Button btnGetLocation = getView().findViewById(R.id.settings_location_button);

        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Getting Location",Toast. LENGTH_SHORT).show();
//                  Function that check if the user has given permission for the APP to use the GPS Location
                grantPermission();

//                  Function to Check if the Location is not enabled already
                checkLocationIsEnabledOrNot();

//                  Location to get the actual Location
                getLocation();
            }
        });


    }

    //    Location to get the actual Location
    private void getLocation() {
        try {
            locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    //  Checks if the Location is enabled already and if it is not it pops up a menu asking the user
//  if he wants to enable it and it sends him to the Settings in his phone to turn it on
//  Personally for me on my old Android phone there was an issue with that since it wasn't sending me to the proper place
//  but it works fine with the Emulator
    private void checkLocationIsEnabledOrNot() {
        LocationManager lm = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!gpsEnabled && !networkEnabled) {
            new AlertDialog.Builder(getContext()).setTitle("Enable GPS Service")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //this intent redirect us to the location settings, if gps is disabled this dialog will be shown
                            startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
                        }
                    }).setNegativeButton("Cancel", null)
                    .show();
        }

    }

    //    Checking if permission is given
    private void grantPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }

    }

    //      Function that takes care of the numberOfRecipes EditText
    private void enterNumberOfRecipesPerPage(View view) {
        EditText etNumberOfRecipes = view.findViewById(R.id.settings_num_of_recipes);
        String numberOfRecipes = user.getNumberOfRecipes() + "";
        etNumberOfRecipes.setText(numberOfRecipes);
    }

    //      Function that takes care of the ClearData button
    private void clearDataFunctionality(View view) {

        Button clearData = view.findViewById(R.id.settings_button_clearData);
        clearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"All data is deleted",Toast. LENGTH_SHORT).show();
//              Going through all the recipes and deleting them 1 by 1
                List<Recipe> recipes = mrepository.getAllRecipes();

                for (Recipe recipe : recipes) {
                    mrepository.deleteRecipes(recipe);
                }

//              Going through all the Users and deleting them 1 by 1 (Practically in all the code I make sure that there isn't more than 1 user)
                List<User> users = mrepository.getAllUsers();

                for (User user : users) {
                    mrepository.deleteUser(user);
                }

//              Clearing the Text  that is/was in the two editTexts and returning the User to the Starting Page
                etLocation = getView().findViewById(R.id.settings_set_location);
                etLocation.setText("");

                EditText etNumberOfRecipes = getView().findViewById(R.id.settings_num_of_recipes);
                etNumberOfRecipes.setText("1");

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_settingsPage_to_homePage);
            }
        });
    }


    private void dietFunctionality(View view) {
        RadioGroup radioGroup = view.findViewById(R.id.radio_group);
        RadioButton rbNoVeg = view.findViewById(R.id.settings_radio_noVeg);
        RadioButton rbVeg = view.findViewById(R.id.settings_radio_veg);
        RadioButton rbVegan = view.findViewById(R.id.settings_radio_vegan);

        if (user.getDiet().equals("") || user.getDiet() == null) {
            rbNoVeg.setChecked(true);
            rbVeg.setChecked(false);
            rbVegan.setChecked(false);
        } else if (user.getDiet().equals("Vegetarian")) {
            rbNoVeg.setChecked(false);
            rbVeg.setChecked(true);
            rbVegan.setChecked(false);
        } else if (user.getDiet().equals("Vegan")) {
            rbNoVeg.setChecked(false);
            rbVeg.setChecked(false);
            rbVegan.setChecked(true);
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {

                    case R.id.settings_radio_noVeg:
                        user.setDiet("");
                        mrepository.updateUser(user);
                        break;
                    case R.id.settings_radio_veg:
                        user.setDiet("Vegetarian");
                        mrepository.updateUser(user);
                        break;
                    case R.id.settings_radio_vegan:
                        user.setDiet("Vegan");
                        mrepository.updateUser(user);
                        break;
                }
            }
        });
    }

    //    Location that comes with the interface which is used when location is changed
    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            etLocation.setText(addresses.get(0).getCountryName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    Location that comes with the interface which is used when location is changed
    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        System.out.println(locations.get(0));
        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            System.out.println(locations.get(0).getLatitude());
            System.out.println(locations.get(0).getLongitude());
            List<Address> addresses = geocoder.getFromLocation(locations.get(0).getLatitude(), locations.get(0).getLongitude(), 1);
            etLocation.setText(addresses.get(0).getCountryName());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //    This function aslo comes with the interface but I'm not gonna put any code for it since I have code already that does the same thing later on
    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }


    //  Checks if the Location is enabled already and if it is not it pops up a menu asking the user
//  if he wants to enable it and it sends him to the Settings in his phone to turn it on
//  Personally for me on my old Android phone there was an issue with that since it wasn't sending me to the proper place
//  but it works fine with the Emulator
    @Override
    public void onProviderDisabled(@NonNull String provider) {
        new AlertDialog.Builder(getContext()).setTitle("Enable GPS Service")
                .setCancelable(false)
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //this intent redirect us to the location settings, if gps is disabled this dialog will be shown
                        startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
                    }
                }).setNegativeButton("Cancel", null)
                .show();
    }
}
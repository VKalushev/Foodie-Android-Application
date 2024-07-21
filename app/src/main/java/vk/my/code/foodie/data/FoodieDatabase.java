package vk.my.code.foodie.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Recipe.class,User.class}, version=14)
public abstract class FoodieDatabase extends RoomDatabase {

    public abstract RecipeDAO recipeDAO();

    public abstract UserDAO userDAO();

    private static FoodieDatabase INSTANCE;

    public static FoodieDatabase getInstance(final Context context){
        if (INSTANCE == null){
            synchronized (Recipe.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context, FoodieDatabase.class, "FoodieDatabase")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}

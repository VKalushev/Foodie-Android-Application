package vk.my.code.foodie.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Query;

import java.util.List;

// This interface is a Data Access Object (DAO) for 'User' objects.
// It defines the methods that can be used to access the 'users' table in the SQLite database
// using the Room persistence library.
@Dao
public interface UserDAO {

    @Update
    void update(User user);

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);

    // Retrieve the 'User' object with the specified primary key from the database.
    @Query("SELECT * FROM users WHERE uid = :uid")
    User getById(int uid);

    // Retrieve all 'User' objects from the database.
    @Query("SELECT * FROM users")
    List<User> getAllUsers();
}
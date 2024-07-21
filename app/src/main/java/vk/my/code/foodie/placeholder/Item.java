package vk.my.code.foodie.placeholder;

// This class represents a generic item that can be shown in a list or grid.
public class Item {

    String imageURL;
    String name;
    String id;

    // Used to show if it comes from My Recipes Page or Recipes Page
    String fromWhere;

    public Item(String image, String name, String id, String fromWhere) {
        this.imageURL = image;
        this.name = name;
        this.id = id;
        this.fromWhere = fromWhere;
    }

    // Get the image associated with the item.
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    // Get the name of the item.
    public String getName() {
        return name;
    }

    // Set the name of the item.
    public void setName(String name_one) {
        this.name = name_one;
    }

    // Get the ID of the item.
    public String getId() {
        return id;
    }

    // Set the ID of the item.
    public void setId(String id) {
        this.id = id;
    }

    // Get the source of the item.
    public String getFromWhere() {
        return fromWhere;
    }

    // Set the source of the item.
    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }
}
package vk.my.code.foodie.placeholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

import vk.my.code.foodie.R;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;

    // This is a list of items that will be displayed in the RecyclerView
    List<Item> items;

    LayoutInflater inflater;

    public MyAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item view layout and return a new view holder
        View view = inflater.inflate(R.layout.item_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nameView.setText(items.get(position).getName());

        // Create an ImageRequest to download an image from the URL in the item at the current position
        ImageRequest imageRequest = new ImageRequest(items.get(position).getImageURL(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(final Bitmap responses) {
                holder.imageView.setImageBitmap(responses);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_INSIDE, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // Set an OnClickListener on the imageView in the ViewHolder to navigate to a different page when the image is clicked
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("We are going to foodPage");
                Bundle bundle = new Bundle();
                bundle.putString("recipeID", items.get(holder.getAbsoluteAdapterPosition()).getId());
                bundle.putString("recipeTitle", items.get(holder.getAbsoluteAdapterPosition()).getName());
                bundle.putString("recipeImage", items.get(holder.getAbsoluteAdapterPosition()).getImageURL());
                NavController navController = Navigation.findNavController(view);

                // Check which page the user is currently on and navigate to the foodPage accordingly
                if (items.get(holder.getAbsoluteAdapterPosition()).getFromWhere().equals("RecipesPage")) {
                    navController.navigate(R.id.action_recipesPage_to_foodPage, bundle);
                } else if (items.get(holder.getAbsoluteAdapterPosition()).getFromWhere().equals("MyRecipes")) {
                    navController.navigate(R.id.action_myRecipes_to_foodPage, bundle);
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(imageRequest);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}

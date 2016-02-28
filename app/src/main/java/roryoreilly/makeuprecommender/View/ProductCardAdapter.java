package roryoreilly.makeuprecommender.View;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import roryoreilly.makeuprecommender.R;
import roryoreilly.makeuprecommender.Recommender.ProductCard;

/**
 * Created by roryoreilly on 26/02/16.
 */
public class ProductCardAdapter extends RecyclerView.Adapter<ProductCardAdapter.ViewHolder>{
    // Store a member variable for the contacts
    private List<ProductCard> mProductCards;
    private Activity activity;

    // Pass in the contact array into the constructor
    public ProductCardAdapter(List<ProductCard> productCards, Activity activity) {
        mProductCards = productCards;
        this.activity = activity;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public RecyclerView childRecyclerView;
        public ImageView mainImageView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            childRecyclerView = (RecyclerView) itemView.findViewById(R.id.productItem);
            mainImageView = (ImageView) itemView.findViewById(R.id.cardImage);


        }
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ProductCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View productView = inflater.inflate(R.layout.card_product, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(productView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ProductCardAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        ProductCard productCard = mProductCards.get(position);

        // Set item views based on the data model
        ImageView mainImageView = viewHolder.mainImageView;
        mainImageView.setImageResource(productCard.getImage());

        // Set item views based on the data model
        RecyclerView childRecyclerView = viewHolder.childRecyclerView;

        ProductItemAdapter adapter = new ProductItemAdapter(productCard.getProductItems());
        // Attach the adapter to the recyclerview to populate items
        childRecyclerView.setAdapter(adapter);
        // Set layout manager to position the items
        RecyclerView.LayoutManager layoutManager = new CustomLinearLayoutManager(activity);
        childRecyclerView.setLayoutManager(layoutManager);

    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return mProductCards.size();
    }
}

package roryoreilly.makeuprecommender.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import roryoreilly.makeuprecommender.R;
import roryoreilly.makeuprecommender.Recommender.ProductItem;

/**
 * Created by roryoreilly on 25/02/16.
 */
public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ViewHolder> {

    // Store a member variable for the contacts
    private List<ProductItem> mProductItems;

    // Pass in the contact array into the constructor
    public ProductItemAdapter(List<ProductItem> productItems) {
        mProductItems = productItems;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public TextView typeTextView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            typeTextView = (TextView) itemView.findViewById(R.id.product_name);
            nameTextView = (TextView) itemView.findViewById(R.id.product_type);


        }
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ProductItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View productView = inflater.inflate(R.layout.item_product, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(productView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ProductItemAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        ProductItem productItem = mProductItems.get(position);

        // Set item views based on the data model
        TextView typeTextView = viewHolder.typeTextView;
        typeTextView.setText(productItem.getType());

        // Set item views based on the data model
        TextView nameTextView = viewHolder.nameTextView;
        nameTextView.setText(productItem.getName());

//        Button button = viewHolder.messageButton;
//
//        if (productItem.isOnline()) {
//            button.setText("Message");
//            button.setEnabled(true);
//        }
//        else {
//            button.setText("Offline");
//            button.setEnabled(false);
//        }

    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return mProductItems.size();
    }
}

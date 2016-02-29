package roryoreilly.makeuprecommender.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import roryoreilly.makeuprecommender.R;
import roryoreilly.makeuprecommender.Recommender.ProductItem;

/**
 * Created by roryoreilly on 25/02/16.
 */
public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ProductViewHolder> {

    // Store a member variable for the contacts
    private List<ProductItem> mProductItems;

    // Pass in the contact array into the constructor
    public ProductItemAdapter(List<ProductItem> productItems) {
        mProductItems = productItems;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView typeTextView;
        public TextView descriptionTextView;
        public ImageView productImageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            typeTextView = (TextView) itemView.findViewById(R.id.product_type);
            nameTextView = (TextView) itemView.findViewById(R.id.product_name);
            descriptionTextView = (TextView) itemView.findViewById(R.id.product_description);
            productImageView = (ImageView) itemView.findViewById(R.id.product_image);

        }
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ProductItemAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);

        return new ProductViewHolder(view);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ProductItemAdapter.ProductViewHolder holder, int position) {
        // Get the data model based on position
        ProductItem productItem = mProductItems.get(position);

        holder.typeTextView.setText(productItem.getType());
        holder.nameTextView.setText(productItem.getName());
        holder.descriptionTextView.setText(productItem.getDescription());
        holder.productImageView.setImageResource(productItem.getImageResource());


    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return mProductItems.size();
    }
}

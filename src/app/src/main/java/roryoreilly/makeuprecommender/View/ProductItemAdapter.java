package roryoreilly.makeuprecommender.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import roryoreilly.makeuprecommender.Database.Product;
import roryoreilly.makeuprecommender.R;

/**
 * Created by roryoreilly on 25/02/16.
 */
public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ProductViewHolder> {

    // Store a member variable for the contacts
    private List<Product> mProductItems;

    // Pass in the contact array into the constructor
    public ProductItemAdapter(List<Product> productItems) {
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
        Product product = mProductItems.get(position);

        holder.typeTextView.setText(product.getType());
        holder.nameTextView.setText(product.getName());
        holder.descriptionTextView.setText(product.getDescription());
//        holder.productImageView.setImageResource(productItem.getImageResource());


        new DownloadImagesTask(product.getImage()).execute(holder.productImageView);
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return mProductItems.size();
    }




    public class DownloadImagesTask extends AsyncTask<ImageView, Void, Bitmap> {
        ImageView imageView = null;
        String url;

        public DownloadImagesTask(String url) {
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(ImageView... imageViews) {
            this.imageView = imageViews[0];
            return download_Image(url);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }

        private Bitmap download_Image(String url) {

            Bitmap bmp = null;
            try {
                URL ulrn = new URL(url);
                HttpURLConnection con = (HttpURLConnection) ulrn.openConnection();
                InputStream is = con.getInputStream();
                bmp = BitmapFactory.decodeStream(is);
                if (null != bmp)
                    return bmp;

            } catch (Exception e) {
            }
            return bmp;
        }
    }
}

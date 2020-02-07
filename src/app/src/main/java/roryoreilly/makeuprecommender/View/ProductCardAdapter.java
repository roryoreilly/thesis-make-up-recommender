package roryoreilly.makeuprecommender.View;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import roryoreilly.makeuprecommender.Database.Product;
import roryoreilly.makeuprecommender.R;
import roryoreilly.makeuprecommender.Recommender.ProductCard;


public class ProductCardAdapter extends RecyclerView.Adapter<ProductCardAdapter.CardViewHolder> {
    private final Context mContext;
    private final List<ProductCard> mItems;

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public ImageView mainImageView;
        public RecyclerView childRecyclerView;
        public FrameLayout titleFrame;

        public CardViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.product_card_title);
            mainImageView = (ImageView) view.findViewById(R.id.cardImage);
            childRecyclerView = (RecyclerView) view.findViewById(R.id.productItem);
            titleFrame = (FrameLayout) view.findViewById(R.id.product_card_frame);
        }
    }

    public ProductCardAdapter(Activity context, RecyclerView recyclerView, List<ProductCard> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_product, parent, false);

        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        holder.title.setText(mItems.get(position).getType());
        holder.mainImageView.setImageResource(mItems.get(position).getImage());
        holder.title.setText(mItems.get(position).getType());
        holder.titleFrame.setClipToOutline(true);

        // set product list with the child recycler view
        List<Product> products = mItems.get(position).getProductItems();
        RecyclerView childRecyclerView = holder.childRecyclerView;
        childRecyclerView.setLayoutManager(
                new LinearLayoutManager(
                        mContext, LinearLayoutManager.VERTICAL, false));

        ProductItemAdapter adapter = new ProductItemAdapter(products);
        childRecyclerView.setAdapter(adapter);
        childRecyclerView.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
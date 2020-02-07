package roryoreilly.makeuprecommender.Recommender;

import java.util.ArrayList;
import java.util.List;

import roryoreilly.makeuprecommender.Database.Product;

/**
 * Created by roryoreilly on 26/02/16.
 */
public class ProductCard {
    private String mType;
    private Integer mMainImage;
    private boolean mOnline;
    List<Product> mProductItems;

    public ProductCard(List<Product> items, String type, Integer image, boolean online) {
        mProductItems = items;
        mType = type;
        mMainImage = image;
        mOnline = online;
    }

    public String getType() {
        return mType;
    }

    public Integer getImage() {
        return mMainImage;
    }

    public boolean isOnline() {
        return mOnline;
    }

    public List<Product> getProductItems() {
        return mProductItems;
    }
}

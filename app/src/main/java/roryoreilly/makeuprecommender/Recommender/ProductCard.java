package roryoreilly.makeuprecommender.Recommender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roryoreilly on 26/02/16.
 */
public class ProductCard {
    private String mType;
    private Integer mMainImage;
    private boolean mOnline;
    List<ProductItem> mProductItems;

    public ProductCard(List<ProductItem> items, String type, Integer image, boolean online) {
        mProductItems = items;
        mType = type;
        mMainImage = image;
        mOnline = online;
        mProductItems = new ArrayList<ProductItem>();
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

    public List<ProductItem> getProductItems() {
        return mProductItems;
    }
}

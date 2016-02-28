package roryoreilly.makeuprecommender.Recommender;

/**
 * Created by roryoreilly on 25/02/16.
 */
public class ProductItem {
    private String mType;
    private String mName;
    private boolean mOnline;

    public ProductItem(String type, String name, boolean online) {
        mType = type;
        mName = name;
        mOnline = online;
    }

    public String getType() {
        return mType;
    }

    public String getName() {
        return mName;
    }

    public boolean isOnline() {
        return mOnline;
    }
}

package roryoreilly.makeuprecommender.Recommender;

/**
 * Created by roryoreilly on 25/02/16.
 */
public class ProductItem {
    private String mType;
    private String mName;
    private String mDescription;
    private Integer mImageResource;

    public ProductItem(String type, String name, String description, Integer imageRes) {
        mType = type;
        mName = name;
        mDescription = description;
        mImageResource = imageRes;
    }

    public String getType() {
        return mType;
    }

    public String getName() {
        return mName;
    }

    public Integer getImageResource() {
        return mImageResource;
    }

    public String getDescription() {
        return mDescription;
    }
}

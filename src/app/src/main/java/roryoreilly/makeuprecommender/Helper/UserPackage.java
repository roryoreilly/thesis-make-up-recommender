package roryoreilly.makeuprecommender.Helper;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class UserPackage implements Parcelable{
    private String eye;
    private String skinCode;
    private String hair;
    private String shape;
    private float[] skinColour;
    private float[] lipColour;
    private boolean warm;

    public UserPackage(String eye, String skinCode, String hair,
                       String shape, float[] skinColour, boolean warm, float[] lipColour) {
        this.eye = eye;
        this.skinCode = skinCode;
        this.hair = hair;
        this.shape = shape;
        this.skinColour = skinColour;
        this.warm = warm;
        this.lipColour = lipColour;
    }


    public UserPackage(Parcel source) {
        this.eye = source.readString();
        this.skinCode = source.readString();
        this.hair = source.readString();
        this.shape = source.readString();
        this.skinColour = source.createFloatArray();
        this.warm = source.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eye);
        dest.writeString(skinCode);
        dest.writeString(hair);
        dest.writeString(shape);
        dest.writeFloatArray(skinColour);
        dest.writeByte((byte) (warm ? 1 : 0));
        dest.writeFloatArray(lipColour);
    }

    public String getEye() {
        return eye;
    }

    public String getSkinCode() {
        return skinCode;
    }

    public String getHair() {
        return hair;
    }

    public String getShape() {
        return shape;
    }

    public float[] getSkinColour() {
        return skinColour;
    }

    public float[] getLipColour() {
        return lipColour;
    }

    public boolean isWarm() {
        return warm;
    }

    public static final Creator<UserPackage> CREATOR = new Creator<UserPackage>() {
        @Override
        public UserPackage[] newArray(int size) {
            return new UserPackage[size];
        }

        @Override
        public UserPackage createFromParcel(Parcel source) {
            return new UserPackage(source);
        }
    };
}
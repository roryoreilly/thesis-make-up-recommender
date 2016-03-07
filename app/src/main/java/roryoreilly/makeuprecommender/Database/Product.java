package roryoreilly.makeuprecommender.Database;

import android.graphics.Color;

/**
 * Created by roryoreilly on 29/02/16.
 */
public class Product {
    private String type;
    private String name;
    private String description;
    private String url;
    private String image;
    private float hue;
    private float saturation;
    private float brightness;
    String rgb;

    public Product(float hue, float saturation, float brightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }

    public Product(String type, String name, String description, String url, String image) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.url = url;
        this.image = image;
    }

    public Product(String type, String name, String description, String url, String image, String rgb) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.url = url;
        this.image = image;

        setHsv(rgb);
    }

    public void setHsv(String rgb) {
        this.rgb = rgb;
        int c = Color.parseColor(rgb);
        int red = Color.red(c);
        int green = Color.green(c);
        int blue = Color.blue(c);
        float[] hsv = new float[3];
        Color.RGBToHSV(red, green, blue, hsv);
        this.hue = hsv[0];
        this.saturation = hsv[1];
        this.brightness = hsv[2];
    }

    public String toString() {
        return String.valueOf(hue)+", "+String.valueOf(saturation)+", "+String.valueOf(brightness);
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }

    public float getHue() {
        return hue;
    }

    public float getSaturation() {
        return saturation;
    }

    public float getBrightness() {
        return brightness;
    }

    public String getRgb() {
        return rgb;
    }

    public float[] getHsv() {
        return new float[]{hue, saturation, brightness};
    }
}

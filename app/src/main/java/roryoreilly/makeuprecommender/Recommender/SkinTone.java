package roryoreilly.makeuprecommender.Recommender;


/**
 * Created by roryoreilly on 29/02/16.
 */
public class SkinTone {
    private String code;
    private String name;
    private float[] hue;

    public SkinTone (String code) {
        this.code = code;
        this.name = "";

    }

    public SkinTone(String code, float[] hue) {
        this.code = code;
        this.name = nameFromCode(code);
        this.hue = hue;
    }

    public double distance(float[] skin) {
        return Math.sqrt(Math.pow(hue[0] - skin[0], 2)
                        + Math.pow(hue[1] - skin[1], 2)
                        + Math.pow(hue[2] - skin[2], 2));
    }

    public String nameFromCode(String code) {
        String name ="";
        int value = Integer.valueOf(code.substring(2));
        if (value <= 10) {
            name += "Pale";
        } else if (value <= 15) {
            name += "Fair";
        } else if (value <= 25) {
            name += "Light";
        } else if (value <= 35) {
            name += "Medium";
        } else if (value <= 45) {
            name += "Olive";
        }  else if (value <= 55) {
            name += "Brown";
        } else {
            name += "Black";
        }

        String type = code.substring(0, 1);
        if (type.equals("NW")) {
            name += "-Cool";
        } else if (type.equals("NC")) {
            name += "-Warm";
        }

        return name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}

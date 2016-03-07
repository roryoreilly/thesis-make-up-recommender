package roryoreilly.makeuprecommender.Recommender;


/**
 * Created by roryoreilly on 29/02/16.
 */
public class SkinTone {
    private String code;
    private String name;
    private float[] hsv;
    private boolean warm;

    public SkinTone (String code) {
        this.code = code;
        this.name = "";

    }

    public SkinTone(String code, float[] hsv) {
        this.code = code;
        this.name = nameFromCode(code);
        this.hsv = hsv;
    }

    public double distance(float[] skin) {
        return Math.sqrt(Math.pow(((hsv[0]/360) - skin[0])*100, 2)
                        + Math.pow((hsv[1] - skin[1])*100, 2)
                        + Math.pow((hsv[2] - skin[2])*100, 2));
    }

    public String nameFromCode(String code) {
        String name ="";
        int value = Integer.valueOf(code.split("\\s")[1]);
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

        String type = code.split("\\s")[0];
        if (type.equals("NW")) {
            name += "-Cool";
            warm = false;
        } else if (type.equals("NC")) {
            name += "-Warm";
            warm = true;
        }

        return name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public boolean isWarm() {
        return warm;
    }
}

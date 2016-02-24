package roryoreilly.makeuprecommender.Model;

/**
 * Created by roryorilly on 15/02/16.
 */
public class EyeModel {
    private static final String BROWN = "Brown";
    private static final String BLUE = "Blue";
    private static final String GREEN = "Green";

    public static String determine(float hue, float saturation, float lightness) {
        if (hue <= 0.097833) {
            return BROWN;
        } else if (hue <= 0.257) {
            return GREEN;
        } else {
            return BLUE;
        }
    }
}

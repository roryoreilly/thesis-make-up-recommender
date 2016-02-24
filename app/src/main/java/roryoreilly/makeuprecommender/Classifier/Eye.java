package roryoreilly.makeuprecommender.Classifier;

import android.graphics.Color;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import roryoreilly.makeuprecommender.utils.WeiszfeldAlgorithm;

public class Eye extends Classifier {
    public Eye(FaceDetect face) {
        super(face);
        this.values = new float[3];
    }

    @Override
    public float[] findValues() {
        Point tl = face.getFacePoint("left_eye_upper_left_quarter");
        Point br = face.getFacePoint("left_eye_lower_right_quarter");

        List<float[]> colourSet = getPixels(tl, br);

        WeiszfeldAlgorithm.removeExtreamsHSV(colourSet, 10, 0.25);

        values = WeiszfeldAlgorithm.geometricMedianHSV(colourSet, 0.001);

        return values;
    }

    private List<float[]> getPixels(Point tl, Point br) {
        List<float[]> pixels = new ArrayList<>();
        for (int i=tl.x; i<br.x; i++) {
            for (int j=tl.y; j<br.y; j++) {
                int rgb = face.getImg().getPixel(i, j);
                float[] hsv = new float[3];
                Color.colorToHSV(rgb, hsv);
                if (hsv[2]>0.15 && hsv[2]<0.65) {
                    pixels.add(hsv);
                }
            }
        }
        return pixels;
    }
}

package roryoreilly.makeuprecommender.Classifier;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.provider.MediaStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import roryoreilly.makeuprecommender.utils.WeiszfeldAlgorithm;

/**
 * Created by roryoreilly on 05/03/16.
 */
public class Lip extends Classifier {
    public Lip(FaceDetect face, Context context) {
        super(face, context);
        this.values = new float[3];
    }

    public float[] findValues() {
        Point bl = face.getFacePoint("mouth_lower_lip_left_contour2");
        Point tc = face.getFacePoint("mouth_lower_lip_top");
        Point br = face.getFacePoint("mouth_lower_lip_right_contour2");

        List<float[]> colourSet = getPixels(bl, tc, br);

        WeiszfeldAlgorithm.removeExtreamsHSV(colourSet, 10, 0.25);

        values = WeiszfeldAlgorithm.geometricMedianHSV(colourSet, 0.001);

        return values;
    }

    private List<float[]> getPixels(Point v1, Point v2, Point v3) {
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(
                    context.getContentResolver(), face.getImg());
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<float[]> pixels = new ArrayList<>();
        int maxX = Math.max(v1.x, Math.max(v2.x, v3.x));
        int minX = Math.min(v1.x, Math.min(v2.x, v3.x));
        int maxY = Math.max(v1.y, Math.max(v2.y, v3.y));
        int minY = Math.min(v1.y, Math.min(v2.y, v3.y));


        Point vs1 = new Point(v2.x - v1.x, v2.y - v1.y);
        Point vs2 = new Point(v3.x - v1.x, v3.y - v1.y);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                Point q = new Point(x - v1.x, y - v1.y);

                float s = crossProduct(q, vs2) / crossProduct(vs1, vs2);
                float t = crossProduct(vs1, q) / crossProduct(vs1, vs2);

                if ((s >= 0) && (t >= 0) && (s + t <= 1)) {
                    int rgb = bmp.getPixel(x, y);
                    float[] hsv = new float[3];
                    Color.colorToHSV(rgb, hsv);
                    pixels.add(hsv);
                }
            }
        }

        return pixels;
    }

    public float crossProduct(Point p1, Point p2) {
        return p1.x * p2.y - p1.y * p2.x;
    }
}

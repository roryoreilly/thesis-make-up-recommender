package roryoreilly.makeuprecommender.Classifier;


import android.graphics.Color;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import roryoreilly.makeuprecommender.utils.WeiszfeldAlgorithm;

/**
 * Created by roryorilly on 11/02/16.
 */
public class Skin extends Classifier{

    public Skin(FaceDetect face) {
        super(face);
        this.values = new float[3];
    }

    @Override
    public float[] findValues() {
        List<float[]> colourSet = new ArrayList<>();

        List<Point> pLeft = getBaseSkinCorners("left");
        List<Point> pRight = getBaseSkinCorners("right");
        colourSet.addAll(getPixels(pLeft.get(0), pLeft.get(1), pLeft.get(2)));
        colourSet.addAll(getPixels(pRight.get(0), pRight.get(1), pRight.get(2)));

        WeiszfeldAlgorithm.removeExtreamsHSV(colourSet, 10, 0.25);

        values = WeiszfeldAlgorithm.geometricMedianHSV(colourSet, 0.001);

        return values;
    }

    private List<Point> getBaseSkinCorners(String side) {
        List<Point> corners = new ArrayList<>();
        Point eye = face.getFacePoint(side + "_eye_lower_" + side + "_quarter");
        eye.y += (face.getFaceWidth()*0.1);
        Point nose = face.getFacePoint("nose_contour_" + side + "2");
        Point mouth = face.getFacePoint("mouth_" + side + "_corner");
        mouth.x += (face.getFaceWidth()*0.075);

        corners.add(0, eye);
        corners.add(1, nose);
        corners.add(2, mouth);
        return corners;
    }

    private List<float[]> getPixels(Point v1, Point v2, Point v3) {
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
                    int rgb = face.getImg().getPixel(x, y);
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

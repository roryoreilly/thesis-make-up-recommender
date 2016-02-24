package roryoreilly.makeuprecommender.Classifier;

import android.graphics.Color;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import roryoreilly.makeuprecommender.utils.WeiszfeldAlgorithm;

/**
 * Created by roryorilly on 11/02/16.
 */
public class Hair extends Classifier {
    public Hair(FaceDetect face) {
        super(face);
        this.values = new float[3];
    }

    public float[] findValues() {
        Point center = face.getCenter();

        Point tl = new Point((int)(center.x-(face.getFaceWidth()/2)), (int)(center.y-(1.2*face.getFaceWidth()/2)));

        Point tc = new Point(center.x, (int)(center.y-(0.9*face.getFaceWidth())));
        if (tc.y<=0) {
            while (tc.y<=0) {
                tc.y += 1;
            }
        }

        Point tr = new Point((int)(center.x+(face.getFaceWidth()/2)), (int)(center.y-(1.2*face.getFaceWidth()/2)));

        List<float[]> colourSet = coloursFromLine(tl, tc);
        colourSet.addAll(coloursFromLine(tc, tr));

        WeiszfeldAlgorithm.removeExtreamsHSV(colourSet, 10, 0.25);

        values = WeiszfeldAlgorithm.geometricMedianHSV(colourSet, 0.001);

        return values;
    }

    private List<float[]> coloursFromLine(Point p1, Point p2) {
        List<float[]> colourSet = new ArrayList<>();
        int x = (int) p1.x, y = (int) p1.y;
        int w = (int) (p2.x-p1.x), h = (int) (p2.y-p1.y);

        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
        if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1;
        if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1;
        if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1;
        int longest = Math.abs(w);
        int shortest = Math.abs(h);
        if (!(longest>shortest)) {
            longest = Math.abs(h);
            shortest = Math.abs(w);
            if (h<0) dy2 = -1; else if (h>0) dy2 = 1;
            dx2 = 0;
        }

        int numerator = longest >> 1;
        for (int i=0;i<=longest;i++) {
            float[] c = new float[3];
            Color.colorToHSV(face.getImg().getPixel(x, y), c);
            if (c[0] > 6) {
                colourSet.add(c);
            }
            numerator += shortest ;
            if (!(numerator<longest)) {
                numerator -= longest ;
                x += dx1 ;
                y += dy1 ;
            } else {
                x += dx2 ;
                y += dy2 ;
            }
        }

        return colourSet;
    }
}

package roryoreilly.makeuprecommender.Classifier;

import android.graphics.Point;

import org.apache.commons.math3.analysis.integration.TrapezoidIntegrator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;

import java.util.ArrayList;
import java.util.List;

public class Shape extends Classifier{

    public Shape(FaceDetect face) {
        super(face);
        this.values = new float[5];
    }

    /*
    TODO change this to be implemented with interface
     */
    @Override
    public float[] findValues() {
        List<Point> contours = getContours(2);
        values[0] = (float) widthOverHeight();

        values[1] = (float) funcErrorRatio(contours);
        values[2] = (float) integralRatio(contours);
        values[3] = (float) chinDistance(contours);
        values[4] = (float) inOutRatio(getContours(4));

        return values;
    }

    private double widthOverHeight() {
        return (face.getFaceWidth()/face.getFaceHeight());
    }

    public double funcErrorRatio(List<Point> contours) {
        PolynomialFunction pf2 = gradientDescent(contours, 2);
        PolynomialFunction pf4 = gradientDescent(contours, 4);
        double error2 = findError(contours, pf2);
        double error4 = findError(contours, pf4);

        return error4 / error2;
    }

    /*
     * Classifiers whereby the ratio of the integral of the curve to the total area
     * in the curve's plane
     */
    public double integralRatio(List<Point> contours) {
        PolynomialFunction pf4 = gradientDescent(contours, 4);
        double integral4 = findIntegral(pf4, contours.get(0), contours.get(contours.size() / 2));
        double size = (contours.get(0).x - contours.get(contours.size()/2).x)
                      * (contours.get(0).y - contours.get(contours.size() / 2).y);

        return (integral4 / size);
    }

    public double inOutRatio(List<Point> contours) {
        PolynomialFunction pf2 = gradientDescent(contours, 2);
        Point corner = new Point((int)(face.getCenter().x+(face.getFaceWidth()/2)),
                                (int)(face.getCenter().y+(face.getFaceHeight()/2)));
        return ratioCenSkinEdge(face.getCenter(), pf2, corner);
    }

    public double chinDistance(List<Point> contours) {
        PolynomialFunction pf2 = gradientDescent(contours, 2);
        double chinDis = chinDistance(pf2, contours.get(contours.size() / 2));
        double height = face.getFaceHeight();

        return chinDis/height;
    }

    // gets all the contours around the face
    private List<Point> getContours(int to) {
        List<Point> contours = new ArrayList<>();

        // adds the contours on the left of the face
        for (int i = to; i <= 8; i++) {
            contours.add(face.getFacePoint("contour_left" + i));
        }

        // adds the contours at the bottom of the chin
        contours.add(face.getFacePoint("contour_chin"));

        // adds the contours on the right of the face
        for (int i = 8; i >= to; i--) {
            contours.add(face.getFacePoint("contour_right" + i));
        }

        return contours;
    }

    // gets the ratio of the center of the face to the point at which
    // it hits the skin with a line to the facial recognition box corner
    // over the total length of center to facial corner
    private double ratioCenSkinEdge(Point center, PolynomialFunction jaw, Point corner) {
        double slope = (center.y-corner.y) / (center.x-corner.x);
        // get value(not object) of center
        int bisectX = center.x;

        //equation: y = slope(x-center.x)+center.y
        while (jaw.value(bisectX) > (slope*(bisectX-center.x))+center.y) {
            bisectX++;
        }
        Point skinEdge = new Point(bisectX, (int)slope*(bisectX-center.x)+center.y);

        double insideLen = Math.sqrt(
                Math.pow(center.x - skinEdge.x, 2)
                        + Math.pow(center.y - skinEdge.y, 2));
        double totalLen = Math.sqrt(
                Math.pow(center.x - corner.x, 2)
                        + Math.pow(center.y - corner.y, 2));

        return insideLen/totalLen;
    }

    // gets the total error of every point from the facial recognition landmarks
    // to the fitted polynomial function
    private double findError (List<Point> contours, PolynomialFunction pf) {
        double totalError = 0.0;

        for (Point point: contours) {
            totalError += distanceFromPoint(point, pf);
        }

        return totalError;
    }

    // finds the closest distance from a point to a polynomial
    private double distanceFromPoint(Point point, PolynomialFunction pf) {
        boolean foundBest = false;

        Point pointOnCurve = new Point(point.x, (int)pf.value(point.x));
        double bestDistance = Math.sqrt(
                Math.pow(point.x-pointOnCurve.x, 2)
                        + Math.pow(point.y-pointOnCurve.y, 2));
        do {
            pointOnCurve = new Point(point.x+1, (int)pf.value(point.x+1));
            double currentDistance = Math.sqrt(
                    Math.pow(point.x-pointOnCurve.x, 2)
                            + Math.pow(point.y-pointOnCurve.y, 2));

            if (currentDistance < bestDistance) {
                bestDistance = currentDistance;
            } else {
                pointOnCurve = new Point(point.x-1, (int)pf.value(point.x-1));
                currentDistance = Math.sqrt(
                        Math.pow(point.x-pointOnCurve.x, 2)
                                + Math.pow(point.y-pointOnCurve.y, 2));

                if (currentDistance < bestDistance) {
                    bestDistance = currentDistance;
                } else {
                    foundBest = true;
                }
            }
        } while (!foundBest);

        return bestDistance;
    }


    //
    public PolynomialFunction gradientDescent(List<Point> contours, int degree) {
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(degree);
        List<WeightedObservedPoint> points = new ArrayList<>();
        for(Point c: contours) {
            points.add(new WeightedObservedPoint(1, c.x, c.y));
        }

        return new PolynomialFunction(fitter.fit(points));
    }

    private double chinDistance(PolynomialFunction pf, Point chin) {
        double yPf = pf.value(chin.x);

        return yPf-chin.y;
    }

    // finds the integral of the curve from a range (lower to upper)
    private double findIntegral(PolynomialFunction pf, Point lowerBound, Point upperBound) {
        TrapezoidIntegrator trapezoid = new TrapezoidIntegrator();

        double area = trapezoid.integrate(10000, pf, lowerBound.x, upperBound.x);

        area -= (upperBound.x - lowerBound.x)*lowerBound.y;

        return area;
    }
}

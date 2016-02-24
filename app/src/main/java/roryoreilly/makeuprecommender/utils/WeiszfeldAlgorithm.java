package roryoreilly.makeuprecommender.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WeiszfeldAlgorithm {

    public static float[] geometricMedianHSV(List<float[]> pointSet, double epsilon) {
        return geometricMedian(pointSet, epsilon, 360.0, 1.0, 1.0);
    }

    public static float[] geometricMedian(List<float[]> pointSet, double epsilon,
                                          double range1, double range2, double range3) {
        int size = 0;
        // get the centroid
        float[] appMed = {0, 0, 0};
        for(float[] d : pointSet) {
            if (d == null)
                continue;
            size++;
			d[0] /= range1;
			d[1] /= range2;
			d[2] /= range3;
            appMed[0] += d[0];
            appMed[1] += d[1];
            appMed[2] += d[2];
        }
        appMed[0] = appMed[0]/size;
        appMed[1] = appMed[1]/size;
        appMed[2] = appMed[2]/size;

        int count = 0;
        while (true){
            count++;
            float[] newMed = medianApprox(pointSet, appMed);
            if (eucDis(newMed, appMed) < epsilon || count>50) {
                for(float[] d : pointSet) {
					d[0] *= range1;
					d[1] *= range2;
					d[2] *= range3;
                }
				newMed[0] *= range1;
				newMed[1] *= range2;
				newMed[2] *= range3;
                return newMed;
            }
            appMed = newMed;
        }
    }

    private static float[] medianApprox(List<float[]> pointSet, float[] prevMed) {
        float[] geoMed = {0, 0, 0};
        float totalWeight = 0.0f;
        for (float[] point: pointSet) {
            if (point == null)
                continue;

            float distance = eucDis(point, prevMed);
            if (distance != 0) {
                float weight = 1.0f / distance;
                totalWeight += weight;
                geoMed[0] += point[0] * weight;
                geoMed[1] += point[1] * weight;
                geoMed[2] += point[2] * weight;
            }
        }
        return new float[] {geoMed[0]/totalWeight, geoMed[1]/totalWeight, geoMed[2]/totalWeight};
    }

    private static float eucDis(float[] q, float[] p) {
        return (float)(Math.sqrt(
                Math.pow(q[0]-p[0], 2)
                        + Math.pow(q[1]-p[1], 2)
                        + Math.pow(q[2]-p[2], 2)));
    }

    public static void removeExtreamsHSV(List<float[]> colourSet, int itterations, double cutAmount) {
        for (int i=0; i<=itterations; i ++) {
            float[] average = geometricMedianHSV(colourSet, 0.001);
            sortCSet(colourSet, average);
            int removeAmount = (int) (colourSet.size()*(cutAmount));
            while (removeAmount>0) {
                colourSet.remove(colourSet.size()-1);
                removeAmount--;
            }
        }
    }

    private static void sortCSet(List<float[]> colourSet, final float[] average) {
        Collections.sort(colourSet, new Comparator<float[]>() {
            @Override
            public int compare(float[] c1, float[] c2) {
                return Double.compare(eucDis(c1, average), eucDis(c2, average));
            }

            private double eucDis(float[] q, float[] p) {
                return Math.sqrt(
                        Math.pow(q[0] - p[0], 2)
                                + Math.pow(q[1] - p[1], 2)
                                + Math.pow(q[2] - p[2], 2));
            }
        });
    }
}
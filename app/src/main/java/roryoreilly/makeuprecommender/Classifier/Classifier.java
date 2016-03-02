package roryoreilly.makeuprecommender.Classifier;


import android.content.Context;

public class Classifier {
    protected FaceDetect face;
    protected float[] values;
    protected String className;
    protected Context context;

    public Classifier(FaceDetect face) {
        this.face = face;
    }

    public Classifier(FaceDetect face, Context context) {
        this.face = face;
        this.context = context;
    }

    public float[] findValues() {
        values = new float[]{0.0f, 0.0f, 0.0f};
        return values;
    }

    public float[] getValues() {
        return values;
    }

    public void setClassName(String name) {
        this.className = name;
    }

    public String getClassName() {
        return className;
    }
}

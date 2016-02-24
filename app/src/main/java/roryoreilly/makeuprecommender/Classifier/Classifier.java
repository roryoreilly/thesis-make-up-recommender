package roryoreilly.makeuprecommender.Classifier;


public class Classifier {
    protected FaceDetect face;
    protected float[] values;
    protected String className;

    public Classifier(FaceDetect face) {
        this.face = face;
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

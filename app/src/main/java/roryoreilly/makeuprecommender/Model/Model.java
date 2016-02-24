package roryoreilly.makeuprecommender.Model;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import roryoreilly.makeuprecommender.R;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class Model {
    public static final int EYE_FILE = R.raw.eye_colour;
    public static final int SHAPE_FILE = R.raw.face_shape;
    public static final int HAIR_FILE = R.raw.eye_colour;

    public Context context;

    public Model(Context context) {
        this.context = context;
    }

    public String findClass(float[] values, int file) throws NullPointerException{
        BufferedReader datafile = readDataFile(file);

        Instances training = null;
        try {
            training = new Instances(datafile);
        } catch (IOException e) {
            System.err.print("Failed to create training set");
            e.printStackTrace();
        }
        if (training != null) {
            training.setClassIndex(training.numAttributes() - 1);
        } else {
            throw new NullPointerException();
        }

        Instances testing = createTest(training, values);

        // Use a set of classifiers
        Classifier models = new J48();

        Evaluation validation = null;
        try {
            validation = classify(models, training, testing);
        } catch (Exception e) {
            System.err.print("Failed to Classify test set");
            e.printStackTrace();
        }

        int classIndex = 0;
        if (validation != null) {
            classIndex = getClass(validation.confusionMatrix(), training.numClasses());
        }

        return training.classAttribute().value(classIndex);
    }

    public int getClass(double[][] matrix, int classes) {
        for (int i=0; i< classes; i++) {
            for (int j=0; j< classes; j++) {
                if (matrix[i][j] > 0) {
                    return i;
                }
            }
        }

        return 0;
    }

    public Instances createTest(Instances training, float[] values) {
        FastVector atts = new FastVector();

        for (int i=0; i< training.numAttributes(); i++) {
            atts.addElement(training.attribute(i));
        }

        Instances testInstance = new Instances("TestInstances", atts, 0);

        double[] attributeValues = new double[testInstance.numAttributes()];

        for (int i=0; i<values.length; i++) {
            attributeValues[i] = (double)values[i];
        }
        attributeValues[values.length] = 0;

        testInstance.add(new Instance(1.0, attributeValues));

        testInstance.setClassIndex(testInstance.numAttributes()-1);

        return testInstance;
    }

    public BufferedReader readDataFile(int file) {
        InputStream is = context.getResources().openRawResource(file);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader inputReader = new BufferedReader(isr, 8192);

        return inputReader;
    }

    public static Evaluation classify(Classifier model,
                                      Instances trainingSet, Instances testingSet) throws Exception {

        Evaluation evaluation = new Evaluation(trainingSet);

        model.buildClassifier(trainingSet);
        evaluation.evaluateModel(model, testingSet);

        return evaluation;
    }

}

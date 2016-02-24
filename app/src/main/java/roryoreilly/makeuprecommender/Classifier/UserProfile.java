package roryoreilly.makeuprecommender.Classifier;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;



import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import roryoreilly.makeuprecommender.Model.Model;
import roryoreilly.makeuprecommender.Model.SkinModel;
import roryoreilly.makeuprecommender.R;

public class UserProfile {
//    private FaceDetect face;
    private Eye eye;
    private Hair hair;
    private Shape shape;
    private Skin skin;
    private Context context;


    public UserProfile(Context context) {
        this.context = context;
    }

//    public void detectAndClassify(byte[] img) {
//        AsyncTask asyncDetect = new Detect().execute(toObjects(img));
//        try {
//            asyncDetect.get(TIMEOUT, TimeUnit.MILLISECONDS);
//        } catch (InterruptedException e) {
//            Log.d("Face Detection", "Interruptied Exception");
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            Log.d("Face Detection", "Execution Exception");
//            e.printStackTrace();
//        } catch (TimeoutException e) {
//            Log.d("Face Detection", "Timeout Exception");
//            e.printStackTrace();
//        }
//    }

    public void classify(FaceDetect face)  {
        eye = new Eye(face);
        hair = new Hair(face);
        shape = new Shape(face);
        skin = new Skin(face);

        eye.findValues();
        hair.findValues();
        shape.findValues();
        skin.findValues();

        Model model = new Model(context);
        eye.setClassName(model.findClass(eye.getValues(), Model.EYE_FILE));
        hair.setClassName(model.findClass(hair.getValues(), Model.HAIR_FILE));
        shape.setClassName(model.findClass(shape.getValues(), Model.SHAPE_FILE));
        skin.setClassName(SkinModel.findClass(skin.getValues()));
    }


    public Eye getEye() {
        return eye;
    }

    public Hair getHair() {
        return hair;
    }

    public Shape getShape() {
        return shape;
    }

    public Skin getSkin() {
        return skin;
    }

    public String[] summary() {
        return new String[]{skin.getClassName() + " skin tones",
                eye.getClassName() + " eyes",
                eye.getClassName() + " hair",
                eye.getClassName() + " face shape"};
    }




}

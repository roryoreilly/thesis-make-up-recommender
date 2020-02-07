package roryoreilly.makeuprecommender.Classifier;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;



import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import roryoreilly.makeuprecommender.Helper.UserPackage;
import roryoreilly.makeuprecommender.Model.Model;
import roryoreilly.makeuprecommender.Model.SkinModel;
import roryoreilly.makeuprecommender.R;
import roryoreilly.makeuprecommender.Recommender.SkinTone;

public class UserProfile {
    private Eye eye;
    private Hair hair;
    private Shape shape;
    private Skin skin;
    private Lip lip;
    private Context context;


    public UserProfile(Context context) {
        this.context = context;
    }

    public void classify(FaceDetect face)  {
        eye = new Eye(face, context);
        hair = new Hair(face, context);
        shape = new Shape(face);
        skin = new Skin(face, context);
        lip = new Lip(face, context);

        eye.findValues();
        hair.findValues();
        shape.findValues();
        skin.findValues();
        lip.findValues();

        Log.d("Skin Hue", String.valueOf(skin.getValues()[0])
                + ", " + String.valueOf(skin.getValues()[1])
                + ", " +String.valueOf(skin.getValues()[2]));

        Model model = new Model(context);
        eye.setClassName(model.findClass(eye.getValues(), Model.EYE_FILE));
        hair.setClassName(model.findClass(hair.getValues(), Model.HAIR_FILE));
        shape.setClassName(model.findClass(shape.getValues(), Model.SHAPE_FILE));
        SkinTone tone = SkinModel.findClass(skin.getValues());
        skin.setClassName(tone.getName());
        skin.setTone(tone);
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
        return new String[]{skin.getTone().getCode(),
                eye.getClassName() + " eyes",
                hair.getClassName() + " hair",
                shape.getClassName() + " face shape"};
    }

    public UserPackage getPackage() {
        return new UserPackage(eye.getClassName(),
                skin.getTone().getCode(),
                hair.getClassName(),
                shape.getClassName(),
                skin.getValues(),
                skin.getTone().isWarm(),
                lip.getValues());
    }




}

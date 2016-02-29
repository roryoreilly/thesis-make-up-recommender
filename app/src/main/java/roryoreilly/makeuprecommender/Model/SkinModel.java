package roryoreilly.makeuprecommender.Model;

import java.util.ArrayList;
import java.util.List;

import roryoreilly.makeuprecommender.Recommender.SkinTone;

/**
 * Created by roryorilly on 15/02/16.
 */
public class SkinModel {
    private List<SkinTone> tones;
    public static String findClass(float[] values) {

        return "light-warm";
    }

    private void setUpSkinTones(){
        tones = new ArrayList<>();
        tones.add(new SkinTone("N"));
    }
}

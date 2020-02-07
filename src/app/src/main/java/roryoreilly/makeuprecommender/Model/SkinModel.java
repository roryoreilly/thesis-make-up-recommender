package roryoreilly.makeuprecommender.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import roryoreilly.makeuprecommender.Recommender.SkinTone;

/**
 * Created by roryorilly on 15/02/16.
 */
public class SkinModel {
    public static SkinTone findClass(float[] skin) {
        List<SkinTone> tones = setUpSkinTones();

        sortTonesToSkin(tones, skin);
        for(int i=0; i<5; i++) {
            Log.d("SkinTone", tones.get(i).getCode());
        }

        return tones.get(0);
    }

    private static List<SkinTone> setUpSkinTones(){
        List<SkinTone> tones = new ArrayList<>(); // 0.0827,  0.4796,  0.7038
        tones.add(new SkinTone("NC 15", new float[]{0.09394f, 0.2466f, 0.8745f}));
        tones.add(new SkinTone("NC 20", new float[]{0.09313f, 0.3119f, 0.8549f}));
        tones.add(new SkinTone("NC 25", new float[]{0.09233f, 0.3410f, 0.8510f}));
        tones.add(new SkinTone("NC 30", new float[]{0.09869f, 0.3585f, 0.8314f}));
        tones.add(new SkinTone("NC 35", new float[]{0.09211f, 0.3654f, 0.8157f}));
        tones.add(new SkinTone("NC 40", new float[]{0.09147f, 0.3923f, 0.8196f}));
        tones.add(new SkinTone("NC 45", new float[]{0.08144f, 0.4467f, 0.7725f}));
        tones.add(new SkinTone("NC 50", new float[]{0.07875f, 0.5028f, 0.7098f}));
        tones.add(new SkinTone("NC 55", new float[]{0.08244f, 0.5196f, 0.7020f}));

        tones.add(new SkinTone("NW 10", new float[]{0.08119f, 0.1703f, 0.8980f}));
        tones.add(new SkinTone("NW 15", new float[]{0.08333f, 0.2768f, 0.8784f}));
        tones.add(new SkinTone("NW 20", new float[]{0.08055f, 0.2740f, 0.8588f}));
        tones.add(new SkinTone("NW 25", new float[]{0.07827f, 0.3099f, 0.8353f}));
        tones.add(new SkinTone("NW 30", new float[]{0.07894f, 0.3568f, 0.8353f}));
        tones.add(new SkinTone("NW 35", new float[]{0.08333f, 0.3961f, 0.8118f}));
        tones.add(new SkinTone("NW 40", new float[]{0.08722f, 0.4236f, 0.7961f}));
        tones.add(new SkinTone("NW 45", new float[]{0.07544f, 0.5135f, 0.7255f}));
        tones.add(new SkinTone("NW 50", new float[]{0.07227f, 0.5188f, 0.6275f}));
        tones.add(new SkinTone("NW 55", new float[]{0.06677f, 0.5385f, 0.6627f}));

        return tones;
    }

    private static void sortTonesToSkin(List<SkinTone> tones, final float[] skin) {
        Collections.sort(tones, new Comparator<SkinTone>() {
            @Override
            public int compare(SkinTone lhs, SkinTone rhs) {
                return Double.compare(lhs.distance(skin), rhs.distance(skin));
            }
        });
    }
}

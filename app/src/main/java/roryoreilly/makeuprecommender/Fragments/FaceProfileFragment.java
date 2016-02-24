package roryoreilly.makeuprecommender.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import roryoreilly.makeuprecommender.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class FaceProfileFragment extends Fragment {

    public FaceProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        return inflater.inflate(R.layout.fragment_face_profile, container, false);
    }
}

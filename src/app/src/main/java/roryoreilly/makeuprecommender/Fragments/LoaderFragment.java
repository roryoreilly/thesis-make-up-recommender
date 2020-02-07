package roryoreilly.makeuprecommender.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import roryoreilly.makeuprecommender.R;

public class LoaderFragment extends Fragment {
    public final String IMAGE_ID = "bundle_image";
    private OnFragmentInteractionListener mListener;
    ImageView loaderImage;
    private int curImage;

    Integer[] imageId = {
            R.drawable.loading_1, R.drawable.loading_2, R.drawable.loading_3,
            R.drawable.loading_4, R.drawable.loading_5, R.drawable.loading_6,
            R.drawable.loading_7, R.drawable.loading_8, R.drawable.loading_9,
            R.drawable.loading_10,R.drawable.loading_11,R.drawable.loading_12,
            R.drawable.loading_13,R.drawable.loading_14,R.drawable.loading_15,
            R.drawable.loading_16,R.drawable.loading_17,R.drawable.loading_18,
            R.drawable.loading_19,R.drawable.loading_20,R.drawable.loading_21,
            R.drawable.loading_22,R.drawable.loading_23,R.drawable.loading_24,
            R.drawable.loading_25,R.drawable.loading_26,R.drawable.loading_27,
            R.drawable.loading_28,R.drawable.loading_29,R.drawable.loading_30,
            R.drawable.loading_31,R.drawable.loading_32,R.drawable.loading_33,
            R.drawable.loading_34,R.drawable.loading_35,R.drawable.loading_36,
            R.drawable.loading_37,R.drawable.loading_38};

    public LoaderFragment() {
        curImage = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        curImage = getArguments().getInt(IMAGE_ID);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loader, container, false);
//        loaderImage = (ImageView) view.findViewById(R.id.loaderImage);
//        loaderImage.setImageResource(imageId[curImage]);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onUpdateNextImage(int i) {
        loaderImage.setImageResource(imageId[curImage]);
        curImage++;
        if (curImage > 37) {
            curImage = 0;
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

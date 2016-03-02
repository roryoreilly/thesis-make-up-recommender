package roryoreilly.makeuprecommender;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

//import com.facepp.http.HttpRequests;
//import com.facepp.http.PostParameters;
//import com.facepp.result.FaceppResult;
import roryoreilly.makeuprecommender.Facepp.Http.HttpRequests;
import roryoreilly.makeuprecommender.Facepp.Http.PostParameters;
import roryoreilly.makeuprecommender.Facepp.Result.FaceppResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import roryoreilly.makeuprecommender.Classifier.FaceDetect;
import roryoreilly.makeuprecommender.Classifier.UserProfile;
import roryoreilly.makeuprecommender.Fragments.LoaderFragment;
import roryoreilly.makeuprecommender.View.ProfileListAdapter;
import roryoreilly.makeuprecommender.utils.BitmapHelper;

public class FaceProfileActivity extends Activity
        implements LoaderFragment.OnFragmentInteractionListener{
    public static final String EXTRA_IMAGE_BYTE = "faceprofileactivity.extra.image";
    public static final String EXTRA_IMAGE_URI = "faceprofileactivity.extra.uri";
    public static final String LOADER_FRAG = "loader fragment";

    private final int TIMEOUT = 125000;

    @Bind(R.id.profile_list) ListView list;
    @Bind(R.id.profile_image) ImageView profileImage;
    @Bind(R.id.button_to_styles) Button stylesButton;

    FragmentManager fragmentManager;
    UserProfile user;
    boolean loaderInPlace;

    Integer[] imgid = {
            R.drawable.profile_skin,
            R.drawable.profile_eye,
            R.drawable.profile_hair,
            R.drawable.profile_shape };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_profile);
        ButterKnife.bind(this);

        // places the loading fragment over the screen while the data is collected
        fragmentManager = getFragmentManager();

        loaderInPlace = true;

//        byte[] imgByte = getIntent().getExtras().getByteArray(EXTRA_IMAGE_BYTE);
//        Bitmap img = BitmapHelper.byteArrayToBitmap(imgByte);
//        profileImage.setImageBitmap(img);

        String fileSrc = getIntent().getExtras().getString(EXTRA_IMAGE_URI);
        System.out.println(fileSrc);
        Uri uri = Uri.fromFile(new File(fileSrc));
        profileImage.setImageURI(uri);

//        new Thread() {
//            public void run() {
//                int i =0;
//                while(loaderInPlace) {
//                    updateLoader(i);
//                    i++;
//                    if(i>37) i=0;
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();

        FaceClassify face = new FaceClassify(this);

        face.execute(uri);

        /****************
                TESTING
         ****************/
        stylesButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), StylesActivity.class);
                        intent.putExtra(StylesActivity.EXTRA_SKIN, "NC 25");
                        intent.putExtra(StylesActivity.EXTRA_EYE, "Blue Eyes");
                        intent.putExtra(StylesActivity.EXTRA_HAIR, "Brown Hair");
                        intent.putExtra(StylesActivity.EXTRA_SHAPE, "Round Face Shape");
                        v.getContext().startActivity(intent);

                    }
                }
        );
    }


    private void updateLoader(int i) {
        LoaderFragment lf = new LoaderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("bundle_image", i);
        lf.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragmentManager.findFragmentByTag(LOADER_FRAG));
        fragmentTransaction.add(android.R.id.content, lf, LOADER_FRAG);
        fragmentTransaction.commit();
    }

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    private class LoaderAsync extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            LoaderFragment loaderFragment = new LoaderFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("bundle_image", 0);
            loaderFragment.setArguments(bundle);
            fragmentTransaction.add(android.R.id.content, loaderFragment, LOADER_FRAG);
            fragmentTransaction.commit();
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            int i =0;
            while(loaderInPlace) {
                publishProgress(i);
                i++;
                if(i>37) i=0;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            LoaderFragment lf = new LoaderFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("bundle_image", values[0]);
            lf.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragmentManager.findFragmentByTag(LOADER_FRAG));
            fragmentTransaction.add(android.R.id.content, lf, LOADER_FRAG);
            fragmentTransaction.commit();
        }

        @Override
        protected void onPostExecute(Boolean b) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragmentManager.findFragmentByTag(LOADER_FRAG));
            fragmentTransaction.commit();
        }
    }
    private class FaceClassify extends AsyncTask<Uri, Integer, FaceDetect> {
        Activity activity;

        private FaceClassify (Activity activity) {
            this.activity = activity;
        }

        // Places the loading screen over the activity
        @Override
        protected void onPreExecute() {
            LoaderFragment lf = new LoaderFragment();
            Bundle bundle = new Bundle();
            lf.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(android.R.id.content, lf, LOADER_FRAG);
            fragmentTransaction.commit();
        }

        // classifies the face
        @Override
        protected FaceDetect doInBackground(Uri... data) {
            Uri uri = data[0];
            FaceDetect face = new FaceDetect();
            Log.d("FaceDetect", "Attempting to find the face landmarks");

            try {
                InputStream iStream = getContentResolver().openInputStream(uri);
                byte[] imgStream = BitmapHelper.getBytes(iStream);

                HttpRequests httpRequests = new HttpRequests(
    //                    "ac5ac2ee68aa82214738f2de7935c376",     // America server
    //                    "ECiD5AGI5077NKXwJjXqOmB1Xa94eNFn");
    //                    "8804929f191f061f387bcc8a19a7dc0e",   // China server
    //                    "jFOQ0iRKTmvlId_8cAK2vej-eJRfwXeY");
                        "d8f245446c63d3bc8a62a23123488a3f",     // China server
                        "VTWSmVtpxML4P9gLl60et7W0TZ2bZXrw");
                httpRequests.setHttpTimeOut(TIMEOUT);
                face.setImgFromByteArray(uri);

                FaceppResult result = httpRequests.detectionDetect(
                        new PostParameters().setImg(imgStream));
                face.setResult(result);

                String id = result.get("face").get(0).get("face_id").toString();

                face.setLandmark(httpRequests.detectionLandmark(
                        new PostParameters().setFaceId(id)));
                face.setWidthHeight();
                return face;
            } catch (Exception e) {
                Log.d("FaceProfile", "failed to get face detect");
                e.printStackTrace();
                return face;
            }
        }

        @Override
        protected void onPostExecute(FaceDetect face) {
            if (!face.isSet()) {
                Log.d("FaceDetect", "failed to detect face");
                return;
            }
            // creates the user profile and classify their skin tone, hair colour, eye colour and face shape
            user = new UserProfile(activity);
            user.classify(face);

            Uri uri = face.getImg();
            profileImage.setImageURI(uri);

            // adds the data from the user profile to the list view
            final String[] userProfileSummary = user.summary();

            ProfileListAdapter adapter = new ProfileListAdapter(activity, userProfileSummary, imgid);
            list.setAdapter(adapter);


            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragmentManager.findFragmentByTag(LOADER_FRAG));
            fragmentTransaction.commit();
            loaderInPlace = false;

            stylesButton.setOnClickListener(
                    new Button.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), StylesActivity.class);
                            intent.putExtra(StylesActivity.EXTRA_SKIN, userProfileSummary[0]);
                            intent.putExtra(StylesActivity.EXTRA_EYE, userProfileSummary[1]);
                            intent.putExtra(StylesActivity.EXTRA_HAIR, userProfileSummary[2]);
                            intent.putExtra(StylesActivity.EXTRA_SHAPE, userProfileSummary[3]);
                            v.getContext().startActivity(intent);

                        }
                    }
            );
        }
    }
}

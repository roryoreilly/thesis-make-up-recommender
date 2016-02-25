package roryoreilly.makeuprecommender.Fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.ByteArrayOutputStream;

import roryoreilly.makeuprecommender.Classifier.FaceDetect;
import roryoreilly.makeuprecommender.Facepp.Http.HttpRequests;
import roryoreilly.makeuprecommender.Facepp.Http.PostParameters;
import roryoreilly.makeuprecommender.Facepp.Result.FaceppResult;
import roryoreilly.makeuprecommender.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class FaceProfileFragment extends Fragment {

    interface TaskCallbacks {
        void onPreExecute();
        void onProgressUpdate(int percent);
        void onCancelled();
        void onPostExecute();
    }

    private TaskCallbacks mCallbacks;
    private FaceAnalyseTask mTask;
    private final int TIMEOUT = 125000;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (TaskCallbacks) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);

        // Create and execute the background task.
        mTask = new FaceAnalyseTask();
        mTask.execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }


    private class FaceAnalyseTask extends AsyncTask<Bitmap, Void, FaceDetect> {

        @Override
        protected void onPreExecute() {
            if (mCallbacks != null) {
                mCallbacks.onPreExecute();
            }
        }

//        @Override
//        protected Void doInBackground(Void... ignore) {
//            for (int i = 0; !isCancelled() && i < 100; i++) {
//                SystemClock.sleep(100);
//                publishProgress(i);
//            }
//            return null;
//        }

        // classifies the face
        @Override
        protected FaceDetect doInBackground(Bitmap... data) {
            Bitmap img = data[0];
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            float scale = Math.min(1, Math.min(600f / img.getWidth(), 600f / img.getHeight()));
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap imgSmall = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, false);
            imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imgStream = stream.toByteArray();

//            byte[] img = toPrimitive(data);

            HttpRequests httpRequests = new HttpRequests(
//                    "ac5ac2ee68aa82214738f2de7935c376",     // America server
//                    "ECiD5AGI5077NKXwJjXqOmB1Xa94eNFn");
//                    "8804929f191f061f387bcc8a19a7dc0e",   // China server
//                    "jFOQ0iRKTmvlId_8cAK2vej-eJRfwXeY");
                    "d8f245446c63d3bc8a62a23123488a3f",     // China server
                    "VTWSmVtpxML4P9gLl60et7W0TZ2bZXrw");
            httpRequests.setHttpTimeOut(TIMEOUT);
            FaceDetect face = new FaceDetect();

            try {
                FaceppResult result = httpRequests.detectionDetect(
                        new PostParameters().setImg(imgStream));
                face.setResult(result);

                String id = result.get("face").get(0).get("face_id").toString();

                face.setLandmark(httpRequests.detectionLandmark(
                        new PostParameters().setFaceId(id)));
                face.setWidthHeight();
                face.setImgFromByteArray(imgStream);
                return face;
            } catch (Exception e) {
                Log.d("FaceProfile", "failed to get face detect");
                e.printStackTrace();
                return null;
            }


        }

//        @Override
//        protected void onProgressUpdate(Integer... percent) {
//            if (mCallbacks != null) {
//                mCallbacks.onProgressUpdate(percent[0]);
//            }
//        }

        @Override
        protected void onCancelled() {
            if (mCallbacks != null) {
                mCallbacks.onCancelled();
            }
        }

//        @Override
//        protected void onPostExecute(Void ignore) {
//            if (mCallbacks != null) {
//                mCallbacks.onPostExecute();
//            }
//        }
    }
}

package roryoreilly.makeuprecommender.Classifier;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;

//import com.facepp.error.FaceppParseException;
//import com.facepp.result.FaceppResult;
import roryoreilly.makeuprecommender.Facepp.Error.FaceppParseException;
import roryoreilly.makeuprecommender.Facepp.Result.FaceppResult;

import org.json.JSONException;
import org.json.JSONObject;

import roryoreilly.makeuprecommender.utils.BitmapHelper;

/**
 * Created by roryorilly on 13/02/16.
 */
public class FaceDetect {
    private FaceppResult result, landmark;
    private double faceWidth, faceHeight;
    private int imgWidth, imgHeight;
    private Uri img;

    public FaceDetect() {
    }

    public void setResult(FaceppResult result) {
        this.result = result;
    }

    public void setLandmark(FaceppResult landmark) {
        this.landmark = landmark;
    }


    /*
     * gets the correct faceWidth and faceHeight of the persons face
     */
    public void setWidthHeight() {
        try {
            this.imgWidth = this.result.get("img_width").toInteger();
            this.imgHeight = this.result.get("img_height").toInteger();
            this.faceWidth = imgWidth*(result.get("face").get(0).get("position").get("width").toDouble()/100.0);

            double center = result.get("face").get(0).get("position").get("center").get("y").toDouble();
            double chin = new JSONObject(new JSONObject((new JSONObject(
                    landmark.toString()).getJSONArray("result").getJSONObject(0))
                    .get("landmark").toString()).get("contour_chin").toString()).getDouble("y");
            this.faceHeight =(2*(chin-center))/ 100 * imgHeight;
        } catch (FaceppParseException | JSONException e) {
            e.printStackTrace();
        }
    }

    public double getFaceWidth() {
        return faceWidth;
    }

    public double getFaceHeight() {
        return faceHeight;
    }

    // gets the Point of a point from the json object
    public Point getFacePoint(String point) {
        Point v = new Point();
        try {
            JSONObject json = new JSONObject((
                    new JSONObject(
                            this.landmark.toString())
                            .getJSONArray("result")
                            .getJSONObject(0))
                    .get("landmark").toString());

            v = new Point((int) ((new JSONObject(json.get(point).toString()).getDouble("x") / 100) * this.imgWidth),
                    (int) (new JSONObject(json.get(point).toString()).getDouble("y") / 100 * this.imgHeight));
            return v;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return v;
    }

    public Point getCenter() {
        Point center = null;
        try {
            center = new Point((int)((result.get("face").get(0).get("position").get("center").get("x").toDouble()/100.0) * this.imgWidth),
                    (int)((result.get("face").get(0).get("position").get("center").get("y").toDouble()/100.0) * this.imgHeight));
        } catch(Exception e) {
            Log.d("faceDetect.java", "Failed to get center of the face");
            e.printStackTrace();
        }
        return center;
    }

    public void setImgFromByteArray(Uri img) {
        this.img = img;
    }

    public Uri getImg() {
        return img;
    }

    public String toString() {
        String s = "";
        s+= "Face Result: " + result.toString();
        s+= "\nFace Landmark: " + landmark.toString();

        return s;
    }

    public boolean isSet() {
        return (result==null) ? false : true;
    }
}

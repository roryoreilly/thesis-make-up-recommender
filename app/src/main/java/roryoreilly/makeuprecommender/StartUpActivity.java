package roryoreilly.makeuprecommender;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import roryoreilly.makeuprecommender.Database.FillDatabase;
import roryoreilly.makeuprecommender.Database.MySql;
import roryoreilly.makeuprecommender.utils.BitmapHelper;
import roryoreilly.makeuprecommender.utils.CameraIntentHelper;
import roryoreilly.makeuprecommender.utils.CameraIntentHelperCallback;

public class StartUpActivity extends Activity {
    @Bind(R.id.takephoto_button) Button takePhotoButton;
    @Bind(R.id.choosephoto_button) Button choosePhotoButton;
    private final int REQUEST_GET_PHOTO = 1;
    CameraIntentHelper mCameraIntentHelper;
    MySql db = new MySql(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        ButterKnife.bind(this);

        db.onUpgrade(db.getWritableDatabase(), 1, 2);
        FillDatabase.addProducts(db, this);

        takePhotoButton.setOnClickListener(
            new Button.OnClickListener() {
                public void onClick(View v) {
                    if (mCameraIntentHelper != null) {
                        mCameraIntentHelper.setView(v);
                        mCameraIntentHelper.startCameraIntent();
                    }
                }
            }
        );

        setupCameraIntentHelper();

        choosePhotoButton.setOnClickListener(
            new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, REQUEST_GET_PHOTO);


                    /***** TESTING ****/
//                    String fileSrc = "/storage/emulated/0/Pictures/p4finalblog-13.jpg";
//                    String fileSrc = "/storage/emulated/0/Pictures/1408174972189.jpg";
//                    Uri photoUri = Uri.fromFile(new File(fileSrc));



//                    Intent intent = new Intent(v.getContext(), FaceProfileActivity.class);
//                    intent.putExtra(FaceProfileActivity.EXTRA_IMAGE_URI, photoUri.getPath());
//                    startActivity(intent);

                }
            }
        );
    }

    private void setupCameraIntentHelper() {
        mCameraIntentHelper = new CameraIntentHelper(this, new CameraIntentHelperCallback() {
            @Override
            public void onPhotoUriFound(Date dateCameraIntentStarted, Uri photoUri, int rotateXDegrees, View view) {

                if (photoUri != null) {
                    Intent intent = new Intent(view.getContext(), FaceProfileActivity.class);
                    intent.putExtra(FaceProfileActivity.EXTRA_IMAGE_URI, photoUri.getPath());
                    view.getContext().startActivity(intent);

                }
            }

            @Override
            public void deletePhotoWithUri(Uri photoUri) {
                BitmapHelper.deleteImageWithUriIfExists(photoUri, StartUpActivity.this);
            }

            @Override
            public void onSdCardNotMounted() {
                Toast.makeText(getApplicationContext(), getString(R.string.error_sd_card_not_mounted), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCanceled() {
                Toast.makeText(getApplicationContext(), getString(R.string.warning_camera_intent_canceled), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCouldNotTakePhoto() {
                Toast.makeText(getApplicationContext(), getString(R.string.error_could_not_take_photo), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPhotoUriNotFound() {
//                messageView.setText(getString(R.string.activity_camera_intent_photo_uri_not_found));
            }

            @Override
            public void logException(Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_sth_went_wrong), Toast.LENGTH_LONG).show();
                Log.d(getClass().getName(), e.getMessage());
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        mCameraIntentHelper.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCameraIntentHelper.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GET_PHOTO && resultCode == RESULT_OK) {
            // Get photo from gallery instead
            Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String fileSrc = cursor.getString(idx);
            Uri photoUri = Uri.fromFile(new File(fileSrc));



            Log.d("Choose Photo", fileSrc);
            Intent intent = new Intent(this, FaceProfileActivity.class);
            intent.putExtra(FaceProfileActivity.EXTRA_IMAGE_URI, photoUri.getPath());
            this.startActivity(intent);
        } else if (requestCode == CameraIntentHelper.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            mCameraIntentHelper.onActivityResult(requestCode, resultCode, data);
        }
    }
}

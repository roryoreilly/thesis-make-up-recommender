package roryoreilly.makeuprecommender.utils;

import android.net.*;
import android.view.View;

import java.util.*;

/**
 * Specifies the interface of a CameraIntentHelper request. The calling class has to implement the
 * interface in order to be notified when the request completes, either successfully or with an error.
 *
 * @author Ralf Gehrer <ralf@ecotastic.de>
 */
public interface CameraIntentHelperCallback {
    void onPhotoUriFound(Date dateCameraIntentStarted, Uri photoUri, int rotateXDegrees, View v);

    void deletePhotoWithUri(Uri photoUri);

    void onSdCardNotMounted();

    void onCanceled();

    void onCouldNotTakePhoto();

    void onPhotoUriNotFound();

    void logException(Exception e);
}

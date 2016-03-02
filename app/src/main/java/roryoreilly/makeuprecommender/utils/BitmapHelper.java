package roryoreilly.makeuprecommender.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * A helper class to conveniently alter Bitmap data.
 *
 * @author Ralf Gehrer <ralf@ecotastic.de>
 */
public class BitmapHelper {
    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    /**
     * Deletes an image given its Uri and refreshes the gallery thumbnails.
     * @param cameraPicUri
     * @param context
     * @return true if it was deleted successfully, false otherwise.
     */
    public static boolean deleteImageWithUriIfExists(Uri cameraPicUri, Context context) {
        try {
            if (cameraPicUri != null) {
                File fdelete = new File(cameraPicUri.getPath());
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        refreshGalleryImages(context, fdelete);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * Forces the Android gallery to  refresh its thumbnail images.
     * @param context
     * @param fdelete
     */
    private static void refreshGalleryImages(Context context, File fdelete) {
        try {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" +  Environment.getExternalStorageDirectory())));
        } catch (Exception e1) {
            try {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(fdelete);
                mediaScanIntent.setData(contentUri);
                context.sendBroadcast(mediaScanIntent);
            } catch (Exception e2) {
            }
        }
    }
}
package fr.imt_atlantique.example.applicationcamera;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StorageUtils {

    public static File createFile(File storageDir, String folderName) throws IOException {
        // Create a file name
        String fileName = createUniqueFileName();

        // Use an existing directory or create it if necessary
        File picturesDir = new File(storageDir, folderName);
        Log.i("Chemin","Chemin d'enregistrement : "+picturesDir.getAbsolutePath());
        if (!(picturesDir.exists())) {
            picturesDir.mkdir();
        }

        // Create the name of the file with suffix .jpg
        File pathToFile = File.createTempFile(
                fileName,
                ".jpg",
                picturesDir
        );

        return pathToFile;
    }

    private static String createUniqueFileName() throws IOException {
        // Create a unique file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "JPEG_" + timeStamp + "_";

        return fileName;
    }


    //Partie qui concerne le stockage externe

    // Checks if a volume containing external storage is available
    // for read and write.
    public static boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    // Checks if a volume containing external storage is available to at least read.
    public static boolean isExternalStorageReadable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ||
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }

//    void createExternalStoragePrivatePicture() {
//        // Create a path where we will place our picture in our own private
//        // pictures directory.  Note that we don't really need to place a
//        // picture in DIRECTORY_PICTURES, since the media scanner will see
//        // all media in these directories; this may be useful with other
//        // media types such as DIRECTORY_MUSIC however to help it classify
//        // your media for display to the user.
//        File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File file = new File(path, "DemoPicture.jpg");
//
//        try {
//            // Very simple code to copy a picture from the application's
//            // resource into the external file.  Note that this code does
//            // no error checking, and assumes the picture is small (does not
//            // try to copy it in chunks).  Note that if external storage is
//            // not currently mounted this will silently fail.
//            InputStream is = getResources().openRawResource(R.drawable.balloons);
//            OutputStream os = new FileOutputStream(file);
//            byte[] data = new byte[is.available()];
//            is.read(data);
//            os.write(data);
//            is.close();
//            os.close();
//
//            // Tell the media scanner about the new file so that it is
//            // immediately available to the user.
//            MediaScannerConnection.scanFile(this,
//                    new String[] { file.toString() }, null,
//                    new MediaScannerConnection.OnScanCompletedListener() {
//                        public void onScanCompleted(String path, Uri uri) {
//                            Log.i("ExternalStorage", "Scanned " + path + ":");
//                            Log.i("ExternalStorage", "-> uri=" + uri);
//                        }
//                    });
//        } catch (IOException e) {
//            // Unable to create file, likely because external storage is
//            // not currently mounted.
//            Log.w("ExternalStorage", "Error writing " + file, e);
//        }
//    }
//
//    void deleteExternalStoragePrivatePicture() {
//        // Create a path where we will place our picture in the user's
//        // public pictures directory and delete the file.  If external
//        // storage is not currently mounted this will fail.
//        File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        if (path != null) {
//            File file = new File(path, "DemoPicture.jpg");
//            file.delete();
//        }
//    }
//
//    boolean hasExternalStoragePrivatePicture() {
//        // Create a path where we will place our picture in the user's
//        // public pictures directory and check if the file exists.  If
//        // external storage is not currently mounted this will think the
//        // picture doesn't exist.
//        File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        if (path != null) {
//            File file = new File(path, "DemoPicture.jpg");
//            return file.exists();
//        }
//        return false;
//    }
}

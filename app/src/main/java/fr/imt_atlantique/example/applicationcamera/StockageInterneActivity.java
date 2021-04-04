package fr.imt_atlantique.example.applicationcamera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class StockageInterneActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    ImageView myImageView;
    Bitmap imageBitmap;

    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockage_interne);

        myImageView = findViewById(R.id.imageView);
        saveBtn = findViewById(R.id.buttonSauvegarderPhoto);

        if(imageBitmap != null)
        {
            saveBtn.setEnabled(true);
        }
    }

    public void PrendrePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
        }
        catch (ActivityNotFoundException e)
        {
            //Error camera not found
        }
    }

    public void SauvegarderPhoto(View view) {
        try {
            File thumbnailFile = StorageUtils.createFile(getFilesDir(), "thumbnails");
            FileOutputStream fos = new FileOutputStream(thumbnailFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Snackbar mySnackbar = Snackbar.make(view, "Enregistrement effectué, la photo est disponible sur le chemin : "+thumbnailFile.getAbsolutePath(), BaseTransientBottomBar.LENGTH_INDEFINITE);
            View snackbarView = mySnackbar.getView();
            TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setMaxLines(10);

            mySnackbar.setAction("Fermer", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            mySnackbar.show();

            imageBitmap = null;
            myImageView.setImageBitmap(null);
            saveBtn.setEnabled(false);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE){
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            myImageView.setImageBitmap(imageBitmap);
            saveBtn.setEnabled(true);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

//        if(imageBitmap != null) {
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            imageBitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
//            byte[] byteArray = stream.toByteArray();
//
//            outState.putByteArray("image",byteArray);
//        }

        if(imageBitmap != null)
        {
            outState.putParcelable("image",imageBitmap);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

//        byte[] byteArray = savedInstanceState.getByteArray("image");
//
//        if(byteArray != null){
//
//            imageBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
//            myImageView.setImageBitmap(imageBitmap);
//        }

        imageBitmap = savedInstanceState.getParcelable("image");

        if(imageBitmap != null)
        {
            myImageView.setImageBitmap(imageBitmap);
            saveBtn.setEnabled(true);
        }
    }

}
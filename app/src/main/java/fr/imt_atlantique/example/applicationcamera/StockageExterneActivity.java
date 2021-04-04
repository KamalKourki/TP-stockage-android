package fr.imt_atlantique.example.applicationcamera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.security.auth.login.LoginException;

public class StockageExterneActivity extends AppCompatActivity {

    String currentPhotoPath;
    String currentTextPath="";

    ImageView myImageView;
    Bitmap imageBitmap;

    Snackbar mySnackBar;
    Snackbar snackBarText;
    View parentLayout;
    EditText myEditText;

    String myData;

    Switch mySwitch;

    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockage_externe);

        parentLayout = findViewById(R.id.stockageExterneConstraintLayout);
        mySwitch = findViewById(R.id.mySwitch);
        myEditText = findViewById(R.id.editTextTextMultiLine);
        mySwitch.setChecked(true);
        mySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!mySwitch.isChecked()){myEditText.setVisibility(View.INVISIBLE);}
            else{
                myEditText.setText("");
                myEditText.setVisibility(View.VISIBLE);}
        });

        snackBarText = Snackbar.make(parentLayout,"Aucune description pour cette photo",BaseTransientBottomBar.LENGTH_INDEFINITE);
        snackBarText.setAction("Fermer", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        myImageView = findViewById(R.id.imageViewExterne);
        myImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBarText.show();
            }
        });


        Log.i("chemin","chemin de stockage externe : "+getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        Log.i("read","External Storage readable : "+StorageUtils.isExternalStorageReadable()+"");
        Log.i("write","External Storage readable and writable : "+StorageUtils.isExternalStorageWritable()+"");
    }


    public void prendrePhotoIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Vérifier qu'une activité de caméra existe pour supporter l'Intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Créer le fichier ou on va stocker la photo
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }
            catch (IOException ex) {
                // Un erreur est survenu lors de la création du fichier
                ex.fillInStackTrace();
            }
            // Continuer seulement si le fichier est créé
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "fr.imt_atlantique.example.applicationcamera.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoURI);
                currentPhotoPath = photoFile.getAbsolutePath();
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

                mySnackBar = Snackbar.make(view, "Enregistrement effectué, la photo est disponible sur le chemin : "+currentPhotoPath ,BaseTransientBottomBar.LENGTH_INDEFINITE);

                View snackbarView = mySnackBar.getView();
                TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                textView.setMaxLines(10);

                mySnackBar.setAction("Fermer", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE)
        {
            Log.i("onActivityResult", "onActivityResult entred, photo saved in : "+currentPhotoPath);

            if(currentPhotoPath!="") {
                File f = new File(currentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
                    myImageView.setImageBitmap(imageBitmap);

                    mySnackBar.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            if(currentTextPath!="")
            {
                try {
                    FileInputStream fis = new FileInputStream(currentTextPath);
                    DataInputStream in = new DataInputStream(fis);
                    BufferedReader br =
                            new BufferedReader(new InputStreamReader(in));
                    String strLine;
                    while ((strLine = br.readLine()) != null) {
                        if(myData == null)
                        {
                            myData = strLine;
                        }
                        else {
                            myData = myData + strLine;
                        }
                    }
                    in.close();

                    snackBarText.setText("Description de l'image : "+myData);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }


//            if(currentPhotoPath !="")
//            {
//                galleryAddPic();
//            }
//            Bundle extras = data.getExtras();
//            imageBitmap = (Bitmap) extras.get("data");
//            myImageView.setImageBitmap(imageBitmap);
//            saveBtn.setEnabled(true);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(imageBitmap != null)
        {
            outState.putParcelable("image",imageBitmap);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        imageBitmap = savedInstanceState.getParcelable("image");

        if(imageBitmap != null)
        {
            myImageView.setImageBitmap(imageBitmap);
        }
    }

    public void PrendrePhotoExterne(View view) {
        if (mySwitch.isChecked())
        {
            if(myEditText.getText().toString()!="") {
                saveTextFile(myEditText.getText().toString().getBytes());
            }
        }

        myImageView.setImageBitmap(null);

        prendrePhotoIntent(view);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File createTextFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TXT_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File text = File.createTempFile(
                imageFileName,  /* prefix */
                ".txt",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentTextPath = text.getAbsolutePath();
        return text;
    }

    private void saveTextFile(byte[] contenu)
    {
        File textFile = null;
        try {
            textFile = createTextFile();
        }
        catch (IOException ex) {
            // Un erreur est survenu lors de la création du fichier
            ex.fillInStackTrace();
        }
        // Continuer seulement si le fichier est créé
        if (textFile != null) {
            try {
                FileOutputStream fos = new FileOutputStream(textFile);
                fos.write(contenu);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            Uri textURI = FileProvider.getUriForFile(this,
//                    "fr.imt_atlantique.example.applicationcamera.fileprovider",
//                    textFile);
            currentPhotoPath = textFile.getAbsolutePath();
        }

    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}
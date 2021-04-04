package fr.imt_atlantique.example.applicationcamera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void stockageInterne(View view) {
        Intent interneIntent = new Intent(this, StockageInterneActivity.class);
        startActivity(interneIntent);
    }

    public void stockageExterne(View view) {
        Intent externeIntent = new Intent(this, StockageExterneActivity.class);
        startActivity(externeIntent);
    }
}
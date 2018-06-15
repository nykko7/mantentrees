package cl.unab.mantentrees;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DescripcionArbol extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_arbol);

        TextView detalles = findViewById(R.id.tv_detalles);

        //get data from previous activity when item of listview is clicked using intent
        Intent intent = getIntent();
        String contenido = intent.getStringExtra("contenido");

        detalles.setText(contenido);
    }
}

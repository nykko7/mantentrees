package cl.unab.mantentrees;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuPrincipal extends AppCompatActivity {
    Button botonSalir;
    Button botonRegistrar;
    Button botonRegistros;
    Button botonMapa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        botonSalir = (Button) findViewById(R.id.boton_salir);
        botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volver();
            }
        });

        botonRegistrar = (Button) findViewById(R.id.boton_registrar);
        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrir_registrar();
            }
        });

        botonRegistros = (Button) findViewById(R.id.boton_registros);
        botonRegistros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrir_registros();
            }
        });

        botonMapa = (Button) findViewById(R.id.boton_mapa);
        botonMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrir_mapa();
            }
        });

    }

    public void volver(){
        finish();
    }
    public void abrir_registrar(){
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
    }

    public void abrir_registros() {
        Intent intent = new Intent(this, ListaRegistros.class);
        startActivity(intent);
    }

    public void abrir_mapa() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}

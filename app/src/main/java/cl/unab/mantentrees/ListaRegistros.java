package cl.unab.mantentrees;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ListaRegistros extends AppCompatActivity {
    ListView lvArboles;
    int[] IMAGENES = {R.drawable.araucaria,R.drawable.palma,R.drawable.naranjo,R.drawable.manzano,R.drawable.roble,R.drawable.arrayan,R.drawable.alerce};
    String[] NOMBRES = {"Araucaria","Palma Chilena", "Naranjo", "Manzano", "Roble", "Arrayan", "Alerce"};
    String [] UBICACION = {"Quinta Vergara", "Plaza de Viña", "Anibal Pinto", "Pedro Montt", "Cerro Placeres", "UNAB", "UTFSM"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_registros);

        lvArboles = findViewById(R.id.lv_arboles);

        CustomAdapter customAdapter = new CustomAdapter();
        lvArboles.setAdapter(customAdapter);
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return IMAGENES.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.customlayout,null);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            TextView tv_nombre = (TextView) convertView.findViewById(R.id.textView_nombre);
            TextView tv_altura = (TextView) convertView.findViewById(R.id.textView_altura);
            imageView.setImageResource(IMAGENES[position]);
            tv_nombre.setText(NOMBRES[position]);
            tv_altura.setText(UBICACION[position]);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), DescripcionArbol.class);
                    intent.putExtra("contenido", "Aca aparece la descripcion de "+ NOMBRES[position]);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }
}

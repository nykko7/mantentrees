package cl.unab.mantentrees;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Registro extends AppCompatActivity implements View.OnClickListener {

    private Button botonFoto;
    private Button botonSubir;
    private Button botonLocalizacion;

    private View vista;
    private ProgressBar progressBar;
    private EditText editTextNombre;
    private EditText editTextEspecie;
    private EditText editTextEdad;
    private EditText editTextAltura;
    private EditText editTextComentarios;
    private TextView textViewLatitud;
    private TextView textViewLongitud;
    private double latitudArbol;
    private double longitudArbol;
    NumberFormat formatter;

    GPSTracker gps;
    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    Context mContext;
    private StorageTask mUploadTask;

    public Registro() {
    }


    /*AGREGAR BARRA DE PROGRESO :
    progressBar.setVisibility(View.VISIBLE);*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registro);
        mContext = this;
        formatter = new DecimalFormat("#0.000000");
        botonFoto = (Button) findViewById(R.id.boton_foto);
        botonSubir = (Button) findViewById(R.id.boton_subir);
        botonLocalizacion = (Button) findViewById(R.id.boton_localizacion);
        editTextNombre = (EditText) findViewById(R.id.et_nombre);
        editTextEspecie = (EditText) findViewById(R.id.et_especie);
        editTextEdad = (EditText) findViewById(R.id.et_edad);
        editTextAltura = (EditText) findViewById(R.id.et_altura);
        editTextComentarios = (EditText) findViewById(R.id.et_comentarios);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textViewLatitud = (TextView) findViewById(R.id.tv_latitud);
        textViewLongitud = (TextView) findViewById(R.id.tv_longitud);
        textViewLatitud.setText("Latitud: a la espera.");
        textViewLongitud.setText("Longitud: a la espera.");


        mStorageRef = FirebaseStorage.getInstance().getReference("Arboles");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Arboles");
        vista = findViewById(R.id.vista);

        botonFoto.setOnClickListener(this);
        botonSubir.setOnClickListener(this);
        botonLocalizacion.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        switch (v.getId()){
            case R.id.boton_foto:
                abrirFileChooser();
                break;
            case R.id.boton_subir:
                if(mUploadTask != null && mUploadTask.isInProgress())
                    Toast.makeText(this, "Subida en Progreso...", Toast.LENGTH_SHORT).show();
                else
                    registrarArbol();
                break;
            case R.id.boton_localizacion:
                usarGps();
                break;
        }
    }

    private void usarGps(){
        if (ContextCompat.checkSelfPermission(mContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            Toast.makeText(mContext, "Debes aceptar los permisos de Localización.", Toast.LENGTH_SHORT).show();

        } else {
            gps = new GPSTracker(mContext, this);

            // Check if GPS enabled

            if (gps.canGetLocation()) {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                latitudArbol = latitude;
                longitudArbol = longitude;
                textViewLatitud.setText("Latitud: "+ formatter.format(latitudArbol));
                textViewLongitud.setText("Longitud: "+ formatter.format(longitudArbol));
                // \n is for new line

            }

        }

    }


    private void abrirFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData()!=null){
            mImageUri = data.getData();
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void registrarArbol(){
        if(mImageUri != null){
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 5000);
                            Toast.makeText(Registro.this, "El Árbol se ha subido satisfactoriamente", Toast.LENGTH_LONG).show();
                            String file = fileReference.getDownloadUrl().toString();
                            Upload upload = new Upload(editTextNombre.getText().toString().trim(),
                                    editTextEspecie.getText().toString().trim(),
                                    editTextEdad.getText().toString().trim(),
                                    editTextAltura.getText().toString().trim(),
                                    editTextComentarios.getText().toString().trim(),
                                    file, latitudArbol, longitudArbol
                            );
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Registro.this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() /taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });
        }else{
            Toast.makeText(this, "No se seleccionó una imagen", Toast.LENGTH_SHORT).show();
        }
    }
}

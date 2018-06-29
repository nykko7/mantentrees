package cl.unab.mantentrees;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class ArbolAdapter extends RecyclerView.Adapter <ArbolAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;

    public ArbolAdapter(Context context, List<Upload> uploads){
        mContext = context;
        mUploads = uploads;
    }


    @Override
    public ArbolAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.arbol_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        Log.i("algo", uploadCurrent.getmImageUrl());
        String strlat = String.format("%4.5f", uploadCurrent.getmLatitud());
        String strlong = String.format("%4.5f", uploadCurrent.getmLongitud());
        holder.textViewNombre.setText("Nombre: "+ uploadCurrent.getmNombre() + ".");
        holder.textViewEspecie.setText("Especie: "+uploadCurrent.getmEspecie() + ".");
        holder.textViewEdad.setText("Edad: "+uploadCurrent.getmEdad() +" años.");
        holder.textViewAltura.setText("Altura: "+uploadCurrent.getmAltura() + " metros.");
        holder.textViewLatitud.setText("Latitud: " + strlat);
        holder.textViewLongitud.setText("Longitud: "+ strlong);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .oval(false)
                .build();

        Picasso.get()
                .load(uploadCurrent.getmImageUrl().toString())
                .fit()
                .placeholder(R.drawable.default_image)
                .transform(transformation)
                .centerCrop()
                .into(holder.imageViewArbol);

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewNombre;
        public ImageView imageViewArbol;
        public TextView textViewEspecie;
        public TextView textViewEdad;
        public TextView textViewAltura;
        public TextView textViewLatitud;
        public TextView textViewLongitud;

        public ImageViewHolder(View itemView){
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.text_view_nombre);
            textViewEspecie = itemView.findViewById(R.id.text_view_especie);
            textViewEdad = itemView.findViewById(R.id.text_view_edad);
            textViewAltura = itemView.findViewById(R.id.text_view_altura);
            imageViewArbol = itemView.findViewById(R.id.imageView_arbol);
            textViewLatitud = itemView.findViewById(R.id.text_view_latitud);
            textViewLongitud = itemView.findViewById(R.id.text_view_longitud);
        }

    }
}

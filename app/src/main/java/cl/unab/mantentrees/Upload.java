package cl.unab.mantentrees;

public class Upload {
    private String mNombre;
    private String mEspecie;
    private String mEdad;
    private String mAltura;
    private String mComentarios;
    private String mImageUrl;
    private double mLatitud;
    private double mLongitud;

    public Upload(){
        //EMPTY CONSTRUCTOR SE NECESITA
    }
    public Upload(String Nombre, String Especie, String Edad, String Altura, String Comentarios, String ImageUrl, double Latitud, double Longitud){
        if(Nombre.trim().equals(""))
            Nombre = "Sin Nombre";
        if(Especie.trim().equals(""))
            Especie = "Sin Especie";
        if(Edad.trim().equals(""))
            Edad = "Sin Edad";
        if(Altura.trim().equals(""))
            Altura = "Sin Altura";
        if(Comentarios.trim().equals(""))
            Comentarios = "Sin Comentarios";

        mNombre = Nombre;
        mEspecie = Especie;
        mEdad = Edad;
        mAltura = Altura;
        mComentarios = Comentarios;
        mLatitud = Latitud;
        mLongitud = Longitud;
        mImageUrl = ImageUrl;
    }


    public String getmNombre() {
        return mNombre;
    }

    public void setmNombre(String mNombre) {
        this.mNombre = mNombre;
    }

    public String getmEspecie() {
        return mEspecie;
    }

    public void setmEspecie(String mEspecie) {
        this.mEspecie = mEspecie;
    }

    public String getmEdad() {
        return mEdad;
    }

    public void setmEdad(String mEdad) {
        this.mEdad = mEdad;
    }

    public String getmAltura() {
        return mAltura;
    }

    public void setmAltura(String mAltura) {
        this.mAltura = mAltura;
    }

    public String getmComentarios() {
        return mComentarios;
    }

    public void setmComentarios(String mComentarios) {
        this.mComentarios = mComentarios;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public double getmLatitud() {
        return mLatitud;
    }

    public void setmLatitud(double mLatitud) {
        this.mLatitud = mLatitud;
    }

    public double getmLongitud() {
        return mLongitud;
    }

    public void setmLongitud(double mLongitud) {
        this.mLongitud = mLongitud;
    }
}

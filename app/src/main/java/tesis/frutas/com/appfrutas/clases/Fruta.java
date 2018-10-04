package tesis.frutas.com.appfrutas.clases;

import android.content.Context;

import com.orm.SugarRecord;

import tesis.frutas.com.appfrutas.R;

public class Fruta extends SugarRecord{

    private String nombre;
    private String descripcion;
    private long dateIni;
    private long dateEnd;
    private String kcal;
    private String grasas;
    private String proteinas;
    private String carbohidratos;

    public Fruta() {
    }

    public Fruta(String nombre, String descripcion, long dateIni, long dateEnd, String kcal, String grasas, String proteinas, String carbohidratos) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.dateIni = dateIni;
        this.dateEnd = dateEnd;
        this.kcal = kcal;
        this.grasas = grasas;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getDateIni() {
        return dateIni;
    }

    public void setDateIni(long dateIni) {
        this.dateIni = dateIni;
    }

    public long getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(long dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getKcal() {
        return kcal;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }

    public String getGrasas() {
        return grasas;
    }

    public void setGrasas(String grasas) {
        this.grasas = grasas;
    }

    public String getProteinas() {
        return proteinas;
    }

    public void setProteinas(String proteinas) {
        this.proteinas = proteinas;
    }

    public String getCarbohidratos() {
        return carbohidratos;
    }

    public void setCarbohidratos(String carbohidratos) {
        this.carbohidratos = carbohidratos;
    }

    public String getDateString(Context context) {

        if (getDateIni() == 1 && getDateEnd() == 12) {
            return context.getResources().getString(R.string.todoAnio);
        }
        String ini = monthToString(getDateIni(), context);
        return ini + " - " + monthToString(getDateEnd(), context);
    }

    private String monthToString(long l, Context context) {
        switch ((int) l) {
            case 1:
                return context.getResources().getString(R.string.Enero);
            case 2:
                return context.getResources().getString(R.string.Febrero);
            case 3:
                return context.getResources().getString(R.string.Marzo);
            case 4:
                return context.getResources().getString(R.string.Abril);
            case 5:
                return context.getResources().getString(R.string.Mayo);
            case 6:
                return context.getResources().getString(R.string.Junio);
            case 7:
                return context.getResources().getString(R.string.Julio);
            case 8:
                return context.getResources().getString(R.string.Agosto);
            case 9:
                return context.getResources().getString(R.string.Septiembre);
            case 10:
                return context.getResources().getString(R.string.Octubre);
            case 11:
                return context.getResources().getString(R.string.Noviembre);
            case 12:
                return context.getResources().getString(R.string.Diciembre);
            default:
                return "El mes no es valido";
        }
    }

        public String toString() {
            return "Fruta{nombre='" + this.nombre + '\'' + ", descripcion='" + this.descripcion + '\'' + ", kcal=" + this.kcal + ", grasas=" + this.grasas + ", proteinas=" + this.proteinas + ", carbohidratos=" + this.carbohidratos + ", dateIni=" + this.dateIni + ", dateEnd=" + this.dateEnd + '}';
        }

    }

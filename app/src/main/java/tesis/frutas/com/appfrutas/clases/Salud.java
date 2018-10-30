package tesis.frutas.com.appfrutas.clases;

import com.orm.SugarRecord;

public class Salud extends SugarRecord {

    private Long idfruta;
    private String nombre;
    private String descripcion;

    public Salud() {
    }

    public Salud(Long idfruta, String nombre, String descripcion) {
        this.idfruta = idfruta;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Long getIdfruta() {
        return idfruta;
    }

    public void setIdfruta(Long idfruta) {
        this.idfruta = idfruta;
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
}

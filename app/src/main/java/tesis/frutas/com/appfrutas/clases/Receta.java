package tesis.frutas.com.appfrutas.clases;

import com.orm.SugarRecord;

public class Receta extends SugarRecord {

    private Long idfruta;
    private String nombre;
    private String ingredientes;
    private String procedimiento;

    public Receta() {
    }

    public Receta(Long idfruta, String nombre, String ingredientes, String procedimiento) {
        this.idfruta = idfruta;
        this.nombre = nombre;
        this.ingredientes = ingredientes;
        this.procedimiento = procedimiento;
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

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }
}


package entidades;

import java.io.Serializable;
import java.util.Date;

public class Peticion implements Serializable {
    private int id;
    private TipoPeticion tipoPeticion;
    private Date fecha;
    private Prioridad prioridad;
    private Object[] cuerpo;

    public Peticion(int id, TipoPeticion tipoPeticion, Date fecha, Prioridad prioridad, Object []cuerpo) {
        this.id = id;
        this.tipoPeticion = tipoPeticion;
        this.fecha = fecha;
        this.prioridad = prioridad;
        this.cuerpo = cuerpo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoPeticion getTipoPeticion() {
        return tipoPeticion;
    }

    public void setTipoPeticion(TipoPeticion tipoPeticion) {
        this.tipoPeticion = tipoPeticion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public Object[] getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(Object[] cuerpo) {
        this.cuerpo = cuerpo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Peticion other = (Peticion) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "Peticion{" + "id=" + id + ", tipoPeticion=" + tipoPeticion + ", fecha=" + fecha + ", prioridad=" + prioridad + ", cuerpo=" + cuerpo + '}';
    }

}

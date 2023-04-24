package datosInterfaces;

import entidades.Producto;
import java.util.List;

public interface IProductosDAO {

    public boolean agregar(Producto producto);

    public boolean actualizar(Producto producto);

    public boolean eliminar(int id);

    public Producto consultar(int id);

    public List<Producto> consultarEspecial(String[] consultas);

    public List<Producto> consultarTodos();

}

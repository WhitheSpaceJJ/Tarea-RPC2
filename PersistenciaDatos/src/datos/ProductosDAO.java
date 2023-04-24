package datos;

import datosInterfaces.IConexionBD;
import datosInterfaces.IProductosDAO;
import entidades.Producto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class ProductosDAO implements IProductosDAO {

    private final IConexionBD conexion;

    public ProductosDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public boolean agregar(Producto producto) {
        try {
            EntityManager em = this.conexion.crearConexion();
            em.getTransaction().begin();
            em.persist(producto);
            em.getTransaction().commit();
            return true;
        } catch (IllegalStateException ise) {
            System.err.println("No fue posible agregar el producto");
            return false;
        }
    }

    @Override
    public boolean actualizar(Producto producto) {
        try {
            EntityManager em = this.conexion.crearConexion();
            em.getTransaction().begin();
            em.merge(producto);
            em.getTransaction().commit();
            return true;
        } catch (IllegalStateException ise) {
            System.err.println("No fue posible actualizar el producto");
            return false;
        }

    }

    @Override
    public boolean eliminar(int id) {
        try {
            EntityManager em = this.conexion.crearConexion();
            Producto productoBD = em.find(Producto.class, id);
            em.getTransaction().begin();
            em.remove(productoBD);
            em.getTransaction().commit();
            return true;
        } catch (IllegalStateException ise) {
            System.err.println("No fue posible eliminar el producto");
            return false;
        }
    }

    @Override
    public Producto consultar(int id) {

        try {
            EntityManager em = this.conexion.crearConexion();
            em.getTransaction().begin();
            Producto productoBD = em.find(Producto.class, id);
            em.getTransaction().commit();
            return productoBD;
        } catch (IllegalStateException ise) {
            System.err.println("No se pudo consultar el producto");
            return null;
        }
    }

    @Override
    public List<Producto> consultarTodos() {
        try {
            EntityManager em = this.conexion.crearConexion();
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Producto> criteria = builder.createQuery(Producto.class);
            TypedQuery<Producto> query = em.createQuery(criteria);
            return query.getResultList();
        } catch (IllegalStateException ise) {
            System.err.println("No se pudierón consultar los Productos");
            return null;
        }
    }

    @Override
    public List<Producto> consultarEspecial(String[] consultas) {
        try {
            EntityManager em = this.conexion.crearConexion();

            CriteriaBuilder builder = em.getCriteriaBuilder();

            CriteriaQuery<Producto> criteria = builder.createQuery(Producto.class);

            Root<Producto> root = criteria.from(Producto.class);

            if (consultas[0] != null) {
                criteria = criteria.select(root).where(builder.equal(root.get("id"), Integer.valueOf(consultas[0])));
                TypedQuery<Producto> query = em.createQuery(criteria);

                return query.getResultList();
            }
            if (consultas[1] != null) {
                criteria = criteria.select(root).where(builder.like(root.get("nombre"), "%" + consultas[1] + "%"));
                TypedQuery<Producto> query = em.createQuery(criteria);

                return query.getResultList();
            }
            if (consultas[2] != null) {
                criteria = criteria.select(root).where(builder.like(root.get("descripcion"), "%" + consultas[2] + "%"));
                TypedQuery<Producto> query = em.createQuery(criteria);

                return query.getResultList();
            }
            if (consultas[3] != null) {
                criteria = criteria.select(root).where(builder.like(root.get("marca"), "%" + consultas[3] + "%"));
                TypedQuery<Producto> query = em.createQuery(criteria);

                return query.getResultList();
            }
            return new ArrayList<>();
        } catch (IllegalStateException ise) {
            System.err.println("No se pudo consultar los productos por nombre");
            return null;
        }
    }
}

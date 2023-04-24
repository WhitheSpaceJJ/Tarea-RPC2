package ws;

import conexion.ConexionBD;
import datos.ProductosDAO;
import datosInterfaces.IProductosDAO;
import entidades.Peticion;
import entidades.Prioridad;
import entidades.Producto;
import entidades.TipoPeticion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("producto")
public class ProductoResource {

    @Context
    private UriInfo context;

    public ProductoResource() {
    }

    @GET
    @Path("/query")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerProductosEspecial(@QueryParam("id") String id, @QueryParam("nombre") String nombre,
            @QueryParam("descripcion") String descripcion, @QueryParam("marca") String marca) {

        Object[] objetos2= new Object[4];
        objetos2[0] = id != null ? id.replace("\\n", "") : null;
        objetos2[1] = nombre != null ? nombre.replace("\\n", "") : null;
        objetos2[2] = descripcion != null ? descripcion.replace("\\n", "") : null;
        objetos2[3] = marca != null ? marca.replace("\\n", "") : null;
          try {
            Consumidor consumidor = new Consumidor();
            Peticion peticion
                    = consumidor.call(new Peticion(new Random().nextInt(1000),
                            TipoPeticion.CONSULTA_ESPECIAL, Calendar.getInstance().getTime(),
                            Prioridad.ALTA, objetos2));
            if (peticion == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else if (peticion != null && peticion.getCuerpo() != null) {
                Object[] objetos = peticion.getCuerpo();
                JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
                for (Object producto : objetos) {
                    JsonObjectBuilder productoJsonBuilder = Json.createObjectBuilder()
                            .add("id", ((Producto) producto).getId())
                            .add("nombre", ((Producto) producto).getNombre())
                            .add("descripcion", ((Producto) producto).getDescripcion())
                            .add("marca", ((Producto) producto).getMarca());
                    jsonArrayBuilder.add(productoJsonBuilder.build());
                }
                JsonArray jsonArray = jsonArrayBuilder.build();
                return Response.ok().entity(jsonArray.toString()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            System.out.println("Error; " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerProductoPorID(@PathParam("id") String id) {
        try {
            Consumidor consumidor = new Consumidor();
            Object[] objetos = new Object[1];
            objetos[0] = Integer.valueOf(id);
            Peticion peticion
                    = consumidor.call(new Peticion(new Random().nextInt(1000),
                            TipoPeticion.CONSULTA_ID, Calendar.getInstance().getTime(), Prioridad.ALTA, objetos));
            if (peticion == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else if (peticion.getCuerpo() != null && peticion != null) {
                Producto productoABuscar = (Producto) peticion.getCuerpo()[0];
                return Response.ok().entity(productoABuscar).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            System.out.println("Error; " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerProductos() {
        try {
            Consumidor consumidor = new Consumidor();
            Object[] objetos2 = new Object[1];
            Peticion peticion
                    = consumidor.call(new Peticion(new Random().nextInt(1000),
                            TipoPeticion.CONSULTA, Calendar.getInstance().getTime(),
                            Prioridad.ALTA, objetos2));
            if (peticion == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else if (peticion != null && peticion.getCuerpo() != null) {
                Object[] objetos = peticion.getCuerpo();
                JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
                for (Object producto : objetos) {
                    JsonObjectBuilder productoJsonBuilder = Json.createObjectBuilder()
                            .add("id", ((Producto) producto).getId())
                            .add("nombre", ((Producto) producto).getNombre())
                            .add("descripcion", ((Producto) producto).getDescripcion())
                            .add("marca", ((Producto) producto).getMarca());
                    jsonArrayBuilder.add(productoJsonBuilder.build());
                }
                JsonArray jsonArray = jsonArrayBuilder.build();
                return Response.ok().entity(jsonArray.toString()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            System.out.println("Error; " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarProducto(@PathParam("id") String id, Producto productoActualizado) {
         try {
            Consumidor consumidor = new Consumidor();
            Object[] objetos = new Object[1];
            objetos[0] = productoActualizado;
            Peticion peticion
                    = consumidor.call(new Peticion(new Random().nextInt(1000),
                            TipoPeticion.ACTUALIZAR, Calendar.getInstance().getTime(),
                            Prioridad.ALTA, objetos));
            if (peticion == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else if (peticion.getCuerpo() != null && peticion.getCuerpo().length > 0 && peticion.getCuerpo()[0] instanceof Boolean && (boolean) peticion.getCuerpo()[0] == true && peticion != null) {
                Producto productoABuscar = (Producto) peticion.getCuerpo()[0];
                return Response.ok().entity(productoABuscar).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            System.out.println("Error; " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarProducto(@PathParam("id") String id) {
        try {
            Consumidor consumidor = new Consumidor();
            Object[] objetos = new Object[1];
            objetos[0] =Integer.valueOf(id);
            Peticion peticion
                    = consumidor.call(new Peticion(new Random().nextInt(1000),
                            TipoPeticion.ELIMINAR, Calendar.getInstance().getTime(),
                            Prioridad.ALTA, objetos));
            if (peticion == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else if (peticion.getCuerpo() != null && peticion.getCuerpo().length > 0 && peticion.getCuerpo()[0] instanceof Boolean && (boolean) peticion.getCuerpo()[0] == true && peticion != null) {
                Producto productoABuscar = (Producto) peticion.getCuerpo()[0];
                return Response.ok().entity(productoABuscar).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            System.out.println("Error; " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response agregarProducto(Producto producto) {
        try {
            Consumidor consumidor = new Consumidor();
            Object[] objetos = new Object[1];
            objetos[0] = producto;
            Peticion peticion
                    = consumidor.call(new Peticion(new Random().nextInt(1000),
                            TipoPeticion.AGREGAR, Calendar.getInstance().getTime(),
                            Prioridad.ALTA, objetos));
            if (peticion == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else if (peticion.getCuerpo() != null && peticion.getCuerpo().length > 0 && peticion.getCuerpo()[0] instanceof Boolean && (boolean) peticion.getCuerpo()[0] == true && peticion != null) {
                Producto productoABuscar = (Producto) peticion.getCuerpo()[0];
                return Response.ok().entity(productoABuscar).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            System.out.println("Error; " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}

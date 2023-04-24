/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package productor;

import com.rabbitmq.client.*;
import conexion.ConexionBD;
import datos.ProductosDAO;
import datosInterfaces.IConexionBD;
import datosInterfaces.IProductosDAO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import entidades.Peticion;
import entidades.Prioridad;
import entidades.Producto;
import entidades.TipoPeticion;
import java.util.List;
import org.apache.commons.lang3.SerializationUtils;

public class Productor {

    private static final String RPC_QUEUE_NAME = "rpc_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
        channel.queuePurge(RPC_QUEUE_NAME);

        channel.basicQos(1);

        System.out.println(" [x] Awaiting RPC requests");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                    .correlationId(delivery.getProperties().getCorrelationId())
                    .build();
            Peticion peticion = null;
            try {
                peticion = (Peticion) SerializationUtils.deserialize(delivery.getBody());
                System.out.println("Tipo peticion; " + peticion.getTipoPeticion() + ", id; " + peticion.getId());
            } catch (RuntimeException e) {
                System.out.println(" [.] " + e);
            } finally {
                if (peticion.getTipoPeticion() == TipoPeticion.CONSULTA) {
                    IConexionBD conexion = new ConexionBD();
                    IProductosDAO productosDAO = new ProductosDAO(conexion);

                    List<Producto> productos = productosDAO.consultarTodos();
                    if (!productos.isEmpty()) {
                        Object[] objetos = new Object[productos.size()];
                        for (int i = 0; i < productos.size(); i++) {
                            Producto get = productos.get(i);
                            objetos[i] = get;
                        }
                        peticion.setCuerpo(objetos);
                        peticion.setPrioridad(Prioridad.BAJA);
                    } else {
                        peticion.setCuerpo(null);
                        peticion.setPrioridad(Prioridad.ALTA);
                    }
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(peticion);
                    byte[] bytes = bos.toByteArray();
                    channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, bytes);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } else if (peticion.getTipoPeticion() == TipoPeticion.CONSULTA_ID) {
                    IConexionBD conexion = new ConexionBD();
                    IProductosDAO productosDAO = new ProductosDAO(conexion);

                    Producto productoObtener = productosDAO.consultar(((int) peticion.getCuerpo()[0]));
                    if (productoObtener != null) {
                        Object[] objetos = new Object[1];
                        objetos[0] = productoObtener;
                        peticion.setCuerpo(objetos);
                        peticion.setPrioridad(Prioridad.ALTA);
                    } else {
                        peticion.setCuerpo(null);
                        peticion.setPrioridad(Prioridad.ALTA);
                    }
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(peticion);
                    byte[] bytes = bos.toByteArray();
                    channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, bytes);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } //Terminar a partir de aqui
                else if (peticion.getTipoPeticion() == TipoPeticion.ACTUALIZAR) {
                        IConexionBD conexion = new ConexionBD();
                    IProductosDAO productosDAO = new ProductosDAO(conexion);
                    boolean actualizado = productosDAO.actualizar(((Producto) peticion.getCuerpo()[0]));
                    if (actualizado) {
                        Object[] objetos = new Object[1];
                        objetos[0] = true;
                        peticion.setCuerpo(objetos);
                        peticion.setPrioridad(Prioridad.BAJA);
                    } else {
                        Object[] objetos = new Object[1];
                        objetos[0] = false;
                        peticion.setCuerpo(objetos);
                        peticion.setPrioridad(Prioridad.ALTA);
                    }
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(peticion);
                    byte[] bytes = bos.toByteArray();
                    channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, bytes);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
          
                } else if (peticion.getTipoPeticion() == TipoPeticion.AGREGAR) {
                    IConexionBD conexion = new ConexionBD();
                    IProductosDAO productosDAO = new ProductosDAO(conexion);
                    boolean agregado = productosDAO.agregar(((Producto) peticion.getCuerpo()[0]));
                    if (agregado) {
                        Object[] objetos = new Object[1];
                        objetos[0] = true;
                        peticion.setCuerpo(objetos);
                        peticion.setPrioridad(Prioridad.BAJA);
                    } else {
                        Object[] objetos = new Object[1];
                        objetos[0] = false;
                        peticion.setCuerpo(objetos);
                        peticion.setPrioridad(Prioridad.ALTA);
                    }
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(peticion);
                    byte[] bytes = bos.toByteArray();
                    channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, bytes);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } else if (peticion.getTipoPeticion() == TipoPeticion.ELIMINAR) {
                    IConexionBD conexion = new ConexionBD();
                    IProductosDAO productosDAO = new ProductosDAO(conexion);
                    boolean eliminado = productosDAO.eliminar(((int) peticion.getCuerpo()[0]));
                    if (eliminado) {
                        Object[] objetos = new Object[1];
                        objetos[0] = true;
                        peticion.setCuerpo(objetos);
                        peticion.setPrioridad(Prioridad.BAJA);
                    } else {
                        Object[] objetos = new Object[1];
                        objetos[0] = false;
                        peticion.setCuerpo(objetos);
                        peticion.setPrioridad(Prioridad.BAJA);
                    }
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(peticion);
                    byte[] bytes = bos.toByteArray();
                    channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, bytes);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } else {
                    IConexionBD conexion = new ConexionBD();
                    IProductosDAO productosDAO = new ProductosDAO(conexion);
                    String[] objetos = new String[peticion.getCuerpo().length];
                    for (int i = 0; i < objetos.length; i++) {
                        objetos[i] = (String) peticion.getCuerpo()[i];
                    }
                    List<Producto> productos = productosDAO.consultarEspecial(objetos);
                    if (!productos.isEmpty()) {
                        Object[] objetos3 = new Object[productos.size()];
                        for (int i = 0; i < productos.size(); i++) {
                            Producto get = productos.get(i);
                            objetos3[i] = get;
                        }
                        peticion.setCuerpo(objetos3);
                        peticion.setPrioridad(Prioridad.BAJA);
                    } else {
                        peticion.setCuerpo(null);
                        peticion.setPrioridad(Prioridad.ALTA);
                    }
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(peticion);
                    byte[] bytes = bos.toByteArray();
                    channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, bytes);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }

            }
        };

        channel.basicConsume(RPC_QUEUE_NAME, false, deliverCallback, (consumerTag -> {
        }));
    }
}

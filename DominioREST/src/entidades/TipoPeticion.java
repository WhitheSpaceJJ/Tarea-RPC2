package entidades;

import java.io.Serializable;

public enum TipoPeticion implements Serializable{
    //Todos Productos
    CONSULTA,
    //Consulta ID
    CONSULTA_ID,
    //Actualizar
    ACTUALIZAR,
    //EDITAR
    EDITAR,
    //ELIMINAR
    ELIMINAR,
    //AGREGAr
    AGREGAR,
    //ConsultaESPECIAL
    CONSULTA_ESPECIAL
   
}

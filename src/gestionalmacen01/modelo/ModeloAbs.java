package gestionalmacen01.modelo;
import java.util.List;

/**
 * Abstract class ModeloAbs - Clase de acceso a Modelo de DATOS
 * 
 * @author: Alberto Lopez
 * Date: 24/03/2026
 */
public interface  ModeloAbs
{
    
    public boolean insertarProducto ( Producto p);
    
    boolean cargarProductos();
	
    boolean salvarProductos();
    
    boolean borrarProducto ( int codigo );
    
    int borrarProductoStockCero();
    
    public Producto buscarProducto ( int codigo);
    
    void imprimirProductosTodos ();
    
    List <Producto> obtenerProductos(); 
    
    List <Producto> obtenerProductosStockMin();
    
    boolean modificarProducto (Producto nuevo);
    
    
}

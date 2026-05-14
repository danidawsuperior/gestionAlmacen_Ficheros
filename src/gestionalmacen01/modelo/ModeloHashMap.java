
/**
 * Write a description of class ModeloHaspMap here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
package gestionalmacen01.modelo;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ModeloHashMap implements ModeloAbs
{
    private HashMap <Integer,Producto> mapa;
    
    public ModeloHashMap()
    {
       mapa=new HashMap  <Integer,Producto>();
       cargarProductos();
    }

    public boolean insertarProducto ( Producto p){
      assert ( p != null ); // No permito productos nulos  
      mapa.put(p.getCodigo(),p);
      return salvarProductos();
    }
 
    public boolean borrarProducto ( int codigo ){
      // Si es null es que no estaba
      return ( mapa.remove(codigo) != null && salvarProductos());
    }
    
    public Producto buscarProducto ( int codigo) {
        return mapa.get(codigo);
    }
    // Funciona pero no es una solución independiente del la mécanismo de salida.
    // El acceso a datos debe ser independiente de la visualización de los mismos.

    public void imprimirProductosTodos (){
        int i = 1;
        for (Producto p: mapa.values()) {
            System.out.println(" Nº "+i+" "+p);
            i++;
        }
        
        //mapa.values().forEach(p-> System.out.println(p));
    }

    // Devuelvo una lista con los productos con stock mínimo
    // Será el programa principal quien se encargue de mostrarlos
	public List<Producto> obtenerProductosStockMin() {
	    List <Producto> resu1 = new ArrayList<Producto>();
	    for (Producto p: mapa.values()) {
	    	if ( p.getStock() <= p.getStock_min()) {
				resu1.add(p);
			}
        }
	 // Otra forma: Crea una nueva lista a partir de los valores y borro los que supera el stock mínimo
	 List <Producto> resu2 = new ArrayList<Producto>(mapa.values());
	 // Elimino los que superan el mínimo
	 resu2.removeIf(p -> (p.getStock() > p.getStock_min()));
	 
	 return resu1;
	}

	// Solo chequea si el producto ya existia en el almacen.
    // No tiene que hacer nada pues se ha cambiado la referencia
	public boolean modificarProducto(Producto pro) {
		return mapa.containsValue(pro) && salvarProductos();
	}

	// Devuelve la lista con todos los productos
	public List<Producto> obtenerProductos() {
	
		return new ArrayList<Producto>(mapa.values());
	}

	@Override
	public boolean cargarProductos() {
		boolean resu = true;
		try (
			// Creo Flujo de tipo fichero de byte
			FileInputStream fin = new FileInputStream("productos.objetos");
			// Creo un Flujo de objetos sobre el fichero
			ObjectInputStream foin = new ObjectInputStream(fin);) {
			// Leo objetos hasta llegar a final de fichero
			Producto pro = (Producto) foin.readObject();
			mapa.put(pro.getCodigo(), pro);
			// Sale cuando se llege al final de fichero End of File EOF
			while (true) {
				pro = (Producto) foin.readObject();
				mapa.put(pro.getCodigo(), pro);
			}
		} catch (EOFException ex) {
			// NO hago nada, simplemente se ha detectado que no hay mas datos
		} catch (Exception ex) {
			System.err.println("Error al procesar el fichero de productos ");
			resu = false;
		}
		return resu;
	}

	@Override
	public boolean salvarProductos() {
		boolean resu = true;
		try (
			// Creo Flujo de tipo fichero de byte
			var fout = new FileOutputStream("productos.objetos");
			// Creo un Flujo de objetos sobre el fichero
			var foout = new ObjectOutputStream(fout);) {
			// Leo objetos hasta llegar a final de fichero
			for (Producto pro : mapa.values()) {
				foout.writeObject(pro);
			}
			
		} catch (Exception ex) {
			System.err.println("Error al procesar el fichero de productos ");
			resu = false;
		}
		return resu;
	}
    
	@Override
	public int borrarProductoStockCero() {
		int tamañoOld = mapa.size();
		mapa.values().removeIf( p -> p.stock == 0);
		return tamañoOld - mapa.size();
	}
    
}

/** Implementa la parte de Modelo de Datos
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
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ModeloArrayList implements ModeloAbs
{
    protected ArrayList <Producto> lista;
    
    public ModeloArrayList()
    {
       lista=new ArrayList <Producto>();
       cargarProductos();
    }

    public boolean insertarProducto ( Producto p){
      assert ( p != null ); // No permito productos nulos 
      if ( lista.add(p) && salvarProductos()){
    	  return true;
      } else {
    	  return false;
      }
    }
 
    public boolean borrarProducto ( int codigo ){
      Producto p = buscarProducto (codigo);
      if ( p != null ){
          // Remove null 
          return (lista.remove(p) && salvarProductos());
        }
      return false;
    }
    
    public Producto buscarProducto ( int codigo) {
       for ( Producto p: lista ){
           if ( p.getCodigo() == codigo ){
               return p;
            }
        }
        return null;
    }
    // Funciona pero no es una solución independiente del la mécanismo de salida.
    // El acceso a datos debe ser independiente de la visualización de los mismos.
    public void imprimirProductosTodos (){
        int i = 1;
        for ( Producto p: lista){
            System.out.println(" Nº "+i+" "+p);
            i++;
        }
    }
    // Solo chequea si el producto ya existia en el almacen.
    // No tiene que hacer nada pues se ha cambiado la referencia
    public boolean modificarProducto (Producto nuevo){
       Producto p;
       if (lista.contains(nuevo) ) {
    	   salvarProductos();
    	   return true;
       } else {
    	   return false;
       }
    }

    // Devuelvo una lista con los productos con stock mínimo
    // Será el programa principal quien se encargue de mostrarlos
	public List<Producto> obtenerProductosStockMin() {
		
		List <Producto> resu1 = new ArrayList<Producto>();
		
		for ( Producto p:lista) {
			if ( p.getStock() <= p.getStock_min()) {
				resu1.add(p);
			}
	     }
		// Otra forma: Crea una nueva lista a partir de la original y borro los que supera el stock mínimo
		List <Producto> resu2 = new ArrayList<Producto>(lista);
		// Elimino los que superan el mínimo
		resu2.removeIf(p -> (p.getStock() > p.getStock_min()));
		
		// Otra forma: Expresiones lambda sobre stream
		List <Producto> resu3 = lista.stream().filter(p -> (p.getStock() <= p.getStock_min())).collect(Collectors.toList());
		
		return resu1;
	}

	

	@Override
	public List<Producto> obtenerProductos() {
		return lista;
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
				lista.add(pro);
				// Sale cuando se llege al final de fichero End of File EOF
				while (true) {
					pro = (Producto) foin.readObject();
					lista.add(pro);
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
			for (Producto pro : lista) {
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
		int tamañoAntes = lista.size();
		lista.removeIf( p -> p.stock == 0);
		return tamañoAntes - lista.size();
	}
    
	public int borrarProductoStockCero2() {
		int cont = 0;
		Iterator<Producto> it = lista.iterator();
		while (it.hasNext()) {
			if (it.next().stock == 0) {
				it.remove();
				cont ++;
			}
		}
		return cont;
	}
     
}    

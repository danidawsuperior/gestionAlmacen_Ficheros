package gestionalmacen01.modelo;
/**
 * 
 *   Escribe datos de prueba en el fichero de productos.
 * @author (your name) 
 * @version (a version number or a date)
 */
 
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
public class EscribirProductos
{
   
    
    public static void main(String[] args) {
      
    {
    try{
         // Creo Flujo de tipo fichero de byte 
         FileOutputStream fos= new FileOutputStream("productos.objetos");
         // Creo un Flujo de objetos sobre el fichero
         ObjectOutputStream oos= new ObjectOutputStream(fos);
         
         
          oos.writeObject(new Producto (1001,  "Morcillas ",       10,     5,     10.5f));
          oos.writeObject(new Producto (1002,  "Chorizo"   ,       30,     5,     12.5f));
          oos.writeObject(new Producto (1003,  "Chope"   ,         70,     15,    4.5f));
          oos.writeObject(new Producto (1004,  "Fuet"     ,         5,     25,    2.5f));
          oos.writeObject(new Producto (2001,  "Tocino"   ,        10,     5,     1.5f));
          oos.writeObject(new Producto (3021,  "Jamón York",       60,     50,    10.0f));
          oos.writeObject(new Producto (1001,  "Cecina",           10,     15,    15.0f));
          oos.writeObject(new Producto (6001,  "Mortadela",        20,     5,     20.5f));
          oos.writeObject(new Producto (4201,  "Torreznos",      2340,     5,     13.7f));
          oos.writeObject(new Producto (3011,  "Jamón Serrano",    10,     3,     12.5f));
          oos.writeObject(new Producto (1501,  "Lacón ",           10,     15,    20.5f));
     
         
         oos.close(); // Cierro el fujo de objectos
         fos.close(); // Cierro el flujo de 
       }catch(IOException ioe){
            ioe.printStackTrace();
        }    
       System.out.println("Fichero generado."); 
    }
        
    }    
}

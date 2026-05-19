package gestionalmacen01.modelo;

/**
 * Clase basica que gestiona una conexion JDBC a MySQL ModeloDB here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ModeloDB implements ModeloAbs

{
	
	private Connection conexion = null;
	private Statement stmt = null;
	private static String servidor = "jdbc:mysql://10.10.0.200/ProductosDB";
	private static String usuario = "root";
	private static String contraseña = "root";

	/**
	 * Constructor for objects of class ModeloDB Establece la conexion a la base de
	 * datos
	 */
	public ModeloDB() {
		
		// Establecer la conexión con el servidor
		try {
			conexion = DriverManager.getConnection(servidor, usuario, contraseña);
			stmt = conexion.createStatement();
		} catch (SQLException e) {
			System.err.println("ERROR AL CONECTAR CON EL SERVIDOR");
			e.printStackTrace();
			System.exit(0); // PARA LA EJECUCIÓN DEL PROGRAMA
		}

	}

	// INSERT
	public boolean insertarProducto(Producto p) {
		String sqlStr = "INSERT INTO `Productos` (`CODIGO`, `NOMBRE`, `STOCK`, `STOCK_MIN`, `PRECIO`) VALUES (?,?,?,?,?);";
		PreparedStatement sentenciapreparada = null;
		int nfilas = 0;

		// Creo una sentencia preparada
		try {
			sentenciapreparada = conexion.prepareStatement(sqlStr);
		} catch (SQLException e) {
			System.err.println("ERROR en la instrucción de SQL");
			e.printStackTrace();
			return false;
		}

		try {
			sentenciapreparada.setInt   (1, p.getCodigo());
			sentenciapreparada.setString(2, p.getNombre());
			sentenciapreparada.setInt   (3, p.getStock());
			sentenciapreparada.setInt   (4, p.getStock_min());
			sentenciapreparada.setFloat (5, p.getPrecio());
			nfilas = sentenciapreparada.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return (nfilas == 1); // true si se ha producido la insercion
	}

	// DELETE
	public boolean borrarProducto(int codigo) {
		int nfilas = 0;
		String sqlStr = "DELETE FROM Productos where CODIGO = " + codigo;
		try {
			nfilas = stmt.executeUpdate(sqlStr);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (nfilas == 1);
	}

	// SELECT
	public Producto buscarProducto(int codigo) {
		String sqlStr = "select * from Productos WHERE CODIGO = " + codigo;
		Producto resu = null;
		try {
			ResultSet rset = stmt.executeQuery(sqlStr);
			// Solo de debe existir uno;
			if (rset.next()) {
				// Creo el objeto y le asigno los valores de la tabla
				resu = new Producto();
				resu.setCodigo(rset.getInt("CODIGO"));
				resu.setNombre(rset.getString("NOMBRE"));
				resu.setStock(rset.getInt("STOCK"));
				resu.setStock_min(rset.getInt("STOCK_MIN"));
				resu.setPrecio(rset.getFloat("PRECIO"));

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resu;
	}

	// UPDATE
	public boolean modificarProducto(Producto nuevo) {
		String sqlStr = "UPDATE `Productos` SET CODIGO = ?, NOMBRE = ?, STOCK = ?, STOCK_MIN = ?, PRECIO = ? "
				+ " WHERE CODIGO = ?";
		PreparedStatement sentenciapreparada = null;
		int nfilas = 0;

       // Creo una sentencia preparada
		try {
			sentenciapreparada = conexion.prepareStatement(sqlStr);
		} catch (SQLException e) {
			System.err.println("ERROR en la instrucción de SQL");
			e.printStackTrace();
			return false;
		}
        // Ejecuto la sentencia
		try {
			sentenciapreparada.setInt(1, nuevo.getCodigo());
			sentenciapreparada.setString(2, nuevo.getNombre());
			sentenciapreparada.setInt(3, nuevo.getStock());
			sentenciapreparada.setInt(4, nuevo.getStock_min());
			sentenciapreparada.setFloat(5, nuevo.getPrecio());
			sentenciapreparada.setInt(6, nuevo.getCodigo()); // Where
			nfilas = sentenciapreparada.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return (nfilas == 1); // true si se ha producido la modificacion
	}

	/*  ERROR:::
	 *  Este método es correcto aunque no permite un arquitectura independiente 
	 *  del interfaz de usuario (vista) al generar directamente una salida por c
	 *  consola. Lo que impide utilizar esta clase en una aplicación móvil o web 
	 */
	public void imprimirProductosTodos() {

		String sqlStr = "select * from Productos ";
		try {
			ResultSet rset = stmt.executeQuery(sqlStr);
			// Solo de debe existir uno;
			while (rset.next()) {
				Producto p = new Producto();
				p.setCodigo(rset.getInt("CODIGO"));
				p.setNombre(rset.getString("NOMBRE"));
				p.setStock(rset.getInt("STOCK"));
				p.setStock_min(rset.getInt("STOCK_MIN"));
				p.setPrecio(rset.getFloat("PRECIO"));
				System.out.println(p);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 *  Devuelvo una lista con todos los productos .
	 */

	@Override
	public List<Producto> obtenerProductos() {
		var lista = new  ArrayList <Producto>();
		String sqlStr = "select * from Productos ";
		try {
			ResultSet rset = stmt.executeQuery(sqlStr);
			// Solo de debe existir uno;
			while (rset.next()) {
				Producto p = new Producto();
				p.setCodigo(rset.getInt("CODIGO"));
				p.setNombre(rset.getString("NOMBRE"));
				p.setStock(rset.getInt("STOCK"));
				p.setStock_min(rset.getInt("STOCK_MIN"));
				p.setPrecio(rset.getFloat("PRECIO"));
				lista.add(p);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lista;
	}
	
	
	/**
	 *  Devuelvo una lista con los producto con stock igual o inferior al mínimo.
	 */
	public List<Producto> listarProductosStockMin() {
		ArrayList<Producto> lista = new ArrayList<Producto>();
		// Relleno el array list con los resultados de al consulta
		String sqlStr = "select * from Productos where STOCK <= STOCK_MIN";
		try {
			ResultSet rset = stmt.executeQuery(sqlStr);
			// Solo de debe existir uno;
			while (rset.next()) {
				Producto p = new Producto();
				p.setCodigo(rset.getInt("CODIGO"));
				p.setNombre(rset.getString("NOMBRE"));
				p.setStock(rset.getInt("STOCK"));
				p.setStock_min(rset.getInt("STOCK_MIN"));
				p.setPrecio(rset.getFloat("PRECIO"));
				lista.add(p); // Añado el objeto a la coleccion
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lista;
	}

	@Override
	public boolean cargarProductos() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean salvarProducto() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean salvarProductos() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int borrarProductoStockCero() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Producto> obtenerProductosStockMin() {
		// TODO Auto-generated method stub
		return null;
	}



	

}
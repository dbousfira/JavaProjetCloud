package projetcloud.database;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import projetcloud.model.Approval;

public class DataAccessLayer {
	
	private final String DB_NAME = "approvals";
	private final String NAME = "name_approval";
	private final String IS_APPROVED = "is_approved";
	
	/**
	 * Get instance of {@link DataAccessLayer}
	 * @return unique layer instance
	 * @throws AccessLayerException if an error occured while initializing object
	 */
	public static DataAccessLayer get() throws AccessLayerException {
		if (instance == null) {
			instance = new DataAccessLayer();
		}
		return instance;
	}
	
	/**
	 * Get all approvals
	 * @return list of approvals in database
	 * @throws AccessLayerException if an error occurred while loading objects
	 */
	public List<Approval> list() throws AccessLayerException {
		List<Approval> loaded = new ArrayList<>();
		String query = String.format("SELECT %s, %s FROM %s;", NAME, IS_APPROVED, DB_NAME);
		try (Connection connection = source()) {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet results = ps.executeQuery();
			while (results.next()) {
				Approval object = new Approval(results.getString(NAME), results.getBoolean(IS_APPROVED));
				loaded.add(object);
			}
		} catch (Exception e) {
			throw new AccessLayerException(e);
		}
		return loaded;
	}
	
	/**
	 * Insert a new approval into database
	 * @param name approval name
	 * @param approved is it approved
	 * @return created {@link Approval}
	 * @throws AccessLayerException if an error occurred while inserting
	 */
	public Approval insert(String name, boolean approved) throws AccessLayerException  {
		String error = "An error occurred while inserting into database";
		String query = String.format("INSERT INTO %s VALUES (?, ?);", DB_NAME);
		try (Connection connection = source()) {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, name);
			ps.setBoolean(2, approved);
			if (ps.executeUpdate() == 0) {
				throw new AccessLayerException(error);
			}
			return new Approval(name, approved);
		} catch (Exception e) {
			throw new AccessLayerException(error, e);
		}
	}
	
	/**
	 * Delete existing approval from database
	 * @param name approval name to delete
	 * @return boolean state of deletion
	 * @throws AccessLayerException if an error occured while deleting
	 */
	public boolean delete(String name) throws AccessLayerException {
		String query = String.format("DELETE FROM %s WHERE %s = ?;", DB_NAME, NAME);
		try (Connection connection = source()) {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, name);
			return ps.executeUpdate() != 0;
		} catch (Exception e) {
			throw new AccessLayerException(e);
		}
	}

	private static DataAccessLayer instance = null;
	
	private DataAccessLayer() throws AccessLayerException {
		String error = "An error occurred while setting up database";
		String query = String.format("CREATE TABLE IF NOT EXISTS %s (%s varchar(50) PRIMARY KEY, %s boolean NOT NULL);", 
				DB_NAME, NAME, IS_APPROVED);
		try (Connection connection = source()) {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.execute();
		} catch (Exception e) {
			throw new AccessLayerException(error, e);
		}
	}
	
    private Connection source() throws Exception {
        URI uri = new URI(System.getenv("DATABASE_URL"));
        String user = uri.getUserInfo().split(":")[0];
        String pass = uri.getUserInfo().split(":")[1];
        String url = "jdbc:postgresql://" + uri.getHost() + ':' + uri.getPort() + uri.getPath() + "?sslmode=require";
    	return DriverManager.getConnection(url, user, pass);
    }
}

package br.com.demeter.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe respons�vel por fazer a conex�o com o banco de dados oracle.
 * 
 * @author Cristine Ramiro d`Arc Acocella Piccolotto Vasconcellos
 *
 */
public class ConnectionFactory {

	public static Connection getConnection() {
		try {
			String url = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL";
			String username = "RM88251";
			String password = "300889";

			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection connection = DriverManager.getConnection(url, username, password);
			return connection;
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return null;

	}
}

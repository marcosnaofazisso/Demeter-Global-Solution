package br.com.demeter.dao;

import br.com.demeter.to.UsuarioTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

	Connection con = ConnectionFactory.getConnection();

	/**
	 * M�todo para que o usu�rio possa logar em sua conta, utilizando o email e a
	 * senha que est�o salvas no banco de dados.
	 * 
	 * @param email
	 * @param senha
	 * @return UsuarioTO
	 */
	public UsuarioTO login(String email, String senha) {

		String sql = "SELECT U.id_usuario, U.nr_cnpj, U.nm_razao_social, U.ds_email, U.tp_usuario, E.NM_REGIAO "
				+ "FROM T_DEM_USUARIO U INNER JOIN T_DEM_USUARIO_ENDERECO E " + "ON (U.ID_USUARIO = E.ID_USUARIO) "
				+ "WHERE ds_email like ? and ds_senha like ?";

		PreparedStatement prepareStatement = null;
		try {
			prepareStatement = con.prepareStatement(sql);
			prepareStatement.setString(1, "%" + email + "%");
			prepareStatement.setString(2, "%" + senha + "%");
			ResultSet resultSet = prepareStatement.executeQuery();
			resultSet.next();

			UsuarioTO usuarioTO = new UsuarioTO();

			usuarioTO.setIdUsuario(resultSet.getInt("id_usuario"));
			usuarioTO.setCnpjUsuario(resultSet.getLong("nr_cnpj"));
			usuarioTO.setRazaoSocial(resultSet.getString("nm_razao_social"));
			usuarioTO.setEmailUusario(resultSet.getString("ds_email"));
			usuarioTO.setTipoUsuario(resultSet.getString("tp_usuario"));
			usuarioTO.setRegiaoUsuario(resultSet.getString("nm_regiao"));

			con.close();

			return usuarioTO;

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return null;
	}

	/**
	 * M�todo para validar se um email j� est� cadastrado no banco de dados.
	 * 
	 * @param email
	 * @return Um boolean, caso esteja = True, caso n�o esteja = False.
	 * @throws SQLException
	 */
	public boolean isCadastrado(String email) throws SQLException {
		String sql = "SELECT * FROM T_DEM_USUARIO WHERE ds_email like ?";
		PreparedStatement prepareStatement = con.prepareStatement(sql);
		prepareStatement.setString(1, "%" + email + "%");
		ResultSet resultSet = prepareStatement.executeQuery();
		return resultSet.next();
	}

	/**
	 * M�todo para cadastar um usu�rio no banco de dados
	 * 
	 * @param usuarioTO
	 * @returnUm int com o numero de linhas modificadas no banco de dados, por meio
	 *           do ExecuteUpdate.
	 * @throws SQLException
	 */
	public int cadastrarUsuario(UsuarioTO usuarioTO) throws SQLException {

		String sql = "INSERT INTO T_DEM_USUARIO (id_usuario, nr_cnpj, nm_razao_social, ds_email, ds_senha, tp_usuario)"
				+ " VALUES (sq_dem_usuario.nextval, ?, ?, ?, ?, ?)";

		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, usuarioTO.getCnpjUsuario());
		ps.setString(2, usuarioTO.getRazaoSocial());
		ps.setString(3, usuarioTO.getEmailUusario());
		ps.setString(4, usuarioTO.getSenhaUsuario());
		ps.setString(5, usuarioTO.getTipoUsuario());

		return ps.executeUpdate();

	}

	/**
	 * M�todo para realizar o cadastro de uma regi�o no banco de dados.
	 * 
	 * @param usuarioTO
	 * @retur Um int com o numero de linhas modificadas no banco de dados, por meio
	 *        do ExecuteUpdate.
	 * @throws SQLException
	 */
	public int cadastrarRegiao(UsuarioTO usuarioTO) throws SQLException {

		String sql = "INSERT INTO t_dem_usuario_endereco (id_usuario, id_endereco, nm_regiao)"
				+ " values (sq_dem_usuario.currval, sq_dem_usuario_endereco.nextval, ?)";

		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, usuarioTO.getRegiaoUsuario());

		return ps.executeUpdate();

	}

	/**
	 * M�todo para cadastrar um estoque.
	 * 
	 * @throws SQLException
	 */
	public void cadastrarEstoque() throws SQLException {

		String sql = "INSERT INTO T_DEM_ESTOQUE (id_estoque, id_usuario) "
				+ "VALUES (sq_dem_estoque.nextval, sq_dem_usuario.currval)";

		PreparedStatement ps = con.prepareStatement(sql);

		ps.executeUpdate();
		con.close();
	}
}

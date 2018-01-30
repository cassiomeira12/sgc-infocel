package banco.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Celular;
import model.Cliente;

/**
 * DAO responsável pela ações realizadas na base de dados referentes aos
 * clientes
 */
public class ClienteDAO extends DAO {

    public ClienteDAO() {
        super();
    }

    /**
     * Inserir cliente na base de dados
     */
    public void inserir(Cliente cliente) {
        try {
            String sql = "INSERT INTO cliente ( nome ) VALUES (?)";

            stm = conector.prepareStatement(sql);

            stm.setString(1, cliente.getNome());

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao inserir cliente na base de dados", ex.toString());
        }
    }

    /**
     * Atualizar dados cliente na base de dados
     */
    public void editar(Cliente cliente) {
        try {
            String sql = "UPDATE cliente SET nome =? WHERE id =?";

            stm = conector.prepareStatement(sql);

            stm.setString(1, cliente.getNome());

            stm.setInt(2, cliente.getId().intValue());

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao atualizar cliente na base de dados!", ex.toString());
        }
    }

    /**
     * Excluir cliente na base de dados
     */
    public void excluir(int id) {
        try {
            String sql = "DELETE FROM cliente WHERE id=?";

            stm = conector.prepareStatement(sql);

            stm.setInt(1, id);
            stm.execute();

            stm.close();
        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao excluir cliente na base de dados!", ex.toString());
        }
    }

    /**
     * Consultar todos clientes cadastrados na base de dados
     */
    public List<Cliente> listar() {

        List<Cliente> clientes = new ArrayList<>();

        try {
            String sql = "SELECT cliente.* FROM cliente";

            stm = conector.prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Cliente cliente = new Cliente((long) rs.getInt(1), rs.getString(2));

                clientes.add(cliente);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar clientes na base de dados!", ex.toString());
        }

        return clientes;
    }

}

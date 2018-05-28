package banco.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CategoriaProduto;
import model.CategoriaSaida;

/**
 * DAO responsável pela ações realizadas na base de dados referentes as
 * categoria
 */
public class CategoriaSaidaDAO extends DAO {

    public CategoriaSaidaDAO() {
        super();
    }

    /**
     * Inserir categoria na base de dados
     */
    public Long inserir(CategoriaSaida categoria) throws Exception {
        String sql = "INSERT INTO categoria_saida ( descricao ) VALUES (?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setString(1, categoria.getDescricao());

        return super.inserir();
    }

    /**
     * Atualizar dados categoria na base de dados
     */
    public boolean editar(CategoriaProduto categoria) throws SQLException {
        String sql = "UPDATE categoria_saida SET descricao =? WHERE id =?";

        stm = getConector().prepareStatement(sql);

        stm.setString(1, categoria.getDescricao());

        stm.setInt(2, categoria.getId().intValue());

        stm.executeUpdate();
        stm.close();

        return true;
    }

    /**
     * Excluir categoria na base de dados
     */
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM categoria_saida WHERE id=?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, id);
        stm.execute();

        stm.close();

        return true;
    }

    /**
     * Consultar todas categoria cadastradas na base de dados
     */
    public List<CategoriaSaida> listar() throws SQLException {

        List<CategoriaSaida> categorias = new ArrayList<>();
        String sql = "SELECT categoria_saida.* FROM categoria_saida";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            CategoriaSaida categoria = new CategoriaSaida((long) rs.getInt(1), rs.getString(2));

            categorias.add(categoria);
        }

        stm.close();
        rs.close();

        return categorias;
    }

    public List<CategoriaSaida> buscarPorDescricao(String descricao) throws SQLException {

        List<CategoriaSaida> categorias = new ArrayList<>();

        String sql = "SELECT categoria_saida.* FROM categoria_saida WHERE descricao LIKE '%" + descricao + "%'";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            CategoriaSaida categoria = new CategoriaSaida((long) rs.getInt(1), rs.getString(2));

            categorias.add(categoria);
        }

        stm.close();
        rs.close();

        return categorias;
    }

}

/** *********************************************************************
 * Autor: Cassio Meira Silva
 * Nome: Conexao Banco
 * Funcao: Classe pra realizar a conexao com o Banco de Dados
 ********************************************************************** */
package banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Platform;
import util.Alerta;

/**
 * Informações dos dados para conexão com a base de dados
 *
 * @author Angelica Leite/ Editado por Pedro Cordeiro / Editado por Cassio Meira
 */
public class ConexaoBanco {

    private static final String URL = "jdbc:mysql://neolig.com:3306/";
    private static final String DATABASE = "neoli831_teste";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String USERNAME = "neoli831_teste";
    private static final String PASSWORD = "teste";

    private static ConexaoBanco instancia = new ConexaoBanco();
    private Connection connection;

    private ConexaoBanco() {
        try {
            Class.forName(DRIVER).newInstance();
            connection = DriverManager.getConnection(URL + DATABASE, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            System.out.println("[ERRO]: Erro ao conectar-se com a base de dados\n" + ex.toString());
            chamarAlertaErro("[ERRO]: Erro ao conectar-se com a base de dados", ex.toString());
        }
    }

    /**
     * Sigleton para conexão com a base de dados
     *
     * @return
     */
    public static ConexaoBanco instancia() {
        if (instancia == null) {
            instancia = new ConexaoBanco();
        }
        return instancia;
    }

    /**
     * Obter a conexão com a base de dados
     *
     * @return
     */
    public Connection getConnection() {
        return connection;
    }

    public void chamarAlertaErro(String titulo, String mensagem) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alerta.erro(titulo, mensagem);
            }
        });
    }

}//fim class

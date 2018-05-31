package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javax.swing.SwingWorker;
import model.Administrador;
import model.Cliente;
import model.Venda;
import util.alerta.Alerta;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaConsultarVendasController extends AnchorPane {

    private BorderPane painelPrincipal;

    private List<Venda> listaVendas;

    @FXML
    private TableView<Venda> vendasTable;
    @FXML
    private TableColumn<Venda, Cliente> clienteColumn;
    @FXML
    private TableColumn<Administrador, String> vendedorColumn;
    @FXML
    private TableColumn<Venda, Long> dataColumn;
    @FXML
    private TableColumn<Venda, Float> totalColumn;
    @FXML
    private Button visualizarButton;


    public TelaConsultarVendasController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;

        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaConsultarVendas.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Consultar Vendas");
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        //Desativa os Botoes de Editar e Excluir quando nenhum item na tabela esta selecionado
        visualizarButton.disableProperty().bind(vendasTable.getSelectionModel().selectedItemProperty().isNull());
        
        //Formatter.toUpperCase(pesquisaText);

        this.sincronizarBancoDados();
        //this.atualizarTabela();

        //pesquisaText.textProperty().addListener((obs, old, novo) -> {
        //    filtro(novo, listaVendas, vendasTable);
        //});

        vendasTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (event.getClickCount() == 2) {
                        visualizarVenda();
                    }
                }
            }
        });

    }

    @FXML
    private void pesquisarCliente() {
        
    }
    
    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelPrincipal.setCenter(novaTela);
    }

    @FXML
    private void cancelarOperacao() {
        TelaInicialController telaInicial = new TelaInicialController(painelPrincipal);
        this.adicionarPainelInterno(telaInicial);
    }

    @FXML
    private void visualizarVenda() {
//        Cliente cliente = vendasTable.getSelectionModel().getSelectedItem();
//
//        TelaClienteController telaCliente = new TelaClienteController(painelPrincipal, cliente);
//        this.adicionarPainelInterno(telaCliente);
    }

    private void filtro(String texto, List lista, TableView tabela) {
        ObservableList data = FXCollections.observableArrayList(lista);

        FilteredList<Venda> dadosFiltrados = new FilteredList(data, filtro -> true);

        dadosFiltrados.setPredicate(filtro -> {
            if (texto == null || texto.isEmpty()) {
                return true;
            }
            //Coloque aqui as verificacoes da Pesquisa
//            if (filtro.getNome().toLowerCase().contains(texto.toLowerCase())) {
//                return true;
//            }
            if (filtro.getCliente().getNome().toLowerCase().contains(texto.toLowerCase())) {
                return true;
            }
            
            
            return false;
        });

        SortedList dadosOrdenados = new SortedList(dadosFiltrados);
        dadosOrdenados.comparatorProperty().bind(tabela.comparatorProperty());
        tabela.setItems(dadosOrdenados);
    }

    private void atualizarTabela() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(listaVendas);
        
       // this.clienteColumn.setCellValueFactory(new PropertyValueFactory<>("venda.cliente.nome"));//Adiciona o valor da variavel Nome
        //this.vendedorColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        this.dataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));//Adiciona o valor da variavel Telefone
        this.totalColumn.setCellValueFactory(new PropertyValueFactory<>("precoTotal"));
        this.vendasTable.setItems(data);//Adiciona a lista de clientes na Tabela
    }

    private void sincronizarBancoDados() {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Venda> doInBackground() throws Exception {
                return ControleDAO.getBanco().getVendaDAO().listar();
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaVendas = this.get();
                    atualizarTabela();
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
                }
            }
        };

        worker.execute();
    }

    private void chamarAlerta(String mensagem) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alerta.erro(mensagem);
            }
        });
    }

}
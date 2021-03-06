/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javax.swing.SwingWorker;
import model.Administrador;
import model.Cliente;
import model.Manutencao;
import org.apache.log4j.Logger;
import util.DateUtils;
import util.Formatter;
import util.alerta.Alerta;
import util.alerta.Dialogo;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaConsultarManutencoesController extends AnchorPane {
    
    private BorderPane painelPrincipal;

    private List<Manutencao> listaManutencoes;
    
    private LocalDate dataInicio;
    private LocalDate dataFim;
    
    @FXML
    private TextField filtroText;
    @FXML
    private DatePicker inicioDatePicker;
    @FXML
    private DatePicker fimDatePicker;
    
    @FXML
    private TableView<Manutencao> manutencoesTable;
    @FXML
    private TableColumn<Cliente, String> clienteColumn;
    @FXML
    private TableColumn<Manutencao, String> descricaoColumn;
    @FXML
    private TableColumn<Administrador, String> vendedorColumn;
    @FXML
    private TableColumn<Manutencao, String> marcaColumn;
    @FXML
    private TableColumn<Manutencao, String> modeloColumn;
    @FXML
    private TableColumn<Manutencao, String> dataColumn;
    @FXML
    private TableColumn<Manutencao, String> precoColumn;
    @FXML
    private TableColumn<Manutencao, String> finalizadoColumn;
    
    @FXML
    private Button editarButton;
    @FXML
    private Button excluirButton;
  
    public TelaConsultarManutencoesController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaConsultarManutencoes.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            //Logger.getLogger(getClass()).error(ex);
            ex.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        this.dataInicio = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);
        this.dataFim = LocalDate.of(dataInicio.getYear(), dataInicio.getMonthValue(), dataInicio.lengthOfMonth());
        
        Formatter.toUpperCase(filtroText);
        
        inicioDatePicker.setValue(dataInicio);
        fimDatePicker.setValue(dataFim);
        
        editarButton.disableProperty().bind(manutencoesTable.getSelectionModel().selectedItemProperty().isNull());
        excluirButton.disableProperty().bind(manutencoesTable.getSelectionModel().selectedItemProperty().isNull());
        
        inicioDatePicker.setOnAction((e) -> {
            this.dataInicio = inicioDatePicker.getValue();
            sincronizarBancoDados(DateUtils.formatDate(dataInicio), DateUtils.formatDate(dataFim));
        });
        
        fimDatePicker.setOnAction((e) -> {
            this.dataFim = fimDatePicker.getValue();
            sincronizarBancoDados(DateUtils.formatDate(dataInicio), DateUtils.formatDate(dataFim));
        });
        
        filtroText.textProperty().addListener((e) -> {
            String texto = filtroText.getText();
            filtro(texto, listaManutencoes, manutencoesTable);
        });
        
        manutencoesTable.setOnMouseClicked((event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    editarManutencao();
                }
            }
        });
        
        manutencoesTable.setRowFactory(tv -> new TableRow<Manutencao>() {
            @Override
            public void updateItem(Manutencao item, boolean empty) {
                super.updateItem(item, empty);
                
                if (item == null) {
                    setStyle("");
                } else if (item.isFinalizado()) {
                    setStyle("-fx-border-color: #8BC34A;");
                } else {
                    setStyle("-fx-border-color: #F44336;");
                }
                
            }
        });
        
        sincronizarBancoDados(DateUtils.formatDate(dataInicio), DateUtils.formatDate(dataFim));
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
    private void editarManutencao() {
        Manutencao manutencao = manutencoesTable.getSelectionModel().getSelectedItem();
        TelaManutencaoController tela = new TelaManutencaoController(painelPrincipal);
        tela.setManutencao(manutencao);
        this.adicionarPainelInterno(tela);
    }
    
    @FXML
    private void excluirManutencao() {
        Manutencao manutencao = manutencoesTable.getSelectionModel().getSelectedItem();
        
        Dialogo.Resposta resposta = Alerta.confirmar("Excluir a Manutenção selecionada ?");

        if (resposta == Dialogo.Resposta.YES) {
            try {
                if (ControleDAO.getBanco().getManutencaoDAO().excluir(manutencao.getId().intValue())) {
                    Alerta.info("Manutenção excluída com sucesso !");
                    sincronizarBancoDados(DateUtils.formatDate(dataInicio), DateUtils.formatDate(dataFim));
                } else {
                    Alerta.erro("Erro ao excluir Manutenção !");
                }
            } catch (SQLException ex) {
                Logger.getLogger(getClass()).error(ex);
                Alerta.erro("Erro ao excluir Manutenção !");
                ex.printStackTrace();
            }
        }
        
        manutencoesTable.getSelectionModel().clearSelection();
    }
    
    private void sincronizarBancoDados(String dataInicio, String dataFinal) {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Manutencao> doInBackground() throws Exception {
                return ControleDAO.getBanco().getManutencaoDAO().buscarPorIntervalo(dataInicio, dataFinal);
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaManutencoes = this.get();
                    Collections.sort(listaManutencoes);//Ordenando as Operacoes
                    atualizarTabela();
                } catch (InterruptedException | ExecutionException ex) {
                    Logger.getLogger(getClass()).error(ex);
                    chamarAlerta("Erro ao consultar Banco de Dados");
                    ex.printStackTrace();
                }
            }
        };

        worker.execute();
    }
    
    private void atualizarTabela() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(listaManutencoes);
        
        this.clienteColumn.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        this.descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.vendedorColumn.setCellValueFactory(new PropertyValueFactory<>("administrador"));
        this.marcaColumn.setCellValueFactory(new PropertyValueFactory<>("marca"));
        this.modeloColumn.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        this.dataColumn.setCellValueFactory(new PropertyValueFactory<>("dataEditada"));
        this.precoColumn.setCellValueFactory(new PropertyValueFactory<>("precoFormatado"));
        this.finalizadoColumn.setCellValueFactory(new PropertyValueFactory<>("finalizadoEditado"));
        this.manutencoesTable.setItems(data);//Adiciona a lista de clientes na Tabela
    }
    
    private void chamarAlerta(String mensagem) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alerta.erro(mensagem);
            }
        });
    }
    
    private void filtro(String texto, List lista, TableView tabela) {
        ObservableList data = FXCollections.observableArrayList(lista);

        FilteredList<Manutencao> dadosFiltrados = new FilteredList(data, filtro -> true);

        dadosFiltrados.setPredicate(filtro -> {
            if (texto == null || texto.isEmpty()) {
                return true;
            }
            //Coloque aqui as verificacoes da Pesquisa
            if (filtro.getCliente().getNome().toLowerCase().contains(texto.toLowerCase())) {
                return true;
            }
            
            if (filtro.getDescricao().toLowerCase().contains(texto.toLowerCase())) {
                return true;
            }
            
            if (filtro.getAdministrador().getNome().toLowerCase().contains(texto.toLowerCase())) {
                return true;
            }
            
            if (filtro.getMarca().toLowerCase().contains(texto.toLowerCase())) {
                return true;
            }
            
            if (filtro.getModelo().toLowerCase().contains(texto.toLowerCase())) {
                return true;
            }
            
            return false;
        });

        SortedList dadosOrdenados = new SortedList(dadosFiltrados);
        dadosOrdenados.comparatorProperty().bind(tabela.comparatorProperty());
        tabela.setItems(dadosOrdenados);
    }
}

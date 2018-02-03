/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.Formatter;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaVendaController extends AnchorPane {
    
    private BorderPane painelPrincipal;
    
    @FXML
    private TextField nomeText;
    @FXML
    private TextField cpfText;
    @FXML
    private TextField telefoneText;
    @FXML
    private TextField rgText;

  
    public TelaVendaController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaAdicionarVenda.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Adicionar Venda");
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        Formatter.mascaraCPF(cpfText);
        Formatter.mascaraRG(rgText);
        Formatter.mascaraTelefone(telefoneText);
        // TODO
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
    private void finalizarCompra() {
        
    }
    
    @FXML
    private void adicionarProduto() {

        Stage palco = new Stage();
        palco.initModality(Modality.APPLICATION_MODAL);//Impede de clicar na tela em plano de fundo
        palco.centerOnScreen();
        palco.initStyle(StageStyle.UNDECORATED);//Remove a barra de menu

        TelaSelecionarProdutoController telaSelecionarProduto = new TelaSelecionarProdutoController(palco);
        
        palco.setScene(new Scene(telaSelecionarProduto));
        palco.showAndWait();
        
        
        if (telaSelecionarProduto.RESULTADO) {//Selecionou Produto
            System.out.println("Adicionou");
        } else {//Nao selecionou Produto
            System.out.println("Cancelou");
        }
        
    }
    
    @FXML
    private void removerProduto() {
        
    }
}

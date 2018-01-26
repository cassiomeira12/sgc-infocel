/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaSaidaController extends AnchorPane {
    
    private AnchorPane painelInterno;

  
    public TelaSaidaController(AnchorPane painelInterno) {
        this.painelInterno = painelInterno;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaAdicionarSaida.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Adicionar Saida");
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        // TODO
    }
    
    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelInterno.getChildren().clear();
        this.painelInterno.getChildren().add(novaTela);
    }
    
    @FXML
    private void cancelarOperacao() {
        TelaInicialController telaInicial = new TelaInicialController(painelInterno);
        this.adicionarPainelInterno(telaInicial);
    }
    
}

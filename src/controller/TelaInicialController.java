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
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaInicialController extends AnchorPane {
    
    private BorderPane painelPrincipal;

  
    public TelaInicialController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaInicial.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela de Login");
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        // TODO
    }
    
    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelPrincipal.getCenter().setVisible(true);
        this.painelPrincipal.setCenter(novaTela);
    }
    
    @FXML
    private void chamarTelaAdicionarManutencao() {
        TelaManutencaoController telaAdicionarManutencao = new TelaManutencaoController(painelPrincipal);
        this.adicionarPainelInterno(telaAdicionarManutencao);
    }
    
    @FXML
    private void chamarTelaAdicionarReceita() {
        TelaReceitaController telaAdicionarReceita = new TelaReceitaController(painelPrincipal);
        this.adicionarPainelInterno(telaAdicionarReceita);
    }
    
    @FXML
    private void chamarTelaAdicionarSaida() {
        TelaSaidaController telaAdicionarSaida = new TelaSaidaController(painelPrincipal);
        this.adicionarPainelInterno(telaAdicionarSaida);
    }
    
    @FXML
    private void chamarTelaAdicionarVenda() {
        TelaVendaController telaAdicionarVenda = new TelaVendaController(painelPrincipal);
        this.adicionarPainelInterno(telaAdicionarVenda);
    }
    
    
    
    
    
}

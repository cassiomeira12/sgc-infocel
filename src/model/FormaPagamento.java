/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author pedro
 */
public class FormaPagamento {

    private Long id;
    private String descricao;
    private int parcelas;
    private int maximoParcelas;

    public FormaPagamento(Long id, String descricao, int parcelas, int maximoParcelas) {
        this.id = id;
        this.parcelas = parcelas;
        this.descricao = descricao;
        this.maximoParcelas = maximoParcelas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getParcelas() {
        return parcelas;
    }

    public void setParcelas(int parcelas) {
        this.parcelas = parcelas;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getMaximoParcelas() {
        return maximoParcelas;
    }

    public void setMaximoParcelas(int maximoParcelas) {
        this.maximoParcelas = maximoParcelas;
    }

}
package model;

public class Cidade {

    private Long id;
    private String nome;

    public Cidade(Long id, String descricao) {
        this.id = id;
        this.nome = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    @Override
    public String toString() {
        return this.nome;
    }

}

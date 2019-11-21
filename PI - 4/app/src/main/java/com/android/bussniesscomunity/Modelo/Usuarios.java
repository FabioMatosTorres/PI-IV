package com.android.bussniesscomunity.Modelo;

public class Usuarios {
    private String id;
    private String nome;
    private String cpf;
    private String descricao;



    public Usuarios() {

    }

    public Usuarios(String id, String nome, String cpf, String descricao) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.descricao = descricao;

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


}

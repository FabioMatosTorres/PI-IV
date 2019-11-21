package com.android.bussniesscomunity.Modelo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Propostas {

    private String foto;
    private String titulo;
    private String descricao;
    private String telefone;
    private String ID;
    private  String idUsuario;
    private String idProposta;
    private String imagem;

    private DatabaseReference reference;

    public Propostas() {
    }

    public Propostas(String foto, String titulo, String descricao, String telefone, String ID, String idUsuario, String idProposta, String imagem) {
        this.foto = foto;
        this.titulo = titulo;
        this.descricao = descricao;
        this.telefone = telefone;
        this.ID = ID;
        this.idUsuario = idUsuario;
        this.idProposta = idProposta;
        this.imagem = imagem;
    }


    public Propostas(String foto, String titulo) {
        this.foto = foto;
        this.titulo = titulo;
    }
    public void setID() {
        reference = FirebaseDatabase.getInstance().getReference().child("Propostas");
        this.ID = reference.push().getKey();
    }

    public String getID() {
        return ID;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdProposta() {
        return idProposta;
    }

    public void setIdProposta(String idProposta) {
        this.idProposta = idProposta;
    }


    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}


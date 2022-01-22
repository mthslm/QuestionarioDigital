/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bean;

import java.util.ArrayList;

/**
 *
 * @author Matheus
 */
public class Pessoa {
    private int id;
    private String  nome , rua , bairro, numero;
    private ArrayList<Questionario> questionario;

    public Pessoa(int id, String nome, String rua, String bairro, String numero, ArrayList<Questionario> questionario) {
        this.id = id;
        this.nome = nome;
        this.rua = rua;
        this.bairro = bairro;
        this.numero = numero;
        this.questionario = questionario;
    }

    public Pessoa(String nome, String rua, String bairro, String numero, ArrayList<Questionario> questionario) {
        this.nome = nome;
        this.rua = rua;
        this.bairro = bairro;
        this.numero = numero;
        this.questionario = questionario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public ArrayList<Questionario> getQuestionario() {
        return questionario;
    }

    public void setQuestionario(ArrayList<Questionario> questionario) {
        this.questionario = questionario;
    }

    
}

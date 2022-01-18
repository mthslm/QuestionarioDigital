/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bean;

import java.util.Date;

/**
 *
 * @author Matheus
 */
public class Animal {
    private String especie;
    private int quantidade;
    private Date data;

    public Animal(String especie, int quantidade, Date data) {
        this.especie = especie;
        this.quantidade = quantidade;
        this.data = data;
    }

    public Animal(String especie, int quantidade) {
        this.especie = especie;
        this.quantidade = quantidade;
    }
    
    

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
    
    
}

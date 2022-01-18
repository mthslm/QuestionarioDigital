/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bean;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Matheus
 */
public class Questionario {
    private boolean cisterna , cisternaconsumo , cxdagua , tampada , pcartesiano , pococonsumo , fseptica , animal;
    private int capacidade;
    private Date data;
    private ArrayList<Animal> animais;

    public Questionario(boolean cisterna, boolean cisternaconsumo, boolean cxdagua, boolean tampada, boolean pcartesiano, boolean pococonsumo, boolean fseptica, boolean animal, int capacidade, Date data, ArrayList<Animal> animais) {
        this.cisterna = cisterna;
        this.cisternaconsumo = cisternaconsumo;
        this.cxdagua = cxdagua;
        this.tampada = tampada;
        this.pcartesiano = pcartesiano;
        this.pococonsumo = pococonsumo;
        this.fseptica = fseptica;
        this.animal = animal;
        this.capacidade = capacidade;
        this.data = data;
        this.animais = animais;
    }
    
    public boolean isCisterna() {
        return cisterna;
    }

    public void setCisterna(boolean cisterna) {
        this.cisterna = cisterna;
    }

    public boolean isCisternaconsumo() {
        return cisternaconsumo;
    }

    public void setCisternaconsumo(boolean cisternaconsumo) {
        this.cisternaconsumo = cisternaconsumo;
    }

    public boolean isCxdagua() {
        return cxdagua;
    }

    public void setCxdagua(boolean cxdagua) {
        this.cxdagua = cxdagua;
    }

    public boolean isTampada() {
        return tampada;
    }

    public void setTampada(boolean tampada) {
        this.tampada = tampada;
    }

    public boolean isPcartesiano() {
        return pcartesiano;
    }

    public void setPcartesiano(boolean pcartesiano) {
        this.pcartesiano = pcartesiano;
    }

    public boolean isPococonsumo() {
        return pococonsumo;
    }

    public void setPococonsumo(boolean pococonsumo) {
        this.pococonsumo = pococonsumo;
    }

    public boolean isFseptica() {
        return fseptica;
    }

    public void setFseptica(boolean fseptica) {
        this.fseptica = fseptica;
    }

    public boolean isAnimal() {
        return animal;
    }

    public void setAnimal(boolean animal) {
        this.animal = animal;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public ArrayList<Animal> getAnimais() {
        return animais;
    }

    public void setAnimais(ArrayList<Animal> animais) {
        this.animais = animais;
    }

    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import model.bean.Animal;

/**
 *
 * @author Matheus
 */
public class AnimalDAO {

    public ArrayList<Animal> getAnimal(int id) {
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<Animal> animais = new ArrayList<Animal>();
        conexao = ConnectionFactory.conector();
        String sql = "select * from tbl_animais where idanimais = "+id;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                animais.add(new Animal(rs.getString(2), rs.getInt(3), rs.getDate(4)));
            }
            rs.close();
            pst.close();
            conexao.close();
            return animais;
        } catch (Exception e) {
            return null;
        }
    }
}

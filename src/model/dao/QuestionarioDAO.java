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
import model.bean.Animal;
import model.bean.Questionario;

/**
 *
 * @author Matheus
 */
public class QuestionarioDAO {
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    AnimalDAO adao = new AnimalDAO();
    
    public ArrayList<Questionario> getQuestionario(int id){
        ArrayList<Questionario> questionarios = new ArrayList<Questionario>();
        conexao = ConnectionFactory.conector();
        String sql = "select * from tbl_perguntas where idanimais = "+id;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                questionarios.add(new Questionario(rs.getBoolean(2), rs.getBoolean(3), rs.getBoolean(4), rs.getBoolean(5), rs.getBoolean(6), rs.getBoolean(7), rs.getBoolean(8), rs.getBoolean(9), rs.getInt(10), rs.getDate(11), adao.getAnimal(id)));
            }
            return questionarios;
        } catch (Exception e) {
            return null;
        }
    }
}

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
import model.bean.Questionario;

/**
 *
 * @author Matheus
 */
public class QuestionarioDAO {
    
    AnimalDAO adao = new AnimalDAO();

    public ArrayList<Questionario> getQuestionario(int id) {
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<Questionario> questionarios = new ArrayList<Questionario>();
        conexao = ConnectionFactory.conector();
        String sql = "select * from tbl_perguntas where idperguntas = " + id;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                questionarios.add(new Questionario(rs.getBoolean(2), rs.getBoolean(3), rs.getBoolean(4), rs.getBoolean(5), rs.getBoolean(6), rs.getBoolean(7), rs.getBoolean(8), rs.getBoolean(9), rs.getInt(10), rs.getDate(11), adao.getAnimal(id,rs.getDate(11))));
            }
            rs.close();
            pst.close();
            conexao.close();
            return questionarios;
        } catch (Exception e) {
            System.out.println("erro getQuestionario");
            return null;
        }
    }

    public void cadastrarQuestionario(int id){
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        conexao = ConnectionFactory.conector();
        String sql = "INSERT INTO tbl_perguntas (idperguntas,cisterna,cisternaconsumo,cxdagua,tampada,pcartesiano,pococonsumo,fseptica,animais,capacidade,datapergunta) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        
    }
}

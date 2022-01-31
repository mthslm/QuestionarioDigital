/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import com.toedter.calendar.JDateChooser;
import connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
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

    public void cadastrarQuestionario(int id, JCheckBox pergunta1, JCheckBox pergunta1p2, JCheckBox pergunta2, JCheckBox pergunta2p1, JTextField pergunta2p2, JCheckBox pergunta3, JCheckBox pergunta3p1, JCheckBox pergunta4, JCheckBox pergunta5, JDateChooser cadastrarData) throws ParseException{
        
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        conexao = ConnectionFactory.conector();
        String sql = "INSERT INTO tbl_perguntas (idperguntas,cisterna,cisternaconsumo,cxdagua,tampada,capacidade,pcartesiano,pococonsumo,fseptica,animais,datapergunta) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, id);
            pst.setBoolean(2, pergunta1.isSelected());
            pst.setBoolean(3, pergunta1p2.isSelected());
            pst.setBoolean(4, pergunta2.isSelected());
            pst.setBoolean(5, pergunta2p1.isSelected());
            pst.setString(6, pergunta2p2.getText());
            pst.setBoolean(7, pergunta3.isSelected());
            pst.setBoolean(8, pergunta3p1.isSelected());
            pst.setBoolean(9, pergunta4.isSelected());
            pst.setBoolean(10, pergunta5.isSelected());
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            pst.setString(11, dt.format(cadastrarData.getDate()));
            pst.executeUpdate();
            pst.close();
            conexao.close();
        } catch (SQLException ex) {
            Logger.getLogger(QuestionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

package model.dao;


import connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Matheus
 */
public class EnderecoDAO {
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null; 
    
    public void getBairros(JComboBox combo){
        conexao = ConnectionFactory.conector();
        String sql = "select distinct bairro from tbl_bairro_rua order by bairro asc";
        
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                combo.addItem(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(EnderecoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getRuas(JComboBox bairro, JComboBox rua){
        conexao = ConnectionFactory.conector();
        String sql = "select rua from tbl_bairro_rua where bairro = ? order by rua asc";
        rua.removeAllItems();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, bairro.getSelectedItem().toString());
            rs = pst.executeQuery();
            while(rs.next()){
                rua.addItem(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(EnderecoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.bean.Animal;

/**
 *
 * @author Matheus
 */
public class AnimalDAO {

    public ArrayList<Animal> getAnimal(int id, Date data) {
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<Animal> animais = new ArrayList<Animal>();
        conexao = ConnectionFactory.conector();
        String sql = "select especie, sexo, chip, idade, dataanimais, castrado, id from tbl_animais where idanimais = " + id + " and dataanimais = '" + data + "'";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                animais.add(new Animal(rs.getString("especie"), rs.getString("sexo"), rs.getString("chip"), rs.getString("idade"), rs.getDate("dataanimais"), rs.getBoolean("castrado"), rs.getInt("id")));
            }
            rs.close();
            pst.close();
            conexao.close();
            return animais;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean cadastrarAnimal(int id, JTable jTableAnimais, JDateChooser cadastrarData) {
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        conexao = ConnectionFactory.conector();
        String sql = "INSERT INTO tbl_animais (idanimais, especie, sexo, castrado, idade, chip, dataanimais) VALUES (?,?,?,?,?,?,?)";

        try {
            pst = conexao.prepareStatement(sql);
            for (int i = 0; i < jTableAnimais.getRowCount(); i++) {
                pst.setInt(1, id);
                pst.setString(2, jTableAnimais.getValueAt(i, 0) + "");
                pst.setString(3, jTableAnimais.getValueAt(i, 1) + "");
                if (jTableAnimais.getValueAt(i, 2) == null) {
                    pst.setBoolean(4, false);
                } else {
                    pst.setBoolean(4, (boolean) jTableAnimais.getValueAt(i, 2));
                }
                pst.setString(5, jTableAnimais.getValueAt(i, 3) + "");
                pst.setString(6, jTableAnimais.getValueAt(i, 4) + "");
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                pst.setString(7, dt.format(cadastrarData.getDate()));
                pst.executeUpdate();
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(AnimalDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean editarAnimal(int idpessoa, JTable jTableAnimais, JDateChooser cadastrarData, DefaultTableModel listaAnimais) {
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        conexao = ConnectionFactory.conector();

        String sql1 = "UPDATE tbl_animais SET especie=?, sexo=?, castrado=?, idade=?, chip=?, dataanimais=? where id=?";
        String sql2 = "INSERT INTO tbl_animais (idanimais, especie, sexo, castrado, idade, chip, dataanimais) VALUES (?,?,?,?,?,?,?)";

        try {
            for (int i = 0; i < jTableAnimais.getRowCount(); i++) {
                if (listaAnimais.getValueAt(i, 5) != null) {
                    System.out.println("primeiro if");
                    pst = conexao.prepareStatement(sql1);
                    pst.setString(1, jTableAnimais.getValueAt(i, 0) + "");
                    pst.setString(2, jTableAnimais.getValueAt(i, 1) + "");
                    pst.setBoolean(3, (boolean) jTableAnimais.getValueAt(i, 2));
                    pst.setString(4, jTableAnimais.getValueAt(i, 3) + "");
                    pst.setString(5, jTableAnimais.getValueAt(i, 4) + "");
                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                    pst.setString(6, dt.format(cadastrarData.getDate()));
                    pst.setString(7, listaAnimais.getValueAt(i, 5) + "");
                    pst.executeUpdate();
                    pst.close();
                } else {
                    System.out.println("segundo if");
                    pst = conexao.prepareStatement(sql2);
                    pst.setInt(1, idpessoa);
                    pst.setString(2, jTableAnimais.getValueAt(i, 0) + "");
                    pst.setString(3, jTableAnimais.getValueAt(i, 1) + "");
                    if (jTableAnimais.getValueAt(i, 2) == null) {
                        pst.setBoolean(4, false);
                    } else {
                        pst.setBoolean(4, (boolean) jTableAnimais.getValueAt(i, 2));
                    }
                    pst.setString(5, jTableAnimais.getValueAt(i, 3) + "");
                    pst.setString(6, jTableAnimais.getValueAt(i, 4) + "");
                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                    pst.setString(7, dt.format(cadastrarData.getDate()));
                    pst.executeUpdate();
                }
            }

            conexao.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PessoaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean deletarAnimal(int id){
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        conexao = ConnectionFactory.conector();
        
        String sql = "delete from tbl_animais where id=?";
        
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
            pst.close();
            conexao.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(AnimalDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}

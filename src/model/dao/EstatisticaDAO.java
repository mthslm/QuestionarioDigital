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

/**
 *
 * @author Matheus
 */
public class EstatisticaDAO {
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null; 
    
    public int numForms(){
        conexao = ConnectionFactory.conector();
        String sql = "select count(*) from tbl_perguntas";
        int n = 0;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            n = rs.getInt(1);
            pst.close();
            return n;
        } catch (Exception e) {
            return n;
        }
    }
    
    public int numAnimais(){
        conexao = ConnectionFactory.conector();
        String sql = "select count(distinct idperguntas) from tbl_perguntas where animais = 1";
        int n = 0;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            n = rs.getInt(1);
            pst.close();
            return n;
        } catch (Exception e) {
            return n;
        }
    }
    
    public int numCaixa(){
        conexao = ConnectionFactory.conector();
        String sql = "select count(distinct idperguntas) from tbl_perguntas where cxdagua = 1";
        int n = 0;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            n = rs.getInt(1);
            pst.close();
            return n;
        } catch (Exception e) {
            return n;
        }
    }
    
    public int numPoco(){
        conexao = ConnectionFactory.conector();
        String sql = "select count(distinct idperguntas) from tbl_perguntas where pcartesiano = 1";
        int n = 0;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            n = rs.getInt(1);
            pst.close();
            return n;
        } catch (Exception e) {
            return n;
        }
    }
    
    public int numFossa(){
        conexao = ConnectionFactory.conector();
        String sql = "select count(distinct idperguntas) from tbl_perguntas where fseptica = 1";
        int n = 0;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            n = rs.getInt(1);
            pst.close();
            return n;
        } catch (Exception e) {
            return n;
        }
    }
    
    public int numPessoas(){
        conexao = ConnectionFactory.conector();
        String sql = "select count(*) from tbl_pessoas";
        int n = 0;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            n = rs.getInt(1);
            pst.close();
            return n;
        } catch (Exception e) {
            return n;
        }
    }
}

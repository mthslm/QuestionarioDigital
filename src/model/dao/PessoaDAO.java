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
import javax.swing.table.DefaultTableModel;
import model.bean.Pessoa;

/**
 *
 * @author Matheus
 */
public class PessoaDAO {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public void getResultados(DefaultTableModel tabela){
        tabela.setRowCount(0);
        conexao = ConnectionFactory.conector();
        String sql = "select idpessoas, nome, numero, rua, bairro, datapergunta, count(idperguntas) as Qtd from tbl_pessoas, tbl_perguntas where idpessoas = idperguntas group by idpessoas";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                
                tabela.addRow(new Object[] {rs.getString(2),rs.getString(4)+", "+rs.getString(3)+" - "+rs.getString(5),rs.getString(6),rs.getInt(7)});
            }
        }catch (Exception e) {
            
        }
    }
}

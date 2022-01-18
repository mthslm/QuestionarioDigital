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

    QuestionarioDAO qdao = new QuestionarioDAO();

    public Pessoa getPessoa(int id) {
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        conexao = ConnectionFactory.conector();
        String sql = "select * from tbl_pessoas where idpessoas = " + id;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            Pessoa pessoa = new Pessoa(id, rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(2), qdao.getQuestionario(id));
            rs.close();
            pst.close();
            conexao.close();
            return pessoa;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void pesquisar(String bairro, DefaultTableModel tabela) {
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        tabela.setRowCount(0);
        conexao = ConnectionFactory.conector();
        String sql = "select * from tbl_pessoas where bairro = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, bairro);
            rs = pst.executeQuery();
            while (rs.next()) {
                tabela.addRow(new Object[]{
                    getPessoa(rs.getInt(1)).getNome(),
                    getPessoa(rs.getInt(1)).getRua(),
                    getPessoa(rs.getInt(1)).getNumero(),
                    getPessoa(rs.getInt(1)).getBairro(),
                    getPessoa(rs.getInt(1)).getQuestionario().size(),
                    getPessoa(rs.getInt(1)).getId()
                });
            }
            rs.close();
            pst.close();
            conexao.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void getAllResultados(DefaultTableModel tabela) {
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        tabela.setRowCount(0);
        conexao = ConnectionFactory.conector();
        String sql = "select nome, rua, numero, bairro, count(idperguntas) as Qtd, idpessoas from tbl_pessoas, tbl_perguntas where idpessoas = idperguntas group by idpessoas";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                tabela.addRow(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getInt(5),
                    rs.getInt(6)
                });
            }
            rs.close();
            pst.close();
            conexao.close();
        } catch (Exception e) {
            System.out.println("erro");
        }
    }
    
    public void getResultadosBairro(DefaultTableModel tabela, String bairro) {
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        tabela.setRowCount(0);
        conexao = ConnectionFactory.conector();
        String sql = "select nome, rua, numero, bairro, count(idperguntas) as Qtd, idpessoas from tbl_pessoas, tbl_perguntas where idpessoas = idperguntas and bairro = ? group by idpessoas";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, bairro);
            rs = pst.executeQuery();
            while (rs.next()) {
                tabela.addRow(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getInt(5),
                    rs.getInt(6)
                });
            }
            rs.close();
            pst.close();
            conexao.close();
        } catch (Exception e) {
            System.out.println("erro");
        }
    }

    public void listarTodos(DefaultTableModel tabela) {
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        tabela.setRowCount(0);
        conexao = ConnectionFactory.conector();
        String sql = "select idpessoas from tbl_pessoas";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                tabela.addRow(new Object[]{
                    getPessoa(rs.getInt(1)).getNome(),
                    getPessoa(rs.getInt(1)).getRua(),
                    getPessoa(rs.getInt(1)).getNumero(),
                    getPessoa(rs.getInt(1)).getBairro(),
                    getPessoa(rs.getInt(1)).getQuestionario().size(),
                    getPessoa(rs.getInt(1)).getId()
                });
            }
            rs.close();
            pst.close();
            conexao.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

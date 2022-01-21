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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;
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

    public void pesquisar(DefaultTableModel tabela, JCheckBox ruaCheckBox, JComboBox rua, JCheckBox bairroCheckBox, JComboBox bairro, JTextField numero, JDateChooser data1, JDateChooser data2, JTextField especie, JCheckBox cisterna, JCheckBox cxdagua, JCheckBox poco, JCheckBox fossa, JCheckBox animais){
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        tabela.setRowCount(0);
        conexao = ConnectionFactory.conector();

        String condicao = "";

        if (ruaCheckBox.isSelected()) {
            condicao = condicao + " and rua = " + "'"+rua.getSelectedItem().toString()+"'";
        }
        if (bairroCheckBox.isSelected()) {
            condicao = condicao + " and bairro = " + "'"+bairro.getSelectedItem().toString()+"'";
        }
        if (!numero.getText().equals("")) {
            condicao = condicao + " and numero = " + "'"+numero.getText()+"'";
        }
        if (data1.getDate() != null && data2.getDate() != null) {
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            condicao = condicao + " and datapergunta between '" + dt.format(data1.getDate()) + "' and '" + dt.format(data2.getDate()) + "'";
        }
        if (!especie.getText().equals("")) {
            condicao = condicao + " and especie = '" + especie.getText()+"'";
        }
        if (cisterna.isSelected()) {
            condicao = condicao + " and cisterna = 1";
        }
        if (cxdagua.isSelected()) {
            condicao = condicao + " and cxdagua = 1";
        }
        if (poco.isSelected()) {
            condicao = condicao + " and pcartesiano = 1";
        }
        if (fossa.isSelected()) {
            condicao = condicao + " and fseptica = 1";
        }
        if (animais.isSelected()) {
            condicao = condicao + " and animais = 1";
        }

        String sql = "SELECT nome, rua, numero, bairro, Qtd, idpessoas\n"
                + "FROM tbl_pessoas\n"
                + "INNER JOIN (select *, count(idperguntas) as Qtd from tbl_perguntas group by idperguntas) as perguntas\n"
                + "on idpessoas = idperguntas\n"
                + "LEFT JOIN tbl_animais\n"
                + "ON idpessoas = idanimais\n"
                + "where true\n"
                + condicao+"\n"
                + "group by idpessoas "
                + "order by idpessoas asc";

        try {

            pst = conexao.prepareStatement(sql);
            //pst.setString(1, condicao);
            rs = pst.executeQuery();
            while (rs.next()) {
                tabela.addRow(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6)
                });
            }
            rs.close();
            pst.close();
            conexao.close();
        } catch (Exception e) {
            System.out.println("erro: " + e);
        }
    }

}

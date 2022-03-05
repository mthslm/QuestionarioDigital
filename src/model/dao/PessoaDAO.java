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
import javax.swing.JComboBox;
import javax.swing.JPanel;
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
        String sql = "select idpessoas, nome, rua, bairro, numero, complemento, area from tbl_pessoas where idpessoas = " + id;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            Pessoa pessoa = new Pessoa(id, rs.getString("nome"), rs.getString("rua"), rs.getString("bairro"), rs.getString("numero"), rs.getString("complemento"), rs.getInt("area"), qdao.getQuestionario(id));
            rs.close();
            pst.close();
            conexao.close();
            return pessoa;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void pesquisar(DefaultTableModel tabela, JTextField rua, JTextField bairro, JTextField numero, JDateChooser data1, JDateChooser data2, JTextField especie, JCheckBox cisterna, JCheckBox cxdagua, JCheckBox poco, JCheckBox fossa, JCheckBox animais, JTextField area, JCheckBox castrado, JCheckBox masc, JCheckBox fem, JTextField idade) {
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        tabela.setRowCount(0);
        conexao = ConnectionFactory.conector();

        String condicao = "";

        if (castrado.isSelected()) {
            condicao = condicao + " and castrado = 1";
        }

        if (masc.isSelected()) {
            condicao = condicao + " and sexo = 'Masculino'";
        }

        if (fem.isSelected()) {
            condicao = condicao + " and sexo = 'Feminino'";
        }

        if (!idade.getText().equals("")) {
            condicao = condicao + " and idade = '" + idade.getText() + "'";
        }

        if (!area.getText().equals("")) {
            condicao = condicao + " and area like " + "'%" + area.getText() + "%'";
        }

        if (!rua.getText().equals("")) {
            condicao = condicao + " and rua like " + "'%" + rua.getText() + "%'";
        }
        if (!bairro.getText().equals("")) {
            condicao = condicao + " and bairro like " + "'%" + bairro.getText() + "%'";
        }
        if (!numero.getText().equals("")) {
            condicao = condicao + " and numero = " + "'" + numero.getText() + "'";
        }
        if (data1.getDate() != null && data2.getDate() != null) {
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            condicao = condicao + " and datapergunta between '" + dt.format(data1.getDate()) + "' and '" + dt.format(data2.getDate()) + "'";
        }
        if (!especie.getText().equals("")) {
            condicao = condicao + " and especie like '%" + especie.getText() + "%'";
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
                + "LEFT JOIN (select *, count(idperguntas) as Qtd from tbl_perguntas group by idperguntas) as perguntas\n"
                + "on idpessoas = idperguntas\n"
                + "LEFT JOIN tbl_animais\n"
                + "ON idpessoas = idanimais\n"
                + "where true\n"
                + condicao + "\n"
                + "group by idpessoas "
                + "order by idpessoas asc";

        try {

            pst = conexao.prepareStatement(sql);
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

    public int cadastrarPessoa(JTextField cadastrarRuaCampo, JComboBox cadastrarArea, JPanel campoRua, JTextField cadastrarComplemento, JTextField nome, JComboBox rua, JComboBox bairro, JTextField numero) {
        Connection conexao = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;
        conexao = ConnectionFactory.conector();
        String sql = "INSERT INTO tbl_pessoas (nome,rua,bairro,numero, complemento, area) VALUES (?,?,?,?,?,?)";
        String sql1 = "select idpessoas, rua, bairro, numero, complemento, area from tbl_pessoas where rua = ? and bairro = ? and numero = ? and complemento = ? and area = ?";

        int id = 0;

        try {
            pst = conexao.prepareStatement(sql, pst.RETURN_GENERATED_KEYS);
            pst1 = conexao.prepareStatement(sql1);
            if (campoRua.isShowing()) {
                pst1.setString(1, cadastrarRuaCampo.getText());
            } else {
                pst1.setString(1, rua.getSelectedItem().toString());
            }
            pst1.setString(2, bairro.getSelectedItem().toString());
            pst1.setString(3, numero.getText());
            pst1.setString(4, cadastrarComplemento.getText());
            pst1.setString(5, cadastrarArea.getSelectedItem().toString());
            rs = pst1.executeQuery();
            if (!rs.next()) {
                pst.setString(1, nome.getText());
                if (campoRua.isShowing()) {
                    pst.setString(2, cadastrarRuaCampo.getText());
                } else {
                    pst.setString(2, rua.getSelectedItem().toString());
                }
                pst.setString(3, bairro.getSelectedItem().toString());
                pst.setString(4, numero.getText());
                pst.setString(5, cadastrarComplemento.getText());
                pst.setInt(6, Integer.parseInt(cadastrarArea.getSelectedItem().toString()));
                pst.executeUpdate();
                rs = pst.getGeneratedKeys();
                rs.next();
                id = rs.getInt(1);
            } else {
                id = rs.getInt(1);
            }
            rs.close();
            pst.close();
            conexao.close();
        } catch (SQLException ex) {
            Logger.getLogger(PessoaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public boolean editarPessoa(int id, JTextField cadastrarRuaCampo, JComboBox cadastrarArea, JPanel campoRua, JTextField cadastrarComplemento, JTextField nome, JComboBox rua, JComboBox bairro, JTextField numero) {
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        conexao = ConnectionFactory.conector();
        int idold = 0;

        String sql = "UPDATE tbl_pessoas SET nome=?,rua=?,bairro=?,numero=?, complemento=?, area=? where idpessoas = " + id;

        String sql1 = "select idpessoas from tbl_pessoas inner join tbl_perguntas on idpessoas=idperguntas where rua = ? and bairro = ? and numero = ? and complemento = ? and area = ? group by idpessoas";

        String sql2 = "update tbl_perguntas set idperguntas=? where idperguntas=?";
        String sql3 = "update tbl_animais set idanimais=? where idanimais=?";

        try {
            pst = conexao.prepareStatement(sql1);
            if (campoRua.isShowing()) {
                pst.setString(1, cadastrarRuaCampo.getText());
            } else {
                pst.setString(1, rua.getSelectedItem().toString());
            }
            pst.setString(2, bairro.getSelectedItem().toString());
            pst.setString(3, numero.getText());
            pst.setString(4, cadastrarComplemento.getText());
            pst.setString(5, cadastrarArea.getSelectedItem().toString());
            rs = pst.executeQuery();

            while (rs.next()) {
                if (rs.getInt(1) != id) {
                    idold = rs.getInt(1);
                }
            }

            rs.close();
            pst.close();
            // fim verificação se há alguma pessoa com o msm endereço

            pst = conexao.prepareStatement(sql);
            pst.setString(1, nome.getText());
            if (campoRua.isShowing()) {
                pst.setString(2, cadastrarRuaCampo.getText());
            } else {
                pst.setString(2, rua.getSelectedItem().toString());
            }
            pst.setString(3, bairro.getSelectedItem().toString());
            pst.setString(4, numero.getText());
            pst.setString(5, cadastrarComplemento.getText());
            pst.setInt(6, Integer.parseInt(cadastrarArea.getSelectedItem().toString()));
            pst.executeUpdate();
            pst.close();
            // fim edição

            if (idold != 0) {
                pst = conexao.prepareStatement(sql2);
                pst.setInt(1, id);
                pst.setInt(2, idold);
                pst.executeUpdate();
                pst = conexao.prepareStatement(sql3);
                pst.setInt(1, id);
                pst.setInt(2, idold);
                pst.executeUpdate();
            }

            //fim update no id

            pst.close();
            conexao.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PessoaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean deletarPessoa(int id){
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        conexao = ConnectionFactory.conector();
        
        String sql = "delete from tbl_pessoas where idpessoas=?";
        String sql1= "delete from tbl_perguntas where idperguntas=?";
        String sql2 = "delete from tbl_animais where idanimais=?";
        
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
            
            pst = conexao.prepareStatement(sql1);
            pst.setInt(1, id);
            pst.executeUpdate();
            
            pst = conexao.prepareStatement(sql2);
            pst.setInt(1, id);
            pst.executeUpdate();
            
            pst.close();
            conexao.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PessoaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}

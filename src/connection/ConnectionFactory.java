/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Matheus
 */
public class ConnectionFactory {

    public static Connection conector(){
        java.sql.Connection conexao = null;
        // a linha abaixo chama o driver que importei
        String driver = "com.mysql.cj.jdbc.Driver";
        // armazenando infos referentes ao banco
        
        String url = "jdbc:mysql://localhost:3306/bancosecsaude?useSSL=false";
        String user = "root";
        String password = "oioioi";

        //estabelecendo a conexão com o banco de dados
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (Exception e) {
            System.out.println("erro ao conectar");
            return null;
        }
    }

}

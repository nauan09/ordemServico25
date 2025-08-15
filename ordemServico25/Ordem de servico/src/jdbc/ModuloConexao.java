
package jdbc;

import javax.swing.JOptionPane;
import java.sql.DriverManager;


/**
 *
 * @author GERAL
 */
public class ModuloConexao {
    //criando um método ressponável por estabelecer uma conexão com o banco
    public static java.sql.Connection conectar() {
        //criando um método ressponável por estabelecer uma conexão com o banco

        java.sql.Connection conexao = null;
        //criando o driver  correspondente ao banco
        String driver = "com.mysql.cj.jdbc.Driver";
        //armazenando informações referente ao banco de dados
        String url = "jdbc:mysql://localhost:3306/dbos2025?characterEncoding=utf-8"; //useTimezone=true&serverTimezone=UTC
        String user = "root";
        String senha = "root";
        //estabelecer a conexão com o banco
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, senha);
            JOptionPane.showMessageDialog(null, "Conectado com sucesso!!");
            return conexao;
        } catch (Exception erro) {
            //a lihna abaixo server de apoio para esclarecer o erro
            JOptionPane.showMessageDialog(null, erro);
            return null;
        }
    }  
}
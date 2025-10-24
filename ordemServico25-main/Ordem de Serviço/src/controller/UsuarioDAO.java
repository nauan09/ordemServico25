/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.Color;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import javax.swing.JOptionPane;
import jdbc.ModuloConexao;
import model.Usuario;
import view.TelaLogin;
import view.TelaPrincipal;

/**
 *
 * @author clebe
 */
public class UsuarioDAO {

    private Connection conexao;

    public UsuarioDAO() {
        this.conexao = ModuloConexao.conectar();
    }

    //Metodo efetuaLogin
    public void efetuaLogin(String usuario, String senha) {

        try {

            //1 passo - criar SQL
            String sql = "select * from tbusuarios where login = ? and senha = md5(?)";
            PreparedStatement stmt;
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, usuario);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                //Usuario logou
                String perfil = rs.getString(6);
                if (perfil.equals("admin")) {
                    TelaPrincipal tela = new TelaPrincipal();
                    tela.setVisible(true);
                    tela.jMnItmUsuario.setEnabled(true);
                    tela.jMnRelatorio.setEnabled(true);
                    tela.jLblUsuario.setText(rs.getString(2));
                    tela.jLblUsuario.setForeground(Color.RED);
                } else {
                    TelaPrincipal tela = new TelaPrincipal();
                    tela.setVisible(true);
                    tela.jLblUsuario.setText(rs.getString(2));
                }
            } else {
                //Dados incorretos
                JOptionPane.showMessageDialog(null, "Dados incorretos!");
                new TelaLogin().setVisible(true);
            }

        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro : " + erro);
        }

    }

    /**
     * método responsável por adicionar usuário no banco
     *
     * @param obj
     */
    public void adicionarUsuario(Usuario obj) {

        try {
            //1 passo - criar o sql
            String sql = "insert into tbusuarios(iduser, usuario, fone, login, senha, perfil) values(?,?,?,?,md5(?),?)";
            //2 passo o conectar o banco de dados e organizar o comando sql
            conexao = ModuloConexao.conectar();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, obj.getIdUser());
            stmt.setString(2, obj.getUsuario());
            stmt.setString(3, obj.getFone());
            stmt.setString(4, obj.getLogin());
            stmt.setString(5, obj.getSenha());
            stmt.setString(6, obj.getPerfil());

            //3 passo - executar o comando sql
            stmt.execute();
            //  System.out.println(stmt);
            stmt.close();
            JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!!");

        } catch (SQLIntegrityConstraintViolationException e1) {
            JOptionPane.showMessageDialog(null, "Login em uso.\nEscolha outro login.");

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                conexao.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    /**
     * método responsável por alterar usuário no banco
     *
     * @param obj
     */
    public void alterarUsuario(Usuario obj) {

        try {
            //1 passo - criar o sql
            String sql = "update tbusuarios set  usuario=?, fone=?, login=?, senha=?, perfil=? where iduser=?";
            //2 passo o conectar o banco de dados e organizar o comando sql
            conexao = ModuloConexao.conectar();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setString(1, obj.getUsuario());
            stmt.setString(2, obj.getFone());
            stmt.setString(3, obj.getLogin());
            stmt.setString(4, obj.getSenha());
            stmt.setString(5, obj.getPerfil());
            stmt.setInt(6, obj.getIdUser());

            //3 passo - executar o comando sql
            stmt.execute();
            //  System.out.println(stmt);
            stmt.close();
            JOptionPane.showMessageDialog(null, "Usuário alterado com sucesso!!");

        } catch (SQLIntegrityConstraintViolationException e1) {
            JOptionPane.showMessageDialog(null, "Login em uso.\nEscolha outro login.");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                conexao.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    /**
     * Método que busca o usuáro pelo ID
     *
     * @param idUser do tipo inteiro
     * @return Objeto Usuario(model)
     */
    public Usuario buscarUsuario(int idUser) {
        try {
            //1 passo - criar o sql
            String sql = "select * from tbusuarios WHERE iduser = ?;";

            //2 passo o conectar o banco de dados e organizar o comando sql
            conexao = ModuloConexao.conectar();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idUser);

            //3 passo - executar o comando sql
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUser(rs.getInt("iduser"));
                usuario.setUsuario(rs.getString("usuario"));
                usuario.setFone(rs.getString("fone"));
                usuario.setLogin(rs.getString("login"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setPerfil(rs.getString("perfil"));

                return usuario;

            } else {
                JOptionPane.showMessageDialog(null, "Usuário não encontrado!!");
            }
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return null;
    }

}

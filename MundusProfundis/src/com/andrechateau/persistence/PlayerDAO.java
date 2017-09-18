/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andre Chateaubriand
 */
public class PlayerDAO {

    public static Player getPlayerByUsername(String username) throws SQLException {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "Select * from player where name=? LIMIT 1";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        Player player = null;
        if (rs.next()) {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            String password = rs.getString("password");
            int X = rs.getInt("X");
            int Y = rs.getInt("Y");
            int desiredX = rs.getInt("desiredX");
            int desiredY = rs.getInt("desiredY");
            int HP = rs.getInt("HP");
            char direction = rs.getString("direction").charAt(0);
            String outfit = rs.getString("outfit");
            player = new Player(id, name, password, X, Y, desiredX, desiredY, HP, direction, outfit);
        }
        rs.close();
        stmt.close();
        conn.close();
        return player;
    }

    public static Player getPlayerByLogin(String username, String password) throws SQLException {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "Select * from player where name=? and password=? LIMIT 1";
        Player player = null;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                password = rs.getString("password");
                int X = rs.getInt("X");
                int Y = rs.getInt("Y");
                int desiredX = rs.getInt("desiredX");
                int desiredY = rs.getInt("desiredY");
                int HP = rs.getInt("HP");
                char direction = rs.getString("direction").charAt(0);
                String outfit = rs.getString("outfit");
                player = new Player(id, name, password, X, Y, desiredX, desiredY, HP, direction, outfit);
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        conn.close();
        return player;
    }

    public static void savePlayer(Player player) {
        try (Connection conn = ConnectionFactory.getConnection()) {
            String sqlSender = "Update player set X = ?, Y = ?, desiredX = ?, desiredY = ?, HP = ?, direction=? where id=?";
            PreparedStatement stmtSender = conn.prepareStatement(sqlSender);
            stmtSender.setInt(1, player.getX());
            stmtSender.setInt(2, player.getY());
            stmtSender.setInt(3, player.getDesiredX());
            stmtSender.setInt(4, player.getDesiredY());
            stmtSender.setInt(5, player.getHP());
            stmtSender.setString(6, player.getDirection() + "");
            stmtSender.setLong(7, player.getId());
            stmtSender.executeUpdate();
            stmtSender.close();
        } catch (SQLException ex) {
            Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void registerPlayer(String username, String password) {
        try (Connection conn = ConnectionFactory.getConnection()) {
            String sqlSender = "INSERT INTO player (name,password) values (?,?)";
            PreparedStatement stmtSender = conn.prepareStatement(sqlSender);
            stmtSender.setString(1, username);
            stmtSender.setString(2, password);
            stmtSender.executeUpdate();
            stmtSender.close();
        } catch (SQLException ex) {
            Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

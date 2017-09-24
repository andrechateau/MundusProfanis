/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.persistence;

import com.andrechateau.network.GameClient;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Andre Chateaubriand
 */
public class ConnectionFactory {

    private static boolean registred = false;

    public static Connection getConnection() throws SQLException {
        if (!registred) {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            registred = true;
        }
        //return DriverManager.getConnection("jdbc:mysql://localhost/mundusprofundis", "player", "password");
        return DriverManager.getConnection("jdbc:mysql://" + GameClient.host + "/mundusprofundis", "player", "password");
    }
}

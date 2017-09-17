/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Andre Chateaubriand
 */

public class ConnectionFactory {

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://192.168.0.4/mundusprofundis", "player", "password");
    }
}

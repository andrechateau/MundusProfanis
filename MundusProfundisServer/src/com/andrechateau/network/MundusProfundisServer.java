/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.network;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andre Chateaubriand
 */
public class MundusProfundisServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new PositionServer();
        } catch (IOException ex) {
            Logger.getLogger(MundusProfundisServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

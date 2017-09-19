/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.gui;

import com.andrechateau.network.PositionServer;
import com.andrechateau.network.ServerListener;
import com.andrechateau.persistence.Player;
import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import static java.util.Collections.list;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Andre Chateaubriand
 */
public class MainScreen extends JFrame implements ServerListener {

    JLabel lbTitulo;
    JLabel lbRotulo;
    JButton btIniciar;
    JList listCharacteres;
    PositionServer server;
    Runnable pool;

    public MainScreen() throws HeadlessException {
        super("Tibieau Server");
        lbTitulo = new JLabel("Tibieau Server");
        lbTitulo.setBounds(10, 20, 200, 30);
        lbTitulo.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        lbTitulo.setForeground(Color.white);
        add(lbTitulo);
        lbTitulo = new JLabel("Online Players:");
        lbTitulo.setBounds(10, 45, 200, 30);
        lbTitulo.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
        lbTitulo.setForeground(Color.white);
        add(lbTitulo);
        btIniciar = new JButton("Iniciar");
        btIniciar.setBounds(190, 20, 90, 30);
        btIniciar.setBackground(Color.gray);
        btIniciar.setForeground(Color.green);
        btIniciar.setFocusable(false);
        btIniciar.setBorderPainted(false);
        btIniciar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                iniciar();
            }
        });
        Object[] data = {};
        listCharacteres = new JList(data); //data has type Object[]
        listCharacteres.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        //listCharacteres.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        listCharacteres.setVisibleRowCount(-1);
        listCharacteres.setBackground(Color.gray);
        listCharacteres.setEnabled(false);
        JScrollPane listScroller = new JScrollPane(listCharacteres);
        listScroller.setPreferredSize(new Dimension(280, 300));
        listScroller.setBounds(10, 70, 260, 300);
        add(listScroller);
        add(btIniciar);
        setSize(300, 500);

        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.darkGray);

    }

    public void iniciar() {
        if (btIniciar.getText().equals("Iniciar")) {
            try {
                if (server != null) {
                    server.close();
                    server = null;
                }
                listCharacteres.setEnabled(true);
                server = new PositionServer(this);
                btIniciar.setText("Encerrar");
                btIniciar.setForeground(Color.red);
            } catch (IOException ex) {
                Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (btIniciar.getText().equals("Encerrar")) {
            if (server != null) {
                server.close();
                server = null;
            }
            listCharacteres.setEnabled(false);

            btIniciar.setText("Iniciar");
            btIniciar.setForeground(Color.green);

        }
    }

    @Override
    public void changedLoggedUsers(HashSet<Player> LoggedIn) {
        DefaultListModel model = new DefaultListModel();

        for (Player player : LoggedIn) {
            model.addElement(player.getName());
        }
        listCharacteres.setModel(model);
        //listCharacteres.remo
    }
}

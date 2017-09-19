/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.network;

import com.andrechateau.persistence.Player;
import java.util.HashSet;

/**
 *
 * @author Andre Chateaubriand
 */
public interface ServerListener {
    public void changedLoggedUsers(HashSet<Player> loggedIn);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.ecs.components;

import com.artemis.Component;

/**
 *
 * @author Andre Chateaubriand
 */
public class Enemy extends Component {

    private static long globalID = 1;
    private long id;

    public Enemy() {
        this.id = globalID;
        globalID++;

    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

}

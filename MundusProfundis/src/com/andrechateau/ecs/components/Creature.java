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
public class Creature extends Component {

    private String name;
    private String outfit;
    private int HP;
    private int damage;
    private long lastMeelee;
    private int cdMeelee;

    public Creature() {
        damage = 5;
        lastMeelee = 0;
        cdMeelee = 1000;
    }

    public Creature(String name, String outfit, int HP) {
        this.name = name;
        this.outfit = outfit;
        this.HP = HP;
        damage = 5;
        lastMeelee = 0;
        cdMeelee = 1000;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the outfit
     */
    public String getOutfit() {
        return outfit;
    }

    /**
     * @param outfit the outfit to set
     */
    public void setOutfit(String outfit) {
        this.outfit = outfit;
    }

    /**
     * @return the HP
     */
    public int getHP() {
        return HP;
    }

    /**
     * @param HP the HP to set
     */
    public void setHP(int HP) {
        this.HP = HP;
    }

    /**
     * @return the damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * @param damage the damage to set
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * @return the lastMeelee
     */
    public long getLastMeelee() {
        return lastMeelee;
    }

    /**
     * @param lastMeelee the lastMeelee to set
     */
    public void setLastMeelee(long lastMeelee) {
        this.lastMeelee = lastMeelee;
    }

    /**
     * @return the cdMeelee
     */
    public int getCdMeelee() {
        return cdMeelee;
    }

    /**
     * @param cdMeelee the cdMeelee to set
     */
    public void setCdMeelee(int cdMeelee) {
        this.cdMeelee = cdMeelee;
    }

}

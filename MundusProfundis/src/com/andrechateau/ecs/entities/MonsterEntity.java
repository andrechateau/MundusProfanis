/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.ecs.entities;

import com.andrechateau.ecs.components.ActorSprite;
import com.andrechateau.ecs.components.Creature;
import com.andrechateau.ecs.components.Position;
import com.andrechateau.ecs.components.Velocity;
import com.andrechateau.gamestates.Game;
import com.artemis.Entity;
import com.artemis.World;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Andre Chateaubriand
 */
public class MonsterEntity {

    private static long globalID = 1;
    private long id;
    private Creature creature;
    private Position position;
    private Velocity velocity;
    private ActorSprite actorSprite;
    private Entity entity;

    public MonsterEntity(String name, String outfit, int tileX, int tileY, World world) {
        this.id = globalID;
        globalID++;
        this.creature = new Creature();
        this.creature.setHP(100);
        this.creature.setName(name);
        this.creature.setOutfit(outfit);
        this.position = new Position(tileX, tileY);
        this.position.setDirection('s');
        this.velocity = new Velocity(200, 200);
        this.velocity.setDesiredX(tileX);
        this.velocity.setDesiredY(tileY);
        this.entity = world.createEntity();
        this.entity.addComponent(position);
        this.entity.addComponent(velocity);
        this.entity.addComponent(creature);
        this.actorSprite = new ActorSprite(Game.images.get("res/" + creature.getOutfit() + ".png"));
        this.entity.addComponent(actorSprite);
        this.entity.addToWorld();

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

    /**
     * @return the name
     */
    public String getName() {
        return creature.getName();
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        creature.setName(name);
    }

    /**
     * @return the X
     */
    public int getX() {
        return (int) position.getX();
    }

    /**
     * @param X the X to set
     */
    public void setX(int X) {
        position.setX(X);
    }

    /**
     * @return the Y
     */
    public int getY() {
        return (int) position.getY();
    }

    /**
     * @param Y the Y to set
     */
    public void setY(int Y) {
        position.setY(Y);
    }

    /**
     * @return the desiredX
     */
    public int getDesiredX() {
        return (int) velocity.getDesiredX();
    }

    /**
     * @param desiredX the desiredX to set
     */
    public void setDesiredX(int desiredX) {
        velocity.setDesiredX(desiredX);
    }

    /**
     * @return the desiredY
     */
    public int getDesiredY() {
        return (int) velocity.getDesiredY();
    }

    /**
     * @param desiredY the desiredY to set
     */
    public void setDesiredY(int desiredY) {
        velocity.setDesiredY(desiredY);
    }

    /**
     * @return the HP
     */
    public int getHP() {
        return creature.getHP();
    }

    /**
     * @param HP the HP to set
     */
    public void setHP(int HP) {
        creature.setHP(HP);
    }

    /**
     * @return the direction
     */
    public char getDirection() {
        return position.getDirection();
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(char direction) {
        position.setDirection(direction);
    }

    public Entity getEntity() {
        return entity;
    }

    /**
     * @return the outfit
     */
    public String getOutfit() {
        return creature.getOutfit();
    }

    /**
     * @param outfit the outfit to set
     */
    public void setOutfit(String outfit) {
        creature.setOutfit(outfit);
    }

}

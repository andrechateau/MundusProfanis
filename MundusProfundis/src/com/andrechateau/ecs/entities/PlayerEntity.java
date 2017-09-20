/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.ecs.entities;

import com.andrechateau.ecs.components.ActorSprite;
import com.andrechateau.ecs.components.Creature;
import com.andrechateau.ecs.components.Position;
import com.andrechateau.ecs.components.User;
import com.andrechateau.ecs.components.Velocity;
import com.andrechateau.persistence.Player;
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
public class PlayerEntity {

    private long id;
    private Creature creature;
    private Position position;
    private Velocity velocity;
    private ActorSprite actorSprite;
    private User user;
    private Entity entity;
    private Player player;

    public PlayerEntity(Player player, World world) {
        try {
            this.player = player;
            this.id = id;
            this.user = new User();
            this.creature = new Creature();
            this.creature.setHP(player.getHP());
            this.creature.setName(player.getName());
            this.creature.setOutfit(player.getOutfit());
            this.position = new Position(player.getX(), player.getY());
            this.position.setDirection(player.getDirection());
            this.velocity = new Velocity(200, 200);
            this.velocity.setDesiredX(player.getDesiredX());
            this.velocity.setDesiredY(player.getDesiredY());
            this.actorSprite = new ActorSprite(new Image("res/" + creature.getOutfit() + ".png"));
            this.entity = world.createEntity();
            this.entity.addComponent(position);
            this.entity.addComponent(velocity);
            this.entity.addComponent(creature);
            this.entity.addComponent(actorSprite);
            this.entity.addComponent(user);
            this.entity.addToWorld();
        } catch (SlickException ex) {
            Logger.getLogger(PlayerEntity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public PlayerEntity(World world) {
        try {
            this.player = new Player();
            this.id = id;
            this.user = new User();
            this.creature = new Creature();
            this.creature.setHP(100);
            this.creature.setName("Loading...");
            this.creature.setOutfit("GM");
            this.position = new Position(320, 320);
            this.position.setDirection('s');
            this.velocity = new Velocity(200, 200);
            this.velocity.setDesiredX(320);
            this.velocity.setDesiredY(320);
            this.actorSprite = new ActorSprite(new Image("res/GM.png"));
            this.entity = world.createEntity();
            this.entity.addComponent(position);
            this.entity.addComponent(velocity);
            this.entity.addComponent(creature);
            this.entity.addComponent(actorSprite);
            this.entity.addComponent(user);
            this.entity.addToWorld();
        } catch (SlickException ex) {
            Logger.getLogger(PlayerEntity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setPlayer(Player player) {
        try {
            this.player = player;
            this.id = player.getId();
            this.creature.setHP(player.getHP());
            this.creature.setName(player.getName());
            this.creature.setOutfit(player.getOutfit());
            this.getPosition().setX(player.getX());
            this.getPosition().setY(player.getY());
            this.getPosition().setDirection(player.getDirection());
            //this.velocity = new Velocity(200, 200);
            this.velocity.setDesiredX(player.getDesiredX());
            this.velocity.setDesiredY(player.getDesiredY());
            
            this.actorSprite.setNewActorSprite(new Image("res/" + creature.getOutfit() + ".png"));
//            this.entity = world.createEntity();
//            this.entity.addComponent(position);
//            this.entity.addComponent(velocity);
//            this.entity.addComponent(creature);
//            this.entity.addComponent(actorSprite);
//            this.entity.addComponent(user);
            //this.entity.addToWorld();
        } catch (SlickException ex) {
            Logger.getLogger(PlayerEntity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Entity createEntity(Entity e) {
        try {
            actorSprite = new ActorSprite(new Image("res/" + creature.getOutfit() + ".png"));
            //actorSprite = new ActorSprite(new Image("res/hunter.png"));
        } catch (SlickException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
        e.addComponent(getPosition());
        e.addComponent(velocity);
        e.addComponent(creature);
        e.addComponent(actorSprite);
        Entity entity = e;
        return e;
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
        return (int) getPosition().getX();
    }

    /**
     * @param X the X to set
     */
    public void setX(int X) {
        getPosition().setX(X);
    }

    /**
     * @return the Y
     */
    public int getY() {
        return (int) getPosition().getY();
    }

    /**
     * @param Y the Y to set
     */
    public void setY(int Y) {
        getPosition().setY(Y);
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
        return getPosition().getDirection();
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(char direction) {
        getPosition().setDirection(direction);
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

    public Player getPlayer() {
        return new Player(player.getId(), creature.getName(), player.getPassword(), (int) getPosition().getX(), (int) getPosition().getY(), (int) velocity.getDesiredX(), (int) velocity.getDesiredY(), creature.getHP(), getPosition().getDirection(), creature.getOutfit());
    }

    /**
     * @return the position
     */
    public Position getPosition() {
        return position;
    }
}

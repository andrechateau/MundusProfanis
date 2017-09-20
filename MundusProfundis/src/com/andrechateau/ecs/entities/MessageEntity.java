/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.ecs.entities;

import com.andrechateau.ecs.components.ChatMessage;
import com.andrechateau.ecs.components.Position;
import com.andrechateau.gamestates.Game;
import com.artemis.Entity;

/**
 *
 * @author Andre Chateaubriand
 */
public class MessageEntity {

    private ChatMessage msg;
    private Position position;
    private Entity e;

    public MessageEntity(CharacterEntity player, String msg) {
        this.msg = new ChatMessage(msg, player.getName());
        this.position = player.getPosition();
        Entity e = Game.world.createEntity();
        e.addComponent(position);
        e.addComponent(this.msg);
        e.addToWorld();
    }

    /**
     * @return the msg
     */
    public ChatMessage getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(ChatMessage msg) {
        this.msg = msg;
    }

    /**
     * @return the position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Position position) {
        this.position = position;
    }
}

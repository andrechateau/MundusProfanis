/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.ecs.systems;

import com.andrechateau.ecs.components.ActorSprite;
import com.andrechateau.ecs.components.Position;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;

/**
 *
 * @author Andre Chateaubriand
 */
public class ActorFramerSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<ActorSprite> as;

    public ActorFramerSystem() {
        super(Aspect.getAspectForAll(ActorSprite.class));

    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    @Override
    protected void process(Entity entity) {
       as.get(entity).update(world.getDelta());
    }

}

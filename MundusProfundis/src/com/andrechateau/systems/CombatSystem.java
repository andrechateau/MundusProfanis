/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.systems;

import com.andrechateau.components.ActorSprite;
import com.andrechateau.components.Creature;
import com.andrechateau.components.Effect;
import com.andrechateau.components.Enemy;
import com.andrechateau.components.Position;
import com.andrechateau.components.Velocity;
import com.andrechateau.core.Game;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import java.awt.Point;
import java.util.HashMap;
import java.util.Random;
import org.newdawn.slick.Color;

/**
 *
 * @author Andre Chateaubriand
 */
public class CombatSystem extends EntitySystem {

    @Mapper
    ComponentMapper<Position> pm;
    @Mapper
    ComponentMapper<Enemy> em;
    @Mapper
    ComponentMapper<Creature> cm;

    @SuppressWarnings("unchecked")
    public CombatSystem() {
        super(Aspect.getAspectForAll(Position.class, Creature.class));
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        HashMap<Point, Entity> creatures = new HashMap<>();
        for (Entity entity : entities) {
            Position p = pm.get(entity);
            creatures.put(new Point((int) p.getX() / 32, (int) p.getY() / 32), entity);
        }
        for (Entity entity : entities) {
            Position p = pm.get(entity);
            Point pos = new Point((int) p.getX() / 32, (int) p.getY() / 32);
            Entity e = null;
            if (creatures.containsKey(new Point(pos.x + 1, pos.y))) {
                e = creatures.get((new Point(pos.x + 1, pos.y)));
            } else if (creatures.containsKey(new Point(pos.x - 1, pos.y))) {
                e = creatures.get(new Point(pos.x - 1, pos.y));
            } else if (creatures.containsKey(new Point(pos.x, pos.y + 1))) {
                e = creatures.get(new Point(pos.x, pos.y + 1));
            } else if (creatures.containsKey(new Point(pos.x, pos.y - 1))) {
                e = creatures.get(new Point(pos.x, pos.y - 1));
            } else if (creatures.containsKey(new Point(pos.x + 1, pos.y + 1))) {
                e = creatures.get(new Point(pos.x + 1, pos.y + 1));
            } else if (creatures.containsKey(new Point(pos.x - 1, pos.y - 1))) {
                e = creatures.get(new Point(pos.x - 1, pos.y - 1));
            } else if (creatures.containsKey(new Point(pos.x + 1, pos.y - 1))) {
                e = creatures.get(new Point(pos.x + 1, pos.y - 1));
            } else if (creatures.containsKey(new Point(pos.x - 1, pos.y + 1))) {
                e = creatures.get(new Point(pos.x - 1, pos.y + 1));
            }
            if (e != null) {
                if (em.getSafe(entity) != null) {
                    if (em.getSafe(e) == null) {
                        attack(entity, e);
                    }
                } else if (em.getSafe(e) != null) {
                    attack(entity, e);
                }
            }
        }
    }

    private void attack(Entity attacker, Entity attacked) {
        Creature atkr = cm.get(attacker);
        Position pos = pm.get(attacked);
        Creature atkd = cm.get(attacked);
        if (System.currentTimeMillis() >= atkr.getLastMeelee() + atkr.getCdMeelee()) {
            int damage = atkr.getDamage() + (new Random().nextInt(4) - 2);
            atkd.setHP(atkd.getHP() - damage);
            atkr.setLastMeelee(System.currentTimeMillis());
            Entity fx = world.createEntity();
            fx.addComponent(new Position(pos.getX(), pos.getY()));
            fx.addComponent(new Effect("hit"));
            fx.addToWorld();
            fx = world.createEntity();
            fx.addComponent(new Position(pos.getX(), pos.getY()));
            fx.addComponent(new Effect(damage, Color.red));
            fx.addToWorld();
        }
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

}

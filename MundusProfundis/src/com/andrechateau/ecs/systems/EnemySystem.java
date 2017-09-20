/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.ecs.systems;

import com.andrechateau.ecs.components.ActorSprite;
import com.andrechateau.ecs.components.Creature;
import com.andrechateau.ecs.components.Enemy;
import com.andrechateau.ecs.components.Position;
import com.andrechateau.ecs.components.Velocity;
import com.andrechateau.gamestates.Game;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import java.util.Random;

/**
 *
 * @author Andre Chateaubriand
 */
public class EnemySystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<Position> pm;

    @Mapper
    ComponentMapper<Velocity> vm;

    @Mapper
    ComponentMapper<ActorSprite> as;

    @Mapper
    ComponentMapper<Enemy> em;
    @Mapper
    ComponentMapper<Creature> cm;

    @SuppressWarnings("unchecked")
    public EnemySystem() {
        super(Aspect.getAspectForAll(Position.class, Velocity.class, Creature.class, Enemy.class));
    }

    @Override
    protected void process(Entity e) {
        Position position = pm.get(e);
        if ((Math.abs(Game.player.getX() - position.getX()) <= 32) && (Math.abs(Game.player.getY() - position.getY()) <= 32)) {
            attackPlayer(e);
        } else {
            enemyMovement(e);
        }
        Creature creature = cm.get(e);
        if (creature.getHP() <= 0) {
            e.deleteFromWorld();
            
        }

    }

    private void attackPlayer(Entity e) {
//        Creature enemy = cm.get(e);
//        if (System.currentTimeMillis() >= enemy.getLastMeelee() + enemy.getCdMeelee()) {
//            System.out.println(System.currentTimeMillis() - (enemy.getLastMeelee() + enemy.getCdMeelee()));
//            int damage = enemy.getDamage() + (new Random().nextInt(4) - 2);
//            Game.player.setHP(Game.player.getHP() - damage);
//            enemy.setLastMeelee(System.currentTimeMillis());
//            Entity fx = world.createEntity();
//            fx.addComponent(new Position(Game.player.getX(), Game.player.getY()));
//            fx.addComponent(new Effect("hit"));
//            fx.addToWorld();
//            fx = world.createEntity();
//            fx.addComponent(new Position(Game.player.getX(), Game.player.getY()));
//            fx.addComponent(new Effect(damage, Color.red));
//            fx.addToWorld();
//        }

    }

    private void enemyMovement(Entity e) {
        Position position = pm.get(e);
        if (position.getX() % 32 == 0 && position.getY() % 32 == 0) {
            boolean anm = false;
            int dir = new Random().nextInt(6);
            if ((position.getX() > Game.player.getX() - 160 && position.getX() < Game.player.getX() + 160) && (position.getY() > Game.player.getY() - 160 && position.getY() < Game.player.getY() + 160)) {
                if (Math.abs(Game.player.getX() - position.getX()) >= Math.abs(Game.player.getY() - position.getY())) {
                    dir = position.getX() > Game.player.getX() ? 2 : 4;
                } else {
                    dir = position.getY() > Game.player.getY() ? 1 : 3;
                }
            }
            if (dir > 0 && dir < 5) {
                moveTo(e, dir);
            } else {
                updateAnimation(e, false);
            }

        }
    }

    private void moveTo(Entity e, int direction) {
        Position position = pm.get(e);
        Velocity velocity = vm.get(e);

        if (direction == 1) {
            position.setDirection('w');
            if (!Game.map.isBlocked(Game.toTile(position.getX()), Game.toTile(position.getY()) - 1)) {
                velocity.setDesiredY(position.getY() - 32);
                position.addY(-velocity.getY() * world.getDelta());
            }
        } else if (direction == 2) {
            position.setDirection('a');
            if (!Game.map.isBlocked(Game.toTile(position.getX()) - 1, Game.toTile(position.getY()))) {
                velocity.setDesiredX(position.getX() - 32);
                position.addX(-velocity.getX() * world.getDelta());
            }
        } else if (direction == 3) {
            position.setDirection('s');
            if (!Game.map.isBlocked(Game.toTile(position.getX()), Game.toTile(position.getY()) + 1)) {
                velocity.setDesiredY(position.getY() + 32);
                position.addY(velocity.getY() * world.getDelta());
            }
        } else if (direction == 4) {
            position.setDirection('d');
            if (!Game.map.isBlocked(Game.toTile(position.getX()) + 1, Game.toTile(position.getY()))) {
                velocity.setDesiredX(position.getX() + 32);
                position.addX(velocity.getX() * world.getDelta());
            }
        }
        updateAnimation(e, true);
    }

    private void updateAnimation(Entity e, boolean animating) {
        ActorSprite actor = as.getSafe(e);
        if (actor != null) {
            if (actor.isAnimating()) {
                if (animating) {
                    actor.update((int) world.delta);
                } else {
                    actor.stop();
                }
            } else if (animating) {
                actor.animate();
            } else {
                actor.update((int) world.delta);
            }
        }
    }

}

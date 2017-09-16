/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.systems;

import com.andrechateau.components.ActorSprite;
import com.andrechateau.components.Creature;
import com.andrechateau.components.Effect;
import com.andrechateau.components.Position;
import com.andrechateau.components.Velocity;
import com.andrechateau.core.Game;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;

/**
 *
 * @author Andre Chateaubriand
 */
public class ActionSystem extends EntityProcessingSystem {

//    private Creature creature;
//    private Position position;
//    private Velocity velocity;
//    private ActorSprite actorSprite;
    @Mapper
    ComponentMapper<Position> pm;

    @Mapper
    ComponentMapper<Velocity> vm;

    @Mapper
    ComponentMapper<ActorSprite> as;

    @Mapper
    ComponentMapper<Creature> cr;

    @SuppressWarnings("unchecked")
    public ActionSystem() {
        super(Aspect.getAspectForAll(Position.class, Velocity.class, ActorSprite.class, Creature.class));
    }

    @Override
    protected void process(Entity e) {
        if (e.equals(Game.player.getEntity())) {
            playerAction(e);
        }

    }

    private void playerAction(Entity e) {
        playerMovement();
        if (Game.gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            playerInteraction(e);
        }
        checkAlive();
    }

    private void playerInteraction(Entity e) {
        Position playerpos = pm.get(e);
        int tileX = (int) (Game.gc.getInput().getMouseX() / 32 + (playerpos.getX() / 32) - (Game.gc.getWidth() / 64));
        int tileY = (int) (Game.gc.getInput().getMouseY() / 32 + (playerpos.getY() / 32) - (Game.gc.getHeight() / 64));
        int tileid = 0;
        int l;
        for (l = Game.map.getLayerCount() - 1; l >= 0; l--) {
            tileid = Game.map.getTileId(l == 2 ? tileX - 1 : tileX, tileY, l);
            if (tileid != 0) {
                break;
            }
        }
        if (Game.gc.getInput().isKeyDown(Input.KEY_LCONTROL)) {
            switch (tileid) {
                case 387:
                    Game.map.setTileId(l == 2 ? tileX - 1 : tileX, tileY, l, 388);
                    break;
                case 388:
                    Game.map.setTileId(l == 2 ? tileX - 1 : tileX, tileY, l, 387);
                    break;
                case 385:
                    Game.map.setTileId(l == 2 ? tileX - 1 : tileX, tileY, l, 386);
                    break;
                case 386:
                    Game.map.setTileId(l == 2 ? tileX - 1 : tileX, tileY, l, 385);
                    break;
            }
            //System.out.println(tileid + " " + l);
        } else if (Game.gc.getInput().isKeyDown(Input.KEY_LALT)) {
            if (l == 0) {
                if (Game.player.getHP() > 0) {
                    Game.player.setHP(Game.player.getHP() - 10);
                }
            } else {
                Game.player.setHP(Game.player.getHP() + 10);
                if (Game.player.getHP() > 100) {
                    Game.player.setHP(100);
                }
            }
        } else if (Game.gc.getInput().isKeyDown(Input.KEY_LSHIFT)) {
            Entity effectentity = world.createEntity();
            effectentity.addComponent(new Position(tileX * 32, tileY * 32));
            effectentity.addComponent(new Effect("hit"));
            effectentity.addComponent(new Effect(10, Color.red));
            effectentity.addToWorld();

        }
    }

    private void playerMovement() {
        Position position = pm.get(Game.player.getEntity());
        if (position.getX() % 32 == 0 && position.getY() % 32 == 0) {
            boolean anm = false;

            Velocity velocity = vm.get(Game.player.getEntity());
            boolean ctrl = Game.gc.getInput().isKeyDown(Input.KEY_LCONTROL);
            if (Game.gc.getInput().isKeyDown(Input.KEY_W) || Game.gc.getInput().isKeyDown(Input.KEY_UP)) {
                position.setDirection('w');
                if (!Game.map.isBlocked(Game.toTile(position.getX()), Game.toTile(position.getY()) - 1) && !ctrl) {
                    velocity.setDesiredY(position.getY() - 32);
                    position.addY(-velocity.getY() * world.getDelta());
                    anm = true;
                }
            } else if (Game.gc.getInput().isKeyDown(Input.KEY_A) || Game.gc.getInput().isKeyDown(Input.KEY_LEFT)) {
                position.setDirection('a');
                if (!Game.map.isBlocked(Game.toTile(position.getX()) - 1, Game.toTile(position.getY())) && !ctrl) {
                    velocity.setDesiredX(position.getX() - 32);
                    position.addX(-velocity.getX() * world.getDelta());
                    anm = true;
                }
            } else if (Game.gc.getInput().isKeyDown(Input.KEY_S) || Game.gc.getInput().isKeyDown(Input.KEY_DOWN)) {
                position.setDirection('s');
                if (!Game.map.isBlocked(Game.toTile(position.getX()), Game.toTile(position.getY()) + 1) && !ctrl) {
                    velocity.setDesiredY(position.getY() + 32);
                    position.addY(velocity.getY() * world.getDelta());
                    anm = true;
                }
            } else if (Game.gc.getInput().isKeyDown(Input.KEY_D) || Game.gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
                position.setDirection('d');
                if (!Game.map.isBlocked(Game.toTile(position.getX()) + 1, Game.toTile(position.getY())) && !ctrl) {
                    velocity.setDesiredX(position.getX() + 32);
                    position.addX(velocity.getX() * world.getDelta());

                    anm = true;
                }
            }
            updateAnimation(anm);
        }
    }

    private void updateAnimation(boolean animating) {
        ActorSprite actor = as.getSafe(Game.player.getEntity());
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

    private void checkAlive() {
        if (Game.player.getHP() <= 0) {
            Game.player.setHP(100);
            Game.player.setX(832);
            Game.player.setY(1248);
            Game.player.setDesiredX(832);
            Game.player.setDesiredY(1248);
        }
    }
}

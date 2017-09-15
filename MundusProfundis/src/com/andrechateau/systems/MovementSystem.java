package com.andrechateau.systems;

import com.andrechateau.components.ActorSprite;
import com.andrechateau.components.Position;
import com.andrechateau.components.Velocity;
import com.andrechateau.core.Game;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import org.newdawn.slick.Input;

public class MovementSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<Position> pm;

    @Mapper
    ComponentMapper<Velocity> vm;

    @Mapper
    ComponentMapper<ActorSprite> as;

    @SuppressWarnings("unchecked")
    public MovementSystem() {
        super(Aspect.getAspectForAll(Position.class, Velocity.class));
    }

    protected void process(Entity e) {
        Position position = pm.get(e);
        Velocity velocity = vm.get(e);
        if (position.getX() % 32 == 0 && position.getY() % 32 == 0) {
            updateAnimation(e, false);
            if (Game.gc.getInput().isKeyDown(Input.KEY_W)) {
                if (!Game.map.isBlocked(Game.toTile(position.getX()), Game.toTile(position.getY()) - 1)) {
                    position.setDirection('w');
                    velocity.setDesiredY(position.getY() - 32);
                    position.addY(-velocity.getY() * world.getDelta());
                }
            } else if (Game.gc.getInput().isKeyDown(Input.KEY_A)) {
                if (!Game.map.isBlocked(Game.toTile(position.getX()) - 1, Game.toTile(position.getY()))) {
                    position.setDirection('a');
                    velocity.setDesiredX(position.getX() - 32);
                    position.addX(-velocity.getX() * world.getDelta());
                }
            } else if (Game.gc.getInput().isKeyDown(Input.KEY_S)) {
                if (!Game.map.isBlocked(Game.toTile(position.getX()), Game.toTile(position.getY()) + 1)) {
                    position.setDirection('s');
                    velocity.setDesiredY(position.getY() + 32);
                    position.addY(velocity.getY() * world.getDelta());
                }
            } else if (Game.gc.getInput().isKeyDown(Input.KEY_D)) {
                if (!Game.map.isBlocked(Game.toTile(position.getX()) + 1, Game.toTile(position.getY()))) {
                    position.setDirection('d');
                    velocity.setDesiredX(position.getX() + 32);
                    position.addX(velocity.getX() * world.getDelta());
                }
            }
        } else {
            updateAnimation(e, true);
            switch (position.getDirection()) {
                case 'w':
                    if (position.getY() > velocity.getDesiredY()) {
                        position.addY(-velocity.getY() * world.getDelta());
                    } else {
                        position.setY(velocity.getDesiredY());
                    }
                    break;
                case 's':
                    if (position.getY() < velocity.getDesiredY()) {
                        position.addY(velocity.getY() * world.getDelta());
                    } else {
                        position.setY(velocity.getDesiredY());
                    }
                    break;
                case 'a':
                    if (position.getX() > velocity.getDesiredX()) {
                        position.addX(-velocity.getX() * world.getDelta());
                    } else {
                        position.setX(velocity.getDesiredX());
                    }
                    break;
                case 'd':
                    if (position.getX() < velocity.getDesiredX()) {
                        position.addX(velocity.getY() * world.getDelta());
                    } else {
                        position.setX(velocity.getDesiredX());
                    }
                    break;
            }
        }
        if (Game.gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
            System.out.println(Game.map.getTileId(Game.toTile(position.getX()), Game.toTile(position.getY()), 1));

        }
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

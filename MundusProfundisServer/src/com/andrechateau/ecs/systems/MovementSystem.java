package com.andrechateau.ecs.systems;

import com.andrechateau.ecs.components.Position;
import com.andrechateau.ecs.components.Velocity;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;

public class MovementSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<Position> pm;

    @Mapper
    ComponentMapper<Velocity> vm;

    @SuppressWarnings("unchecked")
    public MovementSystem() {
        super(Aspect.getAspectForAll(Position.class, Velocity.class));
    }

    protected void process(Entity e) {
        Position position = pm.get(e);
        Velocity velocity = vm.get(e);
        if (velocity.getDesiredX() < 0) {
            velocity.setDesiredX(position.getX());
        }
        if (velocity.getDesiredY() < 0) {
            velocity.setDesiredY(position.getY());
        }
        if (position.getX() % 32 == 0 && position.getY() % 32 == 0) {
        } else {
//            System.out.print(position.getY() + " " + velocity.getDesiredY() + " " + position.getDirection() + "   ");
//            System.out.println(position.getX() + " " + velocity.getDesiredX());
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
                        position.addX(velocity.getX() * world.getDelta());
                    } else {
                        position.setX(velocity.getDesiredX());
                    }
                    break;
            }
        }
    }

}

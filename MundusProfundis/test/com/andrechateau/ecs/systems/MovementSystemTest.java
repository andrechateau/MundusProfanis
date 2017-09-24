/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.ecs.systems;

import com.andrechateau.ecs.components.Position;
import com.andrechateau.ecs.components.Velocity;
import com.artemis.Entity;
import com.artemis.World;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Andre Chateaubriand
 */
public class MovementSystemTest {

    World world;
    Entity entity;
    Position p;
    Velocity v;

    public MovementSystemTest() {
    }

    @Before
    public void setUp() {
        world = new World();
        world.setSystem(new MovementSystem());
        world.initialize();
        entity = world.createEntity();
        p = new Position(320, 320);
        v = new Velocity(200, 200);
        v.setDesiredX(p.getX());
        v.setDesiredY(p.getY());
        entity.addComponent(p);
        entity.addComponent(v);
        entity.addToWorld();
    }

    @After
    public void reset() {
        p.setX(320);
        p.setY(320);
        v.setDesiredX(p.getX());
        v.setDesiredY(p.getY());

    }

    @Test
    public void testMoveNorth() {
        System.out.println("Move North (w)");
        v.setDesiredY(p.getY() - 32);
        p.setY(p.getY() - 1);
        p.setDirection('w');
        world.setDelta(17);
        world.process();
        boolean walked = p.getY() < 320 && p.getX() == 320;
        assertTrue(walked);

    }

    @Test
    public void testMoveSouth() {
        System.out.println("Move South (s)");
        v.setDesiredY(p.getY() + 32);
        p.setY(p.getY() + 1);
        p.setDirection('w');
        world.setDelta(17);
        world.process();
        boolean walked = p.getY() > 320 && p.getX() == 320;
        assertTrue(walked);

    }

    @Test
    public void testMoveWest() {
        System.out.println("Move North (a)");
        v.setDesiredX(p.getX() - 32);
        p.setX(p.getX() - 1);
        p.setDirection('a');
        world.setDelta(17);
        world.process();
        boolean walked = p.getX() < 320 && p.getY() == 320;
        assertTrue(walked);

    }

    @Test
    public void testMoveEast() {
        System.out.println("Move South (d)");
        v.setDesiredX(p.getX() + 32);
        p.setX(p.getX() + 1);
        p.setDirection('d');
        world.setDelta(17);
        world.process();
        boolean walked = p.getX() > 320 && p.getY() == 320;
        assertTrue(walked);

    }
}

package com.andrechateau.core;

import com.andrechateau.components.ActorSprite;
import com.andrechateau.systems.DebugPointRenderer;
import com.andrechateau.systems.MovementSystem;
import com.andrechateau.components.Position;
import com.andrechateau.components.Velocity;
import com.andrechateau.engine.GameMap;
import com.andrechateau.systems.ActorFramerSystem;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.artemis.Entity;
import com.artemis.World;
import org.newdawn.slick.Image;

public class Game extends BasicGame {

    private World world;

    public static GameContainer gc;
    public static GameMap map;
    public static GameMap ground;

    public Game() throws SlickException {
        super("Coding Quick Tips");
    }

    public void init(GameContainer gc) throws SlickException {
        map = new GameMap("res/teste22.tmx");
        world = new World();
        DebugPointRenderer renderer = new DebugPointRenderer();
        map.setRenderer(renderer);
        world.setSystem(renderer);
        world.setSystem(new MovementSystem());
        world.setSystem(new ActorFramerSystem());
        world.initialize();
        Entity e = world.createEntity();
        e.addComponent(new Position(1184, 1568));
        e.addComponent(new Velocity(200, 200));
        e.addComponent(new ActorSprite(new Image("res/GM.png")));
        e.addToWorld();
        e = world.createEntity();
        e.addComponent(new Position(0, 0));
        e.addComponent(new ActorSprite(new Image("res/GM.png")));
        //e.addComponent( new Velocity(100,20) );
        e.addToWorld();
    }

    public void render(GameContainer gc, Graphics g) throws SlickException {
        drawDebugLines(g, 50);

        Game.gc = gc;
        world.process();
    }

    public void update(GameContainer gc, int delta) throws SlickException {
        world.setDelta((delta / 1000.0f));

    }

    // Draw a grid on the screen for easy positioning 
    public void drawDebugLines(Graphics g, int size) {
        int resolution = 2000;
        g.setColor(Color.darkGray);
        for (int i = 0; i < resolution; i += size) {
            g.drawLine(i, 0, i, resolution);
            g.drawLine(0, i, resolution, i);
        }
    }

    public static int toTile(float coordinate) {
        return (int) coordinate / 32;
    }
}

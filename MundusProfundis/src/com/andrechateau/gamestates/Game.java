package com.andrechateau.gamestates;

import com.andrechateau.components.ActorSprite;
import com.andrechateau.components.Creature;
import com.andrechateau.components.Enemy;
import com.andrechateau.systems.RendererSystem;
import com.andrechateau.systems.MovementSystem;
import com.andrechateau.components.Position;
import com.andrechateau.components.Velocity;
import com.andrechateau.engine.GameMap;
import com.andrechateau.entities.PlayerEntity;
import com.andrechateau.persistence.Player;
import com.andrechateau.systems.ActionSystem;
import com.andrechateau.systems.ActorFramerSystem;
import com.andrechateau.systems.CombatSystem;
import com.andrechateau.systems.EffectSystem;
import com.andrechateau.systems.EnemySystem;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.artemis.Entity;
import com.artemis.World;
import com.andrechateau.network.GameClient;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends BasicGameState {

    public static World world;
    /**
     * The ID given to this state
     */
    public static final int ID = 3;
    public static Player play;
    public static PlayerEntity player;
    public static GameContainer gc;
    public static GameMap map;
    public static GameMap ground;
    public static GameClient client;
    private static boolean clientConnected = false;

    public Game() {
        //super("Mundus Profundis");
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        map = new GameMap("res/teste22.tmx");
        world = new World();
        RendererSystem renderer = new RendererSystem();
        map.setRenderer(renderer);
        world.setSystem(renderer);
        world.setSystem(new MovementSystem());
        world.setSystem(new ActorFramerSystem());
        world.setSystem(new ActionSystem());
        world.setSystem(new EnemySystem());
        world.setSystem(new EffectSystem());
        world.setSystem(new CombatSystem());
        world.initialize();
        player = new PlayerEntity(world);

        //new PlayerEntity(new Player(14, "jao", "12", 320, 320, 320, 320, 100, 's', "hunter"), world);
        Entity e = world.createEntity();
        e = world.createEntity();
        e.addComponent(new Position(640, 640));
        e.addComponent(new ActorSprite(new Image("res/troll.png")));
        e.addComponent(new Velocity(70, 70));
        e.addComponent(new Creature("Goblinho", "troll", 100));
        e.addComponent(new Enemy());
        e.addToWorld();
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        drawDebugLines(g, 50);
        Game.gc = gc;
        world.process();
        if (Game.client == null || !Game.client.isConnected()) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
            g.setColor(Color.black);
            String msg = "Connecting to server...";
            int x = gc.getWidth() / 2 - g.getFont().getWidth(msg) / 2;
            int y = gc.getHeight() / 2 - g.getFont().getHeight(msg) / 2;
            g.fillRect(x - 2, y - 2, g.getFont().getWidth(msg) + 4, g.getFont().getHeight(msg) + 4);
            g.setColor(Color.white);
            g.drawString(msg, x, y);
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        if (!clientConnected) {
            clientConnected = true;
            Game.client = new GameClient(Game.play.getName());
        } else {
            world.setDelta((delta / 1000.0f));
            Game.client.clientUpdate(player.getPlayer());

        }
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
//
//    @Override
//    public boolean closeRequested() {
//        // PlayerDAO.savePlayer(player.getPlayer());
//        Game.client.close();
//        System.exit(0);
////super.closeRequested();
//        return true;//To change body of generated methods, choose Tools | Templates.
//    }

    public static int toTile(float coordinate) {
        return (int) coordinate / 32;
    }

    @Override
    public int getID() {
        return ID;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.core;

import com.andrechateau.ecs.entities.MonsterEntity;
import com.andrechateau.ecs.systems.CombatSystem;
import com.andrechateau.ecs.systems.EnemySystem;
import com.andrechateau.ecs.systems.MovementSystem;
import com.andrechateau.network.PositionServer;
import com.artemis.World;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andre Chateaubriand
 */
public class GameLoop implements Runnable {

    /**
     * @return the server
     */
    public static PositionServer getServer() {
        return server;
    }

    /**
     * @param aServer the server to set
     */
    public static void setServer(PositionServer aServer) {
        server = aServer;
    }

    private boolean gameRunning;
    private Thread thread;
    private World world;
    private static GameLoop gameloop;
    private Set<MonsterEntity> monsters = Collections.synchronizedSet(new HashSet());
    private static PositionServer server;
    private boolean map[][];

    private GameLoop() {
        loadMap();
    }

    public static void start() {
        if (gameloop != null) {
            stop();
        }

        gameloop.gameloop = new GameLoop();
        if (gameloop.world != null) {
            gameloop.world.dispose();
        }
        gameloop.world = new World();
//        world.setSystem(new MovementSystem());
//        world.setSystem(new ActorFramerSystem());
        gameloop.world.setSystem(new CombatSystem());
        gameloop.world.setSystem(new EnemySystem());
        gameloop.world.setSystem(new MovementSystem());

//        world.setSystem(new EffectSystem());
//        world.setSystem(new CombatSystem());
        gameloop.world.initialize();
        gameloop.gameRunning = true;
        gameloop.thread = new Thread(gameloop);
        gameloop.thread.start();

    }

    public static void stop() {
        if (gameloop != null) {
            if (gameloop.gameRunning) {
                gameloop.gameRunning = false;
            }

            if (gameloop.thread != null) {
                gameloop.thread = null;
            }
            if (gameloop.world != null) {
                gameloop.world.dispose();
                gameloop.world = null;
            }
            if (gameloop != null) {
                gameloop = null;
            }
        }
    }

    private void doGameUpdates(float delta) {
        if (world != null) {
            world.setDelta((delta / 1000.0f));
            world.process();
        }
    }

    public static void addMonster(String name, String outfit, int tileX, int tileY) {
        if (gameloop.world != null) {
            MonsterEntity m = new MonsterEntity(name, outfit, tileX, tileY, gameloop.world);
            gameloop.monsters.add(m);
            server.newMonster(m);
        }
    }

    public static void updateMonster(MonsterEntity m) {
        server.updateMonster(m);
    }

    public static void removeMonster(MonsterEntity m) {
        if (gameloop.world != null) {
            m.getEntity().deleteFromWorld();
            gameloop.monsters.remove(m);
            server.removeMonster(m);
        }
    }

    public static Set<MonsterEntity> getMonsters() {
        if (gameloop.world != null) {
            return gameloop.monsters;

        } else {
            return new HashSet<MonsterEntity>();
        }
    }

    @Override
    public void run() {
        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

        // keep looping round til the game ends
        while (gameRunning) {
            // work out how long its been since the last update, this
            // will be used to calculate how far the entities should
            // move this loop
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            float delta = updateLength / ((float) OPTIMAL_TIME);
            // update the game logic
            doGameUpdates(delta);

            long timeout = (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
            timeout = timeout >= 0 ? timeout : 0;
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameLoop.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void loadMap() {
        try (BufferedReader txtReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/map/map.txt")))) {
            ArrayList<String> list = new ArrayList<>();
            String line = txtReader.readLine();
            while (line != null) {
                list.add(line);
                line = txtReader.readLine();
            }
            int height = list.size();
            int width = list.get(0).length();
            map = new boolean[width][height];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    map[x][y] = list.get(y).charAt(x) != '0';
                    if (x == 20 && y == 20) {
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GameLoop.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean isBlocked(int X, int Y) {
        if (X >= 0 && X < gameloop.map.length && Y >= 0 && Y < gameloop.map[0].length) {
            return gameloop.map[Y][X];
        } else {
            return false;
        }
    }
}

//    public static void main(String[] args) {
//        GameLoop.start();
//    }


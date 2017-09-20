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

    private GameLoop() {
        ;
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
            //System.out.println(delta);
        }
//   for (int i = 0; i < stuff.size(); i++){
//      // all time-related values must be multiplied by delta!
//      Stuff s = stuff.get(i);
//      s.velocity += Gravity.VELOCITY * delta;
//      s.position += s.velocity * delta;
//      
//      // stuff that isn't time-related doesn't care about delta...
//      if (s.velocity >= 1000){
//         s.color = Color.RED;
//      }else{
//         s.color = Color.BLUE;
//      }
//   }
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
            try {
                Thread.sleep((lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameLoop.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


//    public static void main(String[] args) {
//        GameLoop.start();
//    }
}

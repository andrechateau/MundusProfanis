/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.ecs.systems;

/**
 *
 * @author Andre Chateaubriand
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.andrechateau.core.GameLoop;
import com.andrechateau.ecs.components.Creature;
import com.andrechateau.ecs.components.Enemy;
import com.andrechateau.ecs.components.Position;
import com.andrechateau.ecs.components.Velocity;
import com.andrechateau.ecs.entities.MonsterEntity;
import com.andrechateau.persistence.Player;
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
    ComponentMapper<Enemy> em;
    @Mapper
    ComponentMapper<Creature> cm;

    @SuppressWarnings("unchecked")
    public EnemySystem() {
        super(Aspect.getAspectForAll(Position.class, Velocity.class, Creature.class, Enemy.class));
    }

    @Override
    protected void process(Entity e) {
//        Position position = pm.get(e);
        enemyMovement(e);

//        if ((Math.abs(Game.player.getX() - position.getX()) <= 32) && (Math.abs(Game.player.getY() - position.getY()) <= 32)) {
//            attackPlayer(e);
//        } else {
//            enemyMovement(e);
//        }
//        Creature creature = cm.get(e);
//        if (creature.getHP() <= 0) {
//            for (MonsterEntity monster : GameLoop.getMonsters()) {
//                if (monster.getId()) {
//                
//                }
//
//            }
//            GameLoop.getServer().removeMonster(GameLoop.getMonsters());
//            e.deleteFromWorld();
//
//        }
//    }
    }

    private void enemyMovement(Entity e) {
        Position position = pm.get(e);
//        if (position.getX() % 32 == 0 && position.getY() % 32 == 0) {
//            boolean anm = false;
        int dir = new Random().nextInt(6);

        for (Player player : GameLoop.getServer().getLoggedIn()) {
            if ((position.getX() > player.getX() - 160 && position.getX() < player.getX() + 160) && (position.getY() > player.getY() - 160 && position.getY() < player.getY() + 160)) {
                if (Math.abs(player.getX() - position.getX()) >= Math.abs(player.getY() - position.getY())) {
                    dir = position.getX() > player.getX() ? 2 : 4;
                } else {
                    dir = position.getY() > player.getY() ? 1 : 3;
                }
            }
        }

        for (Player player : GameLoop.getServer().getLoggedIn()) {
            int distX = Math.abs((int) position.getX() - (int) player.getX());
            int distY = Math.abs((int) position.getY() - (int) player.getY());
            if (distX <= 32 && distY <= 32) {
                dir = 7;
            }
        }
        if (dir > 0 && dir < 5) {
            moveTo(e, dir);
        }
//
//        }
    }

    private void moveTo(Entity e, int direction) {
        Position position = pm.get(e);
        Velocity velocity = vm.get(e);
        if (position.getX() % 32 == 0 && position.getY() % 32 == 0) {
//            System.out.print("X: " + position.getX() + " (" + (position.getX() % 32) + "  ");
//            System.out.println("Y: " + position.getY() + " (" + (position.getY() % 32) + "  ");
            if (canMove(e, direction)) {
                switch (direction) {
                    case 1:
                        position.setDirection('w');
                        //if (!Game.map.isBlocked(Game.toTile(position.getX()), Game.toTile(position.getY()) - 1)) {
                        velocity.setDesiredY(position.getY() - 32);
                        position.addY(-velocity.getY() * world.getDelta());
                        //}
                        break;
                    case 2:
                        position.setDirection('a');
                        //if (!Game.map.isBlocked(Game.toTile(position.getX()) - 1, Game.toTile(position.getY()))) {
                        velocity.setDesiredX(position.getX() - 32);
                        position.addX(-velocity.getX() * world.getDelta());

                        //}
                        break;
                    case 3:
                        position.setDirection('s');
                        //if (!Game.map.isBlocked(Game.toTile(position.getX()), Game.toTile(position.getY()) + 1)) {
                        velocity.setDesiredY(position.getY() + 32);
                        position.addY(velocity.getY() * world.getDelta());

                        //}
                        break;
                    case 4:
                        position.setDirection('d');
                        //if (!Game.map.isBlocked(Game.toTile(position.getX()) + 1, Game.toTile(position.getY()))) {
                        velocity.setDesiredX(position.getX() + 32);
                        position.addX(velocity.getX() * world.getDelta());

                        // }
                        break;
                    default:
                        break;
                }
                long id = em.get(e).getId();
                for (MonsterEntity monster : GameLoop.getMonsters()) {
                    if (monster.getId() == id) {
                        GameLoop.updateMonster(monster);

                    }
                }
            }
        }
//        updateAnimation(e, true);
    }

    private boolean canMove(Entity e, int direction) {
        boolean can = true;
        Position position = pm.get(e);
        int x = (int) position.getX();
        int y = (int) position.getY();
        switch (direction) {
            case 1:
                position.setDirection('w');
                for (Player player : GameLoop.getServer().getLoggedIn()) {
                    int playerx = (((int) player.getX()) / 32) * 32;
                    int playery = (((int) player.getY()) / 32) * 32;

                    if (playerx == (x) && playery == (y - 32)) {
                        can = false;
                    }
                }
                break;

            case 2:
                position.setDirection('a');
                for (Player player : GameLoop.getServer().getLoggedIn()) {
                    int playerx = (((int) player.getX()) / 32) * 32;
                    int playery = (((int) player.getY()) / 32) * 32;
                    if (playerx == (x - 32) && playery == y) {
                        can = false;
                    }

                }
                break;
            case 3:
                position.setDirection('s');
                for (Player player : GameLoop.getServer().getLoggedIn()) {
                    int playerx = (((int) player.getX()) / 32) * 32;
                    int playery = (((int) player.getY()) / 32) * 32;

                    if (playerx == x && playery == (y + 32)) {
                        can = false;
                    }
                }
                break;
            case 4:
                position.setDirection('d');
                for (Player player : GameLoop.getServer().getLoggedIn()) {
                    int playerx = (((int) player.getX()) / 32) * 32;
                    int playery = (((int) player.getY()) / 32) * 32;

                    if (playerx == (x + 32) && playery == y) {

                        can = false;
                    }

                }
                break;
            default:
                can = false;
                break;
        }
        return true;
    }
//    private void updateAnimation(Entity e, boolean animating) {
//        ActorSprite actor = as.getSafe(e);
//        if (actor != null) {
//            if (actor.isAnimating()) {
//                if (animating) {
//                    actor.update((int) world.delta);
//                } else {
//                    actor.stop();
//                }
//            } else if (animating) {
//                actor.animate();
//            } else {
//                actor.update((int) world.delta);
//            }
//        }
//    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.ecs.systems;

import com.andrechateau.core.GameLoop;
import com.andrechateau.ecs.components.Creature;
import com.andrechateau.ecs.components.Enemy;
import com.andrechateau.ecs.components.Position;
import com.andrechateau.ecs.entities.MonsterEntity;
import com.andrechateau.persistence.Player;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author Andre Chateaubriand
 */
public class CombatSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<Position> pm;
    @Mapper
    ComponentMapper<Enemy> em;
    @Mapper
    ComponentMapper<Creature> cm;

    public CombatSystem() {
        super(Aspect.getAspectForAll(Position.class, Creature.class));
    }

    @Override
    protected void process(Entity e) {
        Enemy enemy = em.getSafe(e);
        Position pos = pm.get(e);
        if (enemy != null) {
            for (Player player : GameLoop.getServer().getLoggedIn()) {
                int distX = Math.abs((int) pos.getX() - (int) player.getX());
                int distY = Math.abs((int) pos.getY() - (int) player.getY());
                if (distX <= 32 && distY <= 32) {
                    attackPlayer(e, player);
                    break;
                }
            }
        } else {
            for (MonsterEntity monster : GameLoop.getMonsters()) {
                int distX = Math.abs((int) pos.getX() - (int) monster.getX());
                int distY = Math.abs((int) pos.getY() - (int) monster.getY());
                if (distX <= 32 && distY <= 32) {

                    attack(e, monster.getEntity());
                    break;
                }
            }
        }
        for (Player player : GameLoop.getServer().getLoggedIn()) {
            for (MonsterEntity monster : GameLoop.getMonsters()) {
                int distX = Math.abs((int) player.getX() - (int) monster.getX());
                int distY = Math.abs((int) player.getY() - (int) monster.getY());
                if (distX <= 32 && distY <= 32) {
                    attackMonster(player, monster.getEntity());
                    break;
                }
            }
        }
    }

    private void attack(Entity attacker, Entity attacked) {
        Creature atkr = cm.get(attacker);
        Position pos = pm.get(attacked);
        Creature atkd = cm.get(attacked);
//        System.out.println(atkr.getName() + " attacked " + atkd.getName());
        if (System.currentTimeMillis() >= atkr.getLastMeelee() + atkr.getCdMeelee()) {
            System.out.println(".");
            int damage = atkr.getDamage() + (new Random().nextInt(4) - 2);
            atkd.setHP(atkd.getHP() - damage);
            atkr.setLastMeelee(System.currentTimeMillis());
            if (atkd.getHP() < 0) {
                atkd.setHP(0);
            }
            Enemy enemy = em.getSafe(attacked);
            if (enemy != null) {
                for (MonsterEntity monster : GameLoop.getMonsters()) {
                    if (monster.getId() == enemy.getId()) {
                        if (monster.getHP() > 0) {
                            GameLoop.updateMonster(monster);
                        } else {
                            GameLoop.removeMonster(monster);
                        }
                    }
                }
            }
            //Entity fx = world.createEntity();
            //fx.addComponent(new Position(pos.getX(), pos.getY()));
            //fx.addComponent(new Effect("hit"));
            //fx.addToWorld();
            // fx = world.createEntity();
            //fx.addComponent(new Position(pos.getX(), pos.getY()));
            //fx.addComponent(new Effect(damage, Color.red));
            //fx.addToWorld();
        } else {
            System.out.println("show");
        }
    }

    private void attackMonster(Player attacker, Entity attacked) {
        Creature atkd = cm.get(attacked);
//        System.out.println(attacker.getName() + " attacked " + atkd.getName());
        if (System.currentTimeMillis() >= attacker.getLastMeelee() + attacker.getCdMeelee()) {
//            System.out.println(System.currentTimeMillis() + " " + (attacker.getLastMeelee() + attacker.getCdMeelee()));
//
            int damage = attacker.getDamage() + (new Random().nextInt(4) - 2);
            atkd.setHP(atkd.getHP() - damage);
            attacker.setLastMeelee(System.currentTimeMillis());
            if (atkd.getHP() < 0) {
                atkd.setHP(0);
            }
            Position pos = pm.get(attacked);
            GameLoop.getServer().sendHit((int) pos.getX(), (int) pos.getY(), damage+"");
            //attacked.setHP(100);
            Enemy enemy = em.getSafe(attacked);
            if (enemy != null) {
                LinkedList<MonsterEntity> toDelete = new LinkedList<>();
                for (MonsterEntity monster : GameLoop.getMonsters()) {
                    if (monster.getId() == enemy.getId()) {
                        if (monster.getHP() > 0) {
                            GameLoop.updateMonster(monster);
                        } else {
                            toDelete.add(monster);
                        }
                    }
                }
                for (MonsterEntity monsterEntity : toDelete) {
                    GameLoop.removeMonster(monsterEntity);
                }

            }

        } else {
            System.out.println("show");
        }
    }

    private void attackPlayer(Entity attacker, Player attacked) {
        Creature atkr = cm.get(attacker);
//        System.out.println(atkr.getName() + " attacked " + attacked.getName());
        if (System.currentTimeMillis() >= atkr.getLastMeelee() + atkr.getCdMeelee()) {
//            System.out.println(System.currentTimeMillis() + " " + (atkr.getLastMeelee() + atkr.getCdMeelee()));

            int damage = atkr.getDamage() + (new Random().nextInt(4) - 2);
            attacked.setHP(attacked.getHP() - damage);
            atkr.setLastMeelee(System.currentTimeMillis());
            if (attacked.getHP() < 0) {
                attacked.setHP(0);
            }
            GameLoop.getServer().sendHit((int) attacked.getDesiredX(), (int) attacked.getDesiredY(), damage+"");
            checkAlive(attacked);

            //attacked.setHP(100);
            com.andrechateau.network.Character c = new com.andrechateau.network.Character();
            c.setX(attacked.getDesiredX());
            c.setY(attacked.getDesiredY());
            c.setDesiredX(attacked.getDesiredX());
            c.setDesiredY(attacked.getDesiredY());
            c.setDirection(attacked.getDirection());
            c.setHP(attacked.getHP());
            c.setName(attacked.getName());
            c.setId((int) attacked.getId());
            GameLoop.getServer().updateCharacter(c);

        } else {
//            System.out.println("show");
        }
    }

    private void checkAlive(Player player) {
        if (player.getHP() <= 0) {
            player.setHP(100);
            player.setX(832);
            player.setY(1248);
            player.setDesiredX(832);
            player.setDesiredY(1248);
        }
    }

}

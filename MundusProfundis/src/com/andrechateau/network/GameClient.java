package com.andrechateau.network;

import com.andrechateau.ecs.components.Effect;
import com.andrechateau.ecs.components.Position;
import com.andrechateau.gamestates.Game;
import com.andrechateau.ecs.entities.CharacterEntity;
import com.andrechateau.ecs.entities.MessageEntity;
import com.andrechateau.ecs.entities.MonsterEntity;
import com.andrechateau.persistence.Player;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JOptionPane;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.andrechateau.network.Network.*;
import com.artemis.Entity;
import com.esotericsoftware.minlog.Log;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.newdawn.slick.Color;

public class GameClient {

    public static String host;
    Client client;
    String name;
    public static HashMap<Long, CharacterEntity> characters = new HashMap();
    public static HashMap<Long, MonsterEntity> monsters = new HashMap();

    public GameClient(String name) {
        client = new Client();
        client.start();
        Network.register(client);
        Log.set(com.esotericsoftware.minlog.Log.LEVEL_INFO);
        // ThreadedListener runs the listener methods on a different thread.
        client.addListener(new ThreadedListener(new Listener() {
            public void connected(Connection connection) {
            }

            public void received(Connection connection, Object object) {

                if (object instanceof AddCharacter) {
                    AddCharacter msg = (AddCharacter) object;
                    System.out.println("> " + msg.character.getName());
                    addCharacter(msg.character);
                    return;
                }

                if (object instanceof UpdateCharacter) {
                    updateCharacter((UpdateCharacter) object);
                    return;
                }

                if (object instanceof RemoveCharacter) {
                    RemoveCharacter msg = (RemoveCharacter) object;
                    removeCharacter(msg.id);
                    return;
                }
                if (object instanceof AddMonster) {
                    AddMonster msg = (AddMonster) object;
                    addMonster(msg);
                    return;
                }

                if (object instanceof UpdateMonster) {
                    updateMonster((UpdateMonster) object);
                    return;
                }

                if (object instanceof RemoveMonster) {
                    RemoveMonster msg = (RemoveMonster) object;
                    removeMonster(msg.id);
                    return;
                }
                if (object instanceof HitEffect) {
                    HitEffect msg = (HitEffect) object;
                    receiveHit(msg);
                    return;
                }
                if (object instanceof ChatMessage) {
                    ChatMessage msg = (ChatMessage) object;
                    receiveChat(msg);
                    return;
                }
            }

            public void disconnected(Connection connection) {
                System.exit(0);
            }
        }));
        new Thread() {
            public void run() {
                try {
                    //client.connect(50000, "10.0.2.2", 54557, 54777);
                    client.connect(5000, host, Network.port);// 10.0.2.2 is addres for connecting localhost from emulator.
                    Login login = new Login();
                    login.name = name;
                    client.sendTCP(login);

                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Ocorreu um erro com a conexão ao servidor.");
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        }.start();
        // Server communication after connection can go here, or in Listener#connected().
    }

    public void clientUpdate(Player player) {
        CharacterEntity cha = characters.get(player.getId());
        if (cha != null) {
            Player oldplayer = cha.getPlayer();
            if (oldplayer.getDesiredX() != player.getDesiredX() || oldplayer.getDesiredY() != player.getDesiredY() || oldplayer.getDirection() != player.getDirection() || oldplayer.getHP() != player.getHP()) {
                MoveCharacter msg = new MoveCharacter();
                msg.x = player.getX();
                cha.setX(msg.x);
                msg.y = player.getY();
                cha.setY(msg.y);
                msg.desiredX = player.getDesiredX();
                cha.setDesiredX(msg.desiredX);
                msg.desiredY = player.getDesiredY();
                cha.setDesiredY(msg.desiredY);
                msg.direction = player.getDirection();
                cha.setDirection(msg.direction);
                msg.hp = player.getHP();
                //cha.setHP(msg.hp);
                characters.replace(player.getId(), cha);
                //System.out.println("sending: " + (msg.x / 32) + " " + (msg.y / 32));
                client.sendTCP(msg);
            }
        }
    }

    private String inputOtherStuff() {
        String input = (String) JOptionPane.showInputDialog(null, "Other Stuff:", "Create account", JOptionPane.QUESTION_MESSAGE,
                null, null, "other stuff");
        if (input == null || input.trim().length() == 0) {
            System.exit(1);
        }
        return input.trim();
    }

    public void addCharacter(Player character) {
        CharacterEntity cha = new CharacterEntity(character, Game.world);
        GameClient.characters.put(character.getId(), cha);
        System.out.println(character.getName() + " added at " + character.getX() + ", " + character.getY());
    }

    public void updateCharacter(Network.UpdateCharacter msg) {
        CharacterEntity character = GameClient.characters.get(msg.id);
        if (character == null) {
            return;
        }
        if (character.getName().equals(Game.player.getName())) {
            if (Game.player.getHP() < msg.hp && msg.hp == 100) {
                Game.player.setX(msg.x);
                Game.player.setY(msg.y);
                Game.player.setDesiredX(msg.desiredX);
                Game.player.setDesiredY(msg.desiredY);
                Game.player.setDirection(msg.direction);
            }
            Game.player.setHP(msg.hp);

        }
        character.setX(msg.x);
        character.setY(msg.y);
        character.setDesiredX(msg.desiredX);
        character.setDesiredY(msg.desiredY);
        character.setDirection(msg.direction);
        character.setHP(msg.hp);

//        character.setX(msg.x);
//        character.setY(msg.y);
        //System.out.println(character.getName() + " moved to " + character.getX() + ", " + character.getY());
    }

    public void removeCharacter(long id) {
        CharacterEntity character = GameClient.characters.remove(id);
        character.getEntity().deleteFromWorld();
        if (character != null) {
            System.out.println(character.getName() + " removed");
        }
    }

    public void addMonster(AddMonster monster) {
        MonsterEntity mon = new MonsterEntity(monster.name, monster.outfit, monster.desiredX, monster.desiredY, Game.world);
        monsters.put(monster.id, mon);
        System.out.println(monster.name + " (" + monster.id + ") added at " + mon.getX() + ", " + mon.getY());
    }

    public void updateMonster(Network.UpdateMonster msg) {
        MonsterEntity mon = GameClient.monsters.get(msg.id);
        if (mon == null) {
            return;
        }

        mon.setX(msg.x);
        mon.setY(msg.y);
        mon.setDesiredX(msg.desiredX);
        mon.setDesiredY(msg.desiredY);
        mon.setDirection(msg.direction);
        mon.setHP(msg.hp);
//        character.setX(msg.x);
//        character.setY(msg.y);
        //System.out.println(character.getName() + " moved to " + character.getX() + ", " + character.getY());
    }

    public void removeMonster(long id) {
        MonsterEntity monster = GameClient.monsters.remove(id);
        monster.getEntity().deleteFromWorld();
        if (monster != null) {
            System.out.println(monster.getName() + " removed");
        }
    }

    public void receiveHit(HitEffect msg) {
        Entity fx = Game.world.createEntity();
        fx.addComponent(new Position(msg.x, msg.y));
        fx.addComponent(new Effect(msg.sprite));
        fx.addToWorld();
        fx = Game.world.createEntity();
        fx.addComponent(new Position(msg.x, msg.y));
        fx.addComponent(new Effect(msg.number, Color.red));
        fx.addToWorld();
    }

    public void sendChat(String message) {
        if (message != null && message.length() > 0) {
            ChatMessage msg = new ChatMessage();
            msg.id = Game.player.getId();
            msg.msg = message;
            msg.name = Game.player.getName();
            client.sendTCP(msg);
//        character.setX(msg.x);
//        character.setY(msg.y);
            //System.out.println(character.getName() + " moved to " + character.getX() + ", " + character.getY());
        }
    }

    public void receiveChat(ChatMessage msg) {
        if (Game.messages.containsKey(msg.name)) {
            MessageEntity e = Game.messages.get(msg.name);
            if (e != null) {
                if (e.getMsg().getTime() <= 0) {
                    Game.messages.put(msg.name, new MessageEntity(characters.get(msg.id), msg.msg));
                } else {
                    e.getMsg().addMsg(msg.msg);
                }
            } else {
                Game.messages.put(msg.name, new MessageEntity(characters.get(msg.id), msg.msg));
            }
        } else {
            Game.messages.put(msg.name, new MessageEntity(characters.get(msg.id), msg.msg));
        }
        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String strmsg = "[" + formatter.format(now) + "] " + msg.name + ": " + msg.msg;
        int chars = 50;
        String str[] = strmsg.split(" ");
        String line = "";
        for (int i = 0; i < str.length; i++) {
            if(line.length()+str.length<=chars){
                line +=str[i] + " ";
            }else{
                Game.msgRecord.add(line+" ");
                line = str[i]+" ";
            }
        }
        if(line.length()>0){
            Game.msgRecord.add(line);
        }
//
//        for (int i = 0; i < line.length(); i += chars) {
//            if (line.length() >= i + chars) {
//                Game.msgRecord.add(line.substring(i, i + chars));
//            } else {
//                Game.msgRecord.add(line.substring(i));
//            }
//        }
    }

    public void close() {
        client.stop();
        client.close();
    }

    public boolean isConnected() {
        return client.isConnected();
    }
}

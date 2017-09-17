package com.esotericsoftware.kryonet.examples.position;

import com.andrechateau.core.Game;
import com.andrechateau.entities.CharacterEntity;
import com.andrechateau.persistence.Player;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JOptionPane;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.esotericsoftware.kryonet.examples.position.Network.*;
import com.esotericsoftware.minlog.Log;

public class GameClient {

    Client client;
    String name;
    public static HashMap<Long, CharacterEntity> characters = new HashMap();

    public GameClient(String host, String name) {
        client = new Client();
        client.start();
        Network.register(client);
        Log.set(com.esotericsoftware.minlog.Log.LEVEL_INFO);
        // ThreadedListener runs the listener methods on a different thread.
        client.addListener(new ThreadedListener(new Listener() {
            public void connected(Connection connection) {
            }

            public void received(Connection connection, Object object) {
                if (object instanceof RegistrationRequired) {
                    Register register = new Register();
                    register.name = name;
                    register.otherStuff = inputOtherStuff();
                    client.sendTCP(register);
                }

                if (object instanceof AddCharacter) {
                    System.out.println("pão");
                    AddCharacter msg = (AddCharacter) object;
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
                    e.printStackTrace();

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
                cha.setHP(msg.hp);
                characters.replace(player.getId(), cha);
                client.sendTCP(msg);
            }
        }
    }

    public String inputHost() {
        String input = (String) JOptionPane.showInputDialog(null, "Host:", "Connect to server", JOptionPane.QUESTION_MESSAGE,
                null, null, "localhost");
        if (input == null || input.trim().length() == 0) {
            System.exit(1);
        }
        return input.trim();
    }

    public String inputName() {
        String input = (String) JOptionPane.showInputDialog(null, "Name:", "Connect to server", JOptionPane.QUESTION_MESSAGE,
                null, null, "Test");
        if (input == null || input.trim().length() == 0) {
            System.exit(1);
        }
        return input.trim();
    }

    public String inputOtherStuff() {
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
        if (!character.getName().equals(Game.player.getName())) {
            //System.out.println(character.getName() + " moveu " + character.getX() + " " + character.getY());
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

    public void close() {
        client.stop();
        client.close();
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    public static void main(String[] args) {
        Log.set(Log.LEVEL_DEBUG);
        new GameClient("localhost", "dendem");
    }
}

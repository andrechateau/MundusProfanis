package com.andrechateau.network;

import com.andrechateau.core.GameLoop;
import com.andrechateau.ecs.entities.MonsterEntity;
import com.andrechateau.persistence.Player;
import com.andrechateau.persistence.PlayerDAO;
import java.io.IOException;
import java.util.HashSet;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.andrechateau.network.Network.*;
import com.esotericsoftware.minlog.Log;
import java.sql.SQLException;

public class PositionServer {

    private Server server;
    private HashSet<Player> loggedIn = new HashSet();
    private ServerListener listener;

    public PositionServer(ServerListener listener) throws IOException {
        server = new Server() {
            protected Connection newConnection() {
                return new CharacterConnection();
            }
        };
        this.listener = listener;
        Log.set(com.esotericsoftware.minlog.Log.LEVEL_INFO);
        Network.register(server);

        server.addListener(new Listener() {
            public void received(Connection c, Object object) {
                // We know all connections for this server are actually CharacterConnections.
                CharacterConnection connection = (CharacterConnection) c;
                Player character = connection.character;

                if (object instanceof Login) {
                    // Ignore if already logged in.
                    if (character != null) {
                        return;
                    }

                    // Reject if the name is invalid.
                    String name = ((Login) object).name;
                    if (!isValid(name)) {
                        c.close();
                        return;
                    }

                    // Reject if already logged in.
                    for (Player other : getLoggedIn()) {
                        if (other.getName().equals(name)) {
                            c.close();
                            return;
                        }
                    }

                    character = loadCharacter(name);

                    // Reject if couldn't load character.
                    if (character == null) {
                        //c.sendTCP(new RegistrationRequired());
                        return;
                    }

                    loggedIn(connection, character);
                    return;
                }

                if (object instanceof MoveCharacter) {
                    // Ignore if not logged in.
                    if (character == null) {
                        System.out.println("not Logged In");
                        return;
                    }

                    MoveCharacter msg = (MoveCharacter) object;
                    character.setX(msg.x);
                    character.setY(msg.y);
                    character.setDesiredX(msg.desiredX);
                    character.setDesiredY(msg.desiredY);
                    character.setDirection(msg.direction);
                    //character.setHP(msg.hp);
                    if (!saveCharacter(character)) {
                        connection.close();
                        return;
                    }

                    UpdateCharacter update = new UpdateCharacter();
                    update.id = character.getId();
                    update.x = character.getX();
                    update.y = character.getY();
                    update.desiredX = character.getDesiredX();
                    update.desiredY = character.getDesiredY();
                    update.direction = character.getDirection();
                    update.hp = character.getHP();
                    server.sendToAllTCP(update);
                    return;
                }
                if (object instanceof ChatMessage) {
                    ChatMessage msg = (ChatMessage) object;
                    if (!processMessage(msg)) {
                        server.sendToAllTCP(msg);
                    }
                }
            }

            private boolean isValid(String value) {
                if (value == null) {
                    return false;
                }
                value = value.trim();
                if (value.length() == 0) {
                    return false;
                }
                return true;
            }

            public void disconnected(Connection c) {
                CharacterConnection connection = (CharacterConnection) c;
                if (connection.character != null) {
                    saveCharacter(connection.character);
                    getLoggedIn().remove(connection.character);
                    listener.changedLoggedUsers(getLoggedIn());                    
                    RemoveCharacter removeCharacter = new RemoveCharacter();
                    removeCharacter.id = connection.character.getId();
                    server.sendToAllTCP(removeCharacter);
                }
            }
        });
        server.bind(Network.port);
        server.start();
    }

    void loggedIn(CharacterConnection c, Player character) {
        c.character = character;

        // Add existing characters to new logged in connection.
        for (Player other : getLoggedIn()) {
            AddCharacter addCharacter = new AddCharacter();
            addCharacter.character = other;
            c.sendTCP(addCharacter);
        }
        for (MonsterEntity m : GameLoop.getMonsters()) {
            AddMonster msg = new AddMonster();
            msg.x = m.getX();
            msg.y = m.getY();
            msg.desiredX = m.getDesiredX();
            msg.desiredY = m.getDesiredY();
            msg.direction = m.getDirection();
            msg.hp = m.getHP();
            msg.id = m.getId();
            msg.name = m.getName();
            msg.outfit = m.getOutfit();
            c.sendTCP(msg);
        }
        System.out.println("added");
        getLoggedIn().add(character);
        listener.changedLoggedUsers(getLoggedIn());

        // Add logged in character to all connections.
        AddCharacter addCharacter = new AddCharacter();
        addCharacter.character = character;
        server.sendToAllTCP(addCharacter);
        System.out.println("added");
    }

    boolean saveCharacter(Player character) {
        Player player = new Player(character.getId(), character.getName(), character.getPassword(), character.getX(), character.getY(), character.getDesiredX(), character.getDesiredY(), character.getHP(), character.getDirection(), character.getOutfit());
        PlayerDAO.savePlayer(player);
        for (Player player1 : loggedIn) {
            if (player1.getName().equals(character.getName())) {
                player1.setDesiredX(character.getDesiredX());
                player1.setDesiredY(character.getDesiredY());
                player1.setX(character.getX());
                player1.setY(character.getY());
                player1.setDirection(character.getDirection());
                player1.setHP(player1.getHP());
            }
        }
        return true;
    }

    Player loadCharacter(String name) {
        try {
            return PlayerDAO.getPlayerByUsername(name);

        } catch (SQLException ex) {
            return null;
        }

    }

    public void newMonster(MonsterEntity m) {
        AddMonster msg = new AddMonster();
        msg.x = m.getX();
        msg.y = m.getY();
        msg.desiredX = m.getDesiredX();
        msg.desiredY = m.getDesiredY();
        msg.direction = m.getDirection();
        msg.hp = m.getHP();
        msg.id = m.getId();
        msg.name = m.getName();
        msg.outfit = m.getOutfit();
        server.sendToAllTCP(msg);
        System.out.println("Added " + msg.id + " (" + m.getName() + ")");
    }

    public void updateMonster(MonsterEntity m) {
        UpdateMonster msg = new UpdateMonster();
        msg.x = m.getX();
        msg.y = m.getY();
        msg.desiredX = m.getDesiredX();
        msg.desiredY = m.getDesiredY();
        msg.direction = m.getDirection();
        msg.hp = m.getHP();
        msg.id = m.getId();
        server.sendToAllTCP(msg);
        //System.out.println("updated " + msg.id + " (" + m.getName() + ") ");
    }

    public void updateCharacter(Character character) {
        UpdateCharacter update = new UpdateCharacter();
        update.id = character.getId();
        update.x = character.getX();
        update.y = character.getY();
        update.desiredX = character.getDesiredX();
        update.desiredY = character.getDesiredY();
        update.direction = character.getDirection();
        update.hp = character.getHP();
        server.sendToAllTCP(update);
        //System.out.println("updated " + msg.id + " (" + m.getName() + ") ");
    }

    public void removeMonster(MonsterEntity m) {
        RemoveMonster msg = new RemoveMonster();
        msg.id = m.getId();

        server.sendToAllTCP(msg);
        System.out.println("removed " + msg.id + " (" + m.getName() + ")");

    }

    public void sendHit(int x, int y, String number) {
        HitEffect msg = new HitEffect();
        msg.x = x;
        msg.y = y;
        msg.number = number;
        msg.sprite = "hit";
        server.sendToAllTCP(msg);
    }

    public boolean processMessage(ChatMessage msg) {
        String text = msg.msg;
        Player p = null;

        for (Player player : loggedIn) {
            if (player.getName().equals(msg.name)) {
                p = player;
            }
        }
        if (p != null) {
            boolean change = false;

            if (text.equalsIgnoreCase("exura")) {
                p.setHP(p.getHP() + 10);
                if (p.getHP() > 100) {
                    p.setHP(100);
                }
                HitEffect fx = new HitEffect();
                fx.x = p.getDesiredX();
                fx.y = p.getDesiredY();
                fx.number = "EXURA";
                fx.sprite = "exura";
                server.sendToAllTCP(fx);
                change = true;
            }
            if (text.equalsIgnoreCase("exura gran")) {
                p.setHP(100);
                change = true;
                HitEffect fx = new HitEffect();
                fx.x = p.getDesiredX();
                fx.y = p.getDesiredY();
                fx.number = "EXURA GRAN";
                fx.sprite = "exura";
                server.sendToAllTCP(fx);
            }
            if (change) {
                Character c = new Character();
                c.setX(p.getDesiredX());
                c.setY(p.getDesiredY());
                c.setDesiredX(p.getDesiredX());
                c.setDesiredY(p.getDesiredY());
                c.setDirection(p.getDirection());
                c.setHP(p.getHP());
                c.setName(p.getName());
                c.setId((int) p.getId());
                updateCharacter(c);
            }
            return change;
        }
        return false;
    }

    public void close() {
        for (Player player : loggedIn) {
            saveCharacter(player);
        }
        server.close();
    }

    /**
     * @return the loggedIn
     */
    public HashSet<Player> getLoggedIn() {
        return loggedIn;
    }

    // This holds per connection state.
    static class CharacterConnection extends Connection {

        public Player character;
    }

}

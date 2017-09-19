package com.andrechateau.network;

import com.andrechateau.persistence.Player;
import com.andrechateau.persistence.PlayerDAO;
import java.io.File;
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
                // By providing our own connection implementation, we can store per
                // connection state without a connection ID to state look up.
                return new CharacterConnection();
            }
        };
        this.listener = listener;
        Log.set(com.esotericsoftware.minlog.Log.LEVEL_INFO);
        // For consistency, the classes to be sent over the network are
        // registered by the same method for both the client and server.
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
                        c.sendTCP(new RegistrationRequired());
                        return;
                    }

                    loggedIn(connection, character);
                    return;
                }

                if (object instanceof Register) {
                    // Ignore if already logged in.
                    if (character != null) {
                        return;
                    }

                    Register register = (Register) object;

                    // Reject if the login is invalid.
                    if (!isValid(register.name)) {
                        c.close();
                        return;
                    }
                    if (!isValid(register.otherStuff)) {
                        c.close();
                        return;
                    }

                    // Reject if character alread exists.
                    if (loadCharacter(register.name) != null) {
                        c.close();
                        return;
                    }
                    PlayerDAO.newPlayer(register.name, register.otherStuff);
                    try {
                        character = PlayerDAO.getPlayerByLogin(register.name, register.otherStuff);
                    } catch (SQLException ex) {
                        c.close();
                        return;
                    }
                    if (character == null) {
                        c.close();
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

                    System.out.println(character.getName() + " moving " + msg.x + "  " + msg.y);
                    character.setX(msg.x);
                    character.setY(msg.y);
                    character.setDesiredX(msg.desiredX);
                    character.setDesiredY(msg.desiredY);
                    character.setDirection(msg.direction);
                    character.setHP(msg.hp);
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
        File file = new File("characters", character.getName().toLowerCase());
        file.getParentFile().mkdirs();
        return true;
    }

    Player loadCharacter(String name) {
        try {
            return PlayerDAO.getPlayerByUsername(name);

        } catch (SQLException ex) {
            return null;
        }

    }

    public void close() {
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

package mundusprofundisserver;

import com.andrechateau.persistence.Player;
import com.andrechateau.persistence.PlayerDAO;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import mundusprofundisserver.Network.*;
import com.esotericsoftware.minlog.Log;
import java.sql.SQLException;

public class PositionServer {

    Server server;
    HashSet<Player> loggedIn = new HashSet();

    public PositionServer() throws IOException {
        server = new Server() {
            protected Connection newConnection() {
                // By providing our own connection implementation, we can store per
                // connection state without a connection ID to state look up.
                return new CharacterConnection();
            }
        };
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
                    System.out.println("login x");
                    // Ignore if already logged in.
                    if (character != null) {
                        return;
                    }
                    System.out.println("login y");

                    // Reject if the name is invalid.
                    String name = ((Login) object).name;
                    if (!isValid(name)) {
                        c.close();
                        return;
                    }
                    System.out.println("login z");

                    // Reject if already logged in.
                    for (Player other : loggedIn) {
                        if (other.getName().equals(name)) {
                            System.out.println("ALREADY LOGGED IN");
                            c.close();
                            return;
                        }
                    }
                    System.out.println("login w");

                    character = loadCharacter(name);

                    // Reject if couldn't load character.
                    if (character == null) {
                        c.sendTCP(new RegistrationRequired());
                        return;
                    }
                    System.out.println("login ok!");

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
                    loggedIn.remove(connection.character);

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
        for (Player other : loggedIn) {
            AddCharacter addCharacter = new AddCharacter();
            addCharacter.character = other;
            c.sendTCP(addCharacter);
        }
        System.out.println("added");
        loggedIn.add(character);

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
//        if (character.getId() == 0) {
//            String[] children = file.getParentFile().list();
//            if (children == null) {
//                return false;
//            }
//            character.setId(children.length + 1);
//        }
//
//        DataOutputStream output = null;
//        try {
//            output = new DataOutputStream(new FileOutputStream(file));
//            output.writeInt(character.getId());
//            output.writeUTF(character.getPassword());
//            output.writeInt(character.getX());
//            output.writeInt(character.getY());
//            return true;
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return false;
//        } finally {
//            try {
//                output.close();
//            } catch (IOException ignored) {
//            }
//        }
    }

    Player loadCharacter(String name) {
        try {
            return PlayerDAO.getPlayerByUsername(name);

        } catch (SQLException ex) {
            return null;
        }
//        File file = new File("characters", name.toLowerCase());
//        if (!file.exists()) {
//            return null;
//        }
//        DataInputStream input = null;
//        try {
//            input = new DataInputStream(new FileInputStream(file));
//            Character character = new Character();
//            character.setId(input.readInt());
//            character.setName(name);
//            character.setPassword(input.readUTF());
//            character.setX(input.readInt());
//            character.setY(input.readInt());
//            input.close();
//            return character;
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        } finally {
//            try {
//                if (input != null) {
//                    input.close();
//                }
//            } catch (IOException ignored) {
//            }
//        }
    }

    // This holds per connection state.
    static class CharacterConnection extends Connection {

        public Player character;
    }

    public static void main(String[] args) throws IOException {
        Log.set(Log.LEVEL_DEBUG);
        new PositionServer();
    }
}

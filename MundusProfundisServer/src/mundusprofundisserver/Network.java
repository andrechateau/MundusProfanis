package mundusprofundisserver;

import com.andrechateau.persistence.Player;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import mundusprofundisserver.Character;
// This class is a convenient place to keep things common to both the client and server.

public class Network {

    static public final int port = 54555;

    // This registers objects that are going to be sent over the network.
    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Login.class);
        kryo.register(RegistrationRequired.class);
        kryo.register(Register.class);
        kryo.register(AddCharacter.class);
        kryo.register(UpdateCharacter.class);
        kryo.register(RemoveCharacter.class);
        kryo.register(Player.class);
        kryo.register(MoveCharacter.class);
    }

    static public class Login {

        public String name;
    }

    static public class RegistrationRequired {
    }

    static public class Register {

        public String name;
        public String otherStuff;
    }

    static public class UpdateCharacter {
        public long id;
        public int x, y;
        public int desiredX, desiredY;
        public char direction;
        public int hp;
    }

    static public class AddCharacter {

        public Player character;
    }

    static public class RemoveCharacter {

        public long id;
    }

    static public class MoveCharacter {
        public int x, y;
        public int desiredX, desiredY;
        public char direction;
        public int hp;
    }
}

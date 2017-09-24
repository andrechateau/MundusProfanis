package mundusprofundis;

import com.andrechateau.gamestates.StateManager;
import com.andrechateau.network.GameClient;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Andre Chateaubriand
 */
public class MundusProfundis {

    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader("host.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            if (line != null && !line.contains("localhost") && !line.equals("")) {
                line = line.trim();
                String[] str = line.split(".");
                boolean bix = true;
                for (String string : str) {
                    if (string.matches("^[0-9]+$")) {
                        int i = Integer.parseInt(string);
                        if (i < 0 || i > 255) {
                            bix = false;
                        }
                    } else {
                        bix = false;
                    }
                }
                if (bix) {
                    GameClient.host = line;
                } else {
                    GameClient.host = "localhost";
                }
            } else {
                GameClient.host = "localhost";

            }
            String everything = sb.toString();
        } catch (FileNotFoundException ex) {
           ServerSelectScreen sl =  new ServerSelectScreen();
           sl.setModal(true);
           sl.setVisible(true);
            GameClient.host = ServerSelectScreen.server;
        } catch (IOException ex) {
            GameClient.host = "localhost";
        }
        System.out.println("host = " + GameClient.host);

        try {
            AppGameContainer app = new AppGameContainer(new ScalableGame(new StateManager(), 800, 600));
            app.setDisplayMode(800, 600, false);
            app.setTitle("Mundos Profundis");
            app.setIcon("res/icons/32x32.tga");
            app.setAlwaysRender(true);
            if (app instanceof AppGameContainer) {
                app.setIcons(new String[]{"res/icons/32x32.tga", "res/icons/24x24.tga", "res/icons/16x16.tga"});
            }
            app.setTargetFrameRate(60);
            app.setVSync(true);
            app.setVerbose(false);
            app.start();

        } catch (SlickException e) {
            e.printStackTrace();
        }

    }

}

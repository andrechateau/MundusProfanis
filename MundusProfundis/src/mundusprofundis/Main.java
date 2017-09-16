package mundusprofundis;

import com.andrechateau.core.Game;
import com.andrechateau.persistence.PlayerDAO;
import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.lwjgl.LWJGLUtil;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;

public class Main {

    public static void main(String[] args) {
        System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
        while (Game.play == null) {
            String log = JOptionPane.showInputDialog("Login");
            String pass = JOptionPane.showInputDialog("Senha");
            try {
                Game.play = PlayerDAO.getPlayerByLogin(log, pass);
                if (Game.play != null) {
                    Game.play.setX(Game.play.getX() % 32 == 0 ? Game.play.getX() : Game.play.getDesiredX());
                    Game.play.setY(Game.play.getY() % 32 == 0 ? Game.play.getY() : Game.play.getDesiredY());
                }
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //JOptionPane.showMessageDialog(null, Game.player.getId());
        try {

            AppGameContainer app = new AppGameContainer(new ScalableGame(new Game(), 800, 600));
            app.setDisplayMode(800, 600, false);
            app.setTitle("Mundos Profundis");
            
            app.setTargetFrameRate(60);
            app.setVSync(true);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

}

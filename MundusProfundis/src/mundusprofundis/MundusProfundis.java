package mundusprofundis;

import com.andrechateau.gamestates.StateManager;
import java.io.File;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Andre Chateaubriand
 */
public class MundusProfundis {

    public static void main(String[] args) {
        System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
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
            System.out.println("oi1");
            app.start();

        } catch (SlickException e) {
            e.printStackTrace();
        }

    }

}

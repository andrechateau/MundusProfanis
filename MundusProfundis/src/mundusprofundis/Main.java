package mundusprofundis;

import com.andrechateau.core.Game;
import java.io.File;
import org.lwjgl.LWJGLUtil;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;

public class Main {

    public static void main(String[] args) {
        System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());

        try {

            AppGameContainer app = new AppGameContainer(new ScalableGame(new Game(), 1280, 720));
            app.setDisplayMode(1280, 720, false);
            app.setFullscreen(true);

            app.setTitle("Mundos Profundis");

            app.setTargetFrameRate(60);
            app.setVSync(true);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

}

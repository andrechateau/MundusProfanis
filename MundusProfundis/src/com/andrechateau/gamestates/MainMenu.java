package com.andrechateau.gamestates;

import com.andrechateau.network.GameClient;
import com.andrechateau.persistence.PlayerDAO;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.CrossStateTransition;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.util.ResourceLoader;

public class MainMenu extends BasicGameState {

    /**
     * The ID given to this state
     */
    public static final int ID = 1;
    /**
     * The background Image
     */
    private Image background;
    private Image window;
    private int windowx, windowy;
    public static Music BGM;
    private TextField tf;
    private TextField pw;
    private Font font;
    private MouseOverArea login;
    private MouseOverArea register;
    private boolean wrongAccount;
    /**
     * The game holding this state
     */
    private StateBasedGame game;

    /**
     * @see org.newdawn.slick.state.BasicGameState#getID()
     */
    public int getID() {
        return ID;
    }

    /**
     * @see
     * org.newdawn.slick.state.BasicGameState#init(org.newdawn.slick.GameContainer,
     * org.newdawn.slick.state.StateBasedGame)
     */
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.game = game;
        wrongAccount = false;
        background = new Image("res/title_back.jpg");
        background = background.getScaledCopy(game.getContainer().getWidth(), game.getContainer().getHeight());
        window = new Image("res/window.png");
        windowx = container.getWidth() / 2 - window.getWidth() / 2;
        windowy = container.getHeight() / 2 - window.getHeight() / 2;
        //try{
        InputStream in1 = ResourceLoader.getResourceAsStream("res/Title.ogg");
        BGM = new Music(in1, "Title.ogg");

        font = new AngelCodeFont("res/small_font.fnt", "res/small_font_0.tga");
        //font = new AngelCodeFont
        login = new MouseOverArea(container, new Image("res/btLogin.png"), windowx + 50, windowy + 235, 170, 35, new ComponentListener() {
            public void componentActivated(AbstractComponent source) {
                login();
            }
        });
        login.setMouseOverImage(new Image("res/btLogin_hover.png"));
        register = new MouseOverArea(container, new Image("res/btRegister.png"), windowx + 50, windowy + 275, 170, 35, new ComponentListener() {
            public void componentActivated(AbstractComponent source) {
                register();
            }
        });
        register.setMouseOverImage(new Image("res/btRegister_hover.png"));
        tf = new TextField(container, font, windowx + 55, windowy + 147, 200, 20, new ComponentListener() {
            public void componentActivated(AbstractComponent source) {
                pw.setFocus(true);
            }
        });
        Color invisible = new Color(0, 0, 0, 0);
        tf.setBackgroundColor(invisible);
        tf.setBorderColor(invisible);
        tf.setFocus(true);
        pw = new TextField(container, font, windowx + 55, windowy + 190, 145, 20, new ComponentListener() {
            public void componentActivated(AbstractComponent source) {
                login();
            }
        });
        pw.setBackgroundColor(invisible);
        pw.setBorderColor(invisible);
        pw.setTextColor(invisible);
    }

    /**
     * @see
     * org.newdawn.slick.state.BasicGameState#render(org.newdawn.slick.GameContainer,
     * org.newdawn.slick.state.StateBasedGame, org.newdawn.slick.Graphics)
     */
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        g.setFont(font);
        g.drawImage(background, 0, 0, 0, 0, background.getWidth(), /*background.getHeight()*/ 600);
        g.drawImage(window, windowx, windowy);
        if (wrongAccount) {
            g.setColor(Color.red);
            g.drawString("Invalid login", tf.getX() + 30, tf.getY() - 15);
        }
        g.setColor(Color.black);
        g.setColor(Color.white);
        tf.render(container, g);
        pw.render(container, g);
        login.render(container, g);
        register.render(container, g);
        g.drawString(new String(new char[pw.getText().length()]).replace("\0", "*") + (pw.hasFocus() ? "_" : ""), pw.getX() + 5, pw.getY() + 5);
        g.setFont(font);
        g.setColor(Color.white);
        String server = "Server: " + GameClient.host;
        g.drawString(server, container.getWidth() - font.getWidth(server) - 10, container.getHeight() - 15);
        g.drawString("contato@andrechateau.com", 10, container.getHeight() - 15);
    }

    /**
     * @see
     * org.newdawn.slick.state.GameState#enter(org.newdawn.slick.GameContainer,
     * org.newdawn.slick.state.StateBasedGame)
     */
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        BGM.setVolume(1);
        BGM.loop();
        pw.setFocus(false);
        tf.setFocus(true);
        tf.setText("");
        pw.setText("");
    }

    /**
     * @see
     * org.newdawn.slick.state.BasicGameState#update(org.newdawn.slick.GameContainer,
     * org.newdawn.slick.state.StateBasedGame, int)
     */
    public void update(GameContainer container, StateBasedGame game, int delta) {
        if (!pw.hasFocus() && !tf.hasFocus()) {
            tf.setFocus(true);
        }
    }

    /**
     * @see org.newdawn.slick.state.BasicGameState#keyReleased(int, char)
     */
    public void keyReleased(int key, char c) {
        if (key == Input.KEY_TAB) {
            if (tf.hasFocus()) {
                pw.setFocus(true);
            } else {
                tf.setFocus(true);
            }
        }
    }

    private void login() {
        String log = tf.getText();
        String pass = pw.getText();
        try {
            Game.play = PlayerDAO.getPlayerByLogin(log, pass);

            if (Game.play != null) {
                Game.play.setX(Game.play.getX() % 32 == 0 ? Game.play.getX() : Game.play.getDesiredX());
                Game.play.setY(Game.play.getY() % 32 == 0 ? Game.play.getY() : Game.play.getDesiredY());
                Game.player.setPlayer(Game.play);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (Game.play == null) {
            wrongAccount();
        } else {
            GameState target = game.getState(Game.ID);
            final long start = System.currentTimeMillis();
            CrossStateTransition t = new CrossStateTransition(target) {
                public boolean isComplete() {
                    return (System.currentTimeMillis() - start) > 2000;
                }

                public void init(GameState firstState, GameState secondState) {
                }
            };

            game.enterState(Game.ID, t, new EmptyTransition());

        }
    }

    public void register() {
        GameState target = game.getState(RegisterMenu.ID);
        final long start = System.currentTimeMillis();
        CrossStateTransition t = new CrossStateTransition(target) {
            public boolean isComplete() {
                return (System.currentTimeMillis() - start) > 2000;
            }

            public void init(GameState firstState, GameState secondState) {
            }
        };
        game.enterState(RegisterMenu.ID, t, new EmptyTransition());
    }

    private void wrongAccount() {
        wrongAccount = true;
        tf.setText("");
        pw.setText("");
        tf.setFocus(true);
    }
}

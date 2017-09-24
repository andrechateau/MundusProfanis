package com.andrechateau.gamestates;

import com.andrechateau.network.GameClient;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.CrossStateTransition;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class InitState extends BasicGameState {

    /**
     * The ID given to this state
     */
    public static final int ID = 0;
    /**
     * The background Image
     */
    private Image background;
    private long time;
    private final long TOTAL_TIME = 3000;
    private Font font;

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
        font = new AngelCodeFont("res/small_font.fnt", "res/small_font_0.tga");

        time = System.currentTimeMillis();
        background = new Image("res/Dends.png");
    }

    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        time = System.currentTimeMillis();
    }

    /**
     * @see
     * org.newdawn.slick.state.BasicGameState#render(org.newdawn.slick.GameContainer,
     * org.newdawn.slick.state.StateBasedGame, org.newdawn.slick.Graphics)
     */
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, container.getWidth(), container.getHeight());
        long now = System.currentTimeMillis() - time;
        float alfa = ((float) now) / (float) TOTAL_TIME;
//            if (alfa > 1) {
//                alfa = 2.0f - alfa;
//            }
        g.drawImage(background, 0, 0, new Color(1, 1, 1, alfa));
        g.setFont(font);
        g.setColor(Color.white);
        String server = "Server: " + GameClient.host;
        g.drawString(server, container.getWidth() - font.getWidth(server) - 10, container.getHeight() - 15);
        g.drawString("contato@andrechateau.com", 10, container.getHeight() - 15);

    }

    /**
     * @see
     * org.newdawn.slick.state.BasicGameState#update(org.newdawn.slick.GameContainer,
     * org.newdawn.slick.state.StateBasedGame, int)
     */
    public void update(GameContainer container, StateBasedGame game, int delta) {
        if (time + TOTAL_TIME <= System.currentTimeMillis()) {
            nextState();
        }

//        if (!pw.hasFocus() && !tf.hasFocus()) {
//            tf.setFocus(true);
//        }
    }

    /**
     * @see org.newdawn.slick.state.BasicGameState#keyReleased(int, char)
     */
    public void keyReleased(int key, char c) {
        nextState();
    }

    private void nextState() {
        GameState target = game.getState(MainMenu.ID);
        final long start = System.currentTimeMillis();

        CrossStateTransition t = new CrossStateTransition(target) {
            public boolean isComplete() {
                return (System.currentTimeMillis() - start) > 2000;

            }

            public void init(GameState firstState, GameState secondState) {
            }
        };
        //game.enterState(MainMenu.ID, t, new EmptyTransition());

        game.enterState(MainMenu.ID, new FadeOutTransition(Color.black, 1000), new FadeInTransition(Color.black, 1000));

    }
}

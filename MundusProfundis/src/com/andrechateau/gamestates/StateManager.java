package com.andrechateau.gamestates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class StateManager extends StateBasedGame {

    /**
     * Create a new test
     */
    public StateManager() {
        super("Tibeau - The Chateau's Tibia");
    }

    /**
     * @see
     * org.newdawn.slick.state.StateBasedGame#initStatesList(org.newdawn.slick.GameContainer)
     */
    public void initStatesList(GameContainer container) {
        addState(new InitState());
        addState(new MainMenu());
        addState(new RegisterMenu());
        addState(new Game());
    }

    @Override
    public boolean closeRequested() {

        if (MainMenu.BGM != null) {
            MainMenu.BGM.stop();
        }
        if (Game.client != null) {
            if (Game.player != null) {
                Game.client.clientUpdate(Game.player.getPlayer());
            }
            Game.client.close();
        }
        System.exit(0);
        //super.closeRequested();
        return true;//To change body of generated methods, choose Tools | Templates.
    }

}

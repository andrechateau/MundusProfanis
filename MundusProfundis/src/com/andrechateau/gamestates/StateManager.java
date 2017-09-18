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
        addState(new MainMenu());
        addState(new RegisterMenu());
        addState(new Game());
    }

    @Override
    public boolean closeRequested() {
        if (Game.client != null) {
            Game.client.close();
        }
        System.exit(0);
        //super.closeRequested();
        return true;//To change body of generated methods, choose Tools | Templates.
    }

}

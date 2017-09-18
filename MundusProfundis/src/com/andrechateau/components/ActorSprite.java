/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.components;

import com.artemis.Component;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

/**
 *
 * @author Andre Chateaubriand
 */
public class ActorSprite extends Component {

    private boolean animating = false;
    private Animation animationW;
    private Animation animationS;
    private Animation animationA;
    private Animation animationD;

    public ActorSprite(Image img) {
        int size = 64;
        animationW = new Animation();
        animationA = new Animation();
        animationS = new Animation();
        animationD = new Animation();
        for (int i = 0; i < 9; i++) {
            animationS.addFrame(img.getSubImage(i * size, 1, size, size), 50);
        }
        for (int i = 0; i < 9; i++) {
            animationD.addFrame(img.getSubImage(i * size, 65, size, size), 50);
        }
        for (int i = 0; i < 9; i++) {
            animationA.addFrame(img.getSubImage(i * size, 129, size, size), 50);
        }
        for (int i = 0; i < 9; i++) {
            animationW.addFrame(img.getSubImage(i * size, 197, size, size), 50);
        }
    }

    public void update(float delta) {
        if (isAnimating() || animationW.getFrame() != 0) {
            animationW.update((int) (delta * 1000.0f));
            animationA.update((int) (delta * 1000.0f));
            animationS.update((int) (delta * 1000.0f));
            animationD.update((int) (delta * 1000.0f));
        }
    }

    public void stop() {
        if (isAnimating()) {
            setAnimating(false);
            animationW.setCurrentFrame(0);
            animationA.setCurrentFrame(0);
            animationS.setCurrentFrame(0);
            animationD.setCurrentFrame(0);
        }
    }

    public void animate() {
        setAnimating(true);
    }

    public Image getImage(char direction) {

        switch (direction) {
            case 'w':
                return animationW.getCurrentFrame();
            case 'a':
                return animationA.getCurrentFrame();
            case 's':
                return animationS.getCurrentFrame();
            case 'd':
                return animationD.getCurrentFrame();
            default:
                return animationS.getCurrentFrame();
        }
    }

    public void setNewActorSprite(Image img) {
        int size = 64;
        animationW = new Animation();
        animationA = new Animation();
        animationS = new Animation();
        animationD = new Animation();
        for (int i = 0; i < 9; i++) {
            animationS.addFrame(img.getSubImage(i * size, 1, size, size), 50);
        }
        for (int i = 0; i < 9; i++) {
            animationD.addFrame(img.getSubImage(i * size, 65, size, size), 50);
        }
        for (int i = 0; i < 9; i++) {
            animationA.addFrame(img.getSubImage(i * size, 129, size, size), 50);
        }
        for (int i = 0; i < 9; i++) {
            animationW.addFrame(img.getSubImage(i * size, 197, size, size), 50);
        }
    }

    /**
     * @return the animating
     */
    public boolean isAnimating() {
        return animating;
    }

    /**
     * @param animating the animating to set
     */
    public void setAnimating(boolean animating) {
        this.animating = animating;
    }

}

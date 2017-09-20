/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.ecs.components;

import com.artemis.Component;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Andre Chateaubriand
 */
public class Effect extends Component {

    private Animation animation;
    private boolean alive;
    private static HashMap<String, Animation> animationpool = new HashMap<String, Animation>();
    private String number;
    private Color color;
    private String effect;
    private boolean isNumber;
    private long time;

    private static HashMap<String, Image> imagepool = new HashMap<String, Image>();

    public Effect(String effect) {
        alive = true;
        isNumber = false;
        this.effect = effect;
    }

    public Effect(String number, Color color) {
        this.number = number;
        this.color = color;
        isNumber = true;
        time = System.currentTimeMillis();
        alive = true;
    }

    public int getFrame() {
        if (animation == null) {
            init();
        }
        return animation.getFrame();
    }

    public void update(float delta) {
        if (animation == null) {
            init();
        }
        if (animation.getFrame() + 1 >= animation.getFrameCount()) {
            alive = false;
        } else {
            animation.update((int) (delta * 1000));
        }
    }

    public Image getImage() {
        if (animation == null) {
            init();
        }
        return animation.getCurrentFrame();
    }

    private void init() {
        try {
            if (isNumber) {
                if (animationpool.containsKey("damage")) {
                    animation = animationpool.get("damage");
                } else {
                    animation = new Animation();
                    for (int i = 0; i < 4; i++) {
                        Image img = new Image(64, 32);
                        img.getGraphics().setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 255 - (i * 10)));
                        img.getGraphics().drawString(number + "", 8, 16 - (i * 6));
                        animation.addFrame(img, 100000);

                    }
                    animation.addFrame(new Image(32, 32), 100000);
                    animationpool.put("damage", animation);
                }
                animation.setCurrentFrame(0);
            } else {
                if (animationpool.containsKey(effect)) {
                    animation = animationpool.get(effect);
                } else {
                    Image img = new Image("res/" + effect + "_effect.png");
                    animation = new Animation();
                    for (int i = 0; i < (img.getWidth()); i += 32) {
                        animation.addFrame(img.getSubImage(i, 0, 32, 32), 100000);
                    }
                    animation.addFrame(new Image(32, 32), 100000);
                    animationpool.put(effect, animation);
                }
                animation.setCurrentFrame(0);

            }
        } catch (SlickException ex) {
            Logger.getLogger(Effect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the alive
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return the isNumber
     */
    public boolean isNumber() {
        return isNumber;
    }

    /**
     * @return the time
     */
    public long getTime() {
        return time;
    }

}

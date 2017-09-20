/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.ecs.systems;

import com.andrechateau.ecs.components.Effect;
import com.andrechateau.ecs.components.Position;
import com.andrechateau.gamestates.Game;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import org.newdawn.slick.Color;

/**
 *
 * @author Andre Chateaubriand
 */
public class EffectSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<Effect> em;

    @Mapper
    ComponentMapper<Position> pm;

    public EffectSystem() {
        super(Aspect.getAspectForAll(Effect.class, Position.class));

    }

    @Override
    protected void process(Entity e) {
        Effect fx = em.get(e);
        Position pos = pm.get(e);
        if (!fx.isNumber()) {
            float x = -Game.player.getX() + pos.getX() + Game.gc.getWidth() / 2 - 8;
            float y = -Game.player.getY() + pos.getY() + Game.gc.getHeight() / 2 - 8;
            fx.getImage().draw(x, y);
        } else {
            float x = -Game.player.getX() + pos.getX() + Game.gc.getWidth() / 2;
            float y = -Game.player.getY() + pos.getY() + Game.gc.getHeight() / 2-32-(fx.getFrame()*4);
            Color clr = Game.gc.getGraphics().getColor();
            Game.gc.getGraphics().setColor(fx.getColor());
            Game.gc.getGraphics().drawString(fx.getNumber()+"", x, y);
            Game.gc.getGraphics().setColor(clr);
                    
//            if (animationpool.containsKey(number + "")) {
//                animation = animationpool.get(number + "");
//            } else {
//                animation = new Animation();
//                for (int i = 0; i < 4; i++) {
//                    Image img = new Image(64, 32);
//                    img.getGraphics().setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 255 - (i * 10)));
//                    img.getGraphics().drawString(number + "", 8, 16 - (i * 6));
//                    animation.addFrame(img, 100);
//                }
//                animation.addFrame(new Image(32, 32), 100);
//            }
//            animation.setCurrentFrame(0);
//            alive = true;
        }

//        Image img;
//        try {
//            img = new Image("res/hit_effect.png");
//            img.getSubImage(0, 0, 32, 32).draw(20, 20);
//            img.getSubImage(32, 0, 32, 32).draw(20, 52);
//            img.getSubImage(64, 0, 32, 32).draw(20, 84);
//        } catch (SlickException ex) {
//            Logger.getLogger(DebugPointRenderer.class.getName()).log(Level.SEVERE, null, ex);
//        }
        fx.update(world.getDelta() * 1000.0f);
        if (!fx.isAlive()) {
            e.deleteFromWorld();
        }
    }
}

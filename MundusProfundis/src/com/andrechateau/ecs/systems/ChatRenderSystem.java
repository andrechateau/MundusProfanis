/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.ecs.systems;

import com.andrechateau.ecs.components.ChatMessage;
import com.andrechateau.ecs.components.Position;
import com.andrechateau.gamestates.Game;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

/**
 *
 * @author Andre Chateaubriand
 */
public class ChatRenderSystem extends EntityProcessingSystem {

    private static Font font;

    @Mapper
    ComponentMapper<Position> pm;
    @Mapper
    ComponentMapper<ChatMessage> cm;

    public ChatRenderSystem() {
        super(Aspect.getAspectForAll(Position.class, ChatMessage.class));
    }

    @Override
    protected void process(Entity e) {
        try {
            if (font == null) {
                font = new AngelCodeFont("res/small_font.fnt", "res/small_font_0.tga");
            }
            ChatMessage msg = cm.get(e);
            Position pos = pm.get(e);
            String largermsg = "";
            for (String string : msg.getMsg()) {
                if (string.length() > largermsg.length()) {
                    largermsg = string;
                }
            }
            int textWidth = font.getWidth(largermsg);
            int textHeight = (font.getHeight(msg.getMsg().get(0)) + 1) * msg.getMsg().size();
            Graphics g = Game.gc.getGraphics();
            int posX = (int) ((msg.getName().equals(Game.player.getName())) ? Game.player.getX() : pos.getX());
            int posY = (int) ((msg.getName().equals(Game.player.getName())) ? Game.player.getY() : pos.getY());
            posX += (int) -Game.player.getPosition().getX() + (Game.gc.getWidth() / 2);
            posY += (int) -Game.player.getPosition().getY() + (Game.gc.getHeight() / 2);
            posX -= textWidth / 2;
            posY -= textHeight;
            posY -= 45;
            int boxHeight = textHeight + 10;
            g.setFont(font);
            g.setColor(Color.white);
            g.fillRoundRect(posX, posY, textWidth + 10, boxHeight, 5);

            Polygon poly = new Polygon();
            poly.addPoint(posX + textWidth / 2, (posY + boxHeight));
            poly.addPoint(posX + textWidth / 2 + 8, (posY + boxHeight));
            poly.addPoint(posX + textWidth / 2, (posY + boxHeight) + 10);
            g.fill(poly);
            g.setColor(Color.black);
            int i = 0;
            for (String string : msg.getMsg()) {
                int x = posX + ((textWidth) / 2) + 5;
                x -= font.getWidth(string) / 2;
                g.drawString(string, x, posY + (i + 1) + 5);
                i += font.getHeight(string);
            }
            msg.decreaseTime(((int) (world.delta * 1000)));
            if (msg.getTime() <= 0) {
                e.deleteFromWorld();
            }
        } catch (SlickException ex) {
            Logger.getLogger(ChatRenderSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

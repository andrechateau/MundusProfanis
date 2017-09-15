package com.andrechateau.systems;

import com.andrechateau.components.ActorSprite;
import com.andrechateau.components.Position;
import com.andrechateau.components.Velocity;
import com.andrechateau.core.Game;
import com.andrechateau.engine.Renderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;

public class DebugPointRenderer extends EntitySystem implements Renderer {

    @Mapper
    ComponentMapper<Position> pm;

    @Mapper
    ComponentMapper<ActorSprite> as;

    @SuppressWarnings("unchecked")
    public DebugPointRenderer() {
        super(Aspect.getAspectForAll(Position.class, ActorSprite.class));
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        Graphics g = Game.gc.getGraphics();
        int s = entities.size();
        Entity player = entities.get(0);
        Position playerpos = pm.get(player);
        int rx = (-(int) (playerpos.getX() % 32)) - 32;
        int ry = -(int) (playerpos.getY() % 32);
        int sx = (int) (playerpos.getX() / 32) - (Game.gc.getWidth() / 64) - 1;
        int sy = (int) (playerpos.getY() / 32) - (Game.gc.getHeight() / 64);
        int wx = (Game.gc.getWidth() / 32) + 2;
        int hy = (Game.gc.getWidth() / 32) + 1;
        Game.map.render(rx, ry, sx, sy, wx, hy, 0, false);
        Game.map.render(rx, ry, sx, sy, wx, hy, 1, true, entities);
        Game.map.render(rx, ry, sx, sy, wx, hy, 2, true, entities);

        // Game.map.render(rx, ry, sx, sy, wx, hy, true, entities);
//
//        for (int lay = 1; lay < Game.map.getLayerCount(); lay++) {
//            Game.map.render(rx, ry, sx, sy, wx, hy, lay, true, entities);
//        }
        int walltilex = (int) (playerpos.getX() / 32 - 1);
        int walltiley = (int) playerpos.getY() / 32;
        if ((Game.map.getTileId(walltilex > 0 ? walltilex : 0, walltiley, 1)) == 0) {
            g.setColor(Color.green);
        } else {
            g.setColor(Color.red);
        }
//
//        g.fillOval(Game.gc.getWidth() / 2 - 10, Game.gc.getHeight() / 2 - 10, 20, 20);
//        g.setColor(Color.white);
//        for (int i = 1; s > i; i++) {
//            Entity e = entities.get(i);
//            Position position = pm.get(e);
//            g.fillOval(-playerpos.getX() + position.getX() + Game.gc.getWidth() / 2 - 10, -playerpos.getY() + position.getY() + Game.gc.getHeight() / 2 - 10, 20, 20);
//        }
        g.drawString("X: " + ((int) playerpos.getX()) + "   Y: " + ((int) playerpos.getY()), 200, 10);
        g.drawString("X: " + ((int) pm.get(entities.get(1)).getX()) + "   Y: " + ((int) pm.get(entities.get(1)).getY()), 200, 20);

        g.drawString("X: " + rx + "   Y: " + ry, 200, 35);
        g.drawString("X: " + sx + "   Y: " + sy, 200, 45);
        g.drawString("X: " + wx + "   Y: " + hy, 200, 55);

    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    @Override
    public void renderObjects(int visualY, int mapY, int layer, ImmutableBag<Entity> entities) {
        for (int i = 0; entities.size() > i; i++) {
            Entity e = entities.get(i);
            Position position = pm.get(e);
            if (((int) position.getY() / 32 == mapY) && layer == Game.map.getLayerIndex("64Wall")) {
                Game.gc.getGraphics().setColor(Color.white);
                Entity player = entities.get(0);
                Position playerpos = pm.get(player);
                float x = -playerpos.getX() + position.getX() + Game.gc.getWidth() / 2;
                float y = -playerpos.getY() + position.getY() + Game.gc.getHeight() / 2;
                as.get(e).getImage(position.getDirection()).draw(x - 16, y - 32, 48, 48);
            }
        }

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.engine;

import com.andrechateau.gamestates.Game;
import com.artemis.Entity;
import com.artemis.utils.ImmutableBag;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import sun.misc.GC;

/**
 *
 * @author Andre Chateaubriand
 */
public class GameMap extends TiledMap {

    Renderer renderer;
    ImmutableBag<Entity> entities;

    public GameMap(String ref) throws SlickException {
        super(ref);
    }

    public void render(int x, int y, int sx, int sy, int width, int height, int layer, boolean lineByLine, ImmutableBag<Entity> entities) {
        this.entities = entities;
        super.render(x, y, sx, sy, width, height, layer, lineByLine);
    }

    public void render(int x, int y, int sx, int sy, int width, int height, boolean lineByLine, ImmutableBag<Entity> entities) {
        this.entities = entities;
        super.render(x, y, sx, sy, width, height, lineByLine);
    }

    @Override
    protected void renderedLine(int visualY, int mapY, int layer) {
        if (renderer != null) {
            renderer.renderObjects(visualY, mapY, layer, entities);
        }
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public boolean isBlocked(int X, int Y) {

        int walltilex = X - 1;
        int walltiley = Y;

        if (walltilex >= 0 && walltilex <= getWidth() && walltiley >= 0 && walltiley <= getHeight()) {
            if (getTileId(walltilex, walltiley, Game.map.getLayerIndex("64Wall")) != 0) {
                return getTileProperty(getTileId(walltilex, walltiley, Game.map.getLayerIndex("64Wall")), "blocked", "false").equals("true");
            } else {
                if (getTileId(walltilex + 1, walltiley, Game.map.getLayerIndex("foreground")) != 0) {
                    return getTileProperty(getTileId(walltilex + 1, walltiley, Game.map.getLayerIndex("foreground")), "blocked", "false").equals("true");
                }
                return false;
            }

        }
        return true;
    }

}

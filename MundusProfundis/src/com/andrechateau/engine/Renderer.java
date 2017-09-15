/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.engine;

import com.artemis.Entity;
import com.artemis.utils.ImmutableBag;

/**
 *
 * @author Andre Chateaubriand
 */
public interface Renderer {
    public void renderObjects(int visualY, int mapY, int layer, ImmutableBag<Entity> entities);
}

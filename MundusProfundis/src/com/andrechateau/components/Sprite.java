/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.components;

import com.artemis.Component;
import org.newdawn.slick.Image;

/**
 *
 * @author Andre Chateaubriand
 */
public class Sprite extends Component {

    private Image image;

    /**
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(Image image) {
        this.image = image;
    }

}

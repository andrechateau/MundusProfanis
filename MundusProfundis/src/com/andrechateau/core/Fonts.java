/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.core;

import java.awt.Color;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.font.effects.OutlineEffect;

/**
 *
 * @author Andre Chateaubriand
 */
public class Fonts {

    public static TrueTypeFont ttfbold = new TrueTypeFont(new Font("Arial", Font.BOLD, 12), true);
    private static UnicodeFont ttf;

    public static UnicodeFont getFont(boolean isBold) {
        if (ttf == null) {
            try {
                ttfbold = new TrueTypeFont(new Font("Verdana", Font.BOLD, 8), true);
                //TrueTypeFont ttf = new TrueTypeFont(, true);

                Font awtFont = new Font("Verdana", Font.BOLD, 12);
                //awtFont = awtFont.deriveFont(100f);
                UnicodeFont font;
                font = new UnicodeFont(awtFont);
                font.addAsciiGlyphs();
                font.getEffects().add(new OutlineEffect(2, Color.black));
                font.getEffects().add(new ColorEffect(Color.white));
                font.loadGlyphs();
                
                ttf = font;
            } catch (SlickException ex) {
                Logger.getLogger(Fonts.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ttf;
    }

}

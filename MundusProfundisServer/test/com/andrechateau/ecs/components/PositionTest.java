/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.ecs.components;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andre Chateaubriand
 */
public class PositionTest {

    public PositionTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getX method, of class Position.
     */
    @Test
    public void testGetX() {
        System.out.println("getX");
        Position instance = new Position(320, 320);
        float expResult = 320;
        float result = instance.getX();
        assertEquals(expResult, result,0.0f);
    }

    /**
     * Test of setX method, of class Position.
     */
    @Test
    public void testSetX() {
        System.out.println("setX");
        float x = 32;
        Position instance = new Position(320, 320);
        instance.setX(x);
        float result = instance.getX();
        assertEquals(x, result,0.0f);

    }

    /**
     * Test of getX method, of class Position.
     */
    @Test
    public void testGetY() {
        System.out.println("getY");
        Position instance = new Position(320, 320);
        float expResult = 320;
        float result = instance.getY();
        assertEquals(expResult, result,0.0f);
    }

    /**
     * Test of setX method, of class Position.
     */
    @Test
    public void testSetY() {
        System.out.println("setY");
        float y = 32;
        Position instance = new Position(320, 320);
        instance.setY(y);
        float result = instance.getY();
        assertEquals(y, result,0.0f);

    }

    /**
     * Test of addX method, of class Position.
     */
    @Test
    public void testAddX() {
        System.out.println("addX");
        float x = 32;
        Position instance = new Position(320, 320);
        instance.addX(x);
        float result = instance.getX();
        assertEquals(x+320, result,0.0f);
    }

    /**
     * Test of addY method, of class Position.
     */
    @Test
    public void testAddY() {
        System.out.println("addY");
        float y = 32;
        Position instance = new Position(320, 320);
        instance.addY(y);
        float result = instance.getY();
        assertEquals(y+320, result,0.0f);
    }


}

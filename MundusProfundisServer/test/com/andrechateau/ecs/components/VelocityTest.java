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
public class VelocityTest {

    public VelocityTest() {
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
     * Test of getX method, of class Velocity.
     */
    @Test
    public void testGetX() {
        System.out.println("getX");
        Velocity instance = new Velocity(200, 200);
        float expResult = 200;
        float result = instance.getX();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getY method, of class Velocity.
     */
    @Test
    public void testGetY() {
        System.out.println("getY");
        Velocity instance = new Velocity(200, 200);
        float expResult = 200;
        float result = instance.getY();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of setX method, of class Velocity.
     */
    @Test
    public void testSetX() {
        System.out.println("setX");
        float x = 100;
        Velocity instance = new Velocity(200, 200);
        instance.setX(x);
        float expResult = 100;
        float result = instance.getX();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of setY method, of class Velocity.
     */
    @Test
    public void testSetY() {
        System.out.println("setY");
        float y = 100;
        Velocity instance = new Velocity();
        instance.setY(y);
        float expResult = 100;
        float result = instance.getY();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of setVelocity method, of class Velocity.
     */
    @Test
    public void testSetVelocity() {
        System.out.println("setVelocity");
        float x = 200;
        float y = 200;
        Velocity instance = new Velocity();
        instance.setVelocity(x, y);
        float expResult = 200;
        float result = instance.getY();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of addX method, of class Velocity.
     */
    @Test
    public void testAddX() {
        System.out.println("addX");
        float x = 50;
        Velocity instance = new Velocity(200, 200);
        instance.addX(x);
        float expResult = 200 + x;
        float result = instance.getX();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of addY method, of class Velocity.
     */
    @Test
    public void testAddY() {
        System.out.println("addY");
        float y = 50;
        Velocity instance = new Velocity(200, 200);
        instance.addY(y);
        float expResult = 200 + y;
        float result = instance.getY();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getDesiredX method, of class Velocity.
     */
    @Test
    public void testGetDesiredX() {
        System.out.println("getDesiredX");
        Velocity instance = new Velocity(200, 200);
        instance.setDesiredX(32);
        float expResult = 32;
        float result = instance.getDesiredX();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getDesiredY method, of class Velocity.
     */
    @Test
    public void testGetDesiredY() {
        System.out.println("getDesiredY");
        Velocity instance = new Velocity();
        instance.setDesiredY(32);
        float expResult = 32;
        float result = instance.getDesiredY();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of setDesiredX method, of class Velocity.
     */
    @Test
    public void testSetDesiredX() {
        System.out.println("setDesiredX");
        float desiredX = 32;
        Velocity instance = new Velocity();
        instance.setDesiredX(desiredX);
        float expResult = 32;
        float result = instance.getDesiredX();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of setDesiredY method, of class Velocity.
     */
    @Test
    public void testSetDesiredY() {
        System.out.println("setDesiredY");
        float desiredY = 32;
        Velocity instance = new Velocity();
        instance.setDesiredY(desiredY);
        float expResult = 32;
        float result = instance.getDesiredY();
        assertEquals(expResult, result, 0.0);
    }

}

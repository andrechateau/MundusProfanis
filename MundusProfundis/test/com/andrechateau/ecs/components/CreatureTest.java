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
public class CreatureTest {

    public CreatureTest() {
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
     * Test of getName method, of class Creature.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        Creature instance = new Creature();
        instance.setName("Nome");
        String expResult = "Nome";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setName method, of class Creature.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        String name = "Nome";
        Creature instance = new Creature();
        instance.setName(name);
        String expResult = "Nome";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getOutfit method, of class Creature.
     */
    @Test
    public void testGetOutfit() {
        System.out.println("getOutfit");
        Creature instance = new Creature();
        instance.setOutfit("hunter");
        String expResult = "hunter";
        String result = instance.getOutfit();
        assertEquals(expResult, result);
    }

    /**
     * Test of setOutfit method, of class Creature.
     */
    @Test
    public void testSetOutfit() {
        System.out.println("setOutfit");
        Creature instance = new Creature();
        instance.setOutfit("hunter");
        String expResult = "hunter";
        String result = instance.getOutfit();
        assertEquals(expResult, result);
    }

    /**
     * Test of getHP method, of class Creature.
     */
    @Test
    public void testGetHP() {
        System.out.println("getHP");
        Creature instance = new Creature();
        instance.setHP(100);
        int expResult = 100;
        int result = instance.getHP();
        assertEquals(expResult, result);
    }

    /**
     * Test of setHP method, of class Creature.
     */
    @Test
    public void testSetHP() {
        System.out.println("setHP");
        Creature instance = new Creature();
        instance.setHP(100);
        int expResult = 100;
        int result = instance.getHP();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDamage method, of class Creature.
     */
    @Test
    public void testGetDamage() {
        System.out.println("getDamage");
        Creature instance = new Creature();
        instance.setDamage(5);
        int expResult = 5;
        int result = instance.getDamage();
        assertEquals(expResult, result);
    }

    /**
     * Test of setDamage method, of class Creature.
     */
    @Test
    public void testSetDamage() {
        System.out.println("setDamage");
        Creature instance = new Creature();
        instance.setDamage(5);
        int expResult = 5;
        int result = instance.getDamage();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCdMeelee method, of class Creature.
     */
    @Test
    public void testGetCdMeelee() {
        System.out.println("getCdMeelee");
        Creature instance = new Creature();
        instance.setCdMeelee(500);
        int expResult = 500;
        int result = instance.getCdMeelee();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCdMeelee method, of class Creature.
     */
    @Test
    public void testSetCdMeelee() {
        System.out.println("setCdMeelee");
        Creature instance = new Creature();
        instance.setCdMeelee(500);
        int expResult = 500;
        int result = instance.getCdMeelee();
        assertEquals(expResult, result);
    }

}

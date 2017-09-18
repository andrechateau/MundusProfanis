package com.andrechateau.network;

public class Character {

    private String name;
    private int id;
    private String password;
    private int X;
    private int Y;
    private int desiredX;
    private int desiredY;
    private int HP;
    private char direction;
    private String outfit;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the X
     */
    public int getX() {
        return X;
    }

    /**
     * @param X the X to set
     */
    public void setX(int X) {
        this.X = X;
    }

    /**
     * @return the Y
     */
    public int getY() {
        return Y;
    }

    /**
     * @param Y the Y to set
     */
    public void setY(int Y) {
        this.Y = Y;
    }

    /**
     * @return the desiredX
     */
    public int getDesiredX() {
        return desiredX;
    }

    /**
     * @param desiredX the desiredX to set
     */
    public void setDesiredX(int desiredX) {
        this.desiredX = desiredX;
    }

    /**
     * @return the desiredY
     */
    public int getDesiredY() {
        return desiredY;
    }

    /**
     * @param desiredY the desiredY to set
     */
    public void setDesiredY(int desiredY) {
        this.desiredY = desiredY;
    }

    /**
     * @return the HP
     */
    public int getHP() {
        return HP;
    }

    /**
     * @param HP the HP to set
     */
    public void setHP(int HP) {
        this.HP = HP;
    }

    /**
     * @return the direction
     */
    public char getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(char direction) {
        this.direction = direction;
    }

    /**
     * @return the outfit
     */
    public String getOutfit() {
        return outfit;
    }

    /**
     * @param outfit the outfit to set
     */
    public void setOutfit(String outfit) {
        this.outfit = outfit;
    }

}

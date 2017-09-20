package com.andrechateau.ecs.components;

import com.artemis.Component;

public class Position extends Component {

    private float x, y;
    private char direction;

    public Position(float x, float y) {
        direction = 's';

        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void addX(float x) {
        this.x += x;
    }

    public void addY(float y) {
        this.y += y;
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

}

package com.andrechateau.ecs.components;

import com.artemis.Component;

public class Velocity extends Component {

    private float x;
    private float y;
    private float desiredX=-1;
    private float desiredY=-1;

    public Velocity() {
    }

    public Velocity(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setVelocity(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void addX(float x) {
        this.x += x;
    }

    public void addY(float y) {
        this.y += y;
    }

    public float getDesiredX() {
        return desiredX;
    }

    public float getDesiredY() {
        return desiredY;
    }

    public void setDesiredX(float desiredX) {
        this.desiredX = desiredX;
    }

    public void setDesiredY(float desiredY) {
        this.desiredY = desiredY;
    }
}

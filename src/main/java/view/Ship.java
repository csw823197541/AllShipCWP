package view;

/**
 * Created by csw on 2016/12/16 11:13.
 * Explain:
 */
public class Ship {

    public int x;
    public int y;
    public int width;
    public int planX;
    public int planY;
    public int planHeight;

    public Ship() {}

    public Ship(int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getPlanX() {
        return planX;
    }

    public void setPlanX(int planX) {
        this.planX = planX;
    }

    public int getPlanY() {
        return planY;
    }

    public void setPlanY(int planY) {
        this.planY = planY;
    }

    public int getPlanHeight() {
        return planHeight;
    }

    public void setPlanHeight(int planHeight) {
        this.planHeight = planHeight;
    }
}

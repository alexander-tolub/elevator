package model;

import java.util.Objects;

public class CallFromOutsideCabin {

    private Direction direction;
    private int floor;

    public CallFromOutsideCabin(Direction direction, int floor) {
        this.direction = direction;
        this.floor = floor;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getFloor() {
        return floor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallFromOutsideCabin that = (CallFromOutsideCabin) o;
        return floor == that.floor &&
                direction == that.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(direction, floor);
    }
}

package model;

import java.util.Objects;

public class CallFromOutsideCabin {

    private Direction direction;
    private int floor;
    private int destinationFloor;

    public CallFromOutsideCabin(Direction direction, int floor, int destinationFloor) {
        this.destinationFloor = destinationFloor;
        this.direction = direction;
        this.floor = floor;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getFloor() {
        return floor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallFromOutsideCabin that = (CallFromOutsideCabin) o;
        return floor == that.floor &&
                destinationFloor == that.destinationFloor &&
                direction == that.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(direction, floor, destinationFloor);
    }

    @Override
    public String toString() {
        return "CallFromOutsideCabin{" +
                "direction=" + direction +
                ", floor=" + floor +
                ", destinationFloor=" + destinationFloor +
                '}';
    }
}

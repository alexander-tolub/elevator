package model;

import java.util.Objects;

public class CallFromWithinCabin {

    private final int selectedFloor;

    public CallFromWithinCabin(int selectedFloor) {
        this.selectedFloor = selectedFloor;
    }

    public int getSelectedFloor() {
        return selectedFloor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallFromWithinCabin that = (CallFromWithinCabin) o;
        return selectedFloor == that.selectedFloor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(selectedFloor);
    }
}

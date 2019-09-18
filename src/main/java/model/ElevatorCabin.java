package model;

public class ElevatorCabin {

    private Door door;

    public ElevatorCabin() {
        this.door = new Door();
    }

    public void openDoor() {
        door.open();
    }

    public void closeDoor() {
        door.close();
    }

}

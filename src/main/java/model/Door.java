package model;

public class Door {

    public boolean open;

    public boolean isOpen() {
        return open;
    }

    public void open() {
        try {
            System.out.println("opening the door");
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            System.out.println("closing the door");
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

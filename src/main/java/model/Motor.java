package model;

public class Motor {

    private int speedPerSecond;

    public Motor(int speedPerSecond) {
        this.speedPerSecond = speedPerSecond;
    }

    public int up() {
        try {
            System.out.println("going up 1 meter");
            Thread.sleep(1000);
            return speedPerSecond;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int down() {
        try {
            System.out.println("going down 1 meter");
            Thread.sleep(1000);
            return speedPerSecond;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

}

package model;

public class Motor {

    public int up() {
        try {
            System.out.println("going up 1 meter");
            Thread.sleep(1000);
            return 1;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int down() {
        try {
            System.out.println("going down 1 meter");
            Thread.sleep(1000);
            return 1;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

}

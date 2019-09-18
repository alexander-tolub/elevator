package model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class AutomatedControlModule {

    private final int floorHeight;
    private double cabinDistanceFromGround = 0;
    private ElevatorCabin elevatorCabin;
    private Motor motor;
    private Set<CallFromOutsideCabin> callFromOutsideCabinSet = new HashSet<>();
    private Queue<CallFromWithinCabin> callFromWithinCabinQueue = new LinkedList<>();

    private boolean stop = false;

    public AutomatedControlModule(int floorHeight) {
        this.floorHeight = floorHeight;
        this.elevatorCabin = new ElevatorCabin();
        this.motor  = new Motor();
    }

    public void addCallFromOutsideCabin(CallFromOutsideCabin callFromOutside) {
        callFromOutsideCabinSet.add(callFromOutside);
    }

    public void addCallFromWithinCabin(CallFromWithinCabin callFromWithinCabin) {
        callFromWithinCabinQueue.add(callFromWithinCabin);
    }

    public void stop() {
        if(stop) {
            stop = false;
            startWorking();
        }
    }

    // на первом шаге лифт стоит и не двигается, в стеке заданых этажей заранее вбиты дестинейшны
    // в листе вывзовов лифта имеем предзаполненный список вызовов пасажиров на этажах
    // если стек дестинейшнов пуст (НО ОН НЕ ПУСТ!!!), находим ближайший вызов на этаже, и едем туда
    public void startWorking() {
        new Thread(() -> {
            while(true) {
                if(stop)
                    return;

                if(removeDestinationIfReached()) {
                    elevatorCabin.openDoor();
                    elevatorCabin.closeDoor();
                }

                if(removeSameDirectionOutsideCallIfReached()) {
                    elevatorCabin.openDoor();
                    elevatorCabin.closeDoor();
                }

                if(calculateCurrentDirection().equals(Direction.UP)) {
                    cabinDistanceFromGround =+ motor.up();
                }
                if(calculateCurrentDirection().equals(Direction.DOWN)) {
                    cabinDistanceFromGround =+ motor.down();
                }

            }

        }).start();
    }

    private boolean removeSameDirectionOutsideCallIfReached() {
        if(isCabinLevel()) {
            CallFromOutsideCabin callFromOutsideCabinUp = new CallFromOutsideCabin(Direction.UP, (int) calculateCurrentFloor());
            CallFromOutsideCabin callFromOutsideCabinDown = new CallFromOutsideCabin(Direction.DOWN, (int) calculateCurrentFloor());
            if(callFromOutsideCabinSet.contains(callFromOutsideCabinUp) && calculateCurrentDirection().equals(Direction.UP)) {
                callFromOutsideCabinSet.remove(callFromOutsideCabinUp);
                return true;
            } else if(callFromOutsideCabinSet.contains(callFromOutsideCabinDown) && calculateCurrentDirection().equals(Direction.DOWN)) {
                callFromOutsideCabinSet.remove(callFromOutsideCabinDown);
                return true;
            }
        }
        return false;
    }

    private boolean removeDestinationIfReached() {
        if(isCabinLevel() && (callFromWithinCabinQueue.peek().getSelectedFloor() == calculateCurrentFloor())) {
            callFromWithinCabinQueue.poll();
            return true;
        } else {
            return false;
        }
    }

    private Direction calculateCurrentDirection() {
        CallFromWithinCabin floor = callFromWithinCabinQueue.peek();
        return (calculateFloorDistanceFromGround(floor.getSelectedFloor()) - cabinDistanceFromGround) > 0 ? Direction.UP : Direction.DOWN;
    }

    private boolean isCabinLevel() {
        return (cabinDistanceFromGround % floorHeight) == 0;
    }

    private double calculateCurrentFloor() {
        return cabinDistanceFromGround / floorHeight;
    }

    private double calculateFloorDistanceFromGround(int floor) {
        return floor * floorHeight;
    }

}

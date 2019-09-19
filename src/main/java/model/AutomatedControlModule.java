package model;

import java.util.*;

public class AutomatedControlModule {

    private final int floorHeight;
    private double cabinDistanceFromGround = 0;
    private ElevatorCabin elevatorCabin;
    private Motor motor;
    private Set<CallFromOutsideCabin> callFromOutsideCabinSet = new HashSet<>();
    private LinkedHashSet<CallFromWithinCabin> callFromWithinCabinQueue = new LinkedHashSet<>();

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

                //TODO chek this
                if(removeDestinationIfReached() | removeSameDirectionOutsideCallIfReached()) {
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
            if(calculateCurrentDirection() == null && ) {

            }
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
        if(isCabinLevel()) {
            CallFromWithinCabin callFromWithinCabin = new CallFromWithinCabin((int) calculateCurrentFloor());
            return callFromWithinCabinQueue.remove(callFromWithinCabin);
        } else {
            return false;
        }
    }

    private Direction calculateCurrentDirection() {
        CallFromWithinCabin floor = null;
        if(!callFromWithinCabinQueue.isEmpty()) {
            floor = callFromWithinCabinQueue.iterator().next();
            return (calculateFloorDistanceFromGround(floor.getSelectedFloor()) - cabinDistanceFromGround) > 0 ? Direction.UP : Direction.DOWN;
        }
        return (calculateFloorDistanceFromGround(findClosestCallFromOutside().getFloor()) - cabinDistanceFromGround) > 0 ? Direction.UP : Direction.DOWN;
    }

    private CallFromOutsideCabin findClosestCallFromOutside() {
        int distance = Integer.MAX_VALUE;
        CallFromOutsideCabin result = null;
        for(CallFromOutsideCabin callFromOutsideCabin : callFromOutsideCabinSet) {
            int distanceTillOutsideCall = callFromOutsideCabin.getDestinationFloor() - (int) calculateCurrentFloor();
            if(Math.abs(distanceTillOutsideCall) < distance)
                result = callFromOutsideCabin;
        }
        return result;
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

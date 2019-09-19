package model;

import java.util.*;

public class AutomatedControlModule {

    private final int floorHeight;
    private double cabinDistanceFromGround = 0;
    private int floorCount;
    private ElevatorCabin elevatorCabin;
    private Motor motor;
    private Set<CallFromOutsideCabin> callFromOutsideCabinSet = new HashSet<>();
    private LinkedHashSet<CallFromWithinCabin> callFromWithinCabinQueue = new LinkedHashSet<>();

    private boolean stop = false;

    public AutomatedControlModule(int floorHeight, int motorSpeed, int floorCount) {
        this.floorHeight = floorHeight;
        this.elevatorCabin = new ElevatorCabin();
        this.motor = new Motor(motorSpeed);
        this.floorCount = floorCount;
    }

    public void addCallFromOutsideCabin(CallFromOutsideCabin callFromOutside) {
        System.out.println("registering call from outside the cabin " + callFromOutside);
        callFromOutsideCabinSet.add(callFromOutside);
    }

    public void addCallFromWithinCabin(CallFromWithinCabin callFromWithinCabin) {
        callFromWithinCabinQueue.add(callFromWithinCabin);
    }

    public void stop() {
        if (stop) {
            stop = false;
            startWorking();
        }
    }

    public void startWorking() {
        while (true) {
            if (stop)
                return;

            System.out.println("current floor is " + (int) calculateCurrentFloor());

            boolean destinationReached = removeDestinationIfReached();
            CallFromOutsideCabin sameDirectionOutsideCallReached = removeSameDirectionOutsideCallIfReached();

            if (sameDirectionOutsideCallReached != null)
                callFromWithinCabinQueue.add(new CallFromWithinCabin(sameDirectionOutsideCallReached.getDestinationFloor()));

            if (destinationReached || (sameDirectionOutsideCallReached != null)) {
                elevatorCabin.openDoor();
                if (destinationReached) {
                    System.out.println("destination reached, dropping passenger");
                }
                if (sameDirectionOutsideCallReached != null) {
                    System.out.println("picking up a passenger");
                }
                elevatorCabin.closeDoor();
            }
            if(callFromOutsideCabinSet.isEmpty() && callFromWithinCabinQueue.isEmpty()) {
                System.out.println("all done");
                return;
            }

            if ((calculateCurrentDirection() != null) && calculateCurrentDirection().equals(Direction.UP)) {
                cabinDistanceFromGround += motor.up();
            }
            if ((calculateCurrentDirection() != null) && calculateCurrentDirection().equals(Direction.DOWN)) {
                cabinDistanceFromGround -= motor.down();
            }
        }
    }

    private CallFromOutsideCabin removeSameDirectionOutsideCallIfReached() {
        if (isCabinLevel()) {
            for (int i = 1; i <= floorCount; i++) {
                CallFromOutsideCabin callFromOutsideCabinUp = new CallFromOutsideCabin(Direction.UP, (int) calculateCurrentFloor(), i);
                CallFromOutsideCabin callFromOutsideCabinDown = new CallFromOutsideCabin(Direction.DOWN, (int) calculateCurrentFloor(), i);
                if (callFromWithinCabinQueue.isEmpty() && callFromOutsideCabinSet.contains(callFromOutsideCabinUp)) {
                    callFromOutsideCabinSet.remove(callFromOutsideCabinUp);
                    return callFromOutsideCabinUp;
                }
                if (callFromOutsideCabinSet.contains(callFromOutsideCabinUp) && calculateCurrentDirection().equals(Direction.UP)) {
                    callFromOutsideCabinSet.remove(callFromOutsideCabinUp);
                    return callFromOutsideCabinUp;
                } else if (callFromOutsideCabinSet.contains(callFromOutsideCabinDown) && calculateCurrentDirection().equals(Direction.DOWN)) {
                    callFromOutsideCabinSet.remove(callFromOutsideCabinDown);
                    return callFromOutsideCabinDown;
                }
            }
        }
        return null;
    }

    private boolean removeDestinationIfReached() {
        if (isCabinLevel()) {
            CallFromWithinCabin callFromWithinCabin = new CallFromWithinCabin((int) calculateCurrentFloor());
            return callFromWithinCabinQueue.remove(callFromWithinCabin);
        } else {
            return false;
        }
    }

    private Direction calculateCurrentDirection() {
        CallFromWithinCabin floor = null;
        if (!callFromWithinCabinQueue.isEmpty()) {
            floor = callFromWithinCabinQueue.iterator().next();
            if ((calculateFloorDistanceFromGround(floor.getSelectedFloor()) - cabinDistanceFromGround) == 0)
                return null;
            return (calculateFloorDistanceFromGround(floor.getSelectedFloor()) - cabinDistanceFromGround) > 0 ? Direction.UP : Direction.DOWN;
        }
        return (calculateFloorDistanceFromGround(findClosestCallFromOutside().getFloor()) - cabinDistanceFromGround) > 0 ? Direction.UP : Direction.DOWN;
    }

    private CallFromOutsideCabin findClosestCallFromOutside() {
        int distance = Integer.MAX_VALUE;
        CallFromOutsideCabin result = null;
        for (CallFromOutsideCabin callFromOutsideCabin : callFromOutsideCabinSet) {
            int distanceTillOutsideCall = callFromOutsideCabin.getDestinationFloor() - (int) calculateCurrentFloor();
            if (Math.abs(distanceTillOutsideCall) < distance)
                result = callFromOutsideCabin;
        }
        return result;
    }

    private boolean isCabinLevel() {
        return (cabinDistanceFromGround % floorHeight) == 0;
    }

    private double calculateCurrentFloor() {
        return (cabinDistanceFromGround / floorHeight) + 1;
    }

    private double calculateFloorDistanceFromGround(int floor) {
        return (floor - 1) * floorHeight;
    }

}

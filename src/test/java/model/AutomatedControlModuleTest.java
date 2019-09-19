package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class AutomatedControlModuleTest {

    @Test
    public void startWorking() {

        AutomatedControlModule automatedControlModule = new AutomatedControlModule(4, 1, 4);
        automatedControlModule.addCallFromOutsideCabin(new CallFromOutsideCabin(Direction.UP, 1, 4));
        automatedControlModule.addCallFromOutsideCabin(new CallFromOutsideCabin(Direction.DOWN, 3, 2));
        automatedControlModule.addCallFromOutsideCabin(new CallFromOutsideCabin(Direction.DOWN, 4, 1));

        automatedControlModule.startWorking();

    }
}
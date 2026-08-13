package org.example;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GreetingServiceTest {

    @Test
    public void greetReturnsHolaCopilot() {
        GreetingService greetingService = new GreetingService();

        assertEquals("hola copilot", greetingService.greet());
    }
}

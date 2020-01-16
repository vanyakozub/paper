import org.junit.Before;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game gam = new Game();
    @Before
    public void setUp() throws Exception {
        gam.firstInit();
    }
    @org.junit.jupiter.api.Test

    void getRedHead(){
        try {
            setUp();
        }
        catch (Exception e) {}
        int expected = 3;
        int actual = gam.getRedHead();
        assertEquals(expected,actual);

    }
    @org.junit.jupiter.api.Test
    void firstInit() {
    }

    @org.junit.jupiter.api.Test
    void start() {
    }

    @org.junit.jupiter.api.Test
    void tick() {
    }

    @org.junit.jupiter.api.Test
    void olyasFillingRed() {
    }

    @org.junit.jupiter.api.Test
    void getSpeed() {
        int actual = gam.getSpeed();
        int expected = 2;
        assertEquals(expected,actual);
    }

}
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player = new Player();
    @Test
    void getIsGreen() {
        boolean ex = false;
        boolean actual = player.getIsGreen();
        assertEquals(ex, actual);
    }

    @Test
    void getArea() {
        int ex = 3;
        int actual = player.getArea();
        assertEquals(ex, actual);
    }


    @Test
    void getTail() {
        int ex = 4;
        int actual = player.getTail();
        assertEquals(ex, actual);
    }

    @Test
    void getHead() {
        int ex = 6;
        int actual = player.getHead();
        assertEquals(ex, actual);
    }


}
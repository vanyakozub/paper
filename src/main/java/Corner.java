public class Corner {
    public int x;
    public int y;
    public int val;

    public Corner(int x, int y, int val) {
        this.x = x;
        this.y = y;
        this.val = val;
    }
    public String toString() {
        return new String(x + ";" + y + ";" + val);
    }
    public boolean equalsC(Corner c) {
        if (this.x==c.x && this.y ==c.y) {
            return true;
        }
        else return false;
    }
}
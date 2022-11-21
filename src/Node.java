public class Node
{
    private Point point;
    private int g;

    private int h;

    private int f;
    private Node next;
    private Node prev;

    Node(Point p) { next = prev = null; this.point = p;}

    public Point getPoint() {
        return point;
    }

    public int getG() {
        return g;
    }
    public int getH() {
        return h;
    }
    public int getF() {
        return f;
    }

    public void setG(int g) {
        this.g = g;
    }

    public Node getPrev() {
        return prev;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void setF(int f) {
        this.f = f;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
}


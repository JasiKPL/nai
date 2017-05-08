import java.util.Comparator;

class Node {
    /**
     * Komparator wg rosnacej wartosci priorytetu
     */
    static final Comparator<Node> COMP_PRIORITY = (n1, n2) -> (int) Math.signum(n1.priority - n2.priority);
    int x;
    int y;
    float priority;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        priority = 0;
    }

    public Node(int x, int y, float priority) {
        this.x = x;
        this.y = y;
        this.priority = priority;
    }

    /**
     * Wezly sa tozsame jesli ich wspolrzedne sa jednakowe
     */
    @Override
    public boolean equals(Object other) {
        boolean isEqual;

        if (other == null) {
            isEqual = false;
        } else if (other == this) {
            isEqual = true;
        } else if (!(other instanceof Node)) {
            isEqual = false;
        } else {
            Node n = (Node) other;
            isEqual = x == n.x && y == n.y;
        }
        return isEqual;
    }
}

import java.util.*;

/**
 * Reprezentacja grafu. Nalezy go traktowac jako mape o rozmiarze width x height,
 * gdzie na kazde pole (z wyjatkiem scian) mozna wejsc.
 *
 * UWAGA. Brak sprawdzania, czy wszsytkie wspolrzedne sa calkowite  i > 0.
 */
class Graph {
    private final int DEFAULT_WEIGHT = 1;
    private Map<Node, Integer> weights = new HashMap<>();
    private List<Node> walls = new ArrayList<>();
    private int width;
    private int height;

    public Graph(int width, int height, List<Node> walls, Map<Node, Integer> weights) {
        this.width = width;
        this.height = height;
        this.walls = walls;
        this.weights = weights;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    /**
     * Zwraca liste wszystkich prawidlowych sasiadow
     *
     * @param node Wezel, ktorego sasiedzi maja byc zwroceni
     * @return Lista sasiadow, na ktorych mozna wejsc
     */
    List<Node> neighbours(Node node) {
        // Stworz liste wszystkich sasiadujacych wezlow
        List<Node> results = new LinkedList<>(Arrays.asList(
                new Node(node.x + 1, node.y),
                new Node(node.x, node.y + 1),
                new Node(node.x - 1, node.y),
                new Node(node.x, node.y - 1)
        ));

        // Odfiltruj wezly niemieszczace sie w grafie...
        results.removeIf(n -> !inBounds(n));
        // ...i te, na ktore nie mozna wejsc
        results.removeIf(n -> !isPassable(n));
        return results;
    }

    int cost(Node fromNode, Node toNode) {
        return weights.getOrDefault(toNode, DEFAULT_WEIGHT);
    }

    /**
     * Sprawdza czy wezel zawiera sie w grafie
     *
     * @param n Wezel do sprawdzenia
     * @return Czy wezel zawiera sie w grafie
     */
    boolean inBounds(Node n) {
        return 0 <= n.x && n.x < width && 0 <= n.y && n.y < height;
    }

    /**
     * Sprawdza czy przez wezel n da sie przejsc
     *
     * @param n Wezel do sprawdzenia
     * @return Czy na wezel n da sie wejsc
     */
    boolean isPassable(Node n) {
        return !walls.contains(n);
    }
}

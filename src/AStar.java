import java.util.*;
import java.util.function.Function;

public class AStar {

    private Graph graph;
    private Node start;
    private Node goal;

    public AStar(Graph graph, Node start, Node goal) {
        this.graph = graph;
        this.start = start;
        this.goal = goal;
    }

    public static void main(String[] args) {
        // Na te pola nie bedzie mozna wejsc
        ArrayList<Node> walls = new ArrayList<>(Arrays.asList(
                new Node(1, 7),
                new Node(1, 8),
                new Node(2, 7),
                new Node(2, 8),
                new Node(3, 7),
                new Node(3, 8))
        );

        // Te pola beda trudniejsze do wejscia (np. gory albo bagno)
        List<Node> hardTerrain = new ArrayList<>(Arrays.asList(
                new Node(3, 4), new Node(3, 5), new Node(4, 1), new Node(4, 2),
                new Node(4, 3), new Node(4, 4), new Node(4, 5), new Node(4, 6),
                new Node(4, 7), new Node(4, 8), new Node(5, 1), new Node(5, 2),
                new Node(5, 3), new Node(5, 4), new Node(5, 5), new Node(5, 6),
                new Node(5, 7), new Node(5, 8), new Node(6, 2), new Node(6, 3),
                new Node(6, 4), new Node(6, 5), new Node(6, 6), new Node(6, 7),
                new Node(7, 3), new Node(7, 4), new Node(7, 5))
        );
        Map<Node, Integer> weights = new HashMap<>();
        for (Node t : hardTerrain) {
            weights.put(t, 5); // wszystkie trudne pola maja wage 5, ale moglyby miec rozne
        }

        Graph graph = new Graph(10, 10, walls, weights);
        AStar aStar = new AStar(graph, new Node(1, 4), new Node(7, 8));
        AStar.Result result = aStar.search();
        System.out.println("Sciezka przejscia: ");
        result.drawPath();
        System.out.println("\nWartosc przejscia: ");
        result.drawCost();
    }

    /**
     * Prosta heurystyka do estymacji kosztu przejscia z wezla a do b
     *
     * @param a Wezel startowy
     * @param b Wezel docelowy
     * @return szacowany koszt przejscia
     */
    private float manhattanDistance(Node a, Node b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    /**
     * Implementacja algorytmu A*
     *
     * @param graph Graf zrodlowy
     * @param start Wezel startowy
     * @param goal  Wezel docelowy
     * @return Kierunek i koszt poruszania
     */
    Result search() {
        Queue<Node> frontier = new PriorityQueue<>(Node.COMP_PRIORITY);
        frontier.add(start);
        Map<Node, Node> cameFrom = new HashMap<>();
        Map<Node, Integer> costSoFar = new HashMap<>();
        cameFrom.put(start, null);
        costSoFar.put(start, 0);

        while (!frontier.isEmpty()) {
            Node current = frontier.poll();

            if (current.equals(goal)) {
                break;
            }

            for (Node next : graph.neighbours(current)) {
                int newCost = (int) (costSoFar.get(current) + graph.cost(current, next));
                if (!costSoFar.containsKey(next) || newCost < costSoFar.get(next)) {
                    costSoFar.put(next, newCost);
                    next.priority = newCost + manhattanDistance(goal, next);
                    frontier.add(next);
                    cameFrom.put(next, current);
                }
            }
        }

        return new Result(cameFrom, costSoFar);
    }

    class Result {
        Map<Node, Node> pointTo;
        Map<Node, Integer> moveCost;

        Result(Map<Node, Node> pointTo, Map<Node, Integer> moveCost) {
            this.pointTo = pointTo;
            this.moveCost = moveCost;
        }

        /**
         * Wypisz wyliczone sciezki
         */
        void drawPath() {
            draw(node -> {
                String s = null;
                Node pointed = pointTo.getOrDefault(node, null);

                // Wybierz strzalke na podstawie pozycji poprzedniego wezla
                if (pointed != null) {
                    if (pointed.x == node.x + 1) {
                        s = "\u2192";
                    } else if (pointed.x == node.x - 1) {
                        s = "\u2190";
                    } else if (pointed.y == node.y + 1) {
                        s = "\u2193";
                    } else if (pointed.y == node.y - 1) {
                        s = "\u2191";
                    }
                }
                return s;
            });
        }

        /**
         * Wypisz wyliczony koszt
         */
        void drawCost() {
            draw(node -> {
                String s = null;
                int cost = moveCost.getOrDefault(node, Integer.MIN_VALUE);
                if (cost != Integer.MIN_VALUE) {
                    s = Integer.toString(cost);
                }
                return s;
            });
        }

        /**
         * Wypisuje rozwiazanie w postaci: A dla wezla startowego, Z dla wezla koncowego,
         * . dla wezla nieodwiedzonego, # dla wezla, ktory jest sciana
         *
         * @param method Dodatkowa metoda formatujaca, w zaleznosci co uzytkownik chce wypisac
         */
        private void draw(Function<Node, String> method) {
            for (int y = 0; y < graph.getHeight(); y++) {
                for (int x = 0; x < graph.getWidth(); x++) {
                    Node current = new Node(x, y);
                    String r = ".";

                    String c = method.apply(current);
                    if (c != null) {
                        r = c;
                    }

                    if (start.equals(current)) {
                        r = "A";
                    }
                    if (goal.equals(current)) {
                        r = "Z";
                    }
                    if (!graph.isPassable(current)) {
                        r = "#";
                    }

                    System.out.print(String.format("%3s", r));
                }
                System.out.print('\n');
            }
        }
    }
}

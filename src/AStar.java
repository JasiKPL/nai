import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class AStar {

    class Result {
        Map<Node, Node> cameFrom;
        Map<Node, Float> moveCost;

        Result(Map<Node, Node> cameFrom, Map<Node, Float> moveCost) {
            this.cameFrom = cameFrom;
            this.moveCost = moveCost;
        }
    }

    private float manhattanDistance(Node a, Node b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    public Result search(Graph graph, Node start, Node goal) {
        Queue<Node> frontier = new PriorityQueue<>(Node.COMP_PRIORITY);
        frontier.add(start);
        Map<Node, Node> cameFrom = new HashMap<>();
        Map<Node, Float> costSoFar = new HashMap<>();
        cameFrom.put(start, null);
        costSoFar.put(start, 0f);

        while (!frontier.isEmpty()) {
            Node current = frontier.poll();

            if (current.equals(goal)) {
                break;
            }

            for (Node next : graph.neighbours(current)) {
                float newCost = costSoFar.get(current) + graph.cost(current, next);
                if (costSoFar.containsKey(next) || newCost < costSoFar.get(next)) {
                    costSoFar.put(next, newCost);
                    next.priority = newCost + manhattanDistance(goal, next);
                    frontier.add(next);
                    cameFrom.put(next, current);
                }
            }
        }

        return new Result(cameFrom, costSoFar);
    }

}

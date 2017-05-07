import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class AStar {
    private float manhattanDistance(Vertex a, Vertex b) {
        return Math.abs(a.point.x - b.point.x) + Math.abs(a.point.y - b.point.y);
    }

    public void search(Graph graph, Vertex start, Vertex goal) {
        Queue<Vertex> frontier = new PriorityQueue<>();
        frontier.add(start);
        Map<Vertex, Vertex> cameFrom = new HashMap<>();
        Map<Vertex, Float> costSoFar = new HashMap<>();
        cameFrom.put(start, null);
        costSoFar.put(start, 0f);

        while (!frontier.isEmpty()) {
            Vertex current = frontier.poll();

            if (current.equals(goal)) {
                break;
            }

            for(Vertex next : graph.neighbours(current)) {
                float newCost = costSoFar.get(current) + graph.cost(current, next);
                if (costSoFar.containsKey(next) || newCost < costSoFar.get(next)) {
                    costSoFar.put(next, newCost);
                    next.priority = newCost + manhattanDistance(goal, next);
                    frontier.add(next);
                    cameFrom.put(next, current);
                }
            }
        }
    }

}

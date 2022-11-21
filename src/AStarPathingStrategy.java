import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy implements PathingStrategy
{
    public class Node {
        private Point p;
        private double f;
        private Node parent;

        public Node(Point p,double f){
            this.p=p;
            this.f=f;
        }
        public double getF() {
            return f;
        }
        public Point getP() {
            return p;
        }
        public Node getParent() {
            return parent;
        }
        public void setParent(Node parent) {
            this.parent = parent;
        }
    }

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        // set up final path, open, closed lists
        List<Point> path = new ArrayList<>();
        HashMap<Point,Point> closed = new HashMap<>(); // for O(1) add
        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparing(Node::getF)); // for O(1) removal
        // count distance from origin point using g
        int g = 0;
        open.add(new Node(start, g + Point.distanceTo(start, end)));
        while(!open.isEmpty()) {
            g++;
            // pop first element from priority queue to get lowest node
            Node curr = open.poll();
            closed.put(curr.getP(), curr.getP());
            // if end node is within reach
            if(withinReach.test(curr.getP(), end)) return constructPath(curr);
        // create and filter neighbors list to get valid list of non obstacles
        List<Point> neighbors = potentialNeighbors.apply(curr.getP()).
                filter(p -> !(start.equals(p) && end.equals(p))).
                filter(canPassThrough). collect(Collectors.toList());
             // iterate through each neighbor point to see if has been checked already to add to open list
            for (Point neighbor : neighbors) {
                if (!closed.containsKey(neighbor)) {
                    // add neighbor to list if not in closed list, set parent to current node
                    Node n = new Node(neighbor, Point.distanceTo(neighbor, end) + g);
                    closed.put(neighbor, neighbor);
                    n.setParent(curr);
                    open.add(n);
                }
            }
        }
        return path;
    }
    public List<Point> constructPath(Node node) {
        List<Point> path = new ArrayList<>();
        while (node.getParent() != null) {
            path.add(node.getP());
            node = node.getParent();
        }
        Collections.reverse(path);
        return path;
    }
}
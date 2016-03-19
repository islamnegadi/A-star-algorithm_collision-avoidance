import java.util.ArrayList;
import java.util.Collections;

public class Astar {

    public Astar() {
        /*empty constructor*/
    }

    /* A* main function */
    public ArrayList<Node> findPath(Node start, Node target) {
        ArrayList<Node> openList = new ArrayList<>();
        ArrayList<Node> closedList = new ArrayList<>();

        start.setG(0);
        start.setH(manhattanDistance(start, target));
        start.setParent(null);
        openList.add(start);

        int nodes = 0;

        Node current;
        do {
            current = lowestScore(openList);
            closedList.add(current);
            openList.remove(current);

            if (isInList(target, closedList)) {
                System.out.println("total nodes A: " + nodes);
                return getPath(current);
            }

            ArrayList<Node> neighbours = getNeighbours(current);
            nodes += neighbours.size();

            for (Node n : neighbours) {
                n.setG(current.getG() + 1);
                n.setParent(current);

                if (isInList(n, closedList)) {
                    continue;
                }

                if (!isInList(n, openList)) {
                    openList.add(n);
                } else {
                    if (current.getG() + 1 < n.getG()) {
                        n.setParent(current);
                    }
                }
            }

        } while (!openList.isEmpty());

        return null;
    }

    public ArrayList<Node> findPathConsideringOtherRobot(Node start, Node target, ArrayList<Node> path) {
        ArrayList<Node> openList = new ArrayList<>();
        ArrayList<Node> closedList = new ArrayList<>();

        start.setG(0);
        start.setH(manhattanDistance(start, target));
        start.setParent(null);
        openList.add(start);

        int nodes = 0;

        Node current;
        do {

            current = lowestScore(openList);
            int i = getPathPosition(current);
            if (i-1 < path.size()-1) {
                if (path.get(i - 1).equals(current)) {
                    //System.out.println("index: " + i + ", node: " + current.toString());
//                System.out.println("Robot 2 considering new position " + current.toString() + " at step " + i + " ** Conflict **");
//                System.out.println("Resolving -- Robot 2 considering alternative new position "
//                        +  nextLowestScore(openList, current).toString() + " at step " + i);
                    current = nextLowestScore(openList, current);
                }
            }

            closedList.add(current);
            openList.remove(current);

            if (isInList(target, closedList)) {
                System.out.println("total nodes B: " + nodes);
                return getPath(current);
            }

            ArrayList<Node> neighbours = getNeighbours(current);
            nodes += neighbours.size();

            for (Node n : neighbours) {
                n.setG(current.getG() + 1);
                n.setParent(current);

                if (isInList(n, closedList)) {
                    continue;
                }

                if (!isInList(n, openList)) {
                    openList.add(n);
                } else {
                    if (current.getG() + 1 < n.getG()) {
                        n.setParent(current);
                    }
                }
            }

        } while (!openList.isEmpty());

        return null;
    }

    public Node lowestScore(ArrayList<Node> list) {
        double minF = list.get(0).getF();
        Node node = list.get(0);
        for (Node n : list) {
            if (n.getF() < minF) {
                minF = n.getF();
                node = n;
            }
        }
        return node;
    }

    public Node nextLowestScore(ArrayList<Node> list, Node excludedNode) {
        double minF = list.get(0).getF();
        Node node = list.get(0);
        for (Node n : list) {
            if (n.getF() < minF && !n.equals(excludedNode)) {
                minF = n.getF();
                node = n;
            }
        }
        return node;
    }

    public boolean isInList(Node node, ArrayList<Node> list) {
        for (Node n : list) {
            if (n.equals(node)) {
                return true;
            }
        }
        return false;
    }

    public int manhattanDistance(Node n1, Node n2) {
        int dis = Math.abs(n1.getPosition().getRow() - n2.getPosition().getRow())
                + Math.abs(n1.getPosition().getCol() - n2.getPosition().getCol());

        return dis;
    }

    public boolean isInsideBounds(Point point, int rows, int columns){
        int row = point.getRow();
        int col = point.getCol();
        return (row >= 1) && (row <= rows) && (col >= 1) && (col <= columns);
    }

    public boolean isObstacle(Node node) {
        RoomScanner roomScanner = new RoomScanner();
        ArrayList<Node> obstacles = roomScanner.getObstacles();
        for (Node n : obstacles) {
            if (node.equals(n)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Node> getNeighbours(Node node) {
        RoomScanner roomScanner = new RoomScanner();

        int roomRows = roomScanner.getRows();
        int roomColumns = roomScanner.getColumns();

        Point pos = node.getPosition();
        int row = pos.getRow();
        int col = pos.getCol();

        ArrayList<Node> neighbours = new ArrayList<>();
        Node north = new Node();
        north.setPosition(new Point(row - 1, col));

        Node east = new Node();
        east.setPosition(new Point(row, col + 1));

        Node south = new Node();
        south.setPosition(new Point(row + 1, col));

        Node west = new Node();
        west.setPosition(new Point(row, col - 1));

        if (isInsideBounds(north.getPosition(), roomRows, roomColumns) && !isObstacle(north)) {
            neighbours.add(north);
        }
        if (isInsideBounds(east.getPosition(), roomRows, roomColumns) && !isObstacle(east)) {
            neighbours.add(east);
        }
        if (isInsideBounds(south.getPosition(), roomRows, roomColumns) && !isObstacle(south)) {
            neighbours.add(south);
        }
        if (isInsideBounds(west.getPosition(), roomRows, roomColumns) && !isObstacle(west)) {
            neighbours.add(west);
        }

        for (Node n : neighbours) {
            n.setH(manhattanDistance(n, roomScanner.getTargetNode()));
        }

        return neighbours;
    }

    public ArrayList<Node> getPath(Node target) {
        ArrayList<Node> path = new ArrayList<>();
        Node node = target.getParent();
        while (node != null) {
            path.add(node);
            node = node.getParent();
        }
        Collections.reverse(path);
        return path;
    }

    public int getPathPosition(Node node) {
        Node n = node;
        int i = 0;
        while (n != null) {
            i++;
            n = n.getParent();
        }
        return i;
    }
}

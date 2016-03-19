/**
 * Two robots, Robot-1 and Robot-2 move in a squared grid with obstacles
 * There are some positions where both robots have to pass from and
 * there is also a final target that they have to reach and meet.
 * Robot-1 uses the A* algorithm to calculate the shortest path from
 * each position to the next one. Robot-2 considers the path of Robot-1 and adapts
 * its path in order to avoid collisions with Robot-1.
 *
 * The method followed to avoid collisions is described bellow:
 * Robot-2 calculates the shortest path without considering the path of Robot-1
 * Then, this path is modified and if there is collisions the path of Robot-2
 * simply goes around the Robot-1 and continues its way to the target.
 */

import javax.swing.*;
import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {

        long start;
        long end;

        start = System.nanoTime();

        RoomScanner roomScanner = new RoomScanner();

        /*target node*/
        Node target = new Node();
        target.setPosition(roomScanner.getTargetPos());
        System.out.println("target: " + target.toString());

        /*Nodes that both robots have to visit*/
        ArrayList<Node> goals = roomScanner.getGoals();

        /*Robot-1*/
        Node startA = new Node();
        startA.setPosition(roomScanner.getStartA());
        System.out.println("startA: " + startA.toString());

        ArrayList<Node> goalsA = new ArrayList<>(goals);
        goalsA.add(0, startA);
        goalsA.add(target);
        System.out.println("goalsA: " + goalsA);

        /*Robot-2*/
        Node startB = new Node();
        startB.setPosition(roomScanner.getStartB());
        System.out.println("startB: " + startB.toString());

        ArrayList<Node> goalsB = new ArrayList<>(goals);
        goalsB.add(0, startB);
        goalsB.add(target);
        System.out.println("goalsB: " + goalsB);

        /* A* for Robot-1 */
        Astar astar = new Astar();
        ArrayList<Node> pathA = new ArrayList<>();
        for (int i = 0; i < goalsA.size()-1; i++) {
            pathA.addAll(astar.findPath(goalsA.get(i), goalsA.get(i+1)));
        }

        /* A* for Robot-2 */
        ArrayList<Node> pathB = new ArrayList<>();
        for (int i = 0; i < goalsB.size()-1; i++) {
            //pathB.addAll(astar.findPathConsideringOtherRobot(goalsB.get(i), goalsB.get(i + 1), pathA));
            pathB.addAll(astar.findPath(goalsB.get(i), goalsB.get(i + 1)));
        }

        System.out.println("Robot-1: " + pathA);
        System.out.println("Robot-2: " + pathB);

        end = System.nanoTime();
        double elapsedSeconds = (end - start) / 1000000000;

        System.out.println("Total time: " + String.valueOf(elapsedSeconds));

        /*draw path of Robot-1*/
        JFrame frameA = new JFrame("Robot-1");
        frameA.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pathA.add(target);
        Painter painterA = new Painter(pathA);
        frameA.add(painterA);
        frameA.setSize(700, 500);
        frameA.setVisible(true);

        /*draw path of Robot-1*/
        JFrame frameB = new JFrame("Robot-2");
        frameB.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pathB.add(target);
        Painter painterB = new Painter(pathB);
        frameB.add(painterB);
        frameB.setSize(700, 500);
        frameB.setVisible(true);
    }
}

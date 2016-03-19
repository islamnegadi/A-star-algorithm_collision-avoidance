import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Painter extends JPanel {
    private ArrayList<Node> tempPathA;
//    private ArrayList<Node> tempPathB;

    private int i = 0;
    public Painter(final ArrayList<Node> pathA) {
        this.tempPathA = new ArrayList<>();
//        this.tempPathB = new ArrayList<>();

        final Timer timer = new Timer(200, null);
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                tempPathA.add(pathA.get(i));
                //tempPathA.add(pathA.get(i + 1));
                repaint();
                i++;

                if (i > pathA.size()-1) {
                    timer.removeActionListener(this);
                    timer.stop();
                    i = 0;
                }
            }
        });
        timer.start();
    }

    public void paint(Graphics g) {
        super.paintComponent(g);

        this.setBackground(Color.GRAY);

        final int rectSize = 30;
        final int offset = 0;

        RoomScanner roomScanner = new RoomScanner();
        int roomRows = roomScanner.getRows();
        int roomCols = roomScanner.getColumns();
        ArrayList<Node> obstacles = roomScanner.getObstacles();
        ArrayList<Node> goals = roomScanner.getGoals();
        Point startA = roomScanner.getStartA();
        Point startB = roomScanner.getStartB();
        Point target = roomScanner.getTargetPos();

        /*draw grid*/
        for (int j = 0; j < roomRows; j++) {
            for (int i = 0; i < roomCols; i++) {
                Color color = Color.WHITE;

                g.setColor(color);
                g.fillRect(offset + i * rectSize, offset + j*rectSize, rectSize, rectSize);
                g.setColor(Color.BLACK);
                g.drawRect(offset + i * rectSize, offset + j*rectSize, rectSize, rectSize);
            }
        }

        /*draw obstacles*/
        for (Node n : obstacles) {
            Color color = Color.BLUE;
            int row = n.getPosition().getRow()-1;
            int col = n.getPosition().getCol()-1;

            g.setColor(color);
            g.fillRect(offset + col * rectSize, offset + row*rectSize, rectSize, rectSize);
            g.setColor(Color.BLACK);
            g.drawRect(offset + col * rectSize, offset + row*rectSize, rectSize, rectSize);
        }

        /*draw goals*/
        for (Node n : goals) {
            Color color = Color.ORANGE;
            int row = n.getPosition().getRow()-1;
            int col = n.getPosition().getCol()-1;

            g.setColor(color);
            g.fillRect(offset + col * rectSize, offset + row*rectSize, rectSize, rectSize);
            g.setColor(Color.BLACK);
            g.drawRect(offset + col * rectSize, offset + row*rectSize, rectSize, rectSize);
        }

        /*draw path*/
        for (int i = 0; i <= tempPathA.size()-2; i++) {
            Node node = tempPathA.get(i);
            Node nodeNext = tempPathA.get(i+1);

            int row = node.getPosition().getRow()-1;
            int col = node.getPosition().getCol()-1;

            int rowNext = nodeNext.getPosition().getRow()-1;
            int colNext = nodeNext.getPosition().getCol()-1;

            try {
                BufferedImage arrowDown = ImageIO.read(
                        new File("C:\\Users\\thodoris\\Documents\\EMP\\Eksamino 7\\ARTIFICIAL INTELLIGENCE\\Project1\\arrow_black.png"));
                BufferedImage arrowUp = rotateImage(arrowDown, Math.PI);
                BufferedImage arrowRight = rotateImage(arrowDown, -Math.PI/2);
                BufferedImage arrowLeft = rotateImage(arrowDown, Math.PI/2);

                int arrowSize = arrowDown.getHeight();

                int centerX = offset + col * rectSize + rectSize/2 - arrowSize/2;
                int centerY = offset + row*rectSize + rectSize/2 - arrowSize/2;

                if (rowNext > row) {
                    g.drawImage(arrowDown, centerX, centerY, null);
                }
                else if (rowNext < row) {
                    g.drawImage(arrowUp, centerX, centerY, null);
                }
                else if (colNext > col) {
                    g.drawImage(arrowRight, centerX, centerY, null);
                }
                else if (colNext < col) {
                    g.drawImage(arrowLeft, centerX, centerY, null);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*startA, startB and target*/
        int row = startA.getRow()-1;
        int col = startA.getCol()-1;
        g.setColor(Color.GREEN);
        g.fillRect(offset + col * rectSize, offset + row*rectSize, rectSize, rectSize);
        g.setColor(Color.BLACK);
        g.drawRect(offset + col * rectSize, offset + row*rectSize, rectSize, rectSize);

        row = startB.getRow()-1;
        col = startB.getCol()-1;
        g.setColor(Color.GREEN);
        g.fillRect(offset + col * rectSize, offset + row*rectSize, rectSize, rectSize);
        g.setColor(Color.BLACK);
        g.drawRect(offset + col * rectSize, offset + row*rectSize, rectSize, rectSize);

        row = target.getRow()-1;
        col = target.getCol()-1;
        g.setColor(Color.RED);
        g.fillRect(offset + col * rectSize, offset + row*rectSize, rectSize, rectSize);
        g.setColor(Color.BLACK);
        g.drawRect(offset + col * rectSize, offset + row*rectSize, rectSize, rectSize);
    }

    public BufferedImage rotateImage(BufferedImage image, double theta) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(theta, image.getWidth()/2, image.getHeight()/2);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(image, null);
    }

    public int getI() {
        return i;
    }

    public void incI() {
        this.i = this.i + 1;
    }
}

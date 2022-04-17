import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

class MyFrame
        extends JFrame
        implements ActionListener {

    /* Form Components */
    private final Container c;
    private final JLabel title;

    private final JLabel label1;
    private final JTextField value1;

    private final JLabel label2;
    private final JTextField value2;

    private final JButton submitButton;

    private final JLabel response;

    /* constructor, to initialize components */
    public MyFrame()
    {
        String heading = "Membuat Lingkaran";

        setTitle(heading);
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);

        title = new JLabel(heading);
        title.setFont(new Font("Arial", Font.PLAIN, 24));
        title.setSize(400, 30);
        title.setLocation(300, 30);
        c.add(title);

        label1 = new JLabel("Pusat Lingkaran (x y)");
        label1.setFont(new Font("Arial", Font.PLAIN, 14));
        label1.setSize(200, 20);
        label1.setLocation(100, 100);
        c.add(label1);

        value1 = new JTextField();
        value1.setFont(new Font("Arial", Font.PLAIN, 15));
        value1.setSize(190, 20);
        value1.setLocation(250, 100);
        c.add(value1);

        label2 = new JLabel("Jari-jari lingkaran (r)");
        label2.setFont(new Font("Arial", Font.PLAIN, 14));
        label2.setSize(200, 20);
        label2.setLocation(100, 150);
        c.add(label2);

        value2 = new JTextField();
        value2.setFont(new Font("Arial", Font.PLAIN, 15));
        value2.setSize(190, 20);
        value2.setLocation(250, 150);
        c.add(value2);

        submitButton = new JButton("Buat Lingkaran");
        submitButton.setFont(new Font("Arial", Font.PLAIN, 15));
        submitButton.setSize(200, 20);
        submitButton.setLocation(100, 200);
        submitButton.addActionListener(this);
        c.add(submitButton);

        response = new JLabel("");
        response.setFont(new Font("Arial", Font.PLAIN, 14));
        response.setSize(500, 25);
        response.setLocation(100, 300);
        c.add(response);
        setVisible(true);
    }

    /* method to get the action performed by the user and act accordingly */
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == submitButton) {
            int x = 0;
            int y = 0;
            int radius;
            String xy = value1.getText().trim();
            String[] values;

            try {
                if(value1.getText().equals("") || value2.getText().equals("")) {
                    throw new Exception("Nilai pada input harus di isi semua !");
                }

                if(xy.equals("")) {
                    throw new Exception("Nilai pada input pusat lingkaran harus memiliki format (x y) !");
                }

                values = xy.split(" ");
                if(values.length != 2) {
                    throw new Exception("Nilai pada input pusat lingkaran harus memiliki format (x y) !");
                }

                x = Integer.parseInt(values[0]);
                y = Integer.parseInt(values[1]);
                radius = Integer.parseInt(value2.getText());

                if( x < 1 || y < 1 || radius < 1 ) {
                    throw new Exception("Nilai pada input harus lebih dari 0 !");
                }

                setVisible(false);
                JPanel panel = new CirclePanel(x, y, radius * 2);
                ((CirclePanel) panel).init();

            } catch (NumberFormatException err) {
                response.setText("Nilai pada input harus berisi angka !");
                response.setForeground(Color.RED);
            } catch (Exception err) {
                response.setText(err.getMessage());
                response.setForeground(Color.RED);
            }
        }
    }

}

class CirclePanel extends JPanel {
    private static final int D_W = 500;
    private static final int D_H = 500;

    private int x = 0;
    private int y = 0;
    private int diameter;

    Ellipse2D.Double circle = new Ellipse2D.Double(100, 60, 100, 100);

    Point p = new Point();
    private boolean insideCircle = false;

    public CirclePanel(int x, int y, int diameter) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;

        addMouseMotionListener(new MouseMotionAdapter(){
            public void mouseMoved(MouseEvent e) {
                insideCircle = circle.contains(e.getPoint());
                p = e.getPoint();
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.draw(circle);
        if (insideCircle) {
            g.drawString("Mouse point is INSIDE the circle", (int)p.getX(), (int)p.getY());
        } else {
            g.drawString("OUTSIDE" , (int)p.getX(), (int)p.getY());
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(D_W, D_H);
    }

    public void init() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                frame.add(new CirclePanel(x, y, diameter));
                frame.setTitle("Hasil Lingkaran");
                frame.pack();
                frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}

public class Main {

    public static void main(String[] args) throws Exception
    {
        MyFrame f = new MyFrame();
    }
}

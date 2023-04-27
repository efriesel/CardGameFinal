import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class GameViewer extends JFrame implements KeyListener, ActionListener{
    public final Font HEADER_FONT = new Font("Sans-Serif", 1, 36);
    public final Font ENDING_FONT = new Font("Sans-Serif", 1, 50);
    public final Font SMALL_FONT = new Font("Sans-Serif", 1, 20);
    private final int HEADER_HEIGHT = 22;
    public final int WINDOW_WIDTH = 800;
    public final int WINDOW_HEIGHT = 822;
    public final int Y_HEIGHT = WINDOW_HEIGHT - HEADER_HEIGHT;
    public final int BUTTON_WIDTH = 100;
    public final int BUTTON_HEIGHT = 50;
    public final int DELAY_IN_MILLISEC = 20;
    private final Image BACKGROUND_IMAGE = new ImageIcon("Resources/Background.jpeg").getImage();
    private final Image BACK_OF_CARD = new ImageIcon("Resources/Back.png").getImage();
    public final int WELCOME_SCREEN = 0;
    public final int NAME_COLLECTION = 1;
    public final int GAME_PLAY = 2;
    private final Game game;
    private int state;
    private ArrayList<Player> players;
    private int playerCount;

    JButton submit = new JButton("Submit");

    public GameViewer (Game game, ArrayList<Player> players) {
        this.game = game;
        this.players = players;
        playerCount = players.size();
        //default operations with name "Poker"
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setTitle("Poker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);
        setVisible(true);
        Timer clock= new Timer(DELAY_IN_MILLISEC, this);
        clock.start();
        repaint();
    }

    public void paint(Graphics g)
    {
        myPaint(g);
        BufferStrategy bf = this.getBufferStrategy();
        if (bf == null)
            return;

        Graphics g2 = null;
        try
        {
            g2 = bf.getDrawGraphics();

            // myPaint does the actual drawing
            myPaint(g2);
        }
        finally
        {
            // It is best to dispose() a Graphics object when done with it.
            g2.dispose();
        }

        // Shows the contents of the backbuffer on the screen.
        bf.show();

        //Tell the System to do the Drawing now, otherwise it can take a few extra ms until
        //Drawing is done which looks very jerky
        Toolkit.getDefaultToolkit().sync();
    }

    public void myPaint(Graphics g){
        printBackground(g);
        if (state == WELCOME_SCREEN) {
            printInstructions(g);
        }
        else if (state == NAME_COLLECTION) {
        }
        else if (state == GAME_PLAY){

        }
    }

    private void printBackground(Graphics g){
        g.drawImage(BACKGROUND_IMAGE, 0, HEADER_HEIGHT, WINDOW_WIDTH, WINDOW_HEIGHT, this);
    }

    public void printInstructions(Graphics g){
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(WINDOW_WIDTH / 4, Y_HEIGHT / 4 + HEADER_HEIGHT, WINDOW_WIDTH / 2, Y_HEIGHT / 2);
        g.setFont(SMALL_FONT);
        g.setColor(Color.BLACK);
        g.drawString("Welcome to poker,", WINDOW_WIDTH / 4, Y_HEIGHT / 4 + HEADER_HEIGHT + SMALL_FONT.getSize());
        g.drawString("for information about ", WINDOW_WIDTH / 4, Y_HEIGHT / 4 + HEADER_HEIGHT + SMALL_FONT.getSize() * 2);
        g.drawString("how to play, look it up", WINDOW_WIDTH / 4, Y_HEIGHT / 4 + HEADER_HEIGHT + SMALL_FONT.getSize() * 3);
        int y = Y_HEIGHT / 4 * 3 + HEADER_HEIGHT - BUTTON_HEIGHT * 2;
        submit.setBounds(WINDOW_WIDTH / 2 - BUTTON_WIDTH / 2, y - BUTTON_HEIGHT / 2, BUTTON_WIDTH, BUTTON_HEIGHT);
        submit.addActionListener(this);
        add(submit);
        g.drawImage(new ImageIcon("Resources/Button.jpeg").getImage(), WINDOW_WIDTH / 2 - BUTTON_WIDTH / 2,
                                                                        y, BUTTON_WIDTH, BUTTON_HEIGHT, this);
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (state == WELCOME_SCREEN) {
            String s = e.getActionCommand();
            if (s != null && s.equals("Submit")) {
                System.out.println("works");
                state++;
            }
        }
        else if (state == NAME_COLLECTION) {

        }
        else if (state == GAME_PLAY){

        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


}

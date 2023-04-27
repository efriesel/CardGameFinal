import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class GameViewer extends JFrame implements ActionListener {
    private static final int MAX_PLAYERS = 4;

    public final Font HEADER_FONT = new Font("Sans-Serif", 1, 36);
    public final Font ENDING_FONT = new Font("Sans-Serif", 1, 50);
    public final Font SMALL_FONT = new Font("Sans-Serif", 1, 20);
    private final int HEADER_HEIGHT = 22;
    public final int WINDOW_WIDTH = 800;
    public final int WINDOW_HEIGHT = 822;
    public final int Y_HEIGHT = WINDOW_HEIGHT - HEADER_HEIGHT;
    public final int BUTTON_WIDTH = 100;
    public final int BUTTON_HEIGHT = 50;
    private final Image BACKGROUND_IMAGE = new ImageIcon("Resources/Background.jpeg").getImage();
    private final Image BACK_OF_CARD = new ImageIcon("Resources/Back.png").getImage();
    public final int WELCOME_SCREEN = 0;
    public final int NUMBER_OF_PLAYER_COLLECTION = 1;
    public final int NAME_COLLECTION = 2;
    public final int BET_PLAY = 3;
    private Game game;

    private int state;
    private ArrayList<Player> players;
    private int playerCount;

    JButton submit = new JButton("Submit");
    JTextField field = new JTextField();

    int count;

    public GameViewer (Game game) {
        this.game = game;
        players = new ArrayList<>();
        //default operations with name "Poker"
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setTitle("Poker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        submit.setBounds(WINDOW_WIDTH / 2 - BUTTON_WIDTH / 2, Y_HEIGHT / 4 * 3 + HEADER_HEIGHT - BUTTON_HEIGHT * 2
                - BUTTON_HEIGHT / 2, BUTTON_WIDTH, BUTTON_HEIGHT);
        submit.addActionListener(this);
        field.setBounds(WINDOW_WIDTH / 4, Y_HEIGHT / 11 * 5 + HEADER_HEIGHT, WINDOW_WIDTH / 2,
                Y_HEIGHT / 11);
        setVisible(true);
    }

    public void paint(Graphics g)
    {
        printBackground(g);
        if (state == WELCOME_SCREEN) {
            printInstructions(g);
            add(submit);
        }
        else if (state == NUMBER_OF_PLAYER_COLLECTION){
            displayPlayerCountRequirements(g);
            add(submit);
            add(field);
        }
        else if (state == NAME_COLLECTION) {
            askForPlayers(g);
            add(submit);
            add(field);
        }
        else if (state == BET_PLAY){

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (state == WELCOME_SCREEN) {
            String s = e.getActionCommand();
            if (s != null && s.equals("Submit")) {
                System.out.println("works");
                state++;
                add(field);
                repaint();
            }
        }
        else if (state == NUMBER_OF_PLAYER_COLLECTION){
            String s = e.getActionCommand();
            if (s != null && s.equals("Submit")) {
                String current = field.getText();
                System.out.println(current);
                if ((isNumeric(current))) {
                    int currentPlayers = Integer.parseInt(current);
                    if (currentPlayers > 1 && currentPlayers <= MAX_PLAYERS) {
                        state++;
                        playerCount = currentPlayers;
                        count = 0;
                    }
                }
                repaint();
            }
        }
        else if (state == NAME_COLLECTION) {
            String current = field.getText();
            if (current != null) {
                players.add(new Player(current, Game.INITIAL_MONEY));
                count++;
                repaint();
            }
            if (count == playerCount){
                state++;
                remove(field);
                remove(submit);
                System.out.println("works");
                game.setPlayers(players);
                repaint();
                game.setDeck();
            }
        }
        else if (state == BET_PLAY){

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
    }

    public void displayPlayerCountRequirements(Graphics g){
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(WINDOW_WIDTH / 4, Y_HEIGHT / 4 + HEADER_HEIGHT, WINDOW_WIDTH / 2, Y_HEIGHT / 2);
        g.setFont(SMALL_FONT);
        g.setColor(Color.BLACK);
        g.drawString("Enter number of players, from 2 to " + MAX_PLAYERS, WINDOW_WIDTH / 4,
                Y_HEIGHT / 4 + HEADER_HEIGHT + SMALL_FONT.getSize());
    }

    public void askForPlayers(Graphics g){
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(WINDOW_WIDTH / 4, Y_HEIGHT / 4 + HEADER_HEIGHT, WINDOW_WIDTH / 2, Y_HEIGHT / 2);
        g.setFont(SMALL_FONT);
        g.setColor(Color.BLACK);
        g.drawString("Enter the name of player " + (count + 1), WINDOW_WIDTH / 4,
                Y_HEIGHT / 4 + HEADER_HEIGHT + SMALL_FONT.getSize());
    }
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }



    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }
    public int getPlayerCount(){
        return playerCount;
    }
    public ArrayList<Player> getPlayers(){
        return players;
    }
}

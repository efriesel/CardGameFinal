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
    public final int BUTTON_WIDTH = WINDOW_WIDTH / 6;
    public final int BUTTON_HEIGHT = 50;
    public final int BUTTON_SPACING = WINDOW_WIDTH / 24;
    private final int CARD_HEIGHT = Y_HEIGHT / 6;
    private final int CARD_WIDTH = CARD_HEIGHT * 5 / 7;
    private final int RIVER_START = WINDOW_WIDTH / 10;
    private final int GAP_BEFORE_EDGE = 20;
    private final int CARD_SPACING = 20;
    private final Image BACKGROUND_IMAGE = new ImageIcon("Resources/Background.jpeg").getImage();
    private final Image BACK_OF_CARD_FRONT = new ImageIcon("Resources/Back.png").getImage();
    private final Image BACK_OF_CARD_SIDE = new ImageIcon("Resources/BackSide.png").getImage();
    public final int WELCOME_SCREEN = 0;
    public final int NUMBER_OF_PLAYER_COLLECTION = 1;
    public final int NAME_COLLECTION = 2;
    public final int PRE_DEAL = 3;
    public final int BET_PLAY = 4;
    public final int INPUT_BET = 5;
    private Game game;
    private Bet b;
    private int state;
    private ArrayList<Player> players;

    private ArrayList<Player> playersIn;
    private int playerCount;
    private ArrayList<Card> river;
    private int turn;
    private JButton submit = new JButton("Submit");
    private Image submit_image = new ImageIcon("Resources/Submit.png").getImage();
    private JButton cancel = new JButton("Cancel");
    private Image cancel_image = new ImageIcon("Resources/Cancel.png").getImage();
    private JButton bet = new JButton("Bet");
    private Image bet_image = new ImageIcon("Resources/Bet.png").getImage();
    private JButton call = new JButton("Call");
    private Image call_image = new ImageIcon("Resources/Call.png").getImage();
    private JButton reveal = new JButton("Reveal");
    private Image reveal_image = new ImageIcon("Resources/Reveal.png").getImage();
    private JButton fold = new JButton("Fold");
    private Image fold_image = new ImageIcon("Resources/Fold.png").getImage();
    private JButton allIn = new JButton("All In");
    private Image allIn_image = new ImageIcon("Resources/All In.png").getImage();
    private JTextField field = new JTextField();
    private Image field_image = new ImageIcon("Resources/Field.png").getImage();
    private int count;
    private boolean show;
    private int pot;

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
        Rectangle r = submit.getBounds();
        r.x = r.x - BUTTON_WIDTH / 2;
        cancel.setBounds(r);
        bet.setBounds(0, WINDOW_HEIGHT - BUTTON_HEIGHT - 100, BUTTON_WIDTH, BUTTON_HEIGHT);
        call.setBounds(BUTTON_WIDTH + BUTTON_SPACING, WINDOW_HEIGHT - BUTTON_HEIGHT - 100, BUTTON_WIDTH, BUTTON_HEIGHT);
        reveal.setBounds(2 * (BUTTON_WIDTH + BUTTON_SPACING), WINDOW_HEIGHT - BUTTON_HEIGHT - 100, BUTTON_WIDTH, BUTTON_HEIGHT);
        fold.setBounds(3 * (BUTTON_WIDTH + BUTTON_SPACING), WINDOW_HEIGHT - BUTTON_HEIGHT - 100, BUTTON_WIDTH, BUTTON_HEIGHT);
        allIn.setBounds(4 * (BUTTON_WIDTH + BUTTON_SPACING), WINDOW_HEIGHT - BUTTON_HEIGHT - 100, BUTTON_WIDTH, BUTTON_HEIGHT);
        submit.addActionListener(this);
        cancel.addActionListener(this);
        bet.addActionListener(this);
        call.addActionListener(this);
        reveal.addActionListener(this);
        fold.addActionListener(this);
        allIn.addActionListener(this);
        this.validate();
        field.setBounds(WINDOW_WIDTH / 4, Y_HEIGHT / 11 * 5 + HEADER_HEIGHT, WINDOW_WIDTH / 2,
                Y_HEIGHT / 11);
        turn = -1;
        this.setLocationRelativeTo(null);
        setVisible(true);
    }

    public void paint(Graphics g)
    {
        JPanel j = new JPanel(null);
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
        else if (state == PRE_DEAL){
            printOutlines(g);
            askForReady(g);
            add(submit);
        }
        else if (state == BET_PLAY){
            printOutlines(g);
            printCards(g);
            if (count != 0)
                printRiver(g);
            printTurn(g);
            setButtons(g);
        }
        else if (state == INPUT_BET){
            remove(bet);
            remove(call);
            remove(reveal);
            remove(fold);
            remove(allIn);
            printOutlines(g);
            printCards(g);
            if (count != 0)
                printRiver(g);
            printTurn(g);
            getInput(g);
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
                if ((isNumeric(current))) {
                    int currentPlayers = Integer.parseInt(current);
                    if (currentPlayers > 1 && currentPlayers <= MAX_PLAYERS) {
                        state++;
                        playerCount = currentPlayers;
                        count = 0;
                        repaint();
                    }
                }
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
                System.out.println("works");
                count = 0;
                game.setPlayers(players);
                game.setDeck();
                game.run();
                repaint();
            }
        }
        else if (state == PRE_DEAL){
            String s = e.getActionCommand();
            if (s != null && s.equals("Submit")) {
                System.out.println("works");
                state++;
                remove(submit);
                show = false;
                b = game.startBet(Game.INITIAL_BET);
                repaint();
            }
        }
        else if (state == BET_PLAY){
            String s = e.getActionCommand();
            if (s.equals("Bet")){
                state = INPUT_BET;
                Rectangle alter = submit.getBounds();
                alter.x = alter.x + alter.width / 2;
                submit.setBounds(alter);
            }
            else if (s.equals("Reveal")){
                if (show)
                    show = false;

                else
                    show = true;
            }
            else{
                if (b.bet(s)) {
                    playersIn = b.getPlayersIn();
                    if (count == 3 || playersIn.size() <= 1) {
                        game.update();
                        state = PRE_DEAL;
                        count = 0;
                        game.run();
                        repaint();
                        pot = 0;
                    }
                    else if (count == 0) {
                        river = game.riverStart(3);
                        b = game.startBet(0);
                        count++;
                    }
                    else {
                        game.river(1);
                        b = game.startBet(0);
                        count++;
                    }
                }
                show = false;
            }
            repaint();
        }
        else if (state == INPUT_BET){
            String s = e.getActionCommand();
            String current = field.getText();
            if (s.equals("Submit")){
                if (isNumeric(current)){
                    int in = Integer.parseInt(current);
                    if (b.bet(in)){
                        state = BET_PLAY;
                        submit.setBounds(submit.getX() - submit.getWidth() / 2, submit.getY(), submit.getWidth(),
                                submit.getHeight());
                        remove(submit);
                        remove(cancel);
                        remove(field);
                    }
                }
                repaint();
            }
            else if (s.equals("Cancel")){
                state = BET_PLAY;
                remove(submit);
                remove(cancel);
                remove(field);
                repaint();
            }
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
    //IF TIME, MAKE IT SO THERE IS A CHARACTER LIMIT FOR NAMES
    public void askForPlayers(Graphics g){
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(WINDOW_WIDTH / 4, Y_HEIGHT / 4 + HEADER_HEIGHT, WINDOW_WIDTH / 2, Y_HEIGHT / 2);
        g.setFont(SMALL_FONT);
        g.setColor(Color.BLACK);
        g.drawString("Enter the name of player " + (count + 1), WINDOW_WIDTH / 4,
                Y_HEIGHT / 4 + HEADER_HEIGHT + SMALL_FONT.getSize());
    }
    public void printOutlines(Graphics g){
        g.setColor(Color.WHITE);
        g.drawRect(RIVER_START - CARD_WIDTH / 2 + CARD_SPACING, HEADER_HEIGHT + GAP_BEFORE_EDGE +
                HEADER_HEIGHT, CARD_WIDTH, CARD_HEIGHT);
        for (int i = 0; i < Game.RIVER_STACKS; i++){
            g.drawRect(RIVER_START + (CARD_WIDTH * (i + 1)) + CARD_SPACING, HEADER_HEIGHT + GAP_BEFORE_EDGE +
                            HEADER_HEIGHT, CARD_WIDTH, CARD_HEIGHT);
        }
        g.setFont(SMALL_FONT);
        int start = GAP_BEFORE_EDGE + CARD_WIDTH + HEADER_HEIGHT;
        int current = 0;
        int x;
        for (int i = 0; i < MAX_PLAYERS; i++){
            if (i % 2 == 0) {
                x = GAP_BEFORE_EDGE;
                current = start + CARD_WIDTH * (2 + i);;
            }
            else {
                x = WINDOW_WIDTH - GAP_BEFORE_EDGE - CARD_HEIGHT;
            }
            if (i != turn) {
                g.drawRect(x, current, CARD_HEIGHT, CARD_WIDTH);
                if (i < playerCount) {
                    g.drawString(players.get(i).getName(), x, current + CARD_WIDTH * 6 / 5);
                    g.drawString(String.valueOf(players.get(i).getMoney() - players.get(i).getInputtedMoney() -
                            players.get(i).getCurrentInputtedMoney()), x, current + CARD_WIDTH * 6 / 5 +
                            SMALL_FONT.getSize());
                }
            }
        }
    }
    public void askForReady(Graphics g){
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(WINDOW_WIDTH / 4, Y_HEIGHT / 4 + HEADER_HEIGHT, WINDOW_WIDTH / 2, Y_HEIGHT / 2);
        g.setFont(SMALL_FONT);
        g.setColor(Color.BLACK);
        g.drawString("Submit When Ready", WINDOW_WIDTH / 4,
                Y_HEIGHT / 4 + HEADER_HEIGHT + SMALL_FONT.getSize());
    }
    public void printCards(Graphics g){
        int start = GAP_BEFORE_EDGE + CARD_WIDTH + HEADER_HEIGHT;
        int current = 0;
        int x;
        g.setFont(SMALL_FONT);
        String bet = "Bet: " + String.valueOf(b.getBet()) + "   Pot: " + String.valueOf(pot);
        g.drawString(bet, WINDOW_WIDTH / 2 - SMALL_FONT.getSize() * bet.length() / 2, HEADER_HEIGHT +
                GAP_BEFORE_EDGE + CARD_HEIGHT + SMALL_FONT.getSize() * 3);
        for (int i = 0; i < players.size(); i++){
            if (i % 2 == 0) {
                x = GAP_BEFORE_EDGE;
                current = start + CARD_WIDTH * (2 + i);
            }
            else {
                x = WINDOW_WIDTH - GAP_BEFORE_EDGE - CARD_HEIGHT;
            }
            if (i != turn && !players.get(i).isElim()) {
                g.drawImage(BACK_OF_CARD_SIDE, x, current, CARD_HEIGHT, CARD_WIDTH, this);
            }
        }
    }
    public void printRiver(Graphics g){
        g.drawImage(BACK_OF_CARD_FRONT, RIVER_START - CARD_WIDTH / 2 + CARD_SPACING, HEADER_HEIGHT +
                GAP_BEFORE_EDGE + HEADER_HEIGHT, CARD_WIDTH, CARD_HEIGHT, this);
        for (int i = 0; i < river.size(); i++){
            g.drawImage(river.get(i).getImage(), RIVER_START + (CARD_WIDTH * (i + 1)) + CARD_SPACING,
                    HEADER_HEIGHT + GAP_BEFORE_EDGE + HEADER_HEIGHT, CARD_WIDTH, CARD_HEIGHT, this);
        }
    }
    public void printTurn(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(SMALL_FONT);
        String name = "Player : " + players.get(turn).getName() + " Money: $" + (players.get(turn).getMoney() -
                players.get(turn).getInputtedMoney() - players.get(turn).getCurrentInputtedMoney());
        g.drawString(name, WINDOW_WIDTH / 2 - (SMALL_FONT.getSize() * name.length()) / 4,
                Y_HEIGHT / 5 * 4 + HEADER_HEIGHT);
        if (show){
            g.drawImage(players.get(turn).getHand().getCards().get(0).getImage(), WINDOW_WIDTH / 2 - CARD_WIDTH,
                    Y_HEIGHT / 2 + HEADER_HEIGHT, CARD_WIDTH, CARD_HEIGHT, this);
            g.drawImage(players.get(turn).getHand().getCards().get(1).getImage(), WINDOW_WIDTH / 2,
                    Y_HEIGHT / 2 + HEADER_HEIGHT, CARD_WIDTH, CARD_HEIGHT, this);
        }
        else {
            g.drawImage(BACK_OF_CARD_FRONT, WINDOW_WIDTH / 2 - CARD_WIDTH, Y_HEIGHT / 2 + HEADER_HEIGHT,
                    CARD_WIDTH, CARD_HEIGHT, this);
            g.drawImage(BACK_OF_CARD_FRONT, WINDOW_WIDTH / 2, Y_HEIGHT / 2 + HEADER_HEIGHT, CARD_WIDTH,
                    CARD_HEIGHT, this);
        }
    }
    public void setButtons(Graphics g){
        add(bet);
        g.drawImage(bet_image, bet.getX(), bet.getY() + bet.getHeight() / 2, bet.getWidth(),
                bet.getHeight(), this);
        add(call);
        g.drawImage(call_image, call.getX(), call.getY() + call.getHeight() / 2, call.getWidth(),
                call.getHeight(), this);
        add(reveal);
        g.drawImage(reveal_image, reveal.getX(), reveal.getY() + reveal.getHeight() / 2, reveal.getWidth(),
                reveal.getHeight(), this);
        add(fold);
        g.drawImage(fold_image, fold.getX(), fold.getY() + fold.getHeight() / 2, fold.getWidth(),
                fold.getHeight(), this);
        add(allIn);
        g.drawImage(allIn_image, allIn.getX(), allIn.getY() + allIn.getHeight() / 2, allIn.getWidth(),
                allIn.getHeight(), this);
    }
    public void getInput(Graphics g){
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(WINDOW_WIDTH / 4, Y_HEIGHT / 4 + HEADER_HEIGHT, WINDOW_WIDTH / 2, Y_HEIGHT / 2);
        g.setFont(SMALL_FONT);
        g.setColor(Color.BLACK);
        g.drawString("Submit When Ready", WINDOW_WIDTH / 4,
                Y_HEIGHT / 4 + HEADER_HEIGHT + SMALL_FONT.getSize());
        add(field);
        g.drawImage(field_image, field.getX(), field.getY() + field.getHeight() / 2, field.getWidth(),
                field.getHeight(), this);
        add(submit);
        g.drawImage(submit_image, submit.getX() - 2, submit.getY() + submit.getHeight() / 2, submit.getWidth() + 2,
                submit.getHeight(), this);
        add(cancel);
        g.drawImage(cancel_image, cancel.getX(), cancel.getY() + cancel.getHeight() / 2 + 5, cancel.getWidth(),
                cancel.getHeight(), this);

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
    public void setTurn(int turn) {
        this.turn = turn;
    }
    public void setPot(int pot){
        this.pot = pot;
    }
}
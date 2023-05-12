import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameViewer extends JFrame implements ActionListener {
    // Constants for window drawing, spacing, etc
    private static final int MAX_PLAYERS = 4;
    private static final int MAX_CHARS = 7;
    public static final Font SMALL_FONT = new Font("Sans-Serif", 1, 20);
    private static final int HEADER_HEIGHT = 22;
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 822;
    public static final int Y_HEIGHT = WINDOW_HEIGHT - HEADER_HEIGHT;
    public final int BUTTON_WIDTH = WINDOW_WIDTH / 6;
    public final int BUTTON_HEIGHT = 50;
    public final int BUTTON_SPACING = WINDOW_WIDTH / 24;
    private final int CARD_HEIGHT = Y_HEIGHT / 6;
    private final int CARD_WIDTH = CARD_HEIGHT * 5 / 7;
    private final int RIVER_START = WINDOW_WIDTH / 10;
    private final int GAP_BEFORE_EDGE = 20;
    private final int CARD_SPACING = 20;
    // Overall card images
    private final Image BACKGROUND_IMAGE = new ImageIcon("Resources/Background.jpeg").getImage();
    private final Image BACK_OF_CARD_FRONT = new ImageIcon("Resources/Back.png").getImage();
    private final Image BACK_OF_CARD_SIDE = new ImageIcon("Resources/BackSide.png").getImage();
    // stages of gameplay
    public final int WELCOME_SCREEN = 0;
    public final int NUMBER_OF_PLAYER_COLLECTION = 1;
    public final int NAME_COLLECTION = 2;
    public final int PRE_DEAL = 3;
    public final int BET_PLAY = 4;
    public final int INPUT_BET = 5;
    public final int WIN = 6;
    // private instance variable game
    private final Game game;
    // the variable b for Bet (to change each round)
    private Bet b;
    // the variable to change the state of the game
    private int state;
    // the players
    private final ArrayList<Player> players;
    // the number of players
    private int playerCount;
    // the river (to change each round)
    private ArrayList<Card> river;
    // the current turn
    private int turn;
    // to control the blind
    private int blind;
    //Easter Egg
    public int EE8;
    private final Image[] EE8_IMAGES = new Image[]{new ImageIcon("Resources/Easter Egg 8/EE81.png").getImage(),
            new ImageIcon("Resources/Easter Egg 8/EE82.png").getImage()};
    // Button declaration and overlay image declaration
    private final JButton submit = new JButton("Submit");
    private final Image submit_image = new ImageIcon("Resources/Submit.png").getImage();
    private final JButton cancel = new JButton("Cancel");
    private final Image cancel_image = new ImageIcon("Resources/Cancel.png").getImage();
    private final JButton bet = new JButton("Bet");
    private final Image bet_image = new ImageIcon("Resources/Bet.png").getImage();
    private final JButton call = new JButton("Call");
    private final Image call_image = new ImageIcon("Resources/Call.png").getImage();
    private final JButton reveal = new JButton("Reveal");
    private final Image reveal_image = new ImageIcon("Resources/Reveal.png").getImage();
    private final JButton fold = new JButton("Fold");
    private final Image fold_image = new ImageIcon("Resources/Fold.png").getImage();
    private final JButton allIn = new JButton("All In");
    private final Image allIn_image = new ImageIcon("Resources/All In.png").getImage();
    // initialize the text field and overlay image
    private final JTextField field = new JTextField();
    private final Image field_image = new ImageIcon("Resources/Field.png").getImage();
    // the count variable which will count how many players are inputted and how many bets are played in a round
    private int count;
    // if the cards for the player whose turn it is shows their cards
    private boolean show;
    // if there is a winner of the round (So win statement can show in the PRE_DEAL state
    private boolean roundWinner;
    // The player who won
    private Player winner;
    // the total pot (for visuals only)
    private int pot;

    public GameViewer (Game game) {
        // initialize the game and players
        this.game = game;
        players = new ArrayList<>();
        //default operations with name "Poker"
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setTitle("Poker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        // initialize the Easter egg
        EE8 = -1;
        // set the bounds for the submit button, with cancel to the left of half of it
        submit.setBounds(WINDOW_WIDTH / 2 - BUTTON_WIDTH / 2, Y_HEIGHT / 4 * 3 + HEADER_HEIGHT - BUTTON_HEIGHT * 2
                - BUTTON_HEIGHT / 2, BUTTON_WIDTH, BUTTON_HEIGHT);
        Rectangle r = submit.getBounds();
        r.x = r.x - BUTTON_WIDTH / 2;
        cancel.setBounds(r);
        // set the bounds of the bet, call, reveal, fold, and allIn buttons to five across the bottom with equal spacing
        bet.setBounds(0, WINDOW_HEIGHT - BUTTON_HEIGHT - 100, BUTTON_WIDTH, BUTTON_HEIGHT);
        call.setBounds(BUTTON_WIDTH + BUTTON_SPACING, WINDOW_HEIGHT - BUTTON_HEIGHT - 100, BUTTON_WIDTH,
                BUTTON_HEIGHT);
        reveal.setBounds(2 * (BUTTON_WIDTH + BUTTON_SPACING), WINDOW_HEIGHT - BUTTON_HEIGHT - 100, BUTTON_WIDTH,
                BUTTON_HEIGHT);
        fold.setBounds(3 * (BUTTON_WIDTH + BUTTON_SPACING), WINDOW_HEIGHT - BUTTON_HEIGHT - 100, BUTTON_WIDTH,
                BUTTON_HEIGHT);
        allIn.setBounds(4 * (BUTTON_WIDTH + BUTTON_SPACING), WINDOW_HEIGHT - BUTTON_HEIGHT - 100, BUTTON_WIDTH,
                BUTTON_HEIGHT);
        // add the action listeners for all buttons
        submit.addActionListener(this);
        cancel.addActionListener(this);
        bet.addActionListener(this);
        call.addActionListener(this);
        reveal.addActionListener(this);
        fold.addActionListener(this);
        allIn.addActionListener(this);
        this.validate();
        // set the bounds of the text field
        field.setBounds(WINDOW_WIDTH / 4, Y_HEIGHT / 11 * 5 + HEADER_HEIGHT, WINDOW_WIDTH / 2,
                Y_HEIGHT / 11);
        // initialize the turn as 1 to start as the person after the blind (Start blind at 0)
        turn = 1;
        blind = 0;
        // initialize the blind as the first person
        roundWinner = false;
        // set LocationRelativeTo to null so I have control
        this.setLocationRelativeTo(null);
        // setVisible to true
        setVisible(true);
    }
    // getter and setter methods
    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }
    public void setTurn(int turn) {
        this.turn = turn;
    }
    public void setPot(int pot){
        this.pot = pot;
    }
    public void setRoundWinner(Player roundWinner){
        this.winner = roundWinner;
    }
    // this method assigns the winner
    public void win(Player p){
        winner = p;
        state = WIN;
    }

    /**
     * This paint method will paint the background and foreground that depends on state of game
     * @param g the specified Graphics window
     */
    public void paint(Graphics g)
    {
        // the Background will always be painted
        printBackground(g);
        // if state is WELCOME_SCREEN, add the welcome state with the instructions and a submit button to finish
        if (state == WELCOME_SCREEN) {
            printInstructions(g);
            add(submit);
        }
        // if the state is NUMBER_OF_PLAYER_COLLECTION, display the player count requirements with the field and submit
        else if (state == NUMBER_OF_PLAYER_COLLECTION){
            displayPlayerCountRequirements(g);
            add(submit);
            add(field);
        }
        // if the state is NAME_COLLECTION, use the askForPlayers method and add submit and field
        else if (state == NAME_COLLECTION) {
            askForPlayers(g);
            add(submit);
            add(field);
        }
        // if the state is PRE_DEAL, make sure there are no buttons from the betting
        // print the outlines of the cards and the ask for ready screen with a submit button
        else if (state == PRE_DEAL){
            remove(bet);
            remove(call);
            remove(reveal);
            remove(fold);
            remove(allIn);
            printOutlines(g);
            askForReady(g);
            // have the winner print their win message when they win
            if (roundWinner){
                winner.printWin(g);
            }
            add(submit);
        }
        // if the state is BET_PLAY, print the outlines, cards, turn, and set up the buttons
        else if (state == BET_PLAY){
            printOutlines(g);
            printCards(g);
            // if there are cards in the river, print the river
            if (count != 0)
                printRiver(g);
            printTurn(g);
            setButtons(g);
        }
        // if the state is INPUT_BET (if the player wants to input a bet), remove BET_PLAY buttons
        // print the outlines, cards, turn, and the getInput message with submit and cancel buttons
        else if (state == INPUT_BET){
            remove(bet);
            remove(call);
            remove(reveal);
            remove(fold);
            remove(allIn);
            printOutlines(g);
            printCards(g);
            // if there is a river, print it
            if (count != 0)
                printRiver(g);
            printTurn(g);
            getInput(g);
        }
        // if there is a winner, draw the win and remove the buttons
        else {
            remove(bet);
            remove(call);
            remove(reveal);
            remove(fold);
            remove(allIn);
            drawWin(g);
        }
    }

    /**
     * This method will manage the inputs of the players as the game goes on, depending on what state the game is
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (state == WELCOME_SCREEN) {
            String s = e.getActionCommand();
            if (s != null && s.equals("Submit")) {
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
            if (current != null && current.length() <= MAX_CHARS) {
                // Easter Egg
                if (current.equals("Ethan") || current.equals("Will"))
                    players.add(new Player(current, 100000));
                else
                    players.add(new Player(current, Game.INITIAL_MONEY));
                count++;
                if (count == playerCount){
                    state++;
                    remove(field);
                    count = 0;
                    game.setPlayers(players);
                    game.setDeck();
                    game.run();
                }
                repaint();
            }
        }
        else if (state == PRE_DEAL){
            String s = e.getActionCommand();
            if (s != null && s.equals("Submit")) {
                if (roundWinner)
                    game.run();
                state++;
                remove(submit);
                show = false;
                b = game.startBet(game.getBet(), turn, 1);
                repaint();
            }
        }
        else if (state == BET_PLAY){
            String s = e.getActionCommand();
            if (s.equals("Bet")){
                if (!players.get(turn).isElim()) {
                    state = INPUT_BET;
                    Rectangle alter = submit.getBounds();
                    alter.x = alter.x + alter.width / 2;
                    submit.setBounds(alter);
                    show = false;
                }
            }
            else if (s.equals("Reveal")){
                if (!players.get(turn).isElim())
                    show = !show;
            }
            else{
                if (b.bet(s)) {
                    if (count == 3) {
                        game.update();
                        game.setPlayersInGame();
                        state = PRE_DEAL;
                        if (players.size() == 1)
                            state = WIN;
                        count = 0;
                        roundWinner = true;
                        blind++;
                        if (blind >= players.size()){
                            blind = 0;
                        }
                        if (blind >= players.size() - 1) {
                            turn = 0;
                        }
                        else
                            turn = blind + 1;
                        if (turn >= players.size())
                            turn = 0;
                        repaint();
                        pot = 0;
                    }
                    else if (count == 0) {
                        river = game.riverStart(3);
                        b = game.startBet(0, turn, 0);
                        count++;
                    }
                    else {
                        game.river(1);
                        b = game.startBet(0, turn, 0);
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
                submit.setBounds(submit.getX() - submit.getWidth() / 2, submit.getY(), submit.getWidth(),
                        submit.getHeight());
                remove(submit);
                remove(cancel);
                remove(field);
                repaint();
            }
        }
    }

    /**
     * This method will print the background
     * @param g: Graphics
     */
    private void printBackground(Graphics g){
        g.drawImage(BACKGROUND_IMAGE, 0, HEADER_HEIGHT, WINDOW_WIDTH, WINDOW_HEIGHT, this);
    }

    /**
     * This method will print the instructions with the proper color and font
     * @param g: Graphics
     */
    public void printInstructions(Graphics g){
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(WINDOW_WIDTH / 4, Y_HEIGHT / 4 + HEADER_HEIGHT, WINDOW_WIDTH / 2, Y_HEIGHT / 2);
        g.setFont(SMALL_FONT);
        g.setColor(Color.BLACK);
        g.drawString("Welcome to poker,", WINDOW_WIDTH / 4, Y_HEIGHT / 4 + HEADER_HEIGHT + SMALL_FONT.getSize());
        g.drawString("for information about ", WINDOW_WIDTH / 4, Y_HEIGHT / 4 + HEADER_HEIGHT + SMALL_FONT.getSize() * 2);
        g.drawString("how to play, look it up", WINDOW_WIDTH / 4, Y_HEIGHT / 4 + HEADER_HEIGHT + SMALL_FONT.getSize() * 3);
    }

    /**
     * This method will print the player count requirements
     * @param g: Graphics
     */
    public void displayPlayerCountRequirements(Graphics g){
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(WINDOW_WIDTH / 4, Y_HEIGHT / 4 + HEADER_HEIGHT, WINDOW_WIDTH / 2, Y_HEIGHT / 2);
        g.setFont(SMALL_FONT);
        g.setColor(Color.BLACK);
        g.drawString("Enter number of players, from 2 to " + MAX_PLAYERS, WINDOW_WIDTH / 4,
                Y_HEIGHT / 4 + HEADER_HEIGHT + SMALL_FONT.getSize());
    }

    /**
     * This method will print the player input window
     * @param g: Graphics
     */
    //IF TIME, MAKE IT SO THERE IS A CHARACTER LIMIT FOR NAMES (Done)
    public void askForPlayers(Graphics g){
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(WINDOW_WIDTH / 4, Y_HEIGHT / 4 + HEADER_HEIGHT, WINDOW_WIDTH / 2, Y_HEIGHT / 2);
        g.setFont(SMALL_FONT);
        g.setColor(Color.BLACK);
        g.drawString("Enter the name of player " + (count + 1), WINDOW_WIDTH / 4,
                Y_HEIGHT / 4 + HEADER_HEIGHT + SMALL_FONT.getSize());
        g.drawString("CHARACTER LIMIT = " + MAX_CHARS, WINDOW_WIDTH / 4,
                Y_HEIGHT / 4 + HEADER_HEIGHT + SMALL_FONT.getSize() * 2);
    }

    /**
     * This method will print the outlines of the players, cards, money, bet, pot
     * @param g: Graphics
     */
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
            // flip from the right side to the left
            if (i % 2 == 0) {
                x = GAP_BEFORE_EDGE;
                current = start + CARD_WIDTH * (2 + i);
            }
            else {
                x = WINDOW_WIDTH - GAP_BEFORE_EDGE - CARD_HEIGHT;
            }
            // only print the outline if it is not the person's turn
            if (i != turn) {
                // rotated card outline
                g.drawRect(x, current, CARD_HEIGHT, CARD_WIDTH);
                if (i < playerCount) {
                    g.drawString(players.get(i).getName(), x, current + CARD_WIDTH * 6 / 5);
                    if (state != PRE_DEAL)
                        g.drawString(String.valueOf(players.get(i).getMoney() - players.get(i).getInputtedMoney() -
                                players.get(i).getCurrentInputtedMoney()), x, current + CARD_WIDTH * 6 / 5 +
                                SMALL_FONT.getSize());
                }
                // print if the person was the blind
                if (i == blind){
                    g.drawString("Blind", x, current + CARD_WIDTH * 6 / 5 +
                            SMALL_FONT.getSize() * 2);
                }
            }
        }
    }

    /**
     * This method will ask if the players are ready
     * @param g: Graphics
     */
    public void askForReady(Graphics g){
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(WINDOW_WIDTH / 4, Y_HEIGHT / 4 + HEADER_HEIGHT, WINDOW_WIDTH / 2, Y_HEIGHT / 2);
        g.setFont(SMALL_FONT);
        g.setColor(Color.BLACK);
        g.drawString("Submit When Ready", WINDOW_WIDTH / 4,
                Y_HEIGHT / 4 + HEADER_HEIGHT + SMALL_FONT.getSize());
    }

    /**
     * This method will print the cards on the outlines depending on if the person is in the game or not
     * @param g: Graphics
     */
    public void printCards(Graphics g){
        int start = GAP_BEFORE_EDGE + CARD_WIDTH + HEADER_HEIGHT;
        int current = 0;
        int x;
        g.setFont(SMALL_FONT);
        String bet = "Bet: " + b.getBet() + "   Pot: " + pot;
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
                // card is rotated
                g.drawImage(BACK_OF_CARD_SIDE, x, current, CARD_HEIGHT, CARD_WIDTH, this);
            }
        }
    }

    /**
     * THis method will print the river with the burn pile
     * @param g: Graphics
     */
    public void printRiver(Graphics g){
        g.drawImage(BACK_OF_CARD_FRONT, RIVER_START - CARD_WIDTH / 2 + CARD_SPACING, HEADER_HEIGHT +
                GAP_BEFORE_EDGE + HEADER_HEIGHT, CARD_WIDTH, CARD_HEIGHT, this);
        for (int i = 0; i < river.size(); i++){
            g.drawImage(river.get(i).getImage(), RIVER_START + (CARD_WIDTH * (i + 1)) + CARD_SPACING,
                    HEADER_HEIGHT + GAP_BEFORE_EDGE + HEADER_HEIGHT, CARD_WIDTH, CARD_HEIGHT, this);
        }
    }

    /**
     * This method will print the input screen for the player whose turn it is
     * @param g: Graphics
     */
    public void printTurn(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(SMALL_FONT);
        String name = "Player : " + players.get(turn).getName();
        if (players.get(turn).getInputtedMoney() == players.get(turn).getMoney())
            name += " Is All In, press ALL IN TO CONTINUE";
        else if (players.get(turn).isElim())
            name += " HAS FOLDED, CALL TO CONTINUE";
        else
            name += " Money: $" + (players.get(turn).getMoney() - players.get(turn).getInputtedMoney() -
                    players.get(turn).getCurrentInputtedMoney());
        g.drawString(name, WINDOW_WIDTH / 2 - (SMALL_FONT.getSize() * name.length()) / 4,
                Y_HEIGHT / 5 * 4 + HEADER_HEIGHT);
        if (turn == blind)
            g.drawString("Blind", WINDOW_WIDTH / 2 - SMALL_FONT.getSize() * 5 / 4,
                    Y_HEIGHT / 5 * 4 + HEADER_HEIGHT - SMALL_FONT.getSize());
        if (!players.get(turn).isElim()){
            if (show) {
                // Easter Egg implementation
                if (EE8 == turn){
                    g.drawImage(EE8_IMAGES[0], WINDOW_WIDTH / 2 - CARD_WIDTH, Y_HEIGHT / 2 + HEADER_HEIGHT,
                            CARD_WIDTH, CARD_HEIGHT, this);
                    g.drawImage(EE8_IMAGES[1], WINDOW_WIDTH / 2, Y_HEIGHT / 2 + HEADER_HEIGHT, CARD_WIDTH,
                            CARD_HEIGHT, this);
                    g.drawString("A Player Deployed a CHEAT CODE", WINDOW_WIDTH / 2 - SMALL_FONT.getSize() * 30 / 4,
                            Y_HEIGHT * 7 / 10 + HEADER_HEIGHT);
                    g.drawString("Nice Loadout tho, lets go tilted", WINDOW_WIDTH / 2 - SMALL_FONT.getSize() * 32 / 4,
                            Y_HEIGHT * 7 / 10 + HEADER_HEIGHT + SMALL_FONT.getSize());
                    EE8 = -1;
                }
                else {
                    g.drawImage(players.get(turn).getHand().getCards().get(0).getImage(), WINDOW_WIDTH / 2 - CARD_WIDTH,
                            Y_HEIGHT / 2 + HEADER_HEIGHT, CARD_WIDTH, CARD_HEIGHT, this);
                    g.drawImage(players.get(turn).getHand().getCards().get(1).getImage(), WINDOW_WIDTH / 2,
                            Y_HEIGHT / 2 + HEADER_HEIGHT, CARD_WIDTH, CARD_HEIGHT, this);
                }
            }
            else {
                g.drawImage(BACK_OF_CARD_FRONT, WINDOW_WIDTH / 2 - CARD_WIDTH, Y_HEIGHT / 2 + HEADER_HEIGHT,
                        CARD_WIDTH, CARD_HEIGHT, this);
                g.drawImage(BACK_OF_CARD_FRONT, WINDOW_WIDTH / 2, Y_HEIGHT / 2 + HEADER_HEIGHT, CARD_WIDTH,
                        CARD_HEIGHT, this);
            }
        }
    }

    /**
     * This method will set up the buttons and their images that overlap them
     * @param g: Graphics
     */
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

    /**
     * This method prints the input screen for a bet
     * @param g: Graphics
     */
    public void getInput(Graphics g){
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(WINDOW_WIDTH / 4, Y_HEIGHT / 4 + HEADER_HEIGHT + SMALL_FONT.getSize(), WINDOW_WIDTH / 2,
                Y_HEIGHT / 2);
        g.setFont(SMALL_FONT);
        g.setColor(Color.BLACK);
        g.drawString("Submit When Ready, a bet that is higher", WINDOW_WIDTH / 4,
                Y_HEIGHT / 4 + HEADER_HEIGHT + SMALL_FONT.getSize() * 2);
        g.drawString("and a multiple of " + Bet.BET_FACTOR, WINDOW_WIDTH / 4,
                Y_HEIGHT / 4 + HEADER_HEIGHT + SMALL_FONT.getSize() * 3);
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

    /**
     * This method draws the win screen for the winner
     * @param g: Graphics
     */
    public void drawWin(Graphics g){
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(WINDOW_WIDTH / 4, Y_HEIGHT / 4 + HEADER_HEIGHT, WINDOW_WIDTH / 2, Y_HEIGHT / 2);
        g.setFont(SMALL_FONT);
        g.setColor(Color.BLACK);
        String name = winner.getName() + " Wins";
        if (name.length() > 15){
            String name1 = name.substring(15);
            name = name.substring(0, 15) + "-";
            g.drawString(name1, WINDOW_WIDTH / 2 - (SMALL_FONT.getSize() * name.length()) / 2,
                    Y_HEIGHT / 2 + SMALL_FONT.getSize());
        }
        g.drawString(name, WINDOW_WIDTH / 2 - (SMALL_FONT.getSize() * name.length()) / 2, Y_HEIGHT / 2);
    }

    /**
     * This method checks if a string is numeric
     * @param strNum: String, the input
     * @return true iff the input is numeric
     * altered from:
     * baeldung.com/java-check-string-number#:~:text=Perhaps%20the%20easiest%20and%20the,Double.parseDouble(String)
     */
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
}
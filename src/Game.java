import java.util.ArrayList;
public class Game {
    // Constants
    public static final int INITIAL_MONEY = 1000;
    public static final int INITIAL_BET = 100;
    private static final int BET_INCREASE_AMOUNT = INITIAL_BET / 2;
    public static final int RIVER_STACKS = 5;
    // Instance variables (window does not change)
    private final GameViewer window;
    private int bet;
    private int playerCount;
    private Deck deck;
    private ArrayList<Player> players;
    private Round r;
    // main method, initializes the game
    public static void main(String[] args) {
        new Game();
    }
    // game constructor, initializes the game viewer
    public Game(){
        window = new GameViewer(this);
    }
    /**
     * This method will set the deck based on the ranks, suits, and points of each card
     */
    public void setDeck() {
        String[] ranks = new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        String[] suits = new String[]{"Clubs", "Diamonds", "Hearts", "Spades"};
        int[] values = new int[]{1, 2, 3, 4, 5, 6, 11, 20, 37, 70, 135, 264, 517};
        deck = new Deck(ranks, suits, values);
        bet = INITIAL_BET;
    }
    /**
     * This method will initialize a new round
     */
    public void run() {
        r = new Round(playerCount, deck, players, window);
    }
    /**
     * This method will initialize a new betting round
     * @param bet the current bet
     * @param turn the current turn
     * @param calls number of calls (tells the bet class if there is a blind or not)
     * @return the current bet to the window
     */
    public Bet startBet(int bet, int turn, int calls){
        return r.startBet(bet, turn, calls);
    }
    /**
     * Getter function for the bet
     * @return bet:int
     */
    public int getBet(){
        return bet;
    }

    /**
     * This method will initialize and return the river
     * @param river the number of cards to be added
     * @return river:ArrayList
     */
    public ArrayList<Card> riverStart(int river){
        r.river(river);
        return r.getRiver();
    }
    /**
     * This method will add to and not return the river
     * @param in the number of cards to be added
     */
    public void river(int in){
        r.river(in);
    }
    /**
     * this method will update the wins at the end of a round and increase the bet based on the BET_INCREASE_AMOUNT
     */
    public void update(){
        r.update();
        bet += BET_INCREASE_AMOUNT;
    }
    /**
     * this method will set the players ArrayList and reset the playerCount to match the players ArrayList
     * @param players ArrayList of players
     */
    public void setPlayers(ArrayList<Player> players){
        this.players = players;
        playerCount = players.size();
    }
    /**
     * This method will update the players ArrayList after a round, and the window to match it
     * if a player is out of money, they are removed and playerCount is decreased
     * if a player can still play, they are reset
        * they are given a new hand (Deck), but it is not filled yet
        * elim is set to false (no one starts folded)
        * inputtedMoney and currentInputtedMoney set to 0
     * iff there is one player left, winner will be assigned in the win method of the window
     */
    public void setPlayersInGame(){
        int i = 0;
        while (i < playerCount) {
            // reset this cheat code, as it only works for one round
            players.get(i).setCODE_17(false);
            if (players.get(i).getMoney() == 0){
                players.remove(i);
                playerCount--;
            }
            else {
                players.get(i).setHand(new Deck());
                players.get(i).setElim(false);
                players.get(i).setInputtedMoney(0);
                players.get(i).setCurrentInputtedMoney(0);
                i++;
            }
        }
        // player count set to the number of players
        window.setPlayerCount(playerCount);
        // iff there is one player left
        if (playerCount == 1){
            window.win(players.get(0));
        }
    }
}



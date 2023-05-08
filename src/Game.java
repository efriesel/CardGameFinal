import java.util.Scanner;
import java.util.ArrayList;
public class Game {
    public static final int INITIAL_MONEY = 1000;

    public static final int INITIAL_BET = 50;

    private static final int BET_INCREASE_AMOUNT = 50;
    // ADD EXTRA FOR THE BURN PILE;
    public static final int RIVER_STACKS = 5;
    private final GameViewer window;
    int bet;
    private int playerCount;
    private Deck deck;
    private ArrayList<Player> players;

    Round r;


    public static void main(String[] args) {
        new Game();
    }

    public Game(){
        window = new GameViewer(this);
    }

    public void setDeck() {
        String[] ranks = new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        String[] suits = new String[]{"Clubs", "Diamonds", "Hearts", "Spades"};
        int[] values = new int[]{1, 2, 3, 4, 5, 6, 11, 20, 37, 70, 135, 264, 517};
        deck = new Deck(ranks, suits, values);
        bet = INITIAL_BET;
    }

    public void run() {
        r = new Round(playerCount, deck, players, bet, window);
    }

    public Bet startBet(int bet){
        return r.startBet(bet);
    }
    public ArrayList<Card> riverStart(int river){
        r.river(river);
        return r.getRiver();
    }
    public void river(int in){
        r.river(in);
    }
    public void update(){
        r.update();
    }

    public void setPlayers(ArrayList<Player> players){
        this.players = players;
        playerCount = players.size();
    }

    public void setPlayersInGame(){
        int i = 0;
        while (i < playerCount) {
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
        window.setPlayerCount(playerCount);
        if (playerCount == 1){
            window.win(players.get(0));
        }
    }
}



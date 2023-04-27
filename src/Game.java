import java.util.Scanner;
import java.util.ArrayList;
public class Game {
    public static final int INITIAL_MONEY = 1000;

    private static final int INITIAL_BET = 50;

    private static final int BET_INCREASE_AMOUNT = 50;
    private final GameViewer window;
    int bet;
    private int playerCount;
    private Deck deck;
    private ArrayList<Player> players;


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
//        while (playerCount > 1) {
//            new Round(playerCount, deck, players, bet, window);
//            setPlayersInGame();
//            bet += BET_INCREASE_AMOUNT;
//        }
    }

    public void setPlayers(ArrayList<Player> players){
        this.players = players;
        playerCount = players.size();
    }

    public void setPlayersInGame(){
        for (int i = 0; i < playerCount; i++){
            if (players.get(i).getMoney() == 0){
                players.remove(i);
                i--;
                playerCount--;
            }
            players.get(i).setHand(new Deck());
        }
        window.setPlayerCount(playerCount);
    }
}



import java.util.Scanner;
import java.util.ArrayList;
public class Game {
    private static final int MAX_PLAYERS = 4;
    private static final int INITIAL_MONEY = 1000;
    private String[] ranks;
    private String[] suits;
    private int[] values;

    private int playerCount;

    private Deck deck;

    private Player[] players;


    public static void main(String[] args) {

    }

    public Game(){
        ranks = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        suits = new String[]{"Spades", "Clubs", "Hearts", "Diamonds"};
        values = new int[]{1,2,3,4,5,6,11,20,37,70,135,264,517};
        deck = new Deck(ranks, suits, values);

        setPlayers();
        Round r = new Round(playerCount, deck, players, 50);
    }

    public void setPlayers() {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter Player Count from 1 to " + MAX_PLAYERS);
        int in = s.nextInt();
        while (in < 1 || in > MAX_PLAYERS) {
            s.nextLine();
            System.out.println("Enter Player Count from 1 to " + MAX_PLAYERS);
            in = s.nextInt();
        }
        s.nextLine();
        playerCount = in;
        players = new Player[playerCount];
        for (int i = 0; i < playerCount; i++){
            String name = s.nextLine();
            players[i] = new Player(name, INITIAL_MONEY);
        }

    }
}

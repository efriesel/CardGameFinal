import java.util.Scanner;
import java.util.ArrayList;
public class Game {
    private static final int MAX_PLAYERS = 4;
    private static final int INITIAL_MONEY = 1000;

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
        Round.printInstructions();
        String[] ranks = new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        String[] suits = new String[]{"Clubs", "Diamonds", "Hearts", "Spades"};
        int[] values = new int[]{1, 2, 3, 4, 5, 6, 11, 20, 37, 70, 135, 264, 517};
        deck = new Deck(ranks, suits, values);
        setPlayers();
        window = new GameViewer(this, players);
        bet = INITIAL_BET;
        while (playerCount > 1) {
            new Round(playerCount, deck, players, bet);
            setPlayersInGame();
            bet += BET_INCREASE_AMOUNT;
        }
        System.out.println(players.get(0).getName() + " has won");
    }

    public void setPlayers() {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter Player Count from 2 to " + MAX_PLAYERS);
        int in = s.nextInt();
        while (in <= 1 || in > MAX_PLAYERS) {
            s.nextLine();
            System.out.println("Enter Player Count from 2 to " + MAX_PLAYERS);
            in = s.nextInt();
        }
        s.nextLine();
        playerCount = in;
        players = new ArrayList<Player>();
        for (int i = 0; i < playerCount; i++){
            System.out.println("Enter name of player " + (i + 1)) ;
            String name = s.nextLine();
            players.add(new Player(name, INITIAL_MONEY));
        }

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

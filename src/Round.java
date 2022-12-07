import java.util.ArrayList;
import java.util.Scanner;

public class Round {

    private int playerCount;

    private Deck deck;

    private Deck river;

    private int riverCount;

    private ArrayList<Player> players;

    private int minBet;

    private int turn = 0;

    public Round(int playerCount, Deck deck, Player[] players, int minBet){
        this.players = new ArrayList<Player>();
        this.playerCount = playerCount;
        this.deck = deck;
        this.minBet = minBet;
        for (Player p : players) {
            this.players.add(p);
        }
        playRound();

    }

    public void playRound(){
        printInstructions();
        initialDeal();
        System.out.println("Now each player will check their hands");

        revealHands();
        river(3);
        for (int i = 0; i < playerCount; i++){
            if (!players.get(i).isElim())
                Player.printHand(players.get(i).getBestHand(players.get(i).getHand(), river));
        }

    }
    public void printInstructions(){
        Scanner s = new Scanner(System.in);
        System.out.println("Welcome to a game of poker. In this game, each person achieves");
        s.nextLine();
    }

    public void initialDeal(){
        deck.setCardsLeft(deck.getSize());
        deck.shuffle();
        deck.shuffle();
        deck.shuffle();
        for (int i = 0; i < 2; i++){
            for (int j = 0; j < playerCount; j++){
                players.get(j).addCard(deck.deal());
            }
        }
    }
    public void revealHands(){
        Scanner s = new Scanner(System.in);
        for (Player p : players){
            System.out.println(p.getName() + "'s hand: (Press Enter to Continue)");
            s.nextLine();
            p.printHand();
            s.nextLine();
        }
    }

    public void river(int cards){
        deck.burn();
        for (int i = 0; i < cards; i++){
            river.add(deck.deal());
            riverCount++;
        }
        for (int i = 0; i < riverCount; i++){
            System.out.println(river.getCards().get(i));
        }

    }
    public void be(){
        Scanner s = new Scanner(System.in);
        for (int i = 0; i < playerCount; i++){
            if (!players.get(i).isElim()){
                System.out.println("Enter bet, with a minimum being " + minBet + " or press h to see current best hand");
               int in = s.nextInt();
                if (in == ( int) 'h')) {
                    s.nextLine();
                    Player.printHand(players.get(i).getBestHand(players.get(i).getHand(), river));
                    System.out.println("Now enter your bet, with a minimum of " + minBet);
                    in = s.nextInt()
                }
                while ( in <= minBet) {

                }
            }
        }
    }




}

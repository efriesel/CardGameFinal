import java.util.ArrayList;
import java.util.Scanner;

public class Round {

    private int playerCount;

    private Deck deck;

    private Deck river;

    private int riverCount;

    private ArrayList<Player> players;

    private ArrayList<SidePot> pots = new ArrayList<SidePot>();

    boolean allIn;

    int allInBettor;

    int numPots = 0;

    int numBetters;
    private int minBet;

    private int turn = 0;

    private int pot;

    private int calls = 0;

    public Round(int playerCount, Deck deck, Player[] players, int minBet){
        this.players = new ArrayList<Player>();
        this.playerCount = playerCount;
        this.deck = deck;
        this.minBet = minBet;
        for (Player p : players) {
            this.players.add(p);
        }
        playRound();
        numBetters = playerCount;

    }

    public void playRound(){
        printInstructions();
        initialDeal();
        System.out.println("Now each player will check their hands");

        revealHands();
        bet(minBet);
        river(3);
        for (int i = 0; i < playerCount; i++){
            if (!players.get(i).isElim())
                Player.printHand(players.get(i).getBestHand(players.get(i).getHand(), river));
        }
        bet(0);
        river(1);
        bet(0);
        river(1);
        bet(0);
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
    public void bet(int bet){
        Scanner s = new Scanner(System.in);
        int i = 0;
        while (calls != numBetters){
            if (!players.get(i).isElim()){
                System.out.println("Enter bet, with a minimum being " + bet + ", enter c to call, a number higher than " + minBet + " to place a higher bet, a for all in, f to fold, or h to see current best hand");
                int in = s.nextInt();
                while (in == (int) 'h') {
                    s.nextLine();
                    Player.printHand(players.get(i).getBestHand(players.get(i).getHand(), river));
                    System.out.println("Now enter your call or bet , with " + bet + " being c to call, a to go all in, f to fold, or higher to bet");
                    in = s.nextInt();
                }
                if (in == (int) 'f'){
                    playerCount--;
                    numBetters--;
                    players.get(i).setElim(true);
                }
                if (in == (int) 'c'){
                    pot += bet;
                    players.get(i).setMoney(players.get(i).getMoney() - bet);
                    calls++;
                }
                if (in == (int) 'a'){
                    if (players.get(i).getMoney() < bet) {
                        ArrayList<Player> sidePlayers = new ArrayList<Player>();
                        for (int j = 0; j < playerCount; j++) {
                            if (!players.get(j).isElim())
                                sidePlayers.add(players.get(j));
                        }
                        numPots++;
                        SidePot p = new SidePot((numBetters * bet), sidePlayers);
                        pots.add(p);
                        numBetters--;
                        players.get(i).setElim(true);
                        players.get(i).setMoney(0);
                    }
                    if (players.get(i).getMoney() > bet) {
                        bet = players.get(i).getMoney();
                        pot += bet;
                        allIn = true;
                        allInBettor = i;
                    }
                }
                else {
                    while (in <= bet) {
                        s.nextLine();
                        System.out.println("Now enter your bet, with a minimum of " + bet);
                        in = s.nextInt();
                    }
                    if (allIn) {
                        ArrayList<Player> sidePlayers = new ArrayList<Player>();
                        for (int j = 0; j < playerCount; j++) {
                            if (!players.get(j).isElim())
                                sidePlayers.add(players.get(j));
                        }
                        SidePot p = new SidePot(bet, sidePlayers);
                        pots.add(p);
                        numPots++;
                        numBetters--;
                        players.get(allInBettor).setElim(true);
                        players.get(allInBettor).setMoney(0);
                        allIn = false;
                    }
                    bet = in;
                }
            }
            if (allIn){
                ArrayList<Player> sidePlayers = new ArrayList<Player>();
                for (int j = 0; j < playerCount; j++) {
                    if (!players.get(j).isElim())
                        sidePlayers.add(players.get(j));
                }
                SidePot p = new SidePot(bet, sidePlayers);
                pots.add(p);
                numPots++;
                players.get(allInBettor).setElim(true);
                players.get(allInBettor).setMoney(0);
                allIn = false;
            }
            i++;
        }
    }
    public void revealWinner(){
        ArrayList<Player> sidePlayers = new ArrayList<Player>();
        for (int j = 0; j < playerCount; j++) {
            if (!players.get(j).isElim())
                sidePlayers.add(players.get(j));
        }
        SidePot finalPot = new SidePot(pot, sidePlayers);
        for (int i = 0; i < numPots; i++){
            findWinner(pots.get(i).getPlayers());
        }
    }
    public ArrayList<Player> findWinner(ArrayList<Player> p){
        int bestScore = 0;
        ArrayList<Player> winners = new ArrayList<Player>();
        for (int i = 0; i < p.size(); i++){
            if (p.get(i).getBestPoints() == bestScore){
                winners.add(p.get(i));
            }
            else if (p.get(i).getBestPoints() > bestScore){
                winners = new ArrayList<Player>();
                winners.add(p.get(i));
                bestScore = p.get(i).getBestPoints();
            }
        }
        return winners;
    }




}

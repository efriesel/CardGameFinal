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

    ArrayList<Player> win;

    public Round(int playerCount, Deck deck, ArrayList<Player> players, int minBet){
        this.players = new ArrayList<Player>();
        this.playerCount = playerCount;
        this.deck = deck;
        this.minBet = minBet;
        river = new Deck();
        for (Player p : players) {
            this.players.add(p);
        }
        numBetters = playerCount;
        playRound();


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
                Player.printHand(players.get(i).getBestHand(river));
        }
        bet(0);
        river(1);
        for (int i = 0; i < playerCount; i++){
            if (!players.get(i).isElim())
                Player.printHand(players.get(i).getBestHand(river));
        }
        bet(0);
        river(1);
        for (int i = 0; i < playerCount; i++){
            if (!players.get(i).isElim())
                Player.printHand(players.get(i).getBestHand(river));
        }
        bet(0);
        revealWinner();
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
            System.out.print("\033[H\033[2J");
            System.out.flush();
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
    private void bet(int bet){
        Scanner s = new Scanner(System.in);
        calls = 0;
        int i = 0;
        while (calls != numBetters){
            if (!players.get(i).isElim()){
                System.out.println("Enter bet, with a minimum being " + bet + ", enter c to call, b to place a higher bet, a for all in, f to fold, or h to see current best hand");
                String in = s.nextLine();
                int inBet;
                if (in.charAt(0) == 'h') {
                    s.nextLine();
                    Player.printHand(players.get(i).getBestHand(river));
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.println("Now enter your call or bet , with " + bet + " being c to call, a to go all in, f to fold, or higher to bet");
                    inBet = s.nextInt();
                }
                else if (in.charAt(0) == ('f')){
                    playerCount--;
                    numBetters--;
                    if (allIn){
                        pots.get(numPots - 1).setPot(pots.get(numPots - 1).getPot() - pots.get(numPots - 1).getPot() / pots.get(numPots - 1).getNumPlayers());
                    }
                    players.get(i).setElim(true);
                }
                else if (in.charAt(0) == ('c')){
                    pot += bet;
                    players.get(i).setMoney(players.get(i).getMoney() - bet);
                    calls++;
                }
                else if (in.charAt(0) == 'a'){
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
                    inBet = 0;
                    while (inBet <= bet) {
                        System.out.println("Now enter your bet, with a minimum of " + bet);
                        inBet = s.nextInt();
                    }
                    if (allIn) {
                        ArrayList<Player> sidePlayers = new ArrayList<Player>();
                        for (int j = 0; j < playerCount; j++) {
                            if (!players.get(j).isElim())
                                sidePlayers.add(players.get(j));
                        }
                        SidePot p = new SidePot(bet, sidePlayers);
                        pots.add(p);
                        players.get(allInBettor).setLastPot(numPots);
                        numPots++;
                        numBetters--;
                        players.get(allInBettor).setElim(true);
                        players.get(allInBettor).setMoney(0);
                        allIn = false;
                    }
                    bet = inBet;
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
            if (i == numBetters){
                i = 0;
            }
        }
    }
    public void revealWinner(){
        ArrayList<Player> sidePlayers = new ArrayList<Player>();
        for (int j = 0; j < playerCount; j++) {
            if (!players.get(j).isElim())
                sidePlayers.add(players.get(j));
        }
        SidePot finalPot = new SidePot(pot, sidePlayers);
        pots.add(finalPot);
        arrangePots();
        for (int i = 0; i < numPots; i++){
            for (int j = i + 1; j < numPots; j++){
                pots.get(j).setPot(pots.get(j).getPot() - pots.get(i).getPot());
            }
            win = findWinner(pots.get(i).getPlayers());
            for (int j = 0; j < win.size(); j++){
                win.get(j).setMoney(win.get(j).getMoney() + pots.get(i).getPot() / pots.get(i).getNumPlayers());
            }
            if (pots.get(i).getPot() % win.size() != 0){
                win.get(0).setMoney(win.get(0).getMoney() + pots.get(i).getPot() % win.size());
            }
        }
    }
    public ArrayList<Player> findWinner(ArrayList<Player> p){
        int bestScore = 0;
        ArrayList<Player> winners = new ArrayList<Player>();
        for (int i = 0; i < p.size(); i++){
            if (p.get(i).getLastPot() >= i) {
                if (p.get(i).getBestPoints() == bestScore) {
                    winners.add(p.get(i));
                } else if (p.get(i).getBestPoints() > bestScore) {
                    winners = new ArrayList<Player>();
                    winners.add(p.get(i));
                    bestScore = p.get(i).getBestPoints();
                }
            }
        }
        return winners;
    }

    private void arrangePots(){
        int small = 0;
        int smallPlace = 0;
        for (int i = 0; i < pots.size(); i++){
            for (int j = i; j < pots.size(); j++)
            {
                if (pots.get(j).getPot() > small){
                    smallPlace = i;
                    small = pots.get(i).getPot();
                }
            }
            SidePot p = pots.remove(smallPlace);
            pots.add(i, p);

        }
    }




}

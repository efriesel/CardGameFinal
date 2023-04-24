
import java.util.ArrayList;
import java.util.Scanner;

public class Round {

    private int playerCount;

    private Deck deck;

    private Deck river;

    private int riverCount;

    private ArrayList<Player> players;

    private ArrayList<SidePot> pots = new ArrayList<SidePot>();

    int allIn;

    ArrayList<Integer> allInBettor;
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
        initialDeal();
        System.out.println("Now each player will check their hands");

        revealHands();
        bet(minBet);
        System.out.println("Pot is up to " + pot + " with " + pots.size() + " side pot");
        river(3);
        for (int i = 0; i < players.size(); i++){
            if (!players.get(i).isElim())
                Player.printHand(players.get(i).getBestHand(river), players.get(i).getMoney(), players.get(i).getName());
        }
        if (numBetters > 1)
            bet(0);
        else {
            for (int j = 0; j < players.size(); j++){
                if (!players.get(j).isElim()){
                    players.get(j).setMoney(players.get(j).getMoney() + pot);
                }
            }
        }
        System.out.println("Pot is up to " + pot + " with " + pots.size() + " side pot");
        river(1);
        for (int i = 0; i < players.size(); i++) {
            if (!players.get(i).isElim())
                Player.printHand(players.get(i).getBestHand(river), players.get(i).getMoney(), players.get(i).getName());
        }
        if (numBetters > 1)
            bet(0);
        else {
            for (int j = 0; j < players.size(); j++){
                if (!players.get(j).isElim()){
                    players.get(j).setMoney(players.get(j).getMoney() + pot);
                }
            }
        }
        System.out.println("Pot is up to " + pot + " with " + pots.size() + " side pot");
        river(1);
        for (int i = 0; i < players.size(); i++){
            if (!players.get(i).isElim())
                Player.printHand(players.get(i).getBestHand(river), players.get(i).getMoney(), players.get(i).getName());
        }
        if (numBetters > 1)
            bet(0);
        else {
            for (int j = 0; j < players.size(); j++){
                if (!players.get(j).isElim()){
                    players.get(j).setMoney(players.get(j).getMoney() + pot);
                }
            }
        }
        System.out.println("Pot is up to " + pot + " with " + pots.size() + " side pot");
        revealWinner();
    }
    public static void printInstructions(){
        Scanner s = new Scanner(System.in);
        System.out.println("Welcome to a game of poker. If you do not know how to play, go to https://bicyclecards.com/how-to-play/basics-of-poker");
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
        System.out.println("River: ");
        for (int i = 0; i < riverCount; i++){
            System.out.println(river.getCards().get(i));
        }
        System.out.println();

    }
    private void bet(int bet){
        allInBettor = new ArrayList<Integer>();
        Scanner s = new Scanner(System.in);
        allIn = 0;
        calls = 0;
        int i = 0;
        for (int j = 0; j < players.size(); j++)
        {
            players.get(j).setLastBet(0);
        }
        while (calls != numBetters){
            if (!players.get(i).isElim()){
                System.out.println(players.get(i).getName() + ", with a minimum being " + bet + ", enter c to call, b to place a higher bet, a for all in, f to fold, or h to see current best hand");
                String in = s.nextLine();
                int inBet;
                if (in.charAt(0) == 'h') {
                    players.get(i).printHand();
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.println("Now enter your call or bet , with " + bet + " being c to call, a to go all in, f to fold, or higher to bet");
                    in = s.nextLine();
                }

                if (in.charAt(0) == ('f')){
                    playerCount--;
                    numBetters--;
                    if (allIn > 0){
                        pots.get(pots.size() - 1).setPot(pots.get(pots.size() - 1).getPot() - pots.get(pots.size()- 1).getPot() / pots.get(pots.size() - 1).getNumPlayers());
                    }
                    players.get(i).setElim(true);
                }
                else if (in.charAt(0) == ('c')){
                    pot += bet;
                    players.get(i).setMoney(players.get(i).getMoney() - bet);
                    if (players.get(i).getLastBet() != bet){
                        players.get(i).setMoney(players.get(i).getMoney() + players.get(i).getLastBet());
                        players.get(i).setLastBet(bet);
                    }
                    calls++;
                    if (players.get(i).getMoney() == 0){
                        numBetters--;
                        calls--;
                        players.get(i).setLastPot(pots.size());
                    }
                    else if (allIn > 0){
                        players.get(i).setLastPot(players.get(i).getLastPot() + 1);
                    }
                }
                else if (in.charAt(0) == 'a'){
                    if (players.get(i).getMoney() < bet) {
                        ArrayList<Player> sidePlayers = new ArrayList<Player>();
                        for (int j = 0; j < playerCount; j++) {
                            if (!players.get(j).isElim())
                                sidePlayers.add(players.get(j));
                        }
                        players.get(i).setLastPot(pots.size());
                        SidePot p = new SidePot((numBetters * bet), sidePlayers);
                        pots.add(p);
                        numBetters--;
                        players.get(i).setMoney(0);
                        allInBettor.add(i);
                    }
                    else if (players.get(i).getMoney() >= bet) {
                        bet = players.get(i).getMoney();
                        pot += bet;
                        players.get(i).setMoney(players.get(i).getMoney() + players.get(i).getLastBet());
                        players.get(i).setLastBet(bet);
                        allIn++;
                        allInBettor.add(i);
                        numBetters--;
                        players.get(i).setLastPot(pots.size());
                        calls = 0;
                    }
                }
                else {
                    inBet = 0;
                    while (inBet <= bet) {
                        System.out.println("Now enter your bet, with a minimum of " + bet);
                        inBet = s.nextInt();
                        s.nextLine();
                    }
                    while (allIn > 0) {
                        ArrayList<Player> sidePlayers = new ArrayList<Player>();
                        for (int j = 0; j < playerCount; j++) {
                            if (!players.get(j).isElim())
                                sidePlayers.add(players.get(j));
                        }
                        SidePot p = new SidePot(bet, sidePlayers);
                        pots.add(p);
                        players.get(allInBettor.get(allIn)).setLastPot(allIn);
                        players.get(allInBettor.get(allIn)).setMoney(0);
                        allIn--;
                    }
                    calls = 1;
                    bet = inBet;
                    players.get(i).setMoney(players.get(i).getMoney() - bet);
                    players.get(i).setMoney(players.get(i).getMoney() + players.get(i).getLastBet());
                    players.get(i).setLastBet(bet);
                    pot+= bet;

                }
            }

            i++;
            if (i == players.size()){
                i = 0;
            }
        }
        while (allIn > 0){
            ArrayList<Player> sidePlayers = new ArrayList<Player>();
            for (int j = 0; j < playerCount; j++) {
                if ((players.get(j).getLastPot() >= pots.size() - 1)) {
                    sidePlayers.add(players.get(j));
                    players.get(j).setLastPot(players.get(j).getLastPot() + 1);
                }
            }
            SidePot p = new SidePot(bet * sidePlayers.size(), sidePlayers);
            pots.add(p);
            players.get(allInBettor.get(allIn - 1)).setMoney(0);
            allIn--;
        }
    }
    public void revealWinner(){
        ArrayList<Player> sidePlayers = new ArrayList<Player>();
        for (int j = 0; j < playerCount; j++) {
            if (players.get(j).getLastPot() == pots.size() - 1) {
                sidePlayers.add(players.get(j));
                players.get(j).setLastPot(players.get(j).getLastPot() + 1);
            }
        }
        if (sidePlayers.size() != 0) {
            SidePot finalPot = new SidePot(pot, sidePlayers);
            pots.add(finalPot);
            arrangePots();
        }
        for (int i = 0; i < pots.size(); i++){
            for (int j = i + 1; j < pots.size(); j++){
                pots.get(j).setPot(pots.get(j).getPot() - pots.get(i).getPot());
            }
            win = findWinner(pots.get(i).getPlayers(), i);
            for (int j = 0; j < win.size(); j++){
                System.out.println(win.get(j).getName() + " has won pot " + i);
                win.get(j).setMoney(win.get(j).getMoney() + pots.get(i).getPot() / win.size());
            }
            if (pots.get(i).getPot() % win.size() != 0){
                win.get(0).setMoney(win.get(0).getMoney() + pots.get(i).getPot() % win.size());
            }
        }
        System.out.println("Money has been updated, make the best of decisions");
    }

    // UPDATE TO SORT LIST OF PLAYERS BY BEST TO WORST HAND USING ARRAYLIST
    public ArrayList<Player> findWinner(ArrayList<Player> p, int current){
        int bestScore = 0;
        ArrayList<Player> winners = new ArrayList<Player>();
        for (int i = 0; i < p.size(); i++){
            if (p.get(i).getLastPot() >= current) {
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

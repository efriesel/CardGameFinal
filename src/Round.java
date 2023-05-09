
import java.util.ArrayList;

public class Round {

    private final int playerCount;

    private Deck deck;

    private ArrayList<Card> river;

    private ArrayList<Player> players;
    private ArrayList<Player> playersIn;
    private int numPlayers;
    private GameViewer window;

    private int minBet;

    private int turn;

    private Bet b;

    private int pot;

    public Round(int playerCount, Deck deck, ArrayList<Player> players, int minBet, GameViewer window){
        this.players = players;
        this.playerCount = playerCount;
        this.deck = deck;
        this.minBet = minBet;
        this.window = window;
        playersIn = new ArrayList<>();
        playersIn.addAll(players);
        river = new ArrayList<>();
        numPlayers = players.size();
        pot = 0;
        initialDeal();
        turn = 0;
        window.repaint();
    }

    public void update(){
        playersIn = new ArrayList<>();
        setBestHands();
        giveWins();
    }
    public Bet startBet(int bet){
        b = new Bet(playersIn, bet, turn, numPlayers, this, window);
        return b;
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
    public void setBestHands(){
        for (Player p : players){
            if (!p.isElim()) {
                p.getBestHand(river);
                playersIn.add(p);
            }
        }
    }

    public void river(int cards){
        deck.burn();
        for (int i = 0; i < cards; i++){
            river.add(deck.deal());
        }
    }

    public ArrayList<Card> getRiver(){
        return river;
    }



    public void giveWins(){
        boolean under = false;
        boolean win = false;
        ArrayList<Player> manage = new ArrayList<>(players);
        ArrayList<Player> winners = findWinner();
        window.setRoundWinner(winners.get(0));
        int i = 0;
        while (!win){
            if (winners.get(0) == manage.get(i))
                i++;
            if (i == manage.size())
                return;
            if (manage.get(i).getInputtedMoney() > winners.get(0).getInputtedMoney()) {
                under = true;
                winners.get(0).setMoney(winners.get(0).getMoney() + winners.get(0).getInputtedMoney());
                manage.get(i).setMoney(winners.get(i).getMoney() - winners.get(0).getInputtedMoney());
                manage.get(i).setInputtedMoney(manage.get(i).getInputtedMoney() - winners.get(0).getInputtedMoney());
                i++;
            }
            else {
                winners.get(0).setMoney(winners.get(0).getMoney() + manage.get(i).getInputtedMoney());
                manage.get(i).setMoney(manage.get(i).getMoney() - manage.get(i).getInputtedMoney());
                winners.remove(manage.get(i));
                manage.remove(i);
            }
            if (i == manage.size()){
                if (under){
                    winners.remove(0);
                    under = false;
                    i = 0;
                }
                else
                    win = true;
            }
        }

    }

    // UPDATE TO SORT LIST OF PLAYERS BY BEST TO WORST HAND USING ARRAYLIST
    public ArrayList<Player> findWinner(){
        ArrayList<Player> order = new ArrayList<>();
        order.addAll(playersIn);
        return mergeSort(order);

    }
    /**
     * This function will recursively call itself until the list is sorted
     * Each call, the base case will be when an inputted ArrayList is of size one
     * The "mid" integer variable will split the inputted ArrayList into two different ArrayLists
     * Each ArrayList will call the mergeSort method on itself
     * When the two split ArrayLists are returned, they are merged and returned
     * @param players (an ArrayList of Players)
     * @return (An ArrayList that is either of size one or merged)
     */
    public ArrayList<Player> mergeSort(ArrayList<Player> players){
        // base case
        if (players.size() == 1)
            return players;
        // reused variable
        int mid = players.size() / 2;
        // split two arrays in half and recursively call method
        ArrayList<Player> list1 = mergeSort(new ArrayList<>(players.subList(0 , mid)));
        ArrayList<Player> list2 = mergeSort(new ArrayList<>(players.subList(mid, players.size())));
        // for non-base case scenarios, after all recursive methods complete, increasingly merge each array
        return merge(list1, list2);
    }
    /**
     * This method will return a single ArrayList of Players consisting of two inputted ArrayLists
     * @param list1 (One inputted list)
     * @param list2 (Another inputted list)
     * @return (the returned ArrayList will be sorted from least to greatest player points using the compareTo function)
     */
    public ArrayList<Player> merge(ArrayList<Player> list1, ArrayList<Player> list2){
        // create a new ArrayList that will house the combined sorted values of both inputted ArrayLists
        ArrayList<Player> order = new ArrayList<>();
        int i = 0, j = 0;
        // while both lists still have elements
        while (i < list1.size() && j < list2.size()){
            // whichever list's current element is larger given the compareTo function
            // add that element to the grand list
            if (list1.get(i).getBestPoints() > list2.get(i).getBestPoints()) {
                order.add(list1.get(i));
                // after the element is added, only increment the list that had an element used in the grand list
                i++;
            }
            else {
                order.add(list2.get(j));
                j++;
            }
        }
        // after one list is used up, a while loop will iterate through the rest of the not-empty list
        while (i < list1.size()){
            order.add(list1.get(i));
            i++;
        }
        while (j < list2.size()){
            order.add(list2.get(j));
            j++;
        }
        return order;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }
}

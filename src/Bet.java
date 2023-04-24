import java.util.ArrayList;
import java.util.Scanner;

public class Bet {
    ArrayList<Player> players;
    int minBet;
    ArrayList<Player> money;
    public Bet (ArrayList<Player> players, int minBet){
        this.players = players;
        this.minBet = minBet;
        money.addAll(players);
        money = sort(money);
        bet(minBet);
    }
    private void bet(int initialBet) {
        int calls = 0;
        int bet = initialBet;
        ArrayList<Player> playersIn = new ArrayList<>();
        playersIn.addAll(players);
        int i = 0;
        while (calls != playersIn.size()) {
            if (call) {
                players.get(i).setInputtedMoney(players.get(i).getInputtedMoney() + bet);
                calls++;
            }
            else if (bet) {
                bet = newBet;
                players.get(i).setInputtedMoney(players.get(i).getInputtedMoney() + bet);
                calls = 1;
            }
            else if (fold) {
                playersIn.remove(i);
                players.get(i).setElim(true);
            }
            else if (allIn) {
                playersIn.remove(i);
                players.get(i).setInputtedMoney(players.get(i).getMoney());
            }
            i++;
            if (i == playersIn.size())
                i = 0;
        }
    }

//    private void bet(int bet, boolean no) {
//        ArrayList<Player> playersIn = new ArrayList<>();
//        ArrayList<SidePot> pots = new ArrayList<>();
//        int pot;
//
//        Scanner s = new Scanner(System.in);
//        int allIn = 0;
//        int calls = 0;
//        int i = 0;
//        for (int j = 0; j < players.size(); j++) {
//            players.get(j).setLastBet(0);
//        }
//        while (calls != playersIn.size()) {
//            System.out.println(playersIn.get(i).getName() + ", with a minimum being " + bet + ", enter c to call, b to place a higher bet, a for all in, f to fold, or h to see current best hand");
//            String in = s.nextLine();
//            int inBet;
////                if (in.charAt(0) == 'h') {
////                    playersIn.get(i).printHand();
////                    System.out.print("\033[H\033[2J");
////                    System.out.flush();
////                    System.out.println("Now enter your call or bet , with " + bet + " being c to call, a to go all in, f to fold, or higher to bet");
////                    in = s.nextLine();
////                }
////
//            if (in.charAt(0) == ('f')) {
//                playersIn.get(i).setElim(true);
//                playersIn.remove(i);
//                for (SidePot p : pots) {
//                    p.setNumPlayers(p.getNumPlayers() - 1);
//                }
//            } else if (in.charAt(0) == ('c')) {
//                pot += bet;
//                playersIn.get(i).setMoney(playersIn.get(i).getMoney() - bet);
//                if (playersIn.get(i).getLastBet() != bet) {
//                    playersIn.get(i).setMoney(playersIn.get(i).getMoney() + players.get(i).getLastBet());
//                    playersIn.get(i).setLastBet(bet);
//                }
//                calls++;
//                if (players.get(i).getMoney() == 0) {
//                    numBetters--;
//                    calls--;
//                    players.get(i).setLastPot(pots.size());
//                } else if (allIn > 0) {
//                    players.get(i).setLastPot(players.get(i).getLastPot() + 1);
//                }
//            } else if (in.charAt(0) == 'a') {
//                if (playersIn.get(i).getMoney() < bet) {
//                    ArrayList<Player> sidePlayers = new ArrayList<Player>();
//
//                    players.get(i).setLastPot(pots.size());
//                    SidePot p = new SidePot(playersIn.get(i).getMoney(), playersIn.get(i));
//                    pots.add(p);
//                    numBetters--;
//                    players.get(i).setMoney(0);
//                } else if (players.get(i).getMoney() >= bet) {
//                    bet = playersIn.get(i).getMoney();
//                    pot += bet;
//                    players.get(i).setMoney(players.get(i).getMoney() + players.get(i).getLastBet());
//                    players.get(i).setLastBet(bet);
//                    allIn++;
//                    allInBettor.add(i);
//                    numBetters--;
//                    players.get(i).setLastPot(pots.size());
//                    calls = 0;
//                }
////                }
////                else {
////                    inBet = 0;
////                    while (inBet <= bet) {
////                        System.out.println("Now enter your bet, with a minimum of " + bet);
////                        inBet = s.nextInt();
////                        s.nextLine();
////                    }
////                    while (allIn > 0) {
////                        ArrayList<Player> sidePlayers = new ArrayList<Player>();
////                        for (int j = 0; j < playerCount; j++) {
////                            if (!players.get(j).isElim())
////                                sidePlayers.add(players.get(j));
////                        }
////                        SidePot p = new SidePot(bet, sidePlayers);
////                        pots.add(p);
////                        players.get(allInBettor.get(allIn)).setLastPot(allIn);
////                        players.get(allInBettor.get(allIn)).setMoney(0);
////                        allIn--;
////                    }
////                    calls = 1;
////                    bet = inBet;
////                    players.get(i).setMoney(players.get(i).getMoney() - bet);
////                    players.get(i).setMoney(players.get(i).getMoney() + players.get(i).getLastBet());
////                    players.get(i).setLastBet(bet);
////                    pot+= bet;
////
////                }
////            }
////
//                i++;
//                if (i == playersIn.size()) {
//                    i = 0;
//                }
//            }
//        }
//    }
    public ArrayList<Player> sort(ArrayList<Player> players) {
        //the returned ArrayList will be assigned to the words instance variable
        return mergeSort(players);
    }

    /**
     * This function will recursively call itself until the list is sorted
     * Each call, the base case will be when an inputted ArrayList is of size one
     * The "mid" integer variable will split the inputted ArrayList into two different ArrayLists
     * Each ArrayList will call the mergeSort method on itself
     * When the two split ArrayLists are returned, they are merged and returned
     * @param players (an ArrayList of Strings)
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
     * This method will return a single ArrayList of Strings consisting of two inputted ArrayLists
     * @param list1 (One inputted list)
     * @param list2 (Another inputted list)
     * @return (the returned ArrayList will be sorted from least to greatest Strings using the compareTo function)
     */
    public ArrayList<Player> merge(ArrayList<Player> list1, ArrayList<Player> list2){
        // create a new ArrayList that will house the combined sorted values of both inputted ArrayLists
        ArrayList<Player> order = new ArrayList<>();
        int i = 0, j = 0;
        // while both lists still have elements
        while (i < list1.size() && j < list2.size()){
            // whichever list's current element is larger given the compareTo function
            // add that element to the grand list
            if (list1.get(i).getMoney() > list2.get(i).getMoney()) {
                order.add(list1.get(i));
                // after the element is added, only increment the list that had an element used in the grand list
                i++;
            }
            else {
                order.add(list1.get(j));
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
//        }
//        while (allIn > 0){
//            ArrayList<Player> sidePlayers = new ArrayList<Player>();
//            for (int j = 0; j < playerCount; j++) {
//                if ((players.get(j).getLastPot() >= pots.size() - 1)) {
//                    sidePlayers.add(players.get(j));
//                    players.get(j).setLastPot(players.get(j).getLastPot() + 1);
//                }
//            }
//            SidePot p = new SidePot(bet * sidePlayers.size(), sidePlayers);
//            pots.add(p);
//            players.get(allInBettor.get(allIn - 1)).setMoney(0);
//            allIn--;
//        }
//
}

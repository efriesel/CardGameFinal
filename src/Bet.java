import java.util.ArrayList;
import java.util.Scanner;

public class Bet {
    private void bet(int bet){
        ArrayList<Player> playersIn = new ArrayList<>();
        ArrayList<SidePot> pots = new ArrayList<>();
        int pot;

        Scanner s = new Scanner(System.in);
        int allIn = 0;
        int calls = 0;
        int i = 0;
        for (int j = 0; j < players.size(); j++)
        {
            players.get(j).setLastBet(0);
        }
        while (calls != playersIn.size()){
                System.out.println(playersIn.get(i).getName() + ", with a minimum being " + bet + ", enter c to call, b to place a higher bet, a for all in, f to fold, or h to see current best hand");
                String in = s.nextLine();
                int inBet;
                if (in.charAt(0) == 'h') {
                    playersIn.get(i).printHand();
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.println("Now enter your call or bet , with " + bet + " being c to call, a to go all in, f to fold, or higher to bet");
                    in = s.nextLine();
                }
//
                if (in.charAt(0) == ('f')){
                    playersIn.get(i).setElim(true);
                    playersIn.remove(i);
                    for (SidePot p : pots){
                        p.setNumPlayers(p.getNumPlayers() - 1);
                    }
                }
                else if (in.charAt(0) == ('c')){
                    pot += bet;
                    playersIn.get(i).setMoney(playersIn.get(i).getMoney() - bet);
                    if (playersIn.get(i).getLastBet() != bet){
                        playersIn.get(i).setMoney(playersIn.get(i).getMoney() + players.get(i).getLastBet());
                        playersIn.get(i).setLastBet(bet);
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
                    if (playersIn.get(i).getMoney() < bet) {
                        ArrayList<Player> sidePlayers = new ArrayList<Player>();

                        players.get(i).setLastPot(pots.size());
                        SidePot p = new SidePot(playersIn.get(i).getMoney(), playersIn.get(i));
                        pots.add(p);
                        numBetters--;
                        players.get(i).setMoney(0);
                    }
                    else if (players.get(i).getMoney() >= bet) {
                        bet = playersIn.get(i).getMoney();
                        pot += bet;
                        players.get(i).setMoney(players.get(i).getMoney() + players.get(i).getLastBet());
                        players.get(i).setLastBet(bet);
                        allIn++;
                        allInBettor.add(i);
                        numBetters--;
                        players.get(i).setLastPot(pots.size());
                        calls = 0;
                    }
//                }
//                else {
//                    inBet = 0;
//                    while (inBet <= bet) {
//                        System.out.println("Now enter your bet, with a minimum of " + bet);
//                        inBet = s.nextInt();
//                        s.nextLine();
//                    }
//                    while (allIn > 0) {
//                        ArrayList<Player> sidePlayers = new ArrayList<Player>();
//                        for (int j = 0; j < playerCount; j++) {
//                            if (!players.get(j).isElim())
//                                sidePlayers.add(players.get(j));
//                        }
//                        SidePot p = new SidePot(bet, sidePlayers);
//                        pots.add(p);
//                        players.get(allInBettor.get(allIn)).setLastPot(allIn);
//                        players.get(allInBettor.get(allIn)).setMoney(0);
//                        allIn--;
//                    }
//                    calls = 1;
//                    bet = inBet;
//                    players.get(i).setMoney(players.get(i).getMoney() - bet);
//                    players.get(i).setMoney(players.get(i).getMoney() + players.get(i).getLastBet());
//                    players.get(i).setLastBet(bet);
//                    pot+= bet;
//
//                }
//            }
//
            i++;
            if (i == playersIn.size()){
                i = 0;
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
//    }
}

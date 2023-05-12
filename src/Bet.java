import java.util.ArrayList;

public class Bet {
    // constant
    public static final int BET_FACTOR = 50;
    // Instance variables
    private final ArrayList<Player> players;
    private final ArrayList<Player> playersIn;
    private final Round r;
    private final GameViewer window;
    private int bet;
    private int calls;
    private int current;
    private int numPlayers;

    public Bet(ArrayList<Player> players, int minBet, int turn, int numPlayers, int calls, Round r, GameViewer window) {
        this.players = players;
        this.window = window;
        this.calls = calls;
        playersIn = new ArrayList<>();
        playersIn.addAll(players);
        if (turn > players.size()) {
            current = 0;
            window.setTurn(current);
        }
        else
            current = turn;
        window.setTurn(current);
        bet = minBet;
        this.r = r;
        this.numPlayers = numPlayers;
        // initialization of the blind, the person before the turn is the blind
        int blind;
        if (calls == 1){
            if (turn == 0)
                blind = players.size() - 1;
            else
                blind = turn - 1;
            // if the bet is higher than the amount of money the blind can pay
            if (minBet >= players.get(blind).getMoney()) {
                // set the player as allIn
                players.get(blind).setInputtedMoney(players.get(blind).getMoney());
                playersIn.remove(players.get(blind));
            }
            else
                players.get(blind).setCurrentInputtedMoney(minBet);
        }
    }
    // getter function for the bet
    public int getBet() {
        return bet;
    }
    /**
     * This method will initialize an input based on the button pressed
     * @param in String value of the input (will be "Call", "Fold", or "All In")
     * @return true iff the round of betting is over (if the number of calls = the number of players total)
     */
    public boolean bet(String in) {
        if (!players.get(current).isElim()) {
            if (in.equals("Call")) {
                // the player will be forced to be all in
                if (players.get(current).getMoney() == bet) {
                    playersIn.remove(players.get(current));
                    players.get(current).setInputtedMoney(players.get(current).getMoney());
                    players.get(current).setCurrentInputtedMoney(0);
                }
                // the player will then again be all in
                if (players.get(current).getMoney() < players.get(current).getInputtedMoney() + bet)
                    return false;
                // set this betting rounds input to the bet
                players.get(current).setCurrentInputtedMoney(bet);
                // add another call
                calls++;
            }
            else if (in.equals("Fold")) {
                players.get(current).setElim(true);
                playersIn.remove(players.get(current));
                numPlayers--;
            }
            else {
                if (players.get(current).getMoney() - players.get(current).getInputtedMoney() > bet) {
                    bet = players.get(current).getMoney() - players.get(current).getInputtedMoney();
                    calls = 1;
                } else {
                    playersIn.remove(players.get(current));
                    calls++;
                }
                players.get(current).setInputtedMoney(players.get(current).getMoney());
                players.get(current).setCurrentInputtedMoney(0);
            }
        }
        current++;
        if (current == players.size())
            current = 0;
        if (players.get(current).getMoney() == players.get(current).getInputtedMoney() + players.get(current).getCurrentInputtedMoney())
            playersIn.remove(players.get(current));
        window.setTurn(current);
        if (calls >= numPlayers) {
            setInputtedBet();
            r.setNumPlayers(numPlayers);
            return true;
        }
        return false;
    }

    /**
     * This overloaded bet function takes an int as the bet inputted
     * @param inBet: int, the bet inputted
     * @return true iff the bet is valid
     */
    public boolean bet(int inBet) {
        // Easter Eggs
        if (inBet / 10 == 8){
            System.out.println("works");
            int input = inBet % 10;
            if (input >= 0 && input < players.size()){
                window.EE8 = input;
            }
        }
        if (inBet == 17){
            players.get(current).setCODE_17(true);
        }
        if (inBet == 27){
            players.get(current).setMoney(players.get(current).getMoney() + 10000);
            return false;
        }
        // a bet is only valid iff it is greater than the current one, a multiple of the BET_FACTOR,
        if (inBet < bet || inBet % BET_FACTOR != 0) {
            return false;
        }
        // and the player has the funds to make the bet
        if (players.get(current).getMoney() < players.get(current).getInputtedMoney() + inBet)
            return false;
        // if true, the bet is inputted, the player is updated, the calls is set to 1, and the turn is increased
        bet = inBet;
        players.get(current).setCurrentInputtedMoney(bet);
        calls = 1;
        current++;
        if (current >= playersIn.size())
            current = 0;
        window.setTurn(current);
        return true;
    }

    /**
     * This method will update the players to add to their total inputtedMoney at the end of this bet round
     */
    private void setInputtedBet() {
        int pot = 0;
        for (Player p : players) {
            p.setInputtedMoney(p.getInputtedMoney() + p.getCurrentInputtedMoney());
            pot += p.getInputtedMoney();
            p.setCurrentInputtedMoney(0);
        }
        window.setPot(pot);
        r.setNumPlayers(numPlayers);
    }
}
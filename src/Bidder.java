import java.util.HashMap;

public class Bidder {
    // Context about this bidder
    private int bidderNumber = 0;
    private String bidderName = null;

    // Context that surrounds this bidder
    private HashMap<Integer, Lot> lotSet = null;

    // State of readiness of the class
    private boolean bidderReady = false;

    public Bidder( HashMap<Integer, Lot> lots, String bidderName, int bidderId ) {
        if ((bidderId > 0) && (bidderName != null) && (bidderName.length() > 0)) {
            this.bidderNumber = bidderId;
            this.bidderName = bidderName;
            this.lotSet = lots;
            bidderReady = true;
        }
    }

    public int getBidderId( ) {
        return bidderNumber;
    }

    public String feesOwed() {
        String owed = "";
        int won = 0;
        int cost = 0;

        for( HashMap.Entry<Integer, Lot> entry : lotSet.entrySet() ){
            Lot theLot = entry.getValue();
            if (theLot.isClosed() && (theLot.winningBidder() == bidderNumber)) {
                // Add this lot to what's reported.
                won++;
                cost = theLot.currentBid();
            }
        }

        owed = bidderName + "\t" + won + "\t" + cost + "\n";

        return owed;
    }

    public boolean bidderIsReady() {
        return bidderReady;
    }
}

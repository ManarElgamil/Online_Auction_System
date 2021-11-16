import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class OnlineAuctionSystem {
    // The state of all the context for the auction system
    private ArrayList<Auction> auctions = null;  // all the auctions
    private ArrayList<Bidder> bidders = null;    // all the bidders
    private HashMap<Integer, Lot> lots = null;  // all the lots among the auctions

    public OnlineAuctionSystem( ) {
        // Create places to store all of the auctions, all of the bidders, and all of the auction lots.

        auctions = new ArrayList<Auction>();
        bidders = new ArrayList<Bidder>();
        lots = new HashMap<Integer, Lot>();
    }

    public Auction createAuction( String auctionName, int firstLotNumber, int lastLotNumber, int minBidIncrement ) {
        Auction theAuction = null;

        // Check that the lot ranges don't overlap.  If two ranges are distinct then one ends before the next one starts

        boolean distinctLotRange = true;
        for (Auction anAuction : auctions) {
            int lotStart = anAuction.getMinLot();
            int lotEnd = anAuction.getMaxLot();

            if (!((firstLotNumber > lotEnd ) || (lastLotNumber < lotStart))) {
                distinctLotRange = false;
                break;
            }
        }

	    // Only create an auction if the auction lot numbers won't overlap with another auction

        if (distinctLotRange) {
            // Make the auction.
            theAuction = new Auction(lots, bidders, auctionName, firstLotNumber, lastLotNumber, minBidIncrement);
            if (theAuction.auctionIsReady()) {
                // Make space for the auction if we don't already have space reserved

                if (auctions == null) {
                    auctions = new ArrayList<>();
                }

                auctions.add(theAuction);
            } else {
                theAuction = null;
            }
        }

        return theAuction;
    }

    public Bidder createBidder( String bidderName ) {
        Bidder theBidder = null;

        // Create the bidder
        theBidder = new Bidder( lots, bidderName, 1+bidders.size());
        if (theBidder.bidderIsReady()) {
            // Make space for the bidder if we don't already have space reserved

            if (bidders == null) {
                bidders = new ArrayList<>();
            }

            bidders.add(theBidder);
        } else {
            theBidder = null;
        }

        return theBidder;
    }

    public String auctionStatus( ) {
        String status = "";
        Auction auction = null;

	    // Gather the status information from each of the individual auctions that we know about

        //replaced while loop with this for loop which is easier to follow, and ensures that all the elements of
        //auctions have been looped over
        for (int i = 0; i < auctions.size(); i++) {
            auction = auctions.get(i);

            status = status + auction.getAuctionName() + "\t";

            if (auction.auctionIsOpen()) {
                status = status + "open\t";
            } else if (auction.auctionIsClosed()) {
                status = status + "closed\t";
            } else {
                status = status + "new ";
            }

            status = status + auction.auctionBidTotal() + "\n";
        }

        return status;

     }

    public int placeBid( int lotNumber, int bidderId, int bid ) {
        int outcome = Lot.LotNotAccepting;

        if ((lotNumber > 0) && (bidderId > 0) && (bid > 0)) {
            // At least it's a feasible bid.  Check out whether it's possible in the current auction config.

            Lot bidLot = lots.get( lotNumber );
            if (bidLot != null) {
                outcome = bidLot.placeBid( bid, bidderId );
            }
        }

        return outcome;
    }

    public String feesOwed( ) {
        String owed = "";

	    // Each bidder knows what they owe, so gather the status from them directly

        for (int i = 0; i < bidders.size(); i++) {
            owed += bidders.get(i).feesOwed();
        }

        return owed;
    }
}

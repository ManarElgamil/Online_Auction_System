import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.ArrayList;

class DataFlowTests {
    @Test
    void createAuctionTests() {
        HashMap<Integer, Lot> lots = new HashMap<>();
        ArrayList<Bidder> bidders = new ArrayList<>();

        Auction theAuction = new Auction( lots, bidders, "First", 2, 3, 1 );
        Bidder theBidder = new Bidder(lots, "someone", 1 );

        assertTrue( theAuction.auctionIsReady() );
        assertTrue( theBidder.bidderIsReady() );

        bidders.add( theBidder);

        // Try to bid on a new auction.  Should fail.

        Lot lot2 = lots.get( 2 );
        Lot lot3 = lots.get( 3 );

        assertEquals( 0, lot2.placeBid( 5, theBidder.getBidderId() ) );

        // Open the auction then try a bid then that should succeed.

        assertTrue( theAuction.openAuction() );
        assertEquals( 3, lot2.placeBid( 10, theBidder.getBidderId()  ) );

        // Close the auction then try a bid then that should fail.

        assertTrue( theAuction.closeAuction() );
        assertEquals( 0, lot3.placeBid( 10, theBidder.getBidderId()  ) );

    }

    @Test
    void openAndCloseTests() {
        HashMap<Integer, Lot> lots = new HashMap<>();
        ArrayList<Bidder> bidders = new ArrayList<>();

        Auction theAuction = new Auction( lots, bidders, "First", 2, 3, 1 );

        assertTrue( theAuction.auctionIsReady() );

        //  Try the options on an auction that is new

        assertFalse( theAuction.closeAuction() );
        assertTrue( theAuction.openAuction() );

        // Try the options on an auction that is open

        assertFalse( theAuction.openAuction() );
        assertTrue( theAuction.closeAuction() );

        // Try the options on an auction that is now closed

        assertFalse( theAuction.openAuction() );
        assertFalse( theAuction.closeAuction() );
    }

}
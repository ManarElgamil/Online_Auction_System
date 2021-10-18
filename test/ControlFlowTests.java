import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ControlFlowTests {

    @Test
    void createAuctionTests() {
        HashMap<Integer, Lot> lots = new HashMap<>();
        ArrayList<Bidder> bidders = new ArrayList<>();

        Auction auction1 = new Auction( lots, bidders, "First", 2, 3, 1 );
        Auction auction2 = new Auction( lots, bidders, "Second", 4, 5, 1 );
        Auction auction3 = new Auction( lots, bidders, "Third", 6, 7, 1 );

        assertTrue( auction1.auctionIsReady() );
        assertTrue( auction2.auctionIsReady() );
        assertTrue( auction3.auctionIsReady() );
    }

    @Test
    void createBidderTests() {
        HashMap<Integer, Lot> lots = new HashMap<>();
        ArrayList<Bidder> bidders = new ArrayList<>();

        Bidder bidder1 = new Bidder( lots, "Alice", 1 );
        Bidder bidder2 = new Bidder( lots, "Bob", 2 );
        Bidder bidder3 = new Bidder( lots, "Charlene", 3 );

        assertTrue( bidder1.bidderIsReady() );
        assertTrue( bidder2.bidderIsReady() );
        assertTrue( bidder3.bidderIsReady() );
    }

    @Test
    void auctionStatusTests() {
        HashMap<Integer, Lot> lots = new HashMap<>();
        ArrayList<Bidder> bidders = new ArrayList<>();

        Auction auction1 = new Auction( lots, bidders, "First", 2, 3, 1 );
        Auction auction2 = new Auction( lots, bidders, "Second", 4, 5, 1 );
        Auction auction3 = new Auction( lots, bidders, "Third", 6, 7, 1 );

        if (auction2.openAuction() && auction3.openAuction() && auction3.closeAuction()) {
            assertEquals("First\tnew\t0\n", auction1.getStatus());
            assertEquals("Second\topen\t0\n", auction2.getStatus());
            assertEquals("Third\tclosed\t0\n", auction3.getStatus());
        }
    }

    @Test
    void placeBidBadBidderTests() {
        HashMap<Integer, Lot> lots = new HashMap<>();
        ArrayList<Bidder> bidders = new ArrayList<>();
        Auction auction1 = new Auction( lots, bidders, "FirstAuction", 2, 6, 1 );
        auction1.openAuction();

        // Make a bid when no bidders exist

        Lot lot2 = lots.get( 2 );
        Lot lot3 = lots.get( 3 );
        Lot lot4 = lots.get( 4 );
        Lot lot5 = lots.get( 5 );
        Lot lot6 = lots.get( 6 );

        assertEquals( 0, lot3.placeBid( 5, 1 ));

        // Make a bid with a bad bidder number when there is one bidder

        Bidder bidder1 = new Bidder( lots, "Alice", 1 );
        bidders.add( bidder1 );

        assertEquals( 0, lot4.placeBid( 5, 3 ));

        // Make a bid with a bad bidder number when there is more than one bidder

        Bidder bidder2 = new Bidder( lots, "Bob", 2 );
        bidders.add( bidder2) ;

        assertEquals( 0, lot5.placeBid( 5, 7 ));

        // Show that valid bids are still possible
        assertEquals( 3, lot2.placeBid( 1, bidder1.getBidderId() ));
        assertEquals( 3, lot6.placeBid( 1, bidder2.getBidderId() ));
    }

    @Test
    void winningBids() {
        HashMap<Integer, Lot> lots = new HashMap<>();
        ArrayList<Bidder> bidders = new ArrayList<>();
        Auction theAuction = new Auction( lots, bidders, "FirstAuction", 10, 15, 2 );
        assertNotNull( theAuction );

        theAuction.openAuction();

        // Make some bidders to work with

        Bidder bidder1 = new Bidder( lots, "Alice", 1 );
        Bidder bidder2 = new Bidder( lots, "Bob", 2 );
        Bidder bidder3 = new Bidder( lots, "Charlie", 3 );

        bidders.add( bidder1 );
        bidders.add( bidder2 );
        bidders.add( bidder3 );

        // Set up some bids on lots.  Leave lot 11 without a bid.

        Lot lot12 = lots.get( 12 );
        Lot lot13 = lots.get( 13 );
        Lot lot14 = lots.get( 14 );

        assertEquals( 3, lot12.placeBid( 2, bidder1.getBidderId() ));

        assertEquals( 3, lot13.placeBid( 4, bidder2.getBidderId() ));
        assertEquals( 3, lot13.placeBid( 6, bidder3.getBidderId() ));

        assertEquals( 3, lot14.placeBid( 10, bidder2.getBidderId() ));
        assertEquals( 2, lot14.placeBid( 6, bidder3.getBidderId() ));

        // Check out the outcomes

        assertEquals( "10\t0\t0\n11\t0\t0\n12\t2\t1\n13\t6\t3\n14\t10\t2\n15\t0\t0\n", theAuction.winningBids() );

    }

    @Test
    void feesOwed() {
        HashMap<Integer, Lot> lots = new HashMap<>();
        ArrayList<Bidder> bidders = new ArrayList<>();

        // Get a mix of auctions to receive bids

        Auction auction1 = new Auction( lots, bidders, "FirstAuction", 10, 19, 2 );
        Auction auction2 = new Auction( lots, bidders, "SecondAuction", 20, 29, 2 );
        Auction auction3 = new Auction( lots, bidders, "ThirdAuction", 30, 39, 2 );
        Auction auction4 = new Auction( lots, bidders, "FourthAuction", 40, 49, 2 );
        Auction auction5 = new Auction( lots, bidders, "NewAuction", 50, 59, 2 );

        assertTrue( auction1.auctionIsReady() );
        assertTrue( auction2.auctionIsReady() );
        assertTrue( auction3.auctionIsReady() );
        assertTrue( auction4.auctionIsReady() );
        assertTrue( auction5.auctionIsReady() );

        assertTrue( auction1.openAuction());
        assertTrue( auction2.openAuction());
        assertTrue( auction3.openAuction());
        assertTrue( auction4.openAuction());

        // Set up some bidders too

        Bidder bidder1 = new Bidder( lots, "Alice", 1 ); // win 0 closed, 0 open
        Bidder bidder2 = new Bidder( lots, "Bob", 2 ); // win 0 closed, 1 open
        Bidder bidder3 = new Bidder( lots, "Charlie", 3 ); // win 1 closed, 0 open
        Bidder bidder4 = new Bidder( lots, "Denise", 4 ); // win 1 closed, 1 open
        Bidder bidder5 = new Bidder( lots, "Edna", 5 ); // win many closed, 0 open
        Bidder bidder6 = new Bidder( lots, "Frank", 6 ); // win many closed, many open

        assertTrue( bidder1.bidderIsReady() );
        assertTrue( bidder2.bidderIsReady() );
        assertTrue( bidder3.bidderIsReady() );
        assertTrue( bidder4.bidderIsReady() );
        assertTrue( bidder5.bidderIsReady() );
        assertTrue( bidder6.bidderIsReady() );

        bidders.add( bidder1 );
        bidders.add( bidder2 );
        bidders.add( bidder3 );
        bidders.add( bidder4 );
        bidders.add( bidder5 );
        bidders.add( bidder6 );

        // Set the bids as we'll expect.  Auctions 2 and 3 will get closed.  Auction 5 is never opened.

        // Win 0 closed, 1 open
        assertEquals( 3, lots.get( 15 ).placeBid( 2, bidder2.getBidderId() ));

        // Win 1 closed, 0 open

        assertEquals( 3, lots.get( 24 ).placeBid( 2, bidder3.getBidderId()  ));

        // Win 1 closed, 1 open

        assertEquals( 3, lots.get( 26 ).placeBid( 2, bidder4.getBidderId()  ));
        assertEquals( 3, lots.get( 16 ).placeBid( 2, bidder4.getBidderId()  ));

        // Win many closed, 0 open

        assertEquals( 3, lots.get( 30 ).placeBid( 2, bidder5.getBidderId()  ));
        assertEquals( 3, lots.get( 31 ).placeBid( 2, bidder5.getBidderId()  ));
        assertEquals( 3, lots.get( 32 ).placeBid( 2, bidder5.getBidderId()  ));
        assertEquals( 3, lots.get( 33 ).placeBid( 2, bidder5.getBidderId()  ));
        assertEquals( 3, lots.get( 34 ).placeBid( 2, bidder5.getBidderId()  ));

        // Win many closed, many open

        assertEquals( 3, lots.get( 35 ).placeBid( 2, bidder6.getBidderId()  ));
        assertEquals( 3, lots.get( 36 ).placeBid( 2, bidder6.getBidderId()  ));
        assertEquals( 3, lots.get( 37 ).placeBid( 2, bidder6.getBidderId()  ));
        assertEquals( 3, lots.get( 38 ).placeBid( 2, bidder6.getBidderId()  ));
        assertEquals( 3, lots.get( 42 ).placeBid( 2, bidder6.getBidderId()  ));
        assertEquals( 3, lots.get( 43 ).placeBid( 2, bidder6.getBidderId()  ));
        assertEquals( 3, lots.get( 44 ).placeBid( 2, bidder6.getBidderId()  ));
        assertEquals( 3, lots.get( 45 ).placeBid( 2, bidder6.getBidderId()  ));
        assertEquals( 3, lots.get( 46 ).placeBid( 2, bidder6.getBidderId()  ));

        // Close off the relevant auctions

        assertTrue( auction2.closeAuction() );
        assertTrue( auction3.closeAuction() );

        // Check out the outcomes

        assertEquals( "Alice\t0\t0\n", bidder1.feesOwed() );
        assertEquals( "Bob\t0\t0\n", bidder2.feesOwed() );
        assertEquals( "Charlie\t1\t2\n", bidder3.feesOwed() );
        assertEquals( "Denise\t1\t2\n", bidder4.feesOwed() );
        assertEquals( "Edna\t5\t10\n", bidder5.feesOwed() );
        assertEquals( "Frank\t4\t8\n", bidder6.feesOwed() );

    }

    @Test
    void lotState() {
        HashMap<Integer, Lot> lots = new HashMap<>();
        ArrayList<Bidder> bidders = new ArrayList<>();

        // Get a mix of auctions to receive bids

        Auction auction1 = new Auction(lots, bidders, "FirstAuction", 10, 19, 2);

        // Ensure that the state of a lot matches the state of the auction to which it belongs.

        Lot aLot = lots.get( 15 );

        assertFalse( aLot.isClosed() );

        auction1.openAuction();
        assertFalse( aLot.isClosed() );

        auction1.closeAuction();
        assertTrue( aLot.isClosed() );
    }
}
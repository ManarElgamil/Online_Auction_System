import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.ArrayList;

class BiddingTests {

    @Test
    void NoPriorNoReserveMinBidWins() {
        HashMap<Integer, Lot> lots = new HashMap<>();
        ArrayList<Bidder> bidders = new ArrayList<>();

        Auction newAuction = new Auction( lots, bidders,"testAuction", 10, 15, 1);
        Bidder newBidder = new Bidder( lots,"person1", 1);
        bidders.add( newBidder );

        assertTrue(newAuction.openAuction());

        Lot aLot = lots.get( 10 );

        assertEquals(3, aLot.placeBid( 1, newBidder.getBidderId() ));
    }

    @Test
    void NoPriorBelowMinBid() {
        HashMap<Integer, Lot> lots = new HashMap<>();
        ArrayList<Bidder> bidders = new ArrayList<>();

        Auction newAuction = new Auction(lots, bidders, "testAuction", 10, 15, 5);
        Bidder newBidder = new Bidder( lots,"person1", 1);
        bidders.add( newBidder );

        assertTrue(newAuction.openAuction());

        Lot aLot = lots.get( 10 );
        assertEquals(2, aLot.placeBid(1, newBidder.getBidderId() ));

        assertEquals("10\t0\t0\n11\t0\t0\n12\t0\t0\n13\t0\t0\n14\t0\t0\n15\t0\t0\n", newAuction.winningBids(), "Bid is below minimum bid");

    }

    @Test
    void NoPriorOverbidWins() {

        HashMap<Integer, Lot> lots = new HashMap<>();
        ArrayList<Bidder> bidders = new ArrayList<>();

        Auction newAuction = new Auction(lots, bidders, "testAuction", 10, 15, 1);
        Bidder newBidder = new Bidder( lots,"person1", 1);
        bidders.add( newBidder );

        assertTrue(newAuction.openAuction());

        Lot aLot = lots.get( 10 );
        assertEquals(3, aLot.placeBid(100, newBidder.getBidderId() ));

        assertEquals("10\t100\t1\n11\t0\t0\n12\t0\t0\n13\t0\t0\n14\t0\t0\n15\t0\t0\n", newAuction.winningBids(), "No reserve bid, overbid wins");

    }


    @Test
    void PriorBidBelowCurrentBid() {

        HashMap<Integer, Lot> lots = new HashMap<>();
        ArrayList<Bidder> bidders = new ArrayList<>();

        Auction newAuction = new Auction( lots, bidders,"testAuction", 10, 15, 5);
        Bidder oldBidder = new Bidder( lots,"person1", 1);
        Bidder middleBidder = new Bidder(lots, "person2", 2);
        Bidder newBidder = new Bidder(lots, "person3", 3);

        bidders.add( oldBidder );
        bidders.add( middleBidder );
        bidders.add( newBidder );

        assertTrue(newAuction.openAuction());

        Lot aLot = lots.get( 10 );

        // Get in a first bidder
        assertEquals(3, aLot.placeBid(10, oldBidder.getBidderId() ));
        // Bump up the current bid to the desired level
        assertEquals(2, aLot.placeBid(10, middleBidder.getBidderId() ));

        // Now bring in the test bidder
        assertEquals(2, aLot.placeBid(5, newBidder.getBidderId() ));

        assertEquals("10\t10\t1\n11\t0\t0\n12\t0\t0\n13\t0\t0\n14\t0\t0\n15\t0\t0\n", newAuction.winningBids(), "No reserve bid, current bid not high enough to win");
    }

    @Test
    void PriorBidBeatMaxBid() {

        HashMap<Integer, Lot> lots = new HashMap<>();
        ArrayList<Bidder> bidders = new ArrayList<>();

        Auction newAuction = new Auction(lots, bidders, "testAuction", 10, 15, 5);
        Bidder oldBidder = new Bidder(lots, "person1", 1);
        Bidder middleBidder = new Bidder(lots, "person2", 2);
        Bidder newBidder = new Bidder(lots, "person3", 3);

        bidders.add( oldBidder );
        bidders.add( middleBidder );
        bidders.add( newBidder );

        assertTrue(newAuction.openAuction());

        Lot aLot = lots.get( 10 );

        // Get in a first bidder
        assertEquals(3, aLot.placeBid(10, oldBidder.getBidderId() ));
        // Bump up the current bid to the desired level
        assertEquals(2, aLot.placeBid(10, middleBidder.getBidderId() ));

        // Now bring in the test bidder
        assertEquals(3, aLot.placeBid(20, newBidder.getBidderId() ));

        assertEquals("10\t20\t3\n11\t0\t0\n12\t0\t0\n13\t0\t0\n14\t0\t0\n15\t0\t0\n", newAuction.winningBids());

    }






    @Test
    void PriorBidBelowMin() {

        HashMap<Integer, Lot> lots = new HashMap<>();
        ArrayList<Bidder> bidders = new ArrayList<>();

        Auction newAuction = new Auction(lots, bidders, "testAuction", 10, 15, 5);
        Bidder oldBidder = new Bidder(lots, "person1", 1);
        Bidder middleBidder = new Bidder(lots, "person2", 2);
        Bidder newBidder = new Bidder(lots, "person3", 3);

        bidders.add( oldBidder );
        bidders.add( middleBidder );
        bidders.add( newBidder );

        assertTrue(newAuction.openAuction());

        Lot aLot = lots.get( 10 );

        // Get in a first bidder
        assertEquals(3, aLot.placeBid(10, oldBidder.getBidderId() ));
        // Bump up the current bid to the desired level
        assertEquals(2, aLot.placeBid(10, middleBidder.getBidderId() ));

        // Now bring in the test bidder
        assertEquals(2, aLot.placeBid(11, newBidder.getBidderId() ));

        assertEquals("10\t10\t1\n11\t0\t0\n12\t0\t0\n13\t0\t0\n14\t0\t0\n15\t0\t0\n", newAuction.winningBids());

    }

    @Test
    void bidTest() {
        HashMap<Integer, Lot> lots = new HashMap<>();
        ArrayList<Bidder> bidders = new ArrayList<>();


        Auction auction1 = new Auction(lots, bidders, "theAuction", 1, 10, 5);
        assertTrue( auction1.auctionIsReady() );

        // Check with a bid on a single lot

        Bidder bidder1 = new Bidder(lots, "newBidder", 1);
        assertTrue(bidder1.bidderIsReady());
        bidders.add( bidder1 );

        Lot lot1 = lots.get( 1 );
        Lot lot2 = lots.get( 2 );
        Lot lot10 = lots.get( 10 );

        auction1.openAuction();
        assertEquals(3, lot1.placeBid(5, bidder1.getBidderId() ));
        assertEquals("theAuction\topen\t5\n", auction1.getStatus());

        // Bid on the other lots and check the outcome of all lots bid upon

        assertEquals(3, lot2.placeBid(10, bidder1.getBidderId() ));
        assertEquals("theAuction\topen\t15\n", auction1.getStatus());
        assertEquals(3, lot10.placeBid(55, bidder1.getBidderId() ));
        assertEquals("theAuction\topen\t70\n", auction1.getStatus());

    }

}
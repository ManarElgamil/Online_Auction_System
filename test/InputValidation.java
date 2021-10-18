import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

class InputValidation {

    @Test
    void createAuction() {

        ArrayList<Bidder> bidderList = null;
        HashMap<Integer, Lot> lotList = null;

        Auction test1 = new Auction( lotList, bidderList, null,10, 15, 1);
        assertFalse( test1.auctionIsReady() );

        Auction test2 = new Auction( lotList, bidderList, "", 10, 15, 1);
        assertFalse( test2.auctionIsReady() );

        Auction test3 = new Auction( lotList, bidderList, "test1", -1, 15, 1);
        assertFalse( test3.auctionIsReady() );

        Auction test4 = new Auction( lotList, bidderList,"test2", 0, 15, 1);
        assertFalse( test4.auctionIsReady() );

        Auction test5 = new Auction( lotList, bidderList,"test3", 10, -1, 1);
        assertFalse( test5.auctionIsReady() );

        Auction test6 = new Auction( lotList, bidderList,"test4", 10, 0, 1);
        assertFalse( test6.auctionIsReady() );

        Auction test7 = new Auction( lotList, bidderList,"test5", 10, 15, -1);
        assertFalse( test7.auctionIsReady() );

        Auction test8 = new Auction( lotList, bidderList,"test6", 10, 15, 0);
        assertFalse( test8.auctionIsReady() );
    }

    @Test
    void createBidder() {
        HashMap<Integer, Lot> lotList = null;

        Bidder bidder1 = new Bidder( lotList, null, 5 );
        assertFalse( bidder1.bidderIsReady() );

        Bidder bidder2 = new Bidder( lotList, "", 6 );
        assertFalse( bidder2.bidderIsReady() );
    }

    @Test
    void placeBid() {
        ArrayList<Bidder> bidderList = new ArrayList<>();
        HashMap<Integer, Lot> lotList = new HashMap<>();

        // Load a basic auction to ensure the failure isn't from having no auction data

        Auction auction1 = new Auction( lotList, bidderList, "first", 1, 3, 10 );
        Auction auction2 = new Auction( lotList, bidderList, "second", 4, 7, 20 );

        Bidder bidder1 = new Bidder( lotList, "Alice ", 1 );
        bidderList.add( bidder1);

        // Make bids with bad parameters.  Only one parameter is bad at a time.

        assertEquals( 0, lotList.get(1).placeBid( -1, 100 ) );
        assertEquals( 0, lotList.get(1).placeBid(  0, 100 ) );
        assertEquals( 0, lotList.get(1).placeBid(  3, 100 ) );
        assertEquals( 0, lotList.get(1).placeBid(  bidder1.getBidderId(), -1 ) );
        assertEquals( 0, lotList.get(1).placeBid(  bidder1.getBidderId(), 0 ) );
    }
}
package org.equilibriums.blackjack;

import java.util.List;
import java.util.ArrayList;

import java.util.stream.IntStream;

import org.junit.Test;
import static org.junit.Assert.*;

// simplified test that uses happy path with random 2 cards for both players and dealer
// we can add more edge cases with players or dealer being busted 
public class BlackjackTest {

    @Test
    public void testWithNoPlayers() {
        test(0);
    }
    
    @Test
    public void testWithSinglePlayer() {
        test(1);
    }
    
    @Test
    public void testWithTwoPlayers() {
        test(2);
    }
    
    private void test(int numOfPlayers) {
        CardDeck cardDeck = new CardDeck();
        cardDeck.shuffle();
        
        
        Blackjack blackjack = new Blackjack();
                
        TestDealerCallback dealerCallback = new TestDealerCallback(blackjack.getDealer());
        List<AbstractPlayerCallback> playerCallbacks = new ArrayList<>();
        
        blackjack.start(
                cardDeck, 
                dealerCallback, 
                IntStream.range(0, numOfPlayers).mapToObj( i -> (p) -> { 
                    TestPlayerCallback playerCallback = new TestPlayerCallback("" + i, blackjack.getDealer(), p); 
                    playerCallbacks.add(playerCallback);
                    return playerCallback;
                 } ) );
        
        dealerCallback.assertCallback();
        playerCallbacks.forEach( pc -> pc.assertCallback() );
    }
    
    private static class AbstractPlayerCallback implements PlayerCallback {  
        protected final String name;
        
        protected final Player player;
        
        protected int firstHandCallCount = 0;
        protected int nextHandCallCount = 0;
        protected List<Player.State> playerStates = new ArrayList<>();
        
        private AbstractPlayerCallback(String name, Player player) {   
            this.name = name;
            this.player = player;
            playerStates.add( player.getState() );
        }

        @Override
        public void firstHand() {
            firstHandCallCount += 1;
            assertEquals( "Expected to only have one card on firstHand call for " + name, 1, player.getCards().count() );
        }

        @Override
        public boolean nextHand() {
            nextHandCallCount += 1;
            assertTrue( "Expected to have at least 2 cards on nextHand call for " + name, player.getCards().count() > 1 );
            return true;
        }

        @Override
        public void stateChanged() {
            playerStates.add( player.getState() );
            if ( player.getState() == Player.State.BUSTED ) {
                assertTrue( "Expected the score to be larger than 21 when state changes to BUSTED for " + name, player.getScore() > 21 );
            } 
        }   
        
        protected void assertCallback() {
            assertEquals("firstHand is expected to be called exacty once per game for " + name, 1, firstHandCallCount);
            assertTrue("nextHand is expected to be called at least once per game for " + name, nextHandCallCount > 0);
            assertTrue("sateChangeCallCount is expected have at least 2 states per game for " + name, playerStates.size() > 1);
            assertEquals("expecting first state to be PLAYING", Player.State.PLAYING, playerStates.get(0) );
            assertTrue("expecting second state to be either STANDING or BUSTED", playerStates.get(1) == Player.State.STANDING || playerStates.get(1) == Player.State.BUSTED );
        }
    } 
    
    private static class TestDealerCallback extends AbstractPlayerCallback {
        
        private TestDealerCallback(Player dealer) {       
            super("computer", dealer);
        }
        
        @Override
        public boolean nextHand() {
            super.nextHand();
            return player.getCards().count() == 2;
        }
    }
    
    private static class TestPlayerCallback extends AbstractPlayerCallback {
        private final Player dealer;
        
        private TestPlayerCallback(String name, Player dealer, Player player) {       
            super(name, player);
            this.dealer = dealer;
        }
        
        @Override
        public boolean nextHand() {
            super.nextHand();
            return player.getCards().count() == 2;
        }
        
        protected void assertCallback() {
            super.assertCallback();
            assertEquals("sateChangeCallCount is expected have exactly 3 states per game for palyer " + name, 3, playerStates.size() );
            assertTrue("expecting third state to be either LOST or WON", playerStates.get(2) == Player.State.LOST_BUSTED || playerStates.get(2) == Player.State.LOST_LOW_CARD ||
                    playerStates.get(2) == Player.State.WON_DEALER_BUSTED || playerStates.get(2) == Player.State.WON_HIGH_CARD );            
            if ( player.getState() == Player.State.LOST_LOW_CARD ) assertTrue( player.getScore() <= dealer.getScore() );            
            else if ( player.getState() == Player.State.LOST_BUSTED ) assertTrue( player.getScore() > 21 );
            else if ( player.getState() == Player.State.WON_HIGH_CARD ) assertTrue( player.getScore() > dealer.getScore() );
            else if ( player.getState() == Player.State.WON_DEALER_BUSTED ) assertTrue( dealer.getState() == Player.State.BUSTED );
        }
    }
    
}

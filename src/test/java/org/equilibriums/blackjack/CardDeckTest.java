package org.equilibriums.blackjack;

import java.util.List;


import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;

public class CardDeckTest {

    @Test
    public void test() {
        CardDeck cardDeck = new CardDeck();
        // run couple of times and make sure CardDeck shuffles cards properly
        for (int i = 0; i < 10; i++) {
            List<Card> c1 = getShuffledCards(cardDeck);
            List<Card> c2 = getShuffledCards(cardDeck);
            
            int matchCount = 0;            
            for (int j = 0; j < c1.size(); j++) {
                if ( c1.get(j).equals(c2.get(j) ) ) matchCount ++;
            }
            
            assertTrue( "Expecting to have less than half of cards matching in between shuffle",  matchCount < 26 );
        }
    }
    
    private List<Card> getShuffledCards(CardDeck cardDeck) {
        cardDeck.shuffle();
        
        List<Card> cards = new ArrayList<>();
        while( cardDeck.hasNext() ) cards.add( cardDeck.next() );
        
        assertEquals( "Expecting 52 cards from the deck", 52, cards.size() );
        
        return cards;
    }
}

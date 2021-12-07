package org.equilibriums.blackjack;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public final class CardDeck {

    private final List<Card> deck = new ArrayList<>(52);
    
    public void shuffle() {
        deck.clear();
        for ( Card.Suits s : Card.Suits.values() ) {
            for ( Card.Ranks r : Card.Ranks.values() ) {
                deck.add( new Card(s, r) );
            }
        }
        Collections.shuffle(deck);
    }

    public Card next() {
        return deck.remove(0);
    }
    
    public boolean hasNext() {
        return !deck.isEmpty();
    }
}

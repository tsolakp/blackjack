package org.equilibriums.blackjack;

import java.util.stream.Stream;

public interface Player {
            
    public enum State {PLAYING, STANDING, BUSTED, LOST_BUSTED, LOST_LOW_CARD, WON_HIGH_CARD, WON_DEALER_BUSTED}
    
    // the state of the player, it starts with PLAYING then goes to either STANDING or BUSTED, then finally to one of LOST or WON states
    // the state is updated by Blackjack algo
    public State getState();
    
    // returns cards dealed up to this point by Blackjack algo
    public Stream<Card> getCards();
    
    // return score that is the sum of the cards dealed up to this point by Blackjack algo
    public int getScore();
}

package org.equilibriums.blackjack;

//Blackjack algo will call these methods during its start method
public interface PlayerCallback {

    public void firstHand();
    
    // return true if hit or false if standing
    public boolean nextHand();
    
    // called by by Blackjack algo when Player state changes
    public void stateChanged();
}

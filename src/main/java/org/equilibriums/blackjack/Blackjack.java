package org.equilibriums.blackjack;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;

import java.util.function.Function;

// Blackjack algo that is separate from how its play moves are displayed to user 
public class Blackjack {    
    private final InternalPlayer dealer = new InternalPlayer();    
    private final List<InternalPlayer> players = new ArrayList<>();
        
    public Player getDealer() {
        return dealer;
    }
    
    public Stream<Player> getPlayers() {
        return players.stream().map(p -> p);
    }
            
    // starts Blackjack algo. We are using Function here in order to allow caller to create PlayerCallback with its Player instance
    public void start(CardDeck cardDeck, PlayerCallback dealerCallback, Stream< Function<Player, PlayerCallback> > playerCallbackFactories) {
        dealer.setPlayerCallback(dealerCallback);
        
        playerCallbackFactories.forEach( f -> {
            InternalPlayer player = new InternalPlayer(); 
            player.setPlayerCallback( f.apply(player) ); 
            players.add(player); 
         } );
                        
        // initial cards to players
        players.forEach( p -> {
            p.addCard( cardDeck.next() );
            p.getPlayerCallback().firstHand();
        } );
        
        // initial cards to dealer
        dealer.addCard( cardDeck.next() );
        dealer.getPlayerCallback().firstHand();
        
        // final cards to players        
        players.forEach( p -> dealFinalCards(cardDeck, p) );
        
        // final cards for dealer
        dealFinalCards(cardDeck, dealer);
        
        // check which player lost or won
        players.forEach( p -> {
            if ( p.getState() == Player.State.BUSTED ) p.setState( Player.State.LOST_BUSTED );
            else if ( dealer.getState() == Player.State.BUSTED ) p.setState( Player.State.WON_DEALER_BUSTED );
            else if ( dealer.getScore() < p.getScore() ) p.setState( Player.State.WON_HIGH_CARD );
            else p.setState( Player.State.LOST_LOW_CARD );
        } );
    }
        
    
    private void dealFinalCards(CardDeck cardDeck, InternalPlayer p) {
        while( p.getState() == Player.State.PLAYING ) {
            p.addCard( cardDeck.next() );
            int score = p.getScore();
            if ( score > 21 ) {
                p.setState(Player.State.BUSTED);
            } else if ( !p.getPlayerCallback().nextHand() ) {
                p.setState(Player.State.STANDING);
            }
        }     
    }
    
    private static final class InternalPlayer implements Player {        
        private State state = State.PLAYING;
        private final List<Card> cards = new ArrayList<>();
        private PlayerCallback playerCallback;
        
        @Override
        public State getState() {
            return state;
        }
        
        @Override
        public Stream<Card> getCards() {
            return cards.stream();
        }
        
        @Override
        public int getScore() {
           int score = 0;
           boolean hasAce = false;
           for (Card c : cards ) {
               if ( c.getRanks() == Card.Ranks.TWO ) {
                   score += 2;
               } else if ( c.getRanks() == Card.Ranks.THREE ) {
                   score += 3;
               } else if ( c.getRanks() == Card.Ranks.FOUR ) {
                   score += 4;
               } else if ( c.getRanks() == Card.Ranks.FIVE ) {
                   score += 5;
               } else if ( c.getRanks() == Card.Ranks.SIX ) {
                   score += 6;
               } else if ( c.getRanks() == Card.Ranks.SEVEN ) {
                   score += 7;
               } else if ( c.getRanks() == Card.Ranks.EIGHT ) {
                   score += 8;
               } else if ( c.getRanks() == Card.Ranks.NINE ) {
                   score += 9;
               } else if ( c.getRanks() == Card.Ranks.TEN ) {
                   score += 10;
               } else if ( c.getRanks() == Card.Ranks.JACK ) {
                   score += 10;
               } else if ( c.getRanks() == Card.Ranks.QUEEN ) {
                   score += 10;
               } else if ( c.getRanks() == Card.Ranks.KING ) {
                   score += 10;
               } else if ( c.getRanks() == Card.Ranks.ACE ) {
                   score += 1;
                   hasAce = true;
               }
            }            
            if (!hasAce) return score;
            else if ( score < 12 ) return score + 10;
            else return score;
        }
        
        
        private void setState(State state) {
            this.state = state;
            playerCallback.stateChanged();
        }
        
        private void addCard(Card card) {
            cards.add(card);
        }
        
        private PlayerCallback getPlayerCallback() {
            return playerCallback;
        }
        
        private void setPlayerCallback(PlayerCallback playerCallback) {
            this.playerCallback = playerCallback;
        }        
    }
}

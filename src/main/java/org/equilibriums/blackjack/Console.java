package org.equilibriums.blackjack;

import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// contains main method that prints moves called back by Blackjack algo.
// it also implements hit or stand logic for human player by using System.in calls.
public class Console {
        
    public static void main(String... args) {
        int numPlayers = Integer.parseInt( args[0] );
        
        System.out.println("Starting game with " + numPlayers + " players." );    
        
        System.out.println("Shuffling");
        CardDeck cardDeck = new CardDeck();
        cardDeck.shuffle();
        
        Blackjack blackjack = new Blackjack();
                
        blackjack.start(
                cardDeck, 
                new ConsoleDealerCallback( blackjack.getDealer() ), 
                IntStream.range(0, numPlayers).mapToObj( i -> (p) -> new ConsolePlayerCallback( blackjack.getDealer(), p, i ) ) );
 
    }
    
    private static String printCards(Player player, String name) {
        return "Dealing to " + name + ", cards: " + player.getCards().map(Card::toString).collect( Collectors.joining(", ") ) + ".";
    }
    
    private static final class ConsoleDealerCallback implements PlayerCallback {   
        private final Player dealer;
        
        private ConsoleDealerCallback(Player dealer) {        
            this.dealer = dealer;
        }

        @Override
        public void firstHand() {
            System.out.println("Dealing to computer, card: face down");
        }

        @Override
        public boolean nextHand() {
            // a very simple logic for dealer indicating it needs to stand when total score goes above 15
            // we can change this logic by implementing another PlayerCallback for dealer ot moving this logic
            // to another strategy object
            int score = dealer.getScore();
            if ( score > 15 ) return false;
            
            System.out.println( printCards(dealer, "computer") + " Dealer hits." );
            return true;
        }

        @Override
        public void stateChanged() {
            if ( dealer.getState() == Player.State.BUSTED ) {
                System.out.println( printCards(dealer, "computer") + " Busted over 21." );
            } else if ( dealer.getState() == Player.State.STANDING ) {
                System.out.println( printCards(dealer, "computer") + " Dealer stands." );
            }
        }       
    }
    
    private static final class ConsolePlayerCallback implements PlayerCallback {
        private static Scanner scanner = new Scanner(System.in);
                
        private final Player dealer;
        private final Player player;
        private final String name;
        
        private ConsolePlayerCallback(Player dealer, Player player, int index) {  
            this.dealer = dealer;
            this.player = player;
            this.name = "" + (index + 1);
        }

        @Override
        public void firstHand() {
            System.out.println( printCards(player, "player " + name) );
        }

        @Override
        public boolean nextHand() {
            System.out.print( printCards(player, "player " + name) + " Hit or Stand?" );
            String input = scanner.next();
            if ( input.startsWith("h") ) return true;
            else return false;
        }

        @Override
        public void stateChanged() {
            if ( player.getState() == Player.State.BUSTED ) {
                System.out.println( printCards(player, "player " + name) + " Busted over 21." );
            } else if (  player.getState() == Player.State.LOST_BUSTED ) {
                System.out.println( "Scoring player " + name + " busted. Dealer wins"); 
            } else if (  player.getState() == Player.State.LOST_LOW_CARD ) {
                System.out.println( "Scoring player " + name + " has " + player.getScore() + ", dealer has " + dealer.getScore() + ". Dealer wins"); 
            } else if (  player.getState() == Player.State.WON_DEALER_BUSTED ) {
                System.out.println( "Scoring player " + name + " has " + player.getScore() + ", dealer busted" + ". Player " + name + " wins"); 
            } else if (  player.getState() == Player.State.WON_HIGH_CARD ) {
                System.out.println( "Scoring player " + name + " has " + player.getScore() + ", dealer has " + dealer.getScore() + ". Player " + name + " wins"); 
            }
        }       
    }
}

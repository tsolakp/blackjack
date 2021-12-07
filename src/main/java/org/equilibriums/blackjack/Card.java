package org.equilibriums.blackjack;

import java.util.Objects;

public final class Card {

    public enum Suits {
        CLUBS("Clubs"),
        DIAMONDS("Diamonds"),
        HEARTS("Hearts"),
        SPADES("Spades");
        
        private String name;
        
        private Suits(String name) {
            this.name = name;
        }      
        
        @Override
        public String toString() {
            return name;
        }
    }

    public enum Ranks {
        TWO("2"),
        THREE("3"),
        FOUR("4"),
        FIVE("5"),
        SIX("6"),
        SEVEN("7"),
        EIGHT("8"),
        NINE("9"),
        TEN("10"),
        JACK("Jack"),
        QUEEN("Queen"),
        KING("King"),
        ACE("Ace");
        
        private String name;
        
        private Ranks(String name) {
            this.name = name;
        }      
        
        @Override
        public String toString() {
            return name;
        }
    }

    private final Suits suits;
    private final Ranks ranks;

    public Card(Suits suits, Ranks ranks) {
        this.suits = suits;
        this.ranks = ranks;
    }

    public Suits getSuits() {
        return suits;
    }

    public Ranks getRanks() {
        return ranks;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(suits, ranks);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return Objects.equals(suits, card.suits) && Objects.equals(ranks, card.ranks);
    }
    
    @Override
    public String toString() {
        return ranks + " " + suits;
    }
}

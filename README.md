Simple console based Blackjack game.

Here is sample output.

```
Starting game with 3 players.
Shuffling
Dealing to player 1, cards: 8 Diamonds.
Dealing to player 2, cards: 3 Clubs.
Dealing to player 3, cards: 3 Diamonds.
Dealing to computer, card: face down
Dealing to player 1, cards: 8 Diamonds, Queen Diamonds. Hit or Stand?s
Dealing to player 2, cards: 3 Clubs, Queen Spades. Hit or Stand?h
Dealing to player 2, cards: 3 Clubs, Queen Spades, Jack Clubs. Busted over 21.
Dealing to player 3, cards: 3 Diamonds, 9 Spades. Hit or Stand?h
Dealing to player 3, cards: 3 Diamonds, 9 Spades, Jack Hearts. Busted over 21.
Dealing to computer, cards: 5 Hearts, 2 Spades. Dealer hits.
Dealing to computer, cards: 5 Hearts, 2 Spades, 4 Diamonds. Dealer hits.
Dealing to computer, cards: 5 Hearts, 2 Spades, 4 Diamonds, 4 Clubs. Dealer hits.
Dealing to computer, cards: 5 Hearts, 2 Spades, 4 Diamonds, 4 Clubs, 2 Diamonds. Dealer stands.
Scoring player 1 has 18, dealer has 17. Player 1 wins
Scoring player 2 busted. Dealer wins
Scoring player 3 busted. Dealer wins
```

You can either run `org.equilibriums.blackjack.Console` from IDE to start the game of build the project with maven and then run the jar like this `java -jar blackjack-1.0-SNAPSHOT.jar 3`
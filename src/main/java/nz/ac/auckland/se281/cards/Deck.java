package nz.ac.auckland.se281.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import nz.ac.auckland.se281.Main.Country;

public class Deck {

  private List<Unit> deck;
  private Random rand;

  public Deck(Country country) {
    deck = new ArrayList<Unit>();
    rand = new Random();

    switch (country) {
      case SOVIET:
        addSovietCards();
        break;
      case USA:
        addUSACards();
        break;
      case JAPAN:
        addJapanCards();
        break;
      case GERMANY:
        addGermanyCards();
        break;
      case BRITAIN:
        addBritianCards();
        break;
    }
  }

  public Unit drawACard() {
    int num = -1;
    Unit temp;

    while (num >= 0 && num < deck.size()) {
      num = rand.nextInt();
    }

    temp = deck.get(num);
    deck.remove(temp);
    return temp;
  }

  public int size() {
    return deck.size();
  }

  private void addSovietCards() {}

  private void addUSACards() {}

  private void addJapanCards() {}

  private void addGermanyCards() {}

  private void addBritianCards() {}
}

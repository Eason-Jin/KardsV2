package code.player;

import code.Main.Country;
import code.cards.Deck;
import code.cards.Line;
import code.cards.Unit;

public abstract class Player {
  protected int actionPoints;
  protected Deck deck;
  protected Line hand;

  public Player(Country country) {
    this.actionPoints = 0;
    this.deck = new Deck(country);
    this.hand = new Line(9);
  }

  public Deck getDeck() {
    return deck;
  }

  public Line getHand() {
    return hand;
  }

  public void addToHand(Unit card) {
    if (!hand.overSizeLimit()) {
      hand.add(card);
    }
  }

  public void setActionPoints(int points) {
    this.actionPoints = points;
  }

  public int getActionPoints() {
    return actionPoints;
  }

  public void subtractActionPoints(int points) {
    this.actionPoints -= points;
  }

  public boolean outOfActionPoints() {
    return actionPoints == 0 ? true : false;
  }
}

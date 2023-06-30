package code.cards;

import code.Main.Country;
import code.Main.Position;

public abstract class Card {
  protected Country country;
  protected int health;
  protected String name;
  protected Position position;

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public String getName() {
    return name;
  }

  public boolean destroyed() {
    return this.health <= 0 ? true : false;
  }

  public int getPosition() {
    return position.ordinal();
  }
}

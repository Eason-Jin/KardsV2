package nz.ac.auckland.se281.cards;

import nz.ac.auckland.se281.Main.Country;
import nz.ac.auckland.se281.Main.Position;
import nz.ac.auckland.se281.Main.Type;
import nz.ac.auckland.se281.player.Player;

public abstract class Unit extends Card {

  // HEALTH is in super class
  private Type type;
  private int attack;
  private int armor;
  private int deployCost;
  private int actionCost;
  private int range;
  private int action;
  private int actionLimit;
  private Player owner;

  public Unit(
      Country country,
      Type type,
      String name,
      int attack,
      int health,
      int armor,
      int deployCost,
      int actionCost) {
    this.country = country;
    this.type = type;
    this.name = name;
    this.attack = attack;
    this.health = health;
    this.armor = armor;
    this.deployCost = deployCost;
    this.actionCost = actionCost;
    this.position = Position.HAND;
    action = 0;

    // TANK can deploy/move or move/attack in one round
    if (type == Type.TANK) {
      actionLimit = 2;
    } else {
      actionLimit = 1;
    }

    // FIGHTER/BOMBER/ARTILLARY has range 2
    if (type == Type.FIGHTER || type == Type.BOMBER || type == Type.ARTILLARY) {
      this.range = 2;
    } else {
      this.range = 1;
    }
  }

  public int getRange() {
    return range;
  }

  public Type getType() {
    return type;
  }

  public String getTypeString() {
    return type.toString();
  }

  public int getAttack() {
    return attack;
  }

  public int getArmor() {
    return armor;
  }

  public int getDeployCost() {
    return deployCost;
  }

  public int getActionCost() {
    return actionCost;
  }

  public void deploy() {
    if (position == Position.HAND) {
      position = Position.SUPPORTLINE;
    } else {
      System.out.println("Unit already deployed!");
    }

    action++;
  }

  public void move() {
    if (!overActionLimit()) {
      if (position == Position.SUPPORTLINE) {
        position = Position.FRONTLINE;
        action++;
      } else if (position == Position.FRONTLINE) {
        System.out.println("Unit already in front line!");
      } else if (position == Position.HAND) {
        System.out.println("Unit is not deployed!");
      }
    } else {
      System.out.println("Unit out of action points!");
    }
  }

  public void attack(Card otherCard) {
    if (!overActionLimit()) {
      otherCard.setHealth(otherCard.getHealth() - this.getAttack());
      // Units attack back
      if (otherCard instanceof Unit) {
        // Armor reduces damage
        otherCard.setHealth(otherCard.getHealth() + ((Unit) otherCard).getArmor());
        // BOMBER/ARTILLARY does not take return fire damage
        if (this.getType() != Type.BOMBER && this.getType() != Type.ARTILLARY) {
          this.setHealth(this.getHealth() - ((Unit) otherCard).getAttack() + this.getArmor());
        }
      }
      action++;
    } else {
      System.out.println("Unit out of action points!");
    }
  }

  public void resetAction() {
    this.action = 0;
  }

  public boolean overActionLimit() {
    return action < actionLimit ? false : true;
  }

  public void setOwner(Player player) {
    this.owner = player;
  }
}

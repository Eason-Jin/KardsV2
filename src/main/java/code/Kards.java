package code;

import code.Main.Country;
import code.cards.Card;
import code.cards.HeadQuarter;
import code.cards.Line;
import code.cards.Unit;
import code.player.Human;
import code.player.Player;
import java.util.ArrayList;
import java.util.List;

public class Kards {

  private int round;
  // Each unit will have a number(pos) in the line. All actions referring to that unit will use pos
  // instead of full name
  private Line friendlySupportLine;
  private Line frontline;
  private Line enemySupportLine;
  private Player frontlineOccupant;

  private Player human;
  // private Ai ai;

  private List<String> combatLog;

  public Kards() {
    // Only this constructor can be used (if you need to initialise fields).
  }

  public void newGame(Country country) {

    // Create new player
    human = new Human(country);
    friendlySupportLine.add(new HeadQuarter(country));
    // Start with 4 cards
    for (int i = 0; i < 4; i++) {
      human.addToHand(human.getDeck().drawACard());
    }

    // Set round
    round = 0;
    human.setActionPoints(round);

    // Create lines
    friendlySupportLine = new Line(5);
    frontline = new Line(5);
    enemySupportLine = new Line(5);
    frontlineOccupant = null;

    // Start round
    nextRound();
    combatLog = new ArrayList<String>();

    printGame();
  }

  public void deploy(String unitCode) {
    // Check if game started
    if (human == null) {
      System.out.println("Game has not started!");
    }
    Unit unit = (Unit) convertStringToUnit(unitCode);

    // Check return
    if (unit == null) {
      return;
    }

    // Check if unit cost is less than actionPoints
    if (unit.getDeployCost() <= human.getActionPoints()) {
      System.out.println("Not enough action points!");
      return;
    }

    // Deploy the unit
    human.getHand().remove(unit);
    unit.deploy();
    friendlySupportLine.add(unit);
    human.setActionPoints(human.getActionPoints() - unit.getDeployCost());
    combatLog.add(unit.getName() + " is deployed!");

    printGame();
  }

  public void attack(String unitA, String unitB) {
    // Check if game started
    if (human == null) {
      System.out.println("Game has not started!");
    }

    Unit attacker;
    Card target; // TARGET can be HQ

    attacker = (Unit) convertStringToUnit(unitA);
    // Check return
    if (attacker == null) {
      return;
    }

    target = convertStringToUnit(unitA);
    // Check return
    if (target == null) {
      return;
    }

    // Check range
    if ((2 - abs(target.getPosition() - attacker.getPosition())) > attacker.getRange()) {
      System.out.println("Target out of range!");
      return;
    }
    attacker.attack(target);
    human.setActionPoints(human.getActionPoints() - attacker.getActionCost());
    combatLog.add(unitA + " attacked " + unitB + "!");

    // Check if HQ is destroyed
    if (target instanceof HeadQuarter && target.destroyed()) {
      combatLog.add(target.getName() + "is destroyed");
      combatLog.add("Game Over!");
      return;
    }

    // Remove destroyed units
    removeDestroyedUnits(enemySupportLine);
    removeDestroyedUnits(frontline);
    removeDestroyedUnits(friendlySupportLine);

    printGame();
  }

  public void move(String unitCode) {
    // Check if game started
    if (human == null) {
      System.out.println("Game has not started!");
    }

    Unit unit = (Unit) convertStringToUnit(unitCode);
    // Check return
    if (unit == null) {
      return;
    }

    // Check if frontline is full
    if (frontline.overSizeLimit()) {
      combatLog.add("Frontline is full!");
    }

    // Move into unoccupied frontline
    if (frontline.isEmpty()) {
      unit.move();
      frontlineOccupant = human;
    }

    // Move into occupied frontline
    if (frontlineOccupant == human) {
      unit.move();
    } else {
      combatLog.add("Frontline captured by enemy!");
    }

    printGame();
  }

  public void nextRound() {
    // Check if game started
    if (human == null) {
      System.out.println("Game has not started!");
    }

    printGame();

    round++;
    human.setActionPoints(round);
    combatLog.clear();

    // Reset unit action counter
    resetLineUnitAction(enemySupportLine);
    resetLineUnitAction(frontline);
    resetLineUnitAction(friendlySupportLine);

    // TODO: AI ACTIONS WILL BE SET HERE
  }

  private int abs(int i) {
    if (i < 0) {
      return -i;
    } else {
      return i;
    }
  }

  private void resetLineUnitAction(Line line) {
    for (int i = 0; i < line.size(); i++) {
      line.getUnit(i).resetAction();
    }
  }

  private void removeDestroyedUnits(Line line) {
    Unit unit;
    for (int i = 0; i < line.size(); i++) {
      unit = line.getUnit(i);
      if (unit.destroyed()) {
        combatLog.add(unit.getName() + " is destroyed!");
        line.remove(unit);
      }
    }
  }

  private Card convertStringToUnit(String string) {
    int unitPos;
    // Check if the input is a number
    try {
      unitPos = Integer.parseInt(string);
      // Check if input is in range
      if (unitPos < 0 || unitPos >= human.getHand().size()) {
        System.out.println(
            "Index out of range! Enter a number between 0 and " + (human.getHand().size() - 1));
        return null;
      } else {
        return human.getHand().getUnit(unitPos);
      }
    } catch (Exception e) {
      System.out.println(
          "Invalid input! Enter a number between 0 and " + (human.getHand().size() - 1));
      return null;
    }
  }

  private void printGame() {
    System.out.println("Enemy Action Points: " + round); // Round as a placeholder
    printLineAcross();

    System.out.println("Enemy Support Line: ");
    enemySupportLine.printLine();
    printLineAcross();
    System.out.println("Frontline");
    if (frontline.isEmpty()) {
      System.out.println();
    } else {
      frontline.printLine();
    }
    printLineAcross();
    System.out.println("Friendly Support Line: ");
    friendlySupportLine.printLine();
    printLineAcross();

    System.out.println("Action Points: " + human.getActionPoints());
    human.getHand().printLine();
    printLineAcross();

    System.out.println("Combat Info: ");
    for (int i = 0; i < combatLog.size(); i++) {
      System.out.println(combatLog.get(i));
    }
  }

  private void printLineAcross() {
    System.out.println("------------------------------------------------------------");
  }
}

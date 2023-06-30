package nz.ac.auckland.se281.cards;

import java.util.ArrayList;
import java.util.List;

// Line class for SUPPORTLINE, FRONTLINE, and HAND
public class Line {

  private List<Card> line;
  private int sizeLimit;

  public Line(int sizeLimit) {
    this.line = new ArrayList<Card>();
    this.sizeLimit = sizeLimit;
  }

  public void add(Card card) {
    line.add(card);
  }

  public void remove(Unit card) {
    line.remove(card);
  }

  public int size() {
    return line.size();
  }

  public boolean isEmpty() {
    return line.isEmpty();
  }

  public boolean overSizeLimit() {
    return this.size() >= sizeLimit ? true : false;
  }

  public Unit getUnit(int index) {
    return (Unit) line.get(index);
  }

  public HeadQuarter getHq() {
    // HQ should always be at position 0
    return (HeadQuarter) line.get(0);
  }

  public void printLine() {
    HeadQuarter hq;
    Unit unit;

    hq = getHq();
    System.out.println("HQ: " + hq.getName() + "\tHP: " + hq.getHealth());
    for (int i = 1; i < this.size(); i++) {
      unit = this.getUnit(i);
      System.out.println(
          "Unit: "
              + unit.getName()
              + "\tATK: "
              + unit.getAttack()
              + "\tHP: "
              + unit.getHealth()
              + "\tARMOR: "
              + unit.getArmor());
    }
  }
}

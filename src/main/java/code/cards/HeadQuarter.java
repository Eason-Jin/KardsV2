package code.cards;

import code.Main.Country;

public class HeadQuarter extends Card {

  public HeadQuarter(Country country) {
    switch (country) {
      case SOVIET:
        this.name = "Stalingrad";
        break;
      case USA:
        this.name = "Cherbourg";
        break;
      case JAPAN:
        this.name = "Turk";
        break;
      case GERMANY:
        this.name = "Danzig";
        break;
      case BRITAIN:
        this.name = "Alexandria";
        break;
    }

    this.health = 20;
  }

  public String getName() {
    return country.toString().toUpperCase();
  }
}

package code;

import java.util.Scanner;

public class Main {
  public enum Country {
    SOVIET,
    USA,
    JAPAN,
    GERMANY,
    BRITAIN;

    public static boolean isValidEnum(String s) {
      // return true if it is a valid enum (to cehck for valid input)
      for (Country c : values()) {
        if (c.name().equals(s)) {
          return true;
        }
      }
      return false;
    }
  }

  public enum Type {
    INFANTRY,
    ARTILLARY,
    TANK,
    BOMBER,
    FIGHTER;
  }

  public enum Position {
    HAND, // 0
    FRONTLINE, // 1
    SUPPORTLINE; // 2
  }

  public enum Command {
    NEW_GAME(
        2,
        "start a new game <COUNTRY>\n"
            + "\t \t \t \t \t \tCOUNTRY:  SOVIET \n"
            + "\t \t \t \t \t \t \t \t   USA \n"
            + "\t \t \t \t \t \t \t \t   JAPAN \n"
            + "\t \t \t \t \t \t \t \t   GERMANY \n"
            + "\t \t \t \t \t \t \t \t   BRITAIN ",
        "What is your name?"),

    DEPLOY(1, "Deploy a unit <UNIT>"),
    MOVE(1, "Move a unit to frontline <UNIT>"),
    ATTACK(2, "<UNIT_A> attacks <UNIT_B>"),
    END_ROUND(0, "End current round"),

    // POLICY_HOME(
    //     0,
    //     "Create a new home policy for the currently-loaded profile",
    //     "Sum to insure",
    //     "Property address",
    //     "Rental? (y/n)"),
    // POLICY_CAR(
    //     0,
    //     "Create a new car policy for the currently-loaded profile",
    //     "Sum to insure",
    //     "Make and model",
    //     "License plate",
    //     "Mechanical warranty? (y/n)"),
    // POLICY_LIFE(0, "Create a new life policy for the currently-loaded profile", "Sum to insure"),

    HELP(0, "Print usage"),
    EXIT(0, "Exit the application");

    private final int numArgs;
    private final String message;
    private final String[] optionPrompts;

    private Command(final int numArgs, final String message) {
      this(numArgs, message, new String[] {});
    }

    private Command(final int numArgs, final String message, final String... optionPrompts) {
      this.numArgs = numArgs;
      this.message = message;
      this.optionPrompts = optionPrompts;
    }

    public boolean hasArguments() {
      return numArgs > 0;
    }

    public int getNumArgs() {
      return numArgs;
    }

    public boolean hasOptions() {
      return optionPrompts.length > 0;
    }

    public int getNumOptions() {
      return optionPrompts.length;
    }

    public String getOptionPrompt(final int index) {
      return optionPrompts[index];
    }
  }

  private static final String COMMAND_PREFIX = "insurance system> ";

  public static void main(final String[] args) {
    new Main(new Scanner(System.in), new Kards()).start();
  }

  public static String help() {
    final StringBuilder sb = new StringBuilder();

    for (final Command command : Command.values()) {
      sb.append(command).append("\t");

      // Add extra padding to align the argument counts
      // if the command name is less than the tab width.
      if (command.toString().length() < 8) {
        sb.append("\t");
      }

      if (command.numArgs > 0) {
        sb.append("[").append(command.numArgs).append(" arguments]");
      } else {
        sb.append("[no args]");
      }

      sb.append("\t").append(command.message).append(System.lineSeparator());
    }

    return sb.toString();
  }

  private static void printBanner() {
    // https://patorjk.com/software/taag/
    // https://www.freeformatter.com/java-dotnet-escape.html#before-output

    System.out.println(
        " .----------------.  .----------------.  .----------------.  .----------------. "
            + " .----------------. \r\n"
            + "| .--------------. || .--------------. || .--------------. || .--------------. ||"
            + " .--------------. |\r\n"
            + "| |  ___  ____   | || |      __      | || |  _______     | || |  ________    | || | "
            + "   _______   | |\r\n"
            + "| | |_  ||_  _|  | || |     /  \\     | || | |_   __ \\    | || | |_   ___ `.  | ||"
            + " |   /  ___  |  | |\r\n"
            + "| |   | |_/ /    | || |    / /\\ \\    | || |   | |__) |   | || |   | |   `. \\ | ||"
            + " |  |  (__ \\_|  | |\r\n"
            + "| |   |  __'.    | || |   / ____ \\   | || |   |  __ /    | || |   | |    | | | || |"
            + "   '.___`-.   | |\r\n"
            + "| |  _| |  \\ \\_  | || | _/ /    \\ \\_ | || |  _| |  \\ \\_  | || |  _| |___.' / |"
            + " || |  |`\\____) |  | |\r\n"
            + "| | |____||____| | || ||____|  |____|| || | |____| |___| | || | |________.'  | || | "
            + " |_______.'  | |\r\n"
            + "| |              | || |              | || |              | || |              | || | "
            + "             | |\r\n"
            + "| '--------------' || '--------------' || '--------------' || '--------------' ||"
            + " '--------------' |\r\n"
            + " '----------------'  '----------------'  '----------------'  '----------------' "
            + " '----------------' ");
  }

  private final Scanner scanner;

  private final Kards kards;

  public Main(final Scanner scanner, final Kards kards) {
    this.scanner = scanner;
    this.kards = kards;
  }

  public void start() {
    printBanner();
    System.out.println(help());

    String command;

    // Prompt and process commands until the exit command.
    do {
      System.out.print(COMMAND_PREFIX);
      command = scanner.nextLine().trim();
    } while (processCommand(command));
  }

  private boolean processCommand(String input) {
    // Remove whitespace at the beginning and end of the input.
    input = input.trim();

    final String[] args = input.split(" ");

    // Allow any case, and dashes to be used instead of underscores.
    final String commandStr = args[0].toUpperCase().replaceAll("-", "_");

    final Command command;

    try {
      // Command names correspond to the enum names.
      command = Command.valueOf(commandStr);
    } catch (final Exception e) {
      MessageCli.COMMAND_NOT_FOUND.printMessage(commandStr);
      return true;
    }

    if (!checkArgs(command, args)) {
      MessageCli.WRONG_ARGUMENT_COUNT.printMessage(
          String.valueOf(command.getNumArgs()), command.getNumArgs() > 1 ? "s" : "", commandStr);
      return true;
    }

    switch (command) {
      case NEW_GAME:
        String country = args[1].toUpperCase();
        if (!Country.isValidEnum(country)) {
          MessageCli.INVALID_COUNTRY.printMessage();
          break;
        }
        kards.newGame(Country.valueOf(country));
        break;
      case DEPLOY:
        kards.deploy(args[1]);
        break;
      case ATTACK:
        kards.attack(args[1], args[2]);
        //   break;
        // case DELETE_PROFILE:
        //   insuranceSystem.deleteProfile(args[1]);
        //   break;
        // case PRINT_DB:
        //   insuranceSystem.printDatabase();
        //   break;
        // case POLICY_HOME:
        //   insuranceSystem.createPolicy(PolicyType.HOME, processOptions(command));
        //   break;
        // case POLICY_CAR:
        //   insuranceSystem.createPolicy(PolicyType.CAR, processOptions(command));
        //   break;
      case END_ROUND:
        kards.nextRound();
        break;
      case EXIT:
        MessageCli.END.printMessage();

        // Signal that the program should exit.
        return false;
      case HELP:
        System.out.println(help());
        break;
    }

    // Signal that another command is expected.
    return true;
  }

  private String[] processOptions(final Command command) {
    final String[] options = new String[command.getNumOptions()];

    for (int i = 0; i < command.getNumOptions(); i++) {
      System.out.print("\t" + command.getOptionPrompt(i) + ": ");
      options[i] = scanner.nextLine().trim();
    }

    return options;
  }

  private boolean checkArgs(final Command command, final String[] args) {
    return command.getNumArgs() == args.length - 1;
  }
}

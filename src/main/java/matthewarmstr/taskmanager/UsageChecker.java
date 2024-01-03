package matthewarmstr.taskmanager;

public class UsageChecker {
    public static void checkCorrectCommandUsage(String[] args) throws MalformedInputException {
        String userAction = args[0];
        switch (userAction) {
            case "commands" -> throw new MalformedInputException("");
            case "start" -> {
                if (args.length != 2) { throw new MalformedInputException("Invalid number of arguments for 'start'.\n"); }
            }
            case "stop" -> {
                if (args.length != 2) { throw new MalformedInputException("Invalid number of arguments for 'stop'.\n"); }
            }
            case "describe" -> {
                if (args.length < 3) { throw new MalformedInputException("Invalid number of arguments for 'describe'.\n"); }
            }
            case "summary" -> {
                if (args.length > 2) { throw new MalformedInputException("Invalid number of arguments for 'summary'.\n"); }
            }
            case "size" -> {
                if (args.length != 3) { throw new MalformedInputException("Invalid number of arguments for 'size'.\n"); }
                if ((!isValidSize(args[2]))) { throw new MalformedInputException(args[2] + " is not a valid size.\n"); }
            }
            case "rename" -> {
                if (args.length != 3) { throw new MalformedInputException("Invalid number of arguments for 'rename'.\n"); }
            }
            case "delete" -> {
                if (args.length != 2) { throw new MalformedInputException("Invalid number of arguments for 'delete'.\n"); }
            }
            default -> throw new MalformedInputException("'" + String.join(" ", args) + "' is not a valid input.\n");
        }
    }

    public static boolean isValidSize(String size) {
        return (size.equals("S") || size.equals("M") || size.equals("L") || size.equals("XL"));
    }

    public static void checkIfValidSortingMethod(String[] inputLine) throws SummaryException {
        if (inputLine.length != 2) { throw new SummaryException("Sorting input requires two arguments."); }

        if (inputLine[0].equals("name") || inputLine[0].equals("size") || inputLine[0].equals("time")) {
            if (inputLine[1].equals("up") || inputLine[1].equals("down")) {
                return;
            }
        }

        throw new SummaryException("Invalid sorting input.");
    }
}

package matthewarmstr.taskmanager;

import java.util.Arrays;

public class UsageChecker {
    public static void checkCorrectUsage(String[] args) throws MalformedInputException {
        String userAction = args[0];
        if (userAction.equals("commands")) {
            throw new MalformedInputException("");
        } else if (userAction.equals("start")) {
            if (args.length != 2) {
                throw new MalformedInputException("Invalid number of arguments for 'start'.\n");
            }
        } else if (userAction.equals("stop")) {
            if (args.length != 2) {
                throw new MalformedInputException("Invalid number of arguments for 'stop'.\n");
            }
        } else if (userAction.equals("describe")) {
            if (args.length < 3) {
                throw new MalformedInputException("Invalid number of arguments for 'describe'.\n");
            }
        } else if (userAction.equals("summary")) {
            if (args.length > 2) {
                throw new MalformedInputException("Invalid number of arguments for 'summary'.\n");
            }
        } else if (userAction.equals("size")) {
            if (args.length != 3) {
                throw new MalformedInputException("Invalid number of arguments for 'size'.\n");
            }
            if ((!isValidSize(args[2]))) {
                throw new MalformedInputException(args[2] + "is not a valid size.\n");
            }
        } else if (userAction.equals("rename")) {
            if (args.length != 3) {
                throw new MalformedInputException("Invalid number of arguments for 'rename'.\n");
            }
        } else if (userAction.equals("delete")) {
            if (args.length != 2) {
                throw new MalformedInputException("Invalid number of arguments for 'delete'.\n");
            }
        } else {
            throw new MalformedInputException("'" + String.join(" ", args) + "' is not a valid input.\n");
        }
    }

    public static boolean isValidSize(String size) {
        return (size.equals("S") || size.equals("M") ||
                size.equals("L") || size.equals("XL"));
    }
}

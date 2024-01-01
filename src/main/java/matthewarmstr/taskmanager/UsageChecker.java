package matthewarmstr.taskmanager;

public class UsageChecker {
    public static void checkCorrectUsage(String[] args) throws MalformedInputException {
        if (args.length == 0) {
            throw new MalformedInputException();
        }

        String userAction = args[0];
        if (userAction.equals("commands")) {
            throw new MalformedInputException();
        } else if (userAction.equals("start")) {
            if (args.length != 2)
            { throw new MalformedInputException(); }
        } else if (userAction.equals("stop")) {
            if (args.length != 2)
            { throw new MalformedInputException(); }
        } else if (userAction.equals("describe")) {
            if (args.length < 3)
            { throw new MalformedInputException(); }
        } else if (userAction.equals("summary")) {
            if (args.length > 2)
            { throw new MalformedInputException(); }
        } else if (userAction.equals("size")) {
            if (args.length != 3)
            { throw new MalformedInputException(); }
            if ((!isValidSize(args[2])))
            { throw new MalformedInputException(); }
        } else if (userAction.equals("rename")) {
            if (args.length != 3)
            { throw new MalformedInputException(); }
        } else if (userAction.equals("delete")) {
            if (args.length != 2)
            { throw new MalformedInputException(); }
        } else {
            throw new MalformedInputException();
        }
    }

    public static boolean isValidSize(String size) {
        return (size.equals("S") || size.equals("M") ||
                size.equals("L") || size.equals("XL"));
    }
}

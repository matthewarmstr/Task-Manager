package matthewarmstr.taskmanager;

public class UsageChecker {
    public static void checkCorrectUsage(String[] args) throws UsageException {
        if (args.length == 0) {
            throw new UsageException();
        }

        String userAction = args[0];
        if (userAction.equals("start")) {
            if (args.length != 2)
            { throw new UsageException(); }
        } else if (userAction.equals("stop")) {
            if (args.length != 2)
            { throw new UsageException(); }
        } else if (userAction.equals("describe")) {
            if (args.length < 3)
            { throw new UsageException(); }
        } else if (userAction.equals("summary")) {
            if (args.length > 2 && args.length < 4)
            { throw new UsageException(); }
        } else if (userAction.equals("size")) {
            if (args.length != 3)
            { throw new UsageException(); }
            if ((!isValidSize(args[2])))
            { throw new UsageException(); }
        } else if (userAction.equals("rename")) {
            if (args.length != 3)
            { throw new UsageException(); }
        } else if (userAction.equals("delete")) {
            if (args.length != 2)
            { throw new UsageException(); }
        } else {
            throw new UsageException();
        }
    }

    public static boolean isValidSize(String size) {
        return (size.equals("S") || size.equals("M") ||
                size.equals("L") || size.equals("XL"));
    }
}

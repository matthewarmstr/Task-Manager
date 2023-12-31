package matthewarmstr.taskmanager;

public class UsageException extends Exception {
    public UsageException() {
        System.out.println("Usage:");
        System.out.println("\tjava TM.java start <task name>");
        System.out.println("\tjava TM.java stop <task name>");
        System.out.println("\tjava TM.java describe <task name> " + "<description> [{S|M|L|XL}]");
        System.out.println("\tjava TM.java summary " + "[<task name> | {S|M|L|XL}]");
        System.out.println("\tjava TM.java summary");
        System.out.println("\tjava TM.java size <task name> {S|M|L|XL}");
        System.out.println("\tjava TM.java rename <old task name> " + "<new task name>");
        System.out.println("\tjava TM.java delete <task name> ");
    }
}

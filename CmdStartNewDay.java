public class CmdStartNewDay extends RecordedCommand {

    private String d1, d2;

    public void execute(String[] cmdParts) {

        SystemDate sd = SystemDate.getInstance();
        d1 = sd.toString();
        d2 = cmdParts[1];
        sd.set(d2);
        addUndoCommand(this);
        clearRedoList();
        System.out.println("Done.");

    }

    @Override
    public void undoMe() {
        SystemDate sd = SystemDate.getInstance();
        sd.set(d1);
        addRedoCommand(this);
    }

    @Override
    public void redoMe() {
        SystemDate sd = SystemDate.getInstance();
        sd.set(d2);
        addUndoCommand(this);
        ;
    }
}
package com.solver.base;

public class Move {

    // TODO: Remove 'String move'
    private String move;

    private byte topColorSize;
    private Color topColor;
    private byte fromFlaskIndex;
    private byte toFlaskIndex;

    private static final String SHORT_MOVE_FORMAT = "%d@%d->%d";
    private static final String VERBOSE_MOVE_FORMAT = "%s from %d to %d\n";

    public Move(String move) {
        this.move = move;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public String getShortVersion(){
        return String.format(SHORT_MOVE_FORMAT, topColorSize, fromFlaskIndex, toFlaskIndex);
    }

    public String getVerboseVersion(){
        return String.format(VERBOSE_MOVE_FORMAT, topColor, fromFlaskIndex, toFlaskIndex);
    }

    public Move getUndoMove(){
        // TODO: Implement.
        return new Move(move);
    }

    @Override
    public String toString() {
        return "Move{" +
                "move='" + move + '\'' +
                '}';
    }
}
package solver.base;

import java.util.Objects;

public class Move {

    private final int topColorSize;
    private final Color topColor;
    private final int fromFlaskIndex;
    private final int toFlaskIndex;

    private static final String SHORT_MOVE_FORMAT = "%d@%d->%d";
    private static final String VERBOSE_MOVE_FORMAT = "%s from %d to %d";

    public Move(int topColorSize, Color topColor, int fromFlaskIndex, int toFlaskIndex) {
        this.topColorSize = topColorSize;
        this.topColor = topColor;
        this.fromFlaskIndex = fromFlaskIndex;
        this.toFlaskIndex = toFlaskIndex;
    }

    public int getTopColorSize() {
        return topColorSize;
    }

    public Color getTopColor() {
        return topColor;
    }

    public int getFromFlaskIndex() {
        return fromFlaskIndex;
    }

    public int getToFlaskIndex() {
        return toFlaskIndex;
    }

    public String getShortVersion(){
        return String.format(SHORT_MOVE_FORMAT, topColorSize, fromFlaskIndex, toFlaskIndex);
    }

    public String getVerboseVersion(){
        return String.format(VERBOSE_MOVE_FORMAT, topColor, fromFlaskIndex, toFlaskIndex);
    }

    public Move getUndoMove(){
        return new Move(topColorSize, topColor, toFlaskIndex, fromFlaskIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return topColorSize == move.topColorSize &&
                fromFlaskIndex == move.fromFlaskIndex &&
                toFlaskIndex == move.toFlaskIndex &&
                topColor == move.topColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(topColorSize, topColor, fromFlaskIndex, toFlaskIndex);
    }

    @Override
    public String toString() {
        return getShortVersion();
    }
}

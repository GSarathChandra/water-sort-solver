package solver.base;

import org.hamcrest.Matchers;

import java.util.*;


public class FlaskGameState{

    public static final int MAX_FLASK_SIZE = 4;

    private static final String ARROW = "->";
    private static final String AT = "@";

    private static final String MOVE_FORMAT = "%d@%d->%d";

    private static final String SOLUTION_STEP_FORMAT = "%2d. %s from %d to %d\n";

    //TODO: Change to  private + constructor & setter.
    //TODO: Implement Builder.
    public List<Stack<Color>> flasks;

    //TODO: Change to List of Moves.
    //TODO: Better named as stateHistory?
    public LinkedList<String> movesHistory;

    private boolean isDebugMode(){
        return false;
    }

    // public for Testing
    public boolean removeReverseMoves(){
        return true;
    } // Yet to be verified.

    public boolean isSolved(){
        for(Stack<Color> flask : flasks) {
            // Consider only non-empty flasks!
            if (!flask.isEmpty() && getTopColorSize(flask) != MAX_FLASK_SIZE) {
                return false;
            }
        }
        return true;
    }

    public List<String> getNextMoves(){
        List<String> moves = new ArrayList<>();

        for(int i = 0; i < flasks.size(); i++){
            for(int j = 0; j < flasks.size(); j++){
                if (i == j) continue;

                Stack<Color> from = flasks.get(i);
                Stack<Color> to = flasks.get(j);

                if (from.isEmpty()) continue;

                int fromTopColorSize = getTopColorSize(from);

                // Scenario: moving to non-empty flask
                if (!to.isEmpty()) {
                    if (from.peek().equals(to.peek()) && fromTopColorSize + to.size() <= MAX_FLASK_SIZE) {
                        String nextMove = String.format(MOVE_FORMAT, fromTopColorSize, i, j);

                        if (removeReverseMoves()) {
                            int toTopColorSize = getTopColorSize(to);
                            String reverseMove = String.format(MOVE_FORMAT, toTopColorSize, j, i);
                            if (!moves.contains(reverseMove)) {
                                moves.add(nextMove);
                            } else if (isDebugMode()) {
                                System.out.println("Reverse move already added for : " + nextMove + " for " + this.getState());
                            }
                        } else {
                            moves.add(nextMove);
                        }
                    }
                }
                // Scenario: moving to empty flask
                else {
                    // When moving to empty, we're allowing only size < MAX to avoid redundant moves
                    // - i.e., moving solved flasks into empty flasks or flask with single color into empty flask
                    if (fromTopColorSize != from.size() && fromTopColorSize < MAX_FLASK_SIZE) {

                        // Check if there exists another non-empty flask which would solve the color.
                        boolean foundLastStepElseWhere = false;
                        for (int k = 0; k < flasks.size(); k++) {
                            if (k == i || k == j) continue;
                            Stack<Color> kth = flasks.get(k);
                            if (kth.isEmpty()) continue;

                            int kthTopColorSize = getTopColorSize(kth);
                            if (from.peek().equals(kth.peek())
                                    && kthTopColorSize == kth.size()
                                    && kthTopColorSize + fromTopColorSize == MAX_FLASK_SIZE) {
                                foundLastStepElseWhere = true;
                                if (isDebugMode()) {
                                    String elsewhereMove = String.format(MOVE_FORMAT, fromTopColorSize, i, k);
                                    System.out.println("Found last step elsewhere: " + elsewhereMove + " for " + this.getState());
                                }
                                break;
                            }
                        }

                        if (!foundLastStepElseWhere) {
                            String nextMove = String.format(MOVE_FORMAT, fromTopColorSize, i, j);
                            if (isDebugMode()) System.out.println("Case: move to empty: " + nextMove);
                            moves.add(nextMove);
                        }
                    }
                }
            }
        }
        return moves;
    }

    private void updateFlasksForMove(String move){
        // Extract top color size.
        int moveColorSize = Integer.parseInt(move.split(AT)[0]);

        String[] indices = move.split(AT)[1].split(ARROW);
        int fromFlaskIndex = Integer.parseInt(indices[0]);
        int toFlaskIndex = Integer.parseInt(indices[1]);

        boolean isMoveValid = true;

        if(isDebugMode()){
            isMoveValid = !this.flasks.get(fromFlaskIndex).isEmpty()
                    && this.flasks.get(toFlaskIndex).size() + moveColorSize <= MAX_FLASK_SIZE;
            if(!isMoveValid){
                System.out.println("Attempted invalid move :- " + move + "for " + this.getState());
            }
        }

        for(int i = 1; i <= moveColorSize; i++){
            this.flasks.get(toFlaskIndex).push(this.flasks.get(fromFlaskIndex).pop());
        }
    }

    //TODO: Change to receive instance of Move instead of String.
    public void makeMove(String move){
        // TODO: Validate move.

        // TODO: Looks like the splitting of the logic into updateFlasksForMove is breaking!!!
        updateFlasksForMove(move);

        // This step used to add "this.getState()" in an earlier version of the code.
        // However, we realized that adding the entire state is overkill. So, keeping track of only moves.
        // And using the moves to build the solution step by step, while saving space.
        this.movesHistory.add(move);
    }

    public void undoLastMove() {
        if(!this.movesHistory.isEmpty()){
            // Get last move and update history.
            String lastMove = this.movesHistory.removeLast();

            // Extract top color size.
            String lastMoveSize = lastMove.split(AT)[0];

            // Reverse indices.
            String[] lastMoveIndices = lastMove.split(AT)[1].split(ARROW);
            String undoMoveIndices = String.join(ARROW, lastMoveIndices[1], lastMoveIndices[0]);

            // Join step count and reversed indices to get the move to effectively undo the last move.
            String undoMove = String.join(AT, lastMoveSize, undoMoveIndices);

            if(Integer.parseInt(lastMoveSize) == 0){
                System.out.println("Attempted invalid move :- " + undoMove + "for lastMove = " + lastMove);
            }
            // Check if undoMove is valid
            updateFlasksForMove(undoMove);
        }
    }

    //TODO: Change to private after testing + add proper JUnits?
    public int getTopColorSize(Stack<Color> flask){
        int topColorSize = 0;
        int topColorIndex = flask.size() - 1;

        // Handle empty flask case - to avoid EmptyStackException.
        if(flask.isEmpty()){
            return 0;
        }

        Color topColor = flask.peek();
        Color nextColor;

        // TODO: Loop can be refactored to be 'cleaner'.
        while(topColorIndex >= 0){
            nextColor = flask.elementAt(topColorIndex--);

            if(nextColor != topColor){
                break;
            } else {
                topColorSize++;
            }
        }

        if(isDebugMode() && topColorSize == 0) System.out.println("Returning zero size for non-empty :- " + flask);
        return topColorSize;
    }

    //TODO: Modernize this using streams, etc. to parallelize and improve performance.
    public FlaskGameState createCopy(){
        // Need tp deep clone manually.
        FlaskGameState copy = new FlaskGameState();

        copy.flasks = new ArrayList<>(this.flasks.size());
        for(Stack<Color> flask : this.flasks){
            Stack<Color> copyFlask = new Stack<>();
            copyFlask.addAll(flask);
            copy.flasks.add(copyFlask);
        }

        copy.movesHistory = new LinkedList<>();
        copy.movesHistory.addAll(this.movesHistory);

        return copy;
    }

    public void printStateHistory(){
        System.out.println("-----");
        for(String move : movesHistory) {
            System.out.print(move + " ");
        }
        System.out.println("\n-----");
    }

    public void printShortSolution(){
        System.out.println(this.movesHistory.toString());
    }

    /* Prints the solution in the format of "Step x. COLOR from _from_flask_index_ to _to_flask_index_ */
    public void printVerboseSolution(){
        System.out.println("-----");

        // Print initState.
        System.out.print(this.getState());

        for (int solutionStepCount = 0; solutionStepCount < this.movesHistory.size(); solutionStepCount++) {
            String move = movesHistory.get(solutionStepCount);

            // Extract values from move.
            String[] indices = move.split(AT)[1].split(ARROW);
            int fromFlaskIndex = Integer.parseInt(indices[0]);
            int toFlaskIndex = Integer.parseInt(indices[1]);
            Color colorToMove = this.flasks.get(fromFlaskIndex).peek();
            System.out.printf(SOLUTION_STEP_FORMAT, solutionStepCount+1, colorToMove, fromFlaskIndex, toFlaskIndex);

            updateFlasksForMove(move);

            if(isDebugMode()) System.out.print(" => " + getState());
        }
        System.out.println("-----");
    }

    public String getState(){
        StringBuilder sb = new StringBuilder();
        sb.append("|| ");
        for (Stack<Color> flask: flasks){
            sb.append("[");
            for (Color color : flask){
                sb.append(" ").append(color).append(",");
            }
            if(!flask.isEmpty())
                sb.deleteCharAt(sb.length()-1);
            sb.append(" ] || ");
        }
        sb.append("\n");

        return sb.toString();
    }

    public static FlaskGameState buildState(List<List<Color>> flasks){
        Map<Color, Integer> colorCounts = new HashMap<>();

        FlaskGameState state = new FlaskGameState();
        state.flasks = new ArrayList<>();

        for(List<Color> flask : flasks){
            if(flask.size() > MAX_FLASK_SIZE)
                throw new IllegalArgumentException("Too many colors in a flask!");

            Stack<Color> stack = new Stack<>();
            for(Color color : flask){
                stack.push(color);
                colorCounts.put(color, colorCounts.getOrDefault(color, 0) + 1);
            }
            state.flasks.add(stack);
        }

        for(Integer colorCount : colorCounts.values()){
            if(colorCount != MAX_FLASK_SIZE){
                System.out.print(colorCounts);
                // throw new IllegalArgumentException("Not enough colors across all flask!");
            }
        }

        state.movesHistory = new LinkedList<>();

        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlaskGameState that = (FlaskGameState) o;

        return Matchers.containsInAnyOrder(flasks.toArray()).matches(that.flasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flasks);
    }
}

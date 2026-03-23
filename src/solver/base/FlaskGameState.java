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

    private boolean debugMode = false;
    private boolean redundantMovesRemoved = true;
    private boolean reverseMovesRemoved = true;

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void setRedundantMovesRemoved(boolean redundantMovesRemoved) {
        this.redundantMovesRemoved = redundantMovesRemoved;
    }

    public void setReverseMovesRemoved(boolean reverseMovesRemoved) {
        this.reverseMovesRemoved = reverseMovesRemoved;
    }

    public boolean isDebugMode(){
        return debugMode;
    }

    // public for Testing
    public boolean removeRedundantMovesToEmpty(){
        return redundantMovesRemoved;
    } // Verified to be working.

    // public for Testing
    public boolean removeReverseMoves(){
        return reverseMovesRemoved;
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
            for(int j = i+1; j < flasks.size(); j++){
                Stack<Color> first = flasks.get(i);
                Stack<Color> second = flasks.get(j);

                int firstTopColorSize = getTopColorSize(first);
                int secondTopColorSize = getTopColorSize(second);

                // Check "non-empty first -> non-empty second" scenario.
                if(!first.isEmpty() && !second.isEmpty() && first.peek().equals(second.peek())
                        // Move should be considered only if the top color can be moved entirely.
                        && firstTopColorSize + second.size() <= MAX_FLASK_SIZE){
                    String nextMove = String.format(MOVE_FORMAT, firstTopColorSize, i, j);
                    if(isDebugMode()) System.out.println("Case 1: " + nextMove);
                    moves.add(nextMove);
                }

                // Check "non-empty second -> non-empty first" scenario.
                if(!first.isEmpty() && !second.isEmpty() && second.peek().equals(first.peek())
                        // Move should be considered only if the top color can be moved entirely.
                        && secondTopColorSize + first.size() <= MAX_FLASK_SIZE){
                    String nextMove = String.format(MOVE_FORMAT, secondTopColorSize, j, i);
                    if(isDebugMode()) System.out.println("Case 2: " + nextMove);

                    String reverseMove = String.format(MOVE_FORMAT, firstTopColorSize, i, j);
                    if(removeReverseMoves()){
                        if(!moves.contains(reverseMove)){
                            moves.add(nextMove);
                        } else {
                            if (isDebugMode()) System.out.println("Reverse move already added for : " + nextMove + " for " + this.getState());
                        }
                    } else {
                        moves.add(nextMove);
                    }
                }

                // When moving to empty, we're allowing only size < MAX to avoid redundant moves
                // - i.e., moving solved flasks into empty flasks or flask with single color into empty flask
                if(removeRedundantMovesToEmpty()){
                    // Check "non-empty first -> empty second" scenario.
                    if(!first.isEmpty() && second.isEmpty()
                            // Move shouldn't be considered if top color is the only color in the flask.
                            && firstTopColorSize != first.size()
                            // Move should be considered only if the top color can be moved entirely.
                            && firstTopColorSize < MAX_FLASK_SIZE){

                        // Check if there exists another non-empty flask which would solve the color.
                        boolean foundLastStepElseWhere = false;
                        for(int k = i; k < flasks.size(); k++){
                            Stack<Color> kth = flasks.get(k);
                            int kthFlaskTopColorSize = getTopColorSize(kth);
                            if(k != i && k != j && !kth.isEmpty() // avoid moving to empty + protection against EmptyStackException
                                    && first.peek().equals(kth.peek()) // colors match
                                    && kthFlaskTopColorSize == kth.size() // topColor in kth flask is the only color in it.
                                    && kthFlaskTopColorSize + firstTopColorSize == MAX_FLASK_SIZE // kth flask and second flask form the whole
                            ){
                                foundLastStepElseWhere = true;
                                String elsewhereMove = String.format(MOVE_FORMAT, firstTopColorSize, i, k);
                                if(isDebugMode()) System.out.println("Found last step elsewhere: " + elsewhereMove + " for " + this.getState());
                            }
                        }
                        if(!foundLastStepElseWhere){
                            String nextMove = String.format(MOVE_FORMAT, firstTopColorSize, i, j);
                            if (isDebugMode()) System.out.println("Case 4: " + nextMove);
                            moves.add(nextMove);
                        }
                    }

                    // Check "non-empty second -> empty first" scenario.
                    if(first.isEmpty() && !second.isEmpty()
                            // Move shouldn't be considered if top color is the only color in the flask.
                            && secondTopColorSize != second.size()
                            // Move should be considered only if the top color can be moved entirely.
                            && secondTopColorSize < MAX_FLASK_SIZE){
                        // Check if there exists another flask which would solve the color.
                        boolean foundLastStepElseWhere = false;
                        for(int k = i; k < flasks.size(); k++){
                            Stack<Color> kth = flasks.get(k);
                            int kthFlaskTopColorSize = getTopColorSize(kth);
                            if(k != i && k != j && !kth.isEmpty()  // avoid moving to empty + protection against EmptyStackException
                                    && second.peek().equals(kth.peek())    // colors match
                                    && kthFlaskTopColorSize == kth.size()  // topColor in kth flask is the only color in it.
                                    && kthFlaskTopColorSize + secondTopColorSize == MAX_FLASK_SIZE // kth flask and second flask form the whole
                            ){
                                foundLastStepElseWhere = true;
                                String elsewhereMove = String.format(MOVE_FORMAT, secondTopColorSize, j, k);
                                if(isDebugMode()) System.out.println("Found last step elsewhere: " + elsewhereMove + " for " + this.getState());
                            }
                        }
                        if(!foundLastStepElseWhere){
                            String nextMove = String.format(MOVE_FORMAT, secondTopColorSize, j, i);
                            if (isDebugMode()) System.out.println("Case 4: " + nextMove);
                            moves.add(nextMove);
                        }
                    }
                } else {
                    // Retaining else block for backup until confident about removeRedundantMovesToEmpty.
                    // Check "non-empty first -> empty second" scenario.
                    if(!first.isEmpty() && second.isEmpty()
                            // Move shouldn't be considered if top color is the only color in the flask.
                            && firstTopColorSize != first.size()
                            // Move should be considered only if the top color can be moved entirely.
                            && firstTopColorSize < MAX_FLASK_SIZE) {
                        String nextMove = String.format(MOVE_FORMAT, firstTopColorSize, i, j);
                        if(isDebugMode()) System.out.println("Case 3: " + nextMove);
                        moves.add(nextMove);
                    }

                    // Check "non-empty second -> empty first" scenario.
                    if(first.isEmpty() && !second.isEmpty()
                            // Move shouldn't be considered if top color is the only color in the flask.
                            && secondTopColorSize != second.size()
                            // Move should be considered only if the top color can be moved entirely.
                            && secondTopColorSize < MAX_FLASK_SIZE){
                        String nextMove = String.format(MOVE_FORMAT, secondTopColorSize, j, i);
                        if (isDebugMode()) System.out.println("Case 4: " + nextMove);
                        moves.add(nextMove);
                    }
                }
            }
        }

        // Check for scenarios where the color is one step away from being solved.
        // In such cases, avoid moving to empty flask
        // and prefer moving the color to flask with lower index - i.e, solved flasks towards the starting.
        // i.e., when firstTopColorSize == 0 || secondTopColorSize == 0
        // is exactly 4;

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

    public boolean isValidMove(String move){
        // Extract flask indices.
        String[] flaskIndices = move.split(AT)[1].split(ARROW);

        int fromIndex = Integer.parseInt(flaskIndices[0]);
        int toIndex = Integer.parseInt(flaskIndices[1]);

        Stack<Color> from = flasks.get(fromIndex);
        Stack<Color> to = flasks.get(toIndex);

        // Extract top color size of from flask.
        int fromFlaskTopColorSize = Integer.parseInt(move.split(AT)[0]);

        // Check "non-empty -> non-empty" scenario.
        // Move should be considered only if the top color can be moved entirely.
        boolean validForNonEmpty = !from.isEmpty() && !to.isEmpty() && from.peek().equals(to.peek())
                && fromFlaskTopColorSize + to.size() <= MAX_FLASK_SIZE;

        // Check "non-empty -> empty" scenario.
        // When moving to empty, we need to check for following conditions to avoid redundant moves:
        //  i. top color size < MAX
        // ii. flask has more than 1 color
        boolean validForEmpty = !from.isEmpty() && to.isEmpty()
                //&& fromFlaskTopColorSize < MAX_FLASK_SIZE
                && fromFlaskTopColorSize != from.size();

        return validForNonEmpty || validForEmpty;
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

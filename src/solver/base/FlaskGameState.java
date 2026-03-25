package solver.base;

import org.hamcrest.Matchers;

import java.util.*;


public class FlaskGameState{

    public static final int MAX_FLASK_SIZE = 4;

    //TODO: Change to  private + constructor & setter.
    //TODO: Implement Builder.
    public List<Stack<Color>> flasks;

    public LinkedList<Move> movesHistory;

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

    public List<Move> getNextMoves(){
        List<Move> moves = new ArrayList<>();

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
                    Move nextMove = new Move(firstTopColorSize, first.peek(), i, j);
                    if(isDebugMode()) System.out.println("Case 1: " + nextMove);
                    moves.add(nextMove);
                }

                // Check "non-empty second -> non-empty first" scenario.
                if(!first.isEmpty() && !second.isEmpty() && second.peek().equals(first.peek())
                        // Move should be considered only if the top color can be moved entirely.
                        && secondTopColorSize + first.size() <= MAX_FLASK_SIZE){
                    Move nextMove = new Move(secondTopColorSize, second.peek(), j, i);
                    if(isDebugMode()) System.out.println("Case 2: " + nextMove);

                    Move reverseMove = new Move(firstTopColorSize, first.peek(), i, j);
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
                                Move elsewhereMove = new Move(firstTopColorSize, first.peek(), i, k);
                                if(isDebugMode()) System.out.println("Found last step elsewhere: " + elsewhereMove + " for " + this.getState());
                            }
                        }
                        if(!foundLastStepElseWhere){
                            Move nextMove = new Move(firstTopColorSize, first.peek(), i, j);
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
                                Move elsewhereMove = new Move(secondTopColorSize, second.peek(), j, k);
                                if(isDebugMode()) System.out.println("Found last step elsewhere: " + elsewhereMove + " for " + this.getState());
                            }
                        }
                        if(!foundLastStepElseWhere){
                            Move nextMove = new Move(secondTopColorSize, second.peek(), j, i);
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
                        Move nextMove = new Move(firstTopColorSize, first.peek(), i, j);
                        if(isDebugMode()) System.out.println("Case 3: " + nextMove);
                        moves.add(nextMove);
                    }

                    // Check "non-empty second -> empty first" scenario.
                    if(first.isEmpty() && !second.isEmpty()
                            // Move shouldn't be considered if top color is the only color in the flask.
                            && secondTopColorSize != second.size()
                            // Move should be considered only if the top color can be moved entirely.
                            && secondTopColorSize < MAX_FLASK_SIZE){
                        Move nextMove = new Move(secondTopColorSize, second.peek(), j, i);
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

    private void updateFlasksForMove(Move move){
        int moveColorSize = move.getTopColorSize();
        int fromFlaskIndex = move.getFromFlaskIndex();
        int toFlaskIndex = move.getToFlaskIndex();

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

    public void makeMove(Move move){
        updateFlasksForMove(move);
        this.movesHistory.add(move);
    }

    public void undoLastMove() {
        if(!this.movesHistory.isEmpty()){
            Move lastMove = this.movesHistory.removeLast();
            updateFlasksForMove(lastMove.getUndoMove());
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
        for(Move move : movesHistory) {
            System.out.print(move.getShortVersion() + " ");
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
            Move move = movesHistory.get(solutionStepCount);

            System.out.printf("%2d. %s\n", solutionStepCount+1, move.getVerboseVersion());

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

    public boolean isValidMove(Move move){
        int fromIndex = move.getFromFlaskIndex();
        int toIndex = move.getToFlaskIndex();
        int moveColorSize = move.getTopColorSize();
        Color moveColor = move.getTopColor();

        Stack<Color> from = flasks.get(fromIndex);
        Stack<Color> to = flasks.get(toIndex);

        if (from.isEmpty()) return false;
        if (!from.peek().equals(moveColor)) return false;
        if (getTopColorSize(from) < moveColorSize) return false;

        // Check "non-empty -> non-empty" scenario.
        if (!to.isEmpty()) {
            return to.peek().equals(moveColor) && moveColorSize + to.size() <= MAX_FLASK_SIZE;
        }

        // Check "non-empty -> empty" scenario.
        return moveColorSize != from.size();
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

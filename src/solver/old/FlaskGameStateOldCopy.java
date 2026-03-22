package solver.old;

import solver.base.Color;
import solver.base.FlaskGameState;

import java.util.*;


public class FlaskGameStateOldCopy {
    public static final int MAX_FLASK_SIZE = 4;

    //TODO: Change to  private + constructor & setter.
    //TODO: Implement Builder.
    public List<Stack<Color>> flasks;

    public ArrayList<String> movesHistory;

    public boolean isExhaustiveSearchMode(){
        return true;
    }

    private boolean isDebugMode(){
        return false;
    }

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

                // Check "non-empty first -> non-empty second" scenario.
                if(!first.isEmpty() && !second.isEmpty() && first.peek().equals(second.peek())
                        // Move should be considered only if the top color can be moved entirely.
                        && getTopColorSize(first) + second.size() <= MAX_FLASK_SIZE){
                    if(isDebugMode()) System.out.println("Case 1: "+i + "->" + j);
                    moves.add(i + "->" + j);
                }

                // Check "non-empty second -> non-empty first" scenario.
                if(!first.isEmpty() && !second.isEmpty() && second.peek().equals(first.peek())
                        // Move should be considered only if the top color can be moved entirely.
                        && getTopColorSize(second) + first.size() <= MAX_FLASK_SIZE){
                    if(isDebugMode()) System.out.println("Case 2: "+j + "->" + i);
                    moves.add(j + "->" + i);
                }

                // Check "non-empty first -> empty second" scenario.
                if(!first.isEmpty() && second.isEmpty()
                        // Move should be considered only if the top color can be moved entirely.
                        && getTopColorSize(first) < MAX_FLASK_SIZE){
                    if(isDebugMode()) System.out.println("Case 3: "+i + "->" + j);
                    moves.add(i + "->" + j);
                }

                // Check "non-empty second -> empty first" scenario.
                if(first.isEmpty() && !second.isEmpty()
                        // Move should be considered only if the top color can be moved entirely.
                        && getTopColorSize(second) < MAX_FLASK_SIZE){
                    if(isDebugMode()) System.out.println("Case 4: "+j + "->" + i);
                    moves.add(j + "->" + i);
                }
            }
        }

        return moves;
    }

    //TODO: Change to receive Move.
    public void makeMove(String move){
        // TODO: Validate move.
        String[] flaskIndices = move.split("->");

        int fromFlaskIndex = Integer.parseInt(flaskIndices[0]);
        int toFlaskIndex = Integer.parseInt(flaskIndices[1]);

        Color colorToPush = null;
        int numberOfSteps = getTopColorSize(this.flasks.get(fromFlaskIndex));

        for(int i = 1; i <= numberOfSteps; i++){
            colorToPush = this.flasks.get(fromFlaskIndex).pop();
            this.flasks.get(toFlaskIndex).push(colorToPush);
        }

        // Add the state of the board after making the move to the history.
        // this.movesHistory.add(this.getState());

        // TODO: Keep track of only moves for now. Expand it later for viewing the solution?
        this.movesHistory.add(this.getState());
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

        return topColorSize;
    }

    //TODO: Modernize this using streams, etc. to parallelize and improve performance.
    public FlaskGameStateOldCopy createCopy(){
        // Need tp deep clone manually.
        FlaskGameStateOldCopy copy = new FlaskGameStateOldCopy();

        copy.flasks = new ArrayList<>(this.flasks.size());
        for(Stack<Color> flask : this.flasks){
            Stack<Color> copyFlask = new Stack<>();
            copyFlask.addAll(flask);
            copy.flasks.add(copyFlask);
        }

        copy.movesHistory = new ArrayList<>(this.movesHistory.size());
        copy.movesHistory.addAll(this.movesHistory);

        return copy;
    }

    public void printStateHistory(){
        System.out.println("-----");
        for(String move : movesHistory) {
            System.out.print(move);
        }
        System.out.println("-----");
    }

    public void buildStateHistory(){
        System.out.println("-----");
        List<String> moves = new ArrayList<>(this.movesHistory);
        System.out.print(moves.get(0));

        for(int j = 1; j < moves.size(); j++) {
            String[] flaskIndices = moves.get(j).split("->");

            int fromFlaskIndex = Integer.parseInt(flaskIndices[0]);
            int toFlaskIndex = Integer.parseInt(flaskIndices[1]);

            Color colorToPush = null;
            int numberOfSteps = getTopColorSize(this.flasks.get(fromFlaskIndex));

            for(int i = 1; i <= numberOfSteps; i++){
                colorToPush = this.flasks.get(fromFlaskIndex).pop();
                this.flasks.get(toFlaskIndex).push(colorToPush);
            }
            System.out.print(getState());
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

    public static FlaskGameStateOldCopy buildState(List<List<Color>> flasks){
        Map<Color, Integer> colorCounts = new HashMap<>();

        FlaskGameStateOldCopy state = new FlaskGameStateOldCopy();
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
                throw new IllegalArgumentException("Not enough colors across all flask!");
            }
        }

        state.movesHistory = new ArrayList<>();
        state.movesHistory.add(state.getState());

        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlaskGameState that = (FlaskGameState) o;

        //return Matchers.containsInAnyOrder(flasks.toArray()).matches(that.flasks);
        return flasks.equals(that.flasks); // TODO: This checks for ordering, refactor to consider non-ordering as well.
    }

    @Override
    public int hashCode() {
        return Objects.hash(flasks);
    }
}

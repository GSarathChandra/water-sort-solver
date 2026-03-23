package solver.base;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class FlaskGameSolver {

    private FlaskGameState initState;
    public int solutionCount = 0;

    private final Set<FlaskGameState> visited = new HashSet<>();

    // Solver mode variables.
    private int solutionsToCompute;
    private int solutionsToPrint;
    private boolean printVerboseSolution;

    // Running time tracking variables.
    private ZonedDateTime solveStartInstant = now();
    private ZonedDateTime solveEndInstant = now();

    // Core debug variables.
    private long stateCounter = 0L;
    private long loopCounter = 0L;

    // Logging related variables.
    private ZonedDateTime nextLoggingTime = now();
    private final TemporalAmount loggingFrequency = Duration.ofSeconds(5);
    private final DecimalFormat numberFormatter = new DecimalFormat("#,###");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private void printDebugData(){
        ZonedDateTime now = now();

        if (now.isAfter(nextLoggingTime)){
            String messageFormat = "[DEBUG] Time: %s | Loop Counter = %s | States covered = %s | Solution Count = %s | Solutions to compute = %s";
            String message = String.format(messageFormat,
                    timeFormatter.format(now),
                    numberFormatter.format(loopCounter),
                    numberFormatter.format(stateCounter),
                    numberFormatter.format(solutionCount),
                    numberFormatter.format(solutionsToCompute));

            System.out.println(message);
            nextLoggingTime = now.plus(loggingFrequency);
        }
    }

    public void solve(FlaskGameState state, int solutionsToCompute, int solutionsToPrint, boolean printVerboseSolution){
        // Validate inputs.
        if(solutionsToPrint > solutionsToCompute){
            throw new IllegalArgumentException("Value of solutionsToPrint cannot be greater than solutionsToCompute.");
        }

        this.solutionsToCompute = solutionsToCompute;
        this.solutionsToPrint = solutionsToPrint;
        this.printVerboseSolution = printVerboseSolution;

        solve(state);
    }


    public void solve(FlaskGameState state){
        if(state == null){
            throw new UnsupportedOperationException("Initial state of the game is not specified!");
        }

        this.initState = state.createCopy();
        // Track running time.
        solveStartInstant = now();
        boolean limitReached = backtrackingDfs(state);

        // If solutionsToCompute >  count of all solutions, code reaches here.
        if(limitReached) {
            System.out.println("\nExiting after finding requested number of solutions.");
        } else {
            System.out.println("\nExiting after finding all solutions.");
        }
        solveEndInstant = now();
        printSolutionCountAndRunningTime();
    }

    private void printSolution(FlaskGameState solution){
        if(this.printVerboseSolution){
            System.out.printf("Solution %d:- \n", solutionCount);
            solution.printVerboseSolution();
        } else {
            System.out.printf("# %d. ", solutionCount);
            solution.printShortSolution();
        }
    }

    private void printSolutionCountAndRunningTime(){
        System.out.printf("Total Solutions found = %d\n", solutionCount);
        System.out.println("Total running time (in milliseconds) = " + Duration.between(solveStartInstant, solveEndInstant).toMillis());
    }

    private boolean backtrackingDfs(FlaskGameState state){
        // Update loopCounter for tracking.
        loopCounter++;

        // Mark current state as visited.
        visited.add(state);

        // Check if solved. If yes, increment solutionCount and print output as per flags.
        if(state.isSolved()){
            solutionCount++;

            //TODO: Add support for printing random solution.
            if(solutionCount <= solutionsToPrint){
                FlaskGameState solution = initState.createCopy();
                solution.movesHistory.addAll(state.movesHistory);
                printSolution(solution);
            }

            if(solutionCount >= solutionsToCompute){
                return true;
            }
            return false;
        }

        // Iterate through nextMoves and enqueue the unvisited ones.
        for(String nextMove : state.getNextMoves()){
            state.makeMove(nextMove);
            stateCounter++;
            printDebugData();

            if(!visited.contains(state)){
                if(backtrackingDfs(state)) return true;
            }

            // Undo the last move in order to iterate through the further probable state.
            state.undoLastMove();
        }
        printDebugData();
        return false;
    }

    private ZonedDateTime now(){
        return Calendar.getInstance().toInstant().atZone(ZoneId.systemDefault());
    }
}

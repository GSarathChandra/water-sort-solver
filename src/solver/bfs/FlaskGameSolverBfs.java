package solver.bfs;

import solver.base.FlaskGameState;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.util.*;
public class FlaskGameSolverBfs {

    private final Queue<FlaskGameState> queue = new LinkedList<>();

    public FlaskGameState initState;

    private final Set<FlaskGameState> visited = new HashSet<>();

    public int solutionCount = 0;

    // Debug variables
    private long stateCounter = 1L;

    private long loopCounter = 1L;

    private ZonedDateTime lastLoggedTime = Calendar.getInstance().toInstant().atZone(ZoneId.systemDefault());

    private final TemporalAmount loggingFrequency = Duration.ofSeconds(5);

    private final DecimalFormat numberFormatter = new DecimalFormat("#,###");

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private void printDebugData(){
        ZonedDateTime now = Calendar.getInstance().toInstant().atZone(ZoneId.systemDefault());

        if (now.isAfter(lastLoggedTime.plus(loggingFrequency))){
            String messageFormat = "[DEBUG] Time: %s | Loop Counter = %s | Queue size = %s | States covered = %s | Solution count = %s";
            String message = String.format(messageFormat,
                    timeFormatter.format(now),
                    numberFormatter.format(loopCounter),
                    numberFormatter.format(queue.size()),
                    numberFormatter.format(stateCounter),
                    numberFormatter.format(solutionCount)
            );

            System.out.println(message);
            lastLoggedTime = now;
        }
    }

    public void solve(){
        if(initState == null){
            throw new UnsupportedOperationException("Initial state of the game is not specified!");
        }

        queue.add(initState);

        while(!queue.isEmpty()){
            FlaskGameState state = queue.remove();
            loopCounter++;

            // Check if solved.
            if(state.isSolved()){
                solutionCount++;
                System.out.printf("Solution %d:- \n", solutionCount);
                state.printStateHistory();

                /*if(!state.isExhaustiveSearchMode()){
                    System.out.println("\nExhaustive Search Mode is off! Exiting after finding first solution.");
                    break;
                }*/
            }

            // Iterate through nextMoves and enqueue the unvisited ones.
            for(String nextMove : state.getNextMoves() ){
                FlaskGameState nextState = state.createCopy();
                nextState.makeMove(nextMove);
                stateCounter++;

                if(!visited.contains(nextState)) {
                    queue.add(nextState);
                    visited.add(nextState);
                    nextState = null; // Early release?

                    printDebugData();
                }
                printDebugData();
            }
            printDebugData();
        }
    }
}
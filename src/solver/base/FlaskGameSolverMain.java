package solver.base;

import java.util.List;

import static solver.base.Color.*;
import static solver.base.FlaskGameState.buildState;

public class FlaskGameSolverMain {

    public static void main(String[] args){
        FlaskGameSolver dfsSolver = new FlaskGameSolver();

        FlaskGameState board;
        if (args.length > 0 && args[0].equals("test")) {
            board = getBoard1();
        } else if (args.length > 0 && args[0].equals("full")) {
            board = getLevel239();
        } else {
            board = getBoard1();
        }

        dfsSolver.solve(board,  Integer.MAX_VALUE -10, 100, false);
    }

    /*
     * Run time tracking history:
     *
     * 22-May-2024
     * (without 'nextLoggingTime' optimization)
     * 1 million solutions in 184 seconds ~ 3 minutes
     * 5 million (5000000) solutions in 994.4 secs  ~ 16.5 minutes
     *
     * (with 'nextLoggingTime' optimization)
     * 5 million solutions in 994.7 secs  ~ 16.5 minutes
     *
     * 7.2 million - ALL solutions for 233 ~ 31 minutes.
     *
     * 26-May-2024
     * (with isTrimRedundantMovesMode)
     * 3.65 million - ALL solutions for level 239 ~ 10.8 minutes
     *
     * (without isTrimRedundantMovesMode)
     * 5.70 million - ALL solutions for level 239 ~ 17.8 minutes
     * */

    // Each flask is represented as a List of colors - entered in "bottom to top of the flask" order.
    // i.e., the right most value in a list in considered as the top of the flask.
    private static FlaskGameState getBoard1(){
        return buildState(
                List.of(
                        List.of(PINK, PINK, RED, RED),
                        List.of(RED, RED, PINK, PINK),
                        List.of()
                ));
    }
    private static FlaskGameState getBoard2(){
        return buildState(
                List.of(
                        List.of(PARROT_GREEN, PARROT_GREEN, PARROT_GREEN),
                        List.of(RED, RED, RED),
                        List.of(BROWN, BROWN, BROWN),
                        List.of(LIGHT_GREEN, LIGHT_GREEN, LIGHT_GREEN),
                        List.of(PARROT_GREEN),
                        List.of(VIOLET, LIGHT_GREEN, RED, BROWN),
                        List.of(VIOLET, VIOLET, VIOLET)
                ));
    }

    private static FlaskGameState getBoard175(){
        return buildState(
                List.of(
                        List.of(ORANGE, PARROT_GREEN, RED, VIOLET),
                        List.of(YELLOW, BROWN, PINK, PINK),
                        List.of(ORANGE, GRAY, LIGHT_BLUE, DARK_BLUE),
                        List.of(LIGHT_GREEN, DARK_BLUE, YELLOW, ORANGE),
                        List.of(PINK, DARK_BLUE, DARK_BLUE, PARROT_GREEN),
                        List.of(BROWN, GRAY, VIOLET, LIGHT_BLUE),
                        List.of(DARK_GREEN, PINK, RED, BROWN),
                        List.of(ORANGE, GRAY, DARK_GREEN, LIGHT_GREEN),
                        List.of(RED, PARROT_GREEN, DARK_GREEN, LIGHT_BLUE),
                        List.of(LIGHT_BLUE, LIGHT_GREEN, YELLOW, RED),
                        List.of(PARROT_GREEN, GRAY, BROWN, YELLOW),
                        List.of(VIOLET, DARK_GREEN, LIGHT_GREEN, VIOLET),
                        List.of(),
                        List.of()
                ));
    }

    private static FlaskGameState getBoard181(){
        return buildState(
                List.of(
                        List.of(GRAY, PARROT_GREEN, DARK_GREEN, ORANGE),
                        List.of(LIGHT_GREEN, PINK, LIGHT_BLUE, ORANGE),
                        List.of(ORANGE, DARK_GREEN, LIGHT_GREEN, BROWN),
                        List.of(BROWN, DARK_BLUE, GRAY, YELLOW),
                        List.of(DARK_BLUE, YELLOW, RED, PARROT_GREEN),
                        List.of(RED, DARK_BLUE, PINK, DARK_GREEN),
                        List.of(DARK_GREEN, VIOLET, RED, LIGHT_BLUE),
                        List.of(LIGHT_GREEN, LIGHT_GREEN, VIOLET, ORANGE),
                        List.of(PINK, DARK_BLUE, PARROT_GREEN, PINK),
                        List.of(RED, GRAY, PARROT_GREEN, YELLOW),
                        List.of(LIGHT_BLUE, BROWN, LIGHT_BLUE, VIOLET),
                        List.of(BROWN, VIOLET, GRAY, YELLOW),
                        List.of(),
                        List.of()
                ));
    }

    private static FlaskGameState getLevel183(){
        return buildState(
                List.of(
                        List.of(ORANGE, ORANGE, VIOLET, VIOLET),
                        List.of(ORANGE, GRAY, DARK_GREEN, YELLOW),
                        List.of(BROWN, DARK_BLUE, PARROT_GREEN, VIOLET),
                        List.of(DARK_BLUE, RED, ORANGE, BROWN),
                        List.of(DARK_GREEN, LIGHT_BLUE, DARK_BLUE, LIGHT_GREEN),
                        List.of(PARROT_GREEN, PARROT_GREEN, RED, LIGHT_BLUE),
                        List.of(GRAY, PINK, BROWN, YELLOW),
                        List.of(RED, GRAY, DARK_GREEN, PARROT_GREEN),
                        List.of(DARK_GREEN, DARK_BLUE, PINK, LIGHT_BLUE),
                        List.of(LIGHT_GREEN, PINK, PINK, LIGHT_BLUE),
                        List.of(LIGHT_GREEN, GRAY, YELLOW, VIOLET),
                        List.of(BROWN, RED, YELLOW, LIGHT_GREEN),
                        List.of(),
                        List.of()
                ));
    }

    private static FlaskGameState getLevel211(){
        return buildState(
                List.of(
                        List.of(ORANGE, GRAY, DARK_BLUE, VIOLET),
                        List.of(LIGHT_BLUE, BROWN, RED, LIGHT_BLUE),
                        List.of(YELLOW, DARK_GREEN, PINK, ORANGE),
                        List.of(LIGHT_GREEN, VIOLET, BROWN, BROWN),
                        List.of(RED, DARK_GREEN, PARROT_GREEN, LIGHT_BLUE),
                        List.of(VIOLET, PINK, VIOLET, LIGHT_GREEN),
                        List.of(YELLOW, RED, PINK, GRAY),

                        List.of(DARK_BLUE, DARK_BLUE, RED, LIGHT_GREEN),
                        List.of(PARROT_GREEN, YELLOW, PARROT_GREEN, DARK_BLUE),
                        List.of(BROWN, GRAY, DARK_GREEN, PINK),
                        List.of(ORANGE, ORANGE, DARK_GREEN, PARROT_GREEN),
                        List.of(LIGHT_BLUE, YELLOW, GRAY, LIGHT_GREEN),

                        List.of(),
                        List.of()
                ));
    }

    private static FlaskGameState getLevel233(){
        return buildState(
                List.of(
                        List.of(YELLOW, YELLOW, DARK_BLUE, BROWN),
                        List.of(VIOLET, ORANGE, PINK, RED),
                        List.of(GRAY, BROWN, ORANGE, DARK_BLUE),
                        List.of(LIGHT_GREEN, LIGHT_GREEN, BROWN, LIGHT_BLUE),
                        List.of(VIOLET, LIGHT_BLUE, PINK, GRAY),
                        List.of(RED, LIGHT_GREEN, BROWN, RED),
                        List.of(LIGHT_GREEN, LIGHT_BLUE, DARK_GREEN, ORANGE),

                        List.of(PARROT_GREEN, GRAY, VIOLET, DARK_GREEN),
                        List.of(DARK_BLUE, DARK_BLUE, PINK, VIOLET),
                        List.of(GRAY, PARROT_GREEN, RED, LIGHT_BLUE), //*
                        List.of(YELLOW, PINK, PARROT_GREEN, PARROT_GREEN),
                        List.of(YELLOW, DARK_GREEN, DARK_GREEN, ORANGE),

                        List.of(),
                        List.of()
                ));
    }

    private static FlaskGameState getLevel239(){
        return buildState(
                List.of(
                        List.of(BROWN, LIGHT_BLUE, PARROT_GREEN, RED),
                        List.of(RED, PARROT_GREEN, DARK_BLUE, DARK_GREEN),
                        List.of(BROWN, GRAY, PARROT_GREEN, PINK),
                        List.of(YELLOW, LIGHT_GREEN, DARK_GREEN, BROWN),
                        List.of(GRAY, PINK, DARK_GREEN, ORANGE),
                        List.of(ORANGE, VIOLET, GRAY, VIOLET),
                        List.of(PINK, DARK_BLUE, YELLOW, DARK_BLUE),
                        List.of(DARK_GREEN, LIGHT_GREEN, YELLOW, PINK),

                        List.of(RED, BROWN, LIGHT_GREEN, LIGHT_BLUE),
                        List.of(YELLOW, ORANGE, GRAY, RED),
                        List.of(VIOLET, LIGHT_BLUE, LIGHT_BLUE, PARROT_GREEN),
                        List.of(ORANGE, VIOLET, DARK_BLUE, LIGHT_GREEN),

                        List.of(),
                        List.of()
                ));
    }
}

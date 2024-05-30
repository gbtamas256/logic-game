package tiltingpuzzle.solver;

import puzzle.solver.BreadthFirstSearch;
import tiltingpuzzle.model.Direction;
import tiltingpuzzle.model.PuzzleState;

public class Main {

    public static void main(String[] args) {
        var bfs = new BreadthFirstSearch<Direction>();
        bfs.solveAndPrintSolution(new PuzzleState());
    }

}

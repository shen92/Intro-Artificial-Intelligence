//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title: Take Stones
// Files: Helper.java, AlphaBetaPruning.java, TakeStones.java, GameState.java
// Course: CS 540 Fall 2019
//
// Author: Yingjie Shen
// Email: shen92@wisc.edu
// Lecturer's Name: Chuck Dyer
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name: N/A
// Partner Email: N/A
// Partner Lecturer's Name: N/A
//
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
// ___ Write-up states that pair programming is allowed for this assignment.
// ___ We have both read and understand the course Pair Programming Policy.
// ___ We have registered our team prior to the team registration deadline.
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully
// acknowledge and credit those sources of help here. Instructors and TAs do
// not need to be credited here, but tutors, friends, relatives, room mates,
// strangers, and others do. If you received no outside help from either type
// of source, then please explicitly indicate NONE.
//
// Persons: (identify each person and describe their help in detail)
// Online Sources: (identify each URL and describe their assistance in detail)
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////
import java.util.List;

public class AlphaBetaPruning {
  private int nextMove;
  private double value;
  private int isVisited;
  private int isEvaluated;
  private int maxSearchDepth;
  private int maxDepth;
  private int branchFactorCount;
  private int numBranchFactor;

  public AlphaBetaPruning() {}

  /**
   * This function will print out the information to the terminal, as specified in the homework
   * description.
   */
  public void printStats() {
    System.out.println("Move: " + nextMove);
    System.out.printf("Value: %1.1f\n", value);
    System.out.println("Number of Nodes Visited: " + isVisited);
    System.out.println("Number of Nodes Evaluated: " + isEvaluated);
    System.out.println("Max Depth Reached: " + --maxDepth);
    System.out.printf("Avg Effective Branching Factor: %1.1f\n",
        numBranchFactor / (double) branchFactorCount);
  }

  /**
   * This function will start the alpha-beta search
   * 
   * @param state This is the current game state
   * @param depth This is the specified search depth
   */
  public void run(GameState state, int depth) {
    this.nextMove = 0;
    this.value = 0.0;
    this.isVisited = 0;
    this.isEvaluated = 0;
    this.maxSearchDepth = depth + 1;
    this.maxDepth = 0;
    this.branchFactorCount = 0;
    this.numBranchFactor = 0;
    this.alphabeta(state, 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, state.getType());
  }


  /**
   * This method is used to implement alpha-beta pruning for both 2 players
   * 
   * @param state This is the current game state
   * @param depth Current depth of search
   * @param alpha Current Alpha value
   * @param beta Current Beta value
   * @param maxPlayer True if player is Max Player; Otherwise, false
   * @return int This is the number indicating score of the best next move
   */
  private double alphabeta(GameState state, int depth, double alpha, double beta,
      boolean maxPlayer) {
    if (depth > maxDepth) {
      maxDepth = depth;
    }

    List<GameState> successor = state.getSuccessors();
    if (depth == maxSearchDepth || successor.size() == 0) {
      isEvaluated++;
      isVisited++;
      if (maxPlayer) { //check if leaf node
        return -state.evaluate();
      } else {
        return state.evaluate();
      }
    }

    if (maxPlayer) {
      isVisited++;
      double evaluate = Double.NEGATIVE_INFINITY;
      int branchFactor = 0;
      for (GameState child : successor) { //traverse its child states
        branchFactor++;
        double maxEvaluation = alphabeta(child, depth + 1, alpha, beta, false);
        if (maxEvaluation > evaluate) {
          evaluate = maxEvaluation;
          if (depth == 1) {
            nextMove = child.getLastMove();
            value = evaluate;
          }
        }
        if (evaluate >= beta) {
          if (branchFactor != 0) {
            branchFactorCount++;
          }
          numBranchFactor += branchFactor;
          return evaluate;
        }
        alpha = Math.max(evaluate, alpha);
        if (evaluate >= alpha) {
          alpha = evaluate;
        }
      }
      if (branchFactor != 0) {
        branchFactorCount++;
      }
      numBranchFactor += branchFactor;
      return evaluate;
    } else {
      isVisited++;
      double evaluate = Double.POSITIVE_INFINITY;
      int branchFactor = 0;
      for (GameState child : successor) { //traverse its child states
        branchFactor++;
        double minEvaluation = alphabeta(child, depth + 1, alpha, beta, true);
        if (evaluate > minEvaluation) {
          evaluate = minEvaluation;
          if (depth == 1) {
            nextMove = child.getLastMove();
            value = evaluate;
          }
        }
        if (evaluate <= alpha) {
          if (branchFactor != 0) {
            branchFactorCount++;
          }
          numBranchFactor += branchFactor;
          return evaluate;
        }
        beta = Math.min(evaluate, beta);
      }
      if (branchFactor != 0) {
        branchFactorCount++;
      }
      numBranchFactor += branchFactor;
      return evaluate;
    }
  }
}


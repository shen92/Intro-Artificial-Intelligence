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


public class TakeStones {

	/**
	* This is the main method.
	* @param args A sequence of integer numbers, including the number of stones,
	* the number of taken stones, a list of taken stone and search depth
	* @exception IndexOutOfBoundsException On input error.
	*/
	public static void main (String[] args) {
		try {
			// Read input from command line
			int n = Integer.parseInt(args[0]);		// the number of stones
			int nTaken = Integer.parseInt(args[1]);	// the number of taken stones
			
			// Initialize the game state
			GameState state = new GameState(n);		// game state
			int stone;
			for (int i = 0; i < nTaken; i++) {
				stone = Integer.parseInt(args[i + 2]);
				state.removeStone(stone);
			}
			state.setType(nTaken);

			int depth = Integer.parseInt(args[nTaken + 2]);	// search depth
			// Process for depth being -1
			if (0 == depth)
				depth = n + 1;

			// Get next move
			AlphaBetaPruning searcher = new AlphaBetaPruning();
			searcher.run(state, depth);

			// Print search stats
			searcher.printStats();

		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
}
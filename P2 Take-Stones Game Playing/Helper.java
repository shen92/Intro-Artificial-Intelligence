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
public class Helper {

  /**
   * Class constructor.
   */
  private Helper() {}

  /**
   * This method is used to check if a number is prime or not
   * 
   * @param x A positive integer number
   * @return boolean True if x is prime; Otherwise, false
   */
  public static boolean isPrime(int x) {
    if (x <= 1) {
      return false;
    }
    for (int i = 2; i < Math.sqrt(x); i++) {
      if (x % i == 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * This method is used to get the largest prime factor
   * 
   * @param x A positive integer number
   * @return int The largest prime factor of x
   */
  public static int getLargestPrimeFactor(int x) {
    int lpf;
    long num = x;
    for (lpf = 2; lpf <= num; lpf++) {
      if (num % lpf == 0) {
        num /= lpf;
        lpf--;
      }
    }
    return lpf;
  }
  
}

//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title: Binary Decision Trees
// Files: DecisionTreeImpl.java, DecTreeNode.java, HW3.java
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
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class HW3 {
  // Runs the tests for HW3
  public static void main(String[] args) {
    // args = new String[] {"train_1.data", "test_1.data", "10", "10"}; // For testing purposes
    List<List<Integer>> trainData = createDataSet(args[0]);
    List<List<Integer>> testData = createDataSet(args[1]);
    // Build tree.
    DecisionTreeImpl mTree =
        new DecisionTreeImpl(trainData, Integer.parseInt(args[2]), Integer.parseInt(args[3]));
    // Print tree.
    mTree.printTree();
    mTree.printTest(testData);
  }

  // Converts from file to integer array. Each row is an instance.
  // The last element of each row is label, previous elements are attribute values.
  private static List<List<Integer>> createDataSet(String file) {
    int n = 0;
    int m = 0;
    // Figure out the dimensions
    try {
      Scanner scan = new Scanner(new File(file));
      while (scan.hasNextLine()) {
        String line = scan.nextLine();
        // Remove rows with missing data containing "?"
        if (line.indexOf('?') == -1) {
          String[] split = line.split(",");
          if (m != 0 && split.length != m)
            m = 0;
          else
            m = split.length;
          n++;
        }
      }
      scan.close();
    } catch (IOException e) {
    }
    // Read the data and convert to integers
    if (n == 0 || m == 0)
      return null;
    else {
      List<List<Integer>> dataSet = new ArrayList<List<Integer>>();
      try {
        Scanner scan = new Scanner(new File(file));
        for (int i = 0; i < n; i++) {
          List<Integer> data = new ArrayList<Integer>();
          String line = scan.nextLine();
          while (line.indexOf('?') != -1)
            line = scan.nextLine();
          String[] split = line.split(",");
          // Do not read the first column
          for (int j = 0; j < m - 2; j++)
            data.add(Integer.parseInt(split[j + 1]));
          // Convert the last column from 2 and 4 to 0 and 1, respectively
          data.add(Integer.parseInt(split[m - 1]) == 2 ? 0 : 1);
          dataSet.add(data);
        }
        scan.close();
      } catch (IOException e) {
      }
      return dataSet;
    }
  }
}

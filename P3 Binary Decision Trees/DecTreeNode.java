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
/**
 * Possible class for internal organization of a decision tree. Included to show standardized output
 * method, print().
 * 
 * Do not modify. If you use, create child class DecTreeNodeImpl that inherits the methods.
 * 
 */
public class DecTreeNode {
  // If leaf, label to return.
  public int classLabel;
  // Attribute split label.
  public int attribute;
  // Threshold that attributes are split on.
  public int threshold;
  // Left child. Can directly access and update. <= threshold.
  public DecTreeNode left = null;
  // Right child. Can directly access and update. > threshold.
  public DecTreeNode right = null;

  DecTreeNode(int classLabel, int attribute, int threshold) {
    this.classLabel = classLabel;
    this.attribute = attribute;
    this.threshold = threshold;
  }

  public boolean isLeaf() {
    return this.left == null && this.right == null;
  }
}

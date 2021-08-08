//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title: Digit Recognition
// Files: DigitClassifier.java, Instance.java, NNImpl.java, Node.java, NodeWeightPair.java
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
import java.util.*;

/**
 * Class for internal organization of a Neural Network. There are 5 types of nodes. Check the type
 * attribute of the node for details. Feel free to modify the provided functions to fit your own
 * implementation
 */

public class Node {
  private int type = 0; // 0=input,1=biasToHidden,2=hidden,3=biasToOutput,4=Output
  public ArrayList<NodeWeightPair> parents = null; // Array List that will contain the parents
                                                   // (including the bias node) with weights if
                                                   // applicable

  private double inputValue = 0.0;
  private double outputValue = 0.0;
  private double outputGradient = 0.0;
  private double delta = 0.0; // input gradient

  // Create a node with a specific type
  Node(int type) {
    if (type > 4 || type < 0) {
      System.out.println("Incorrect value for node type");
      System.exit(1);

    } else {
      this.type = type;
    }

    if (type == 2 || type == 4) {
      parents = new ArrayList<>();
    }
  }

  // For an input node sets the input value which will be the value of a particular attribute
  public void setInput(double inputValue) {
    if (type == 0) { // If input node
      this.inputValue = inputValue;
    }
  }

  /**
   * Calculate the output of a node. You can get this value by using getOutput()
   */
  public void calculateOutput() {
    if (type == 2 || type == 4) { // Not an input or bias node
      // TODO: add code here
      inputValue = 0;
      if (this.type == 2) { // if this node is a hidden node
        this.ReLU();
      } else if (this.type == 4) { // if this node is a output node
        this.getOutInputExp(); // step 1.5: calculate e^(weighted input of a output-node)
      }
    }
  }

  /**
   * This function calculates the output if the node is hidden function
   * 
   */
  private void ReLU() {
    // calculate input
    this.inputValue = 0;
    for (NodeWeightPair parent : this.parents) {
      this.inputValue += parent.weight * parent.node.getOutput();
    }
    // calculate g(a)
    this.outputValue = Math.max(0, this.inputValue);
  }

  /**
   * This function calculates the output if the node is a ouput function
   * 
   */
  private void getOutInputExp() {
    // calculate input
    this.inputValue = 0;
    for (NodeWeightPair parent : this.parents) {
      this.inputValue += parent.weight * parent.node.getOutput();
    }
    // calculate g(a)
    this.outputValue = Math.exp(this.inputValue);
  }


  // step-2: softMax
  public void softMax(double denominator) {
    if (type == 4) {
      this.outputValue = this.outputValue / denominator;
    }
  }

  // Gets the output value
  public double getOutput() {
    if (type == 0) { // Input node
      return inputValue;
    } else if (type == 1 || type == 3) { // Bias node
      return 1.00;
    } else {
      return outputValue;
    }

  }

  public void updateGradient(double gradient) {
    if (type == 2 || type == 4) {
      this.outputGradient += gradient;
    }
  }

  // Calculate the delta value of a node.
  public void calculateDelta() {
    if (type == 2 || type == 4) {
      // TODO: add code here
      if (type == 2) {// if this node is a hidden node
        this.delta = this.deltaHiddenUnit();
      } else if (type == 4) {// if this node is a output node
        this.delta = this.deltaOutputUnit();
      }
      this.outputGradient = 0;
    }
  }

  private double deltaHiddenUnit() {
    if (this.inputValue > 0)
      return this.outputGradient;
    return 0.0;
  }

  private double deltaOutputUnit() {
    return this.outputGradient;
  }

  public double getDelta() {
    return this.delta;
  }

  // Update the weights between parents node and current node
  public void updateWeight(double learningRate) {
    if (type == 2 || type == 4) {
      // TODO: add code here
      for (NodeWeightPair parent : this.parents) {
        parent.weight += learningRate * this.delta * parent.node.getOutput();
      }
    }
  }
}



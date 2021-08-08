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
import java.util.ArrayList;
import java.util.List;

/**
 * Fill in the implementation details of the class DecisionTree using this file. Any methods or
 * secondary classes that you want are fine but we will only interact with those methods in the
 * DecisionTree framework.
 */
public class DecisionTreeImpl {
  public DecTreeNode root;
  public List<List<Integer>> trainData;
  public int maxPerLeaf;
  public int maxDepth;
  public int numAttr;

  // Build a decision tree given a training set
  DecisionTreeImpl(List<List<Integer>> trainDataSet, int mPerLeaf, int mDepth) {
    this.trainData = trainDataSet;
    this.maxPerLeaf = mPerLeaf;
    this.maxDepth = mDepth;
    if (this.trainData.size() > 0)
      this.numAttr = trainDataSet.get(0).size() - 1;
    this.root = buildTree(this.trainData, 0);
  }

  private DecTreeNode buildTree(List<List<Integer>> trainDataSet, int depth) {
    int label = this.getMajorityLabels(trainDataSet);
    if (trainDataSet.size() == 0) { // empty train data
      return new DecTreeNode(1, -1, -1);
    }

    if (this.havsSameLabels(trainDataSet)) { // same labels
      return new DecTreeNode(label, -1, -1);
    }

    if (trainDataSet.size() <= this.maxPerLeaf) {
      return new DecTreeNode(label, -1, -1);
    }

    if (depth == this.maxDepth) {
      return new DecTreeNode(label, -1, -1);
    }

    double[] node = this.getBestAttribute(trainDataSet);
    int attribute = (int) node[0];
    int threshold = (int) node[1];
    double maxGain = node[2];

    if (maxGain == 0) {
      return new DecTreeNode(label, -1, -1);
    }

    DecTreeNode root = new DecTreeNode(label, attribute, threshold);

    List<List<Integer>> leftDataSet = new ArrayList<>();
    List<List<Integer>> rightDataSet = new ArrayList<>();
    for (int i = 0; i < trainDataSet.size(); i++) {
      if (trainDataSet.get(i).get(attribute) <= threshold) {
        leftDataSet.add(trainDataSet.get(i));
      } else if (trainDataSet.get(i).get(attribute) > threshold) {
        rightDataSet.add(trainDataSet.get(i));
      }
    }
    root.left = buildTree(leftDataSet, depth + 1);
    root.right = buildTree(rightDataSet, depth + 1);
    return root;
  }

  private boolean havsSameLabels(List<List<Integer>> trainDataSet) {
    int label = trainDataSet.get(0).get(this.numAttr);
    for (int i = 1; i < trainDataSet.size(); i++) {
      if (trainDataSet.get(i).get(this.numAttr).compareTo(label) != 0) {
        return false;
      }
    }
    return true;
  }

  private int getMajorityLabels(List<List<Integer>> trainDataSet) {
    double class_1_count = 0;
    double class_0_count = 0;
    for (List<Integer> data : trainDataSet) {
      if (data.get(this.numAttr) == 0) {
        class_0_count++;
      } else
        class_1_count++;
    }
    return class_1_count >= class_0_count ? 1 : 0;
  }

  /**
   * This method gets the best attribute of the attributes
   */
  private double[] getBestAttribute(List<List<Integer>> trainDataSet) {
    double classEntropy = getClassEntropy(trainDataSet, -1);
    double maxGain = 0;
    double[] node = new double[] {0, 0, 0};
    for (int i = 0; i < 9; i++) {
      for (int j = 1; j <= 10; j++) {
        int class_0_left = 0;
        int class_1_left = 0;
        int class_0_right = 0;
        int class_1_right = 0;
        for (int k = 0; k < trainDataSet.size(); k++) {
          if (trainDataSet.get(k).get(i) <= j) {
            if (trainDataSet.get(k).get(this.numAttr).compareTo(0) == 0) {
              class_0_left++;
            } else if (trainDataSet.get(k).get(this.numAttr).compareTo(0) == 1) {
              class_1_left++;
            }
          } else if (trainDataSet.get(k).get(i) > j) {
            if (trainDataSet.get(k).get(this.numAttr).compareTo(0) == 0) {
              class_0_right++;
            } else if (trainDataSet.get(k).get(this.numAttr).compareTo(0) == 1) {
              class_1_right++;
            }
          }
        }
        int left_count = class_0_left + class_1_left;
        int right_count = class_0_right + class_1_right;
        double p_left = (double) left_count / trainDataSet.size();
        double p_right = (double) right_count / trainDataSet.size();
        double thresholdEntropy = p_left * getEntropy(class_0_left, class_1_left)
            + p_right * getEntropy(class_0_right, class_1_right);
        double thresholdGain = classEntropy - thresholdEntropy;
        if (thresholdGain - maxGain > 0) {
          maxGain = thresholdGain;
          node[0] = i; // attribute
          node[1] = j; // threshold
          node[2] = maxGain; // gain
        }
      }
    }
    return node;
  }

  private double getEntropy(int l_count, int r_count) {
    int count = l_count + r_count;
    double p_left = (double) l_count / count;
    double p_right = (double) r_count / count;
    double entropy = -p_left * log2(p_left) - p_right * log2(p_right);
    return entropy;
  }

  private double getClassEntropy(List<List<Integer>> trainData, int breakPoint) {
    int l_count = breakPoint + 1;
    int r_count = trainData.size() - l_count;
    int l_0_count = 0;
    int r_0_count = 0;
    for (int i = 0; i <= breakPoint; i++) {
      if (trainData.get(i).get(this.numAttr).compareTo(0) == 0) {
        l_0_count++;
      }
    }
    for (int i = breakPoint + 1; i < trainData.size(); i++) {
      if (trainData.get(i).get(this.numAttr).compareTo(0) == 0) {
        r_0_count++;
      }
    }

    double p_l_0 = (double) l_0_count / l_count;
    int l_1_count = l_count - l_0_count;
    double p_l_1 = (double) l_1_count / l_count;
    if (l_count == 0) {
      p_l_0 = 0;
      p_l_1 = 0;
    }

    double p_r_0 = (double) r_0_count / r_count;
    int r_1_count = r_count - r_0_count;
    double p_r_1 = (double) r_1_count / r_count;

    double p_left = (double) l_count / (l_count + r_count);
    double p_right = (double) r_count / (l_count + r_count);

    double l_ce = -p_l_0 * log2(p_l_0) - p_l_1 * log2(p_l_1);
    double r_ce = -p_r_0 * log2(p_r_0) - p_r_1 * log2(p_r_1);

    double entropy = p_left * l_ce + p_right * r_ce;

    return entropy;
  }

  private double log2(double x) {
    if (x == 0) {
      return 0;
    }
    return (double) Math.log(x) / Math.log(2);
  }

  public int classify(List<Integer> instance) {
    DecTreeNode root = this.root;
    while (!root.isLeaf()) {
      if (instance.get(root.attribute) <= root.threshold) {
        root = root.left;
      } else
        root = root.right;
    }
    return root.classLabel;
  }

  // Print the decision tree in the specified format
  public void printTree() {
    printTreeNode("", this.root);
  }

  public void printTreeNode(String prefixStr, DecTreeNode node) {
    String printStr = prefixStr + "X_" + node.attribute;
    System.out.print(printStr + " <= " + String.format("%d", node.threshold));
    if (node.left.isLeaf()) {
      System.out.println(" : " + String.valueOf(node.left.classLabel));
    } else {
      System.out.println();
      printTreeNode(prefixStr + "|\t", node.left);
    }
    System.out.print(printStr + " > " + String.format("%d", node.threshold));
    if (node.right.isLeaf()) {
      System.out.println(" : " + String.valueOf(node.right.classLabel));
    } else {
      System.out.println();
      printTreeNode(prefixStr + "|\t", node.right);
    }
  }

  public double printTest(List<List<Integer>> testDataSet) {
    int numEqual = 0;
    int numTotal = 0;
    for (int i = 0; i < testDataSet.size(); i++) {
      int prediction = classify(testDataSet.get(i));
      int groundTruth = testDataSet.get(i).get(testDataSet.get(i).size() - 1);
      System.out.println(prediction);
      if (groundTruth == prediction) {
        numEqual++;
      }
      numTotal++;
    }
    double accuracy = numEqual * 100.0 / (double) numTotal;
    System.out.println(String.format("%.2f", accuracy) + "%");
    return accuracy;
  }

}

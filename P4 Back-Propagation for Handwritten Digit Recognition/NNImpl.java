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
 * The main class that handles the entire network Has multiple attributes each with its own use
 */

public class NNImpl {
  private ArrayList<Node> inputNodes; // list of the output layer nodes.
  private ArrayList<Node> hiddenNodes; // list of the hidden layer nodes
  private ArrayList<Node> outputNodes; // list of the output layer nodes

  private ArrayList<Instance> trainingSet; // the training set

  private double learningRate; // variable to store the learning rate
  private int maxEpoch; // variable to store the maximum number of epochs
  private Random random; // random number generator to shuffle the training set

  /**
   * This constructor creates the nodes necessary for the neural network Also connects the nodes of
   * different layers After calling the constructor the last node of both inputNodes and hiddenNodes
   * will be bias nodes.
   */

  NNImpl(ArrayList<Instance> trainingSet, int hiddenNodeCount, Double learningRate, int maxEpoch,
      Random random, Double[][] hiddenWeights, Double[][] outputWeights) {
    this.trainingSet = trainingSet;
    this.learningRate = learningRate;
    this.maxEpoch = maxEpoch;
    this.random = random;

    // input layer nodes
    inputNodes = new ArrayList<>();
    int inputNodeCount = trainingSet.get(0).attributes.size();
    int outputNodeCount = trainingSet.get(0).classValues.size();
    for (int i = 0; i < inputNodeCount; i++) {
      Node node = new Node(0);
      inputNodes.add(node);
    }

    // bias node from input layer to hidden
    Node biasToHidden = new Node(1);
    inputNodes.add(biasToHidden);

    // hidden layer nodes
    hiddenNodes = new ArrayList<>();
    for (int i = 0; i < hiddenNodeCount; i++) {
      Node node = new Node(2);
      // Connecting hidden layer nodes with input layer nodes
      for (int j = 0; j < inputNodes.size(); j++) {
        NodeWeightPair nwp = new NodeWeightPair(inputNodes.get(j), hiddenWeights[i][j]);
        node.parents.add(nwp);
      }
      hiddenNodes.add(node);
    }

    // bias node from hidden layer to output
    Node biasToOutput = new Node(3);
    hiddenNodes.add(biasToOutput);

    // Output node layer
    outputNodes = new ArrayList<>();
    for (int i = 0; i < outputNodeCount; i++) {
      Node node = new Node(4);
      // Connecting output layer nodes with hidden layer nodes
      for (int j = 0; j < hiddenNodes.size(); j++) {
        NodeWeightPair nwp = new NodeWeightPair(hiddenNodes.get(j), outputWeights[i][j]);
        node.parents.add(nwp);
      }
      outputNodes.add(node);
    }
  }

  /**
   * Get the prediction from the neural network for a single instance Return the idx with highest
   * output values. For example if the outputs of the outputNodes are [0.1, 0.5, 0.2], it should
   * return 1. The parameter is a single instance
   */

  public int predict(Instance instance) {
    for (int i = 0; i < instance.attributes.size(); i++)
      inputNodes.get(i).setInput(instance.attributes.get(i));

    for (Node hiddenNode : hiddenNodes) {
      hiddenNode.calculateOutput();
    }

    for (Node outputNode : outputNodes) {
      outputNode.calculateOutput();
    }

    double outputSum = 0; // sum for softmax denominator
    for (Node outputNode : this.outputNodes) {
      outputNode.calculateOutput(); // calculate the exp(weighted input)
      outputSum += outputNode.getOutput();
    }

    for (Node outputNode : outputNodes) {
      outputNode.softMax(outputSum);
    }

    int label = 0;
    double max = 0.0;

    for (int i = 0; i < instance.classValues.size(); i++) {
      if (outputNodes.get(i).getOutput() > max) {
        max = outputNodes.get(i).getOutput();
        label = i;
      }
    }
    return label;

  }


  /**
   * Train the neural networks with the given parameters
   * <p>
   * The parameters are stored as attributes of this class
   */

  public void train() {
    for (int epoch_i = 0; epoch_i < this.maxEpoch; epoch_i++) {
      Collections.shuffle(trainingSet, random);

      for (Instance instance : this.trainingSet) {
        // forward pass
        for (int attribute_i = 0; attribute_i < instance.attributes.size(); attribute_i++)
          inputNodes.get(attribute_i).setInput(instance.attributes.get(attribute_i));

        for (Node hiddenNode : hiddenNodes) {
          hiddenNode.calculateOutput();
        }

        for (Node outputNode : outputNodes) {
          outputNode.calculateOutput();
        }

        double outputSum = 0; // sum for softmax denominator
        for (Node outputNode : this.outputNodes) {
          outputNode.calculateOutput(); // calculate the exp(weighted input)
          outputSum += outputNode.getOutput();
        }

        for (Node outputNode : outputNodes) {
          outputNode.softMax(outputSum);
        }

        // backward pass
        for (int classValue_i = 0; classValue_i < instance.classValues.size(); classValue_i++) {
          double error =
              instance.classValues.get(classValue_i) - outputNodes.get(classValue_i).getOutput();
          outputNodes.get(classValue_i).updateGradient(error);
        }

        for (Node outputNode : outputNodes) {
          outputNode.calculateDelta();
        }

        for (Node outputNode : outputNodes) {
          for (NodeWeightPair parent : outputNode.parents) {
            double error = parent.weight * outputNode.getDelta();
            parent.node.updateGradient(error);
          }
        }

        for (Node hiddenNode : hiddenNodes) {
          hiddenNode.calculateDelta();
        }

        for (Node outputNode : this.outputNodes) {
          outputNode.updateWeight(learningRate);
        }
        for (Node hiddenNode : this.hiddenNodes) {
          hiddenNode.updateWeight(learningRate);
        }
      }

      double sumLoss = 0;
      for (Instance instance : this.trainingSet) {
        sumLoss += this.loss(instance);
      }
      System.out.printf("Epoch: %d, Loss: %.3e \n", epoch_i, (sumLoss / this.trainingSet.size()));
    }
  }

  private double loss(Instance instance) {
    for (int i = 0; i < instance.attributes.size(); i++)
      inputNodes.get(i).setInput(instance.attributes.get(i));

    for (Node hiddenNode : hiddenNodes) {
      hiddenNode.calculateOutput();
    }

    for (Node outputNode : outputNodes) {
      outputNode.calculateOutput();
    }

    double outputSum = 0; // sum for softmax denominator
    for (Node outputNode : this.outputNodes) {
      outputNode.calculateOutput(); // calculate the exp(weighted input)
      outputSum += outputNode.getOutput();
    }

    for (Node outputNode : outputNodes) {
      outputNode.softMax(outputSum);
    }

    double p = outputNodes.get(instance.classValues.indexOf(1)).getOutput();
    return -Math.log(p);
  }
}

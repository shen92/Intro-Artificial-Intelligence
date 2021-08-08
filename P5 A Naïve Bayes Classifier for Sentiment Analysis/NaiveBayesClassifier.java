import java.util.List;
import java.util.Map;
import java.util.*;

/**
 * Your implementation of a naive bayes classifier. Please implement all four methods.
 */
public class NaiveBayesClassifier implements Classifier {
  private int v;
  private Map<String, Integer> positiveWords; // positive words partition
  private Map<String, Integer> negativeWords; // negative words partition
  private int numPositiveTypes;
  private int numNegativeTypes;
  private int numPositiveTokens;
  private int numNegativeTokens;

  /**
   * This method trains your classifier using the given training data. The integer argument v is the
   * size of the total vocabulary in your model.
   * 
   * Store this argument as a field because you will need it in computing the smoothed
   * class-conditional probabilities.
   */
  @Override
  public void train(List<Instance> trainData, int v) {
    // Initialize the fields
    this.v = v;
    this.positiveWords = new HashMap<String, Integer>();
    this.negativeWords = new HashMap<String, Integer>();
    this.numPositiveTypes = 0;
    this.numNegativeTypes = 0;
    this.numPositiveTokens = 0;
    this.numNegativeTokens = 0;

    for (Instance instance : trainData) {
      if (instance.label.equals(Label.POSITIVE)) {
        this.numPositiveTypes++;
        for (String word : instance.words) {
          this.numPositiveTokens++;
          if (this.positiveWords.containsKey(word)) {// if the word exists
            int num = positiveWords.get(word);
            this.positiveWords.put(word, ++num);
          } else {// if the word not exist
            this.positiveWords.put(word, 1);
          }
        }
      } else if (instance.label.equals(Label.NEGATIVE)) {
        this.numNegativeTypes++;
        for (String word : instance.words) {
          numNegativeTokens++;
          if (this.negativeWords.containsKey(word)) {// if the word exists
            int num = negativeWords.get(word);
            this.negativeWords.put(word, ++num);
          } else {// if the word not exist
            this.negativeWords.put(word, 1);
          }
        }
      }
    }
  }

  /*
   * This method counts the number of words per label in the training set
   * 
   * returns a map that stores the (label, number of words) key-value pair.
   */
  @Override
  public Map<Label, Integer> getWordsCountPerLabel(List<Instance> trainData) {
    Map<Label, Integer> result = new HashMap<Label, Integer>();
    int numPositive = 0;
    int numNegative = 0;
    for (Instance instance : trainData) {
      if (instance.label.equals(Label.POSITIVE)) {
        numPositive += instance.words.size();
      } else if (instance.label.equals(Label.NEGATIVE)) {
        numNegative += instance.words.size();
      }
    }
    result.put(Label.POSITIVE, numPositive);
    result.put(Label.NEGATIVE, numNegative);
    return result;
  }

  /*
   * This method counts the number of reviews per class label in the training set
   * 
   * returns a map that stores the (label, number of documents) key-value pair.
   */
  @Override
  public Map<Label, Integer> getDocumentsCountPerLabel(List<Instance> trainData) {
    Map<Label, Integer> result = new HashMap<Label, Integer>();
    int numPositive = 0;
    int numNegative = 0;
    for (Instance instance : trainData) {
      if (instance.label.equals(Label.POSITIVE)) {
        numPositive++;
      } else if (instance.label.equals(Label.NEGATIVE)) {
        numNegative++;
      }
    }
    result.put(Label.POSITIVE, numPositive);
    result.put(Label.NEGATIVE, numNegative);
    return result;
  }

  /**
   * Returns the prior probability of the label parameter, i.e. P(POSITIVE) or P(NEGATIVE)
   */
  private double p_l(Label label) {
    int numWordTypes = this.numPositiveTypes + this.numNegativeTypes;
    return label.equals(Label.POSITIVE) ? (double) this.numPositiveTypes / numWordTypes
        : (double) this.numNegativeTypes / numWordTypes;
  }

  /**
   * Returns the smoothed conditional probability of the word given the label, i.e. P(word|POSITIVE)
   * or P(word|NEGATIVE)
   */
  private double p_w_given_l(String word, Label label) {
    if (label.equals(Label.POSITIVE)) {
      return !this.positiveWords.containsKey(word)
          ? (double) 1 / (1 * this.v + this.numPositiveTokens)
          : ((double) this.positiveWords.get(word) + 1) / (1 * this.v + this.numPositiveTokens);
    } else if (label.equals(Label.NEGATIVE)) {
      return !this.negativeWords.containsKey(word)
          ? (double) 1 / (1 * this.v + this.numNegativeTokens)
          : ((double) this.negativeWords.get(word) + 1) / (1 * this.v + this.numNegativeTokens);
    }
    return 0.0;
  }

  /**
   * Classifies an array of words as either POSITIVE or NEGATIVE.
   */
  @Override
  public ClassifyResult classify(List<String> words) {
    ClassifyResult classifyResult = new ClassifyResult();
    Map<Label, Double> logProbPerLabel = new HashMap<Label, Double>();

    double positive_p_w_given_l = Math.log(this.p_l(Label.POSITIVE));
    double negative_p_w_given_l = Math.log(this.p_l(Label.NEGATIVE));
    for (String word : words) {
      positive_p_w_given_l += Math.log(this.p_w_given_l(word, Label.POSITIVE));
      negative_p_w_given_l += Math.log(this.p_w_given_l(word, Label.NEGATIVE));
    }
    logProbPerLabel.put(Label.POSITIVE, positive_p_w_given_l);
    logProbPerLabel.put(Label.NEGATIVE, negative_p_w_given_l);

    classifyResult.label =
        positive_p_w_given_l - negative_p_w_given_l > 0 ? Label.POSITIVE : Label.NEGATIVE;
    classifyResult.logProbPerLabel = logProbPerLabel;

    return classifyResult;
  }
}

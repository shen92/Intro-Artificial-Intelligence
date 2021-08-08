import java.util.ArrayList;
import java.util.List;

public class CrossValidation {
  /*
   * Returns the k-fold cross validation score of classifier clf on training data.
   */
  public static double kFoldScore(Classifier clf, List<Instance> trainData, int k, int v) {
    double score = 0.0;
    int count = 0;
    int size = trainData.size();
    List<Instance> trainingSet;
    List<Instance> testingSet;

    for (int epoch = 0; epoch < k; epoch++) {
      int test = size * (epoch) / k;
      int train = size * (epoch + 1) / k;

      trainingSet = new ArrayList<Instance>();
      testingSet = new ArrayList<Instance>();

      for (int i = 0; i < trainData.size(); i++) {
        Instance inst = trainData.get(i);

        if (test <= i && i < train) {
          testingSet.add(inst);
        } else {
          trainingSet.add(inst);
        }

      }

      clf.train(trainingSet, v);
      int numCorrect = 0;
      for (Instance instance : testingSet) {
        ClassifyResult res = clf.classify(instance.words);
        if (res.label.equals(instance.label))
          numCorrect++;
      }

      double result = 1.0 * (numCorrect) / testingSet.size();

      score = (score * count + result) / (count + 1);
      count++;
    }

    return score;
  }
}

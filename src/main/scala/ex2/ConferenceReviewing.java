package ex2;

import scala.Int;
import scala.collection.immutable.List;
import scala.collection.immutable.Map;
import scala.collection.immutable.Set;
import ex2.Pair.Pair;

public interface ConferenceReviewing {

    void loadReview(int id, int relevance, int significance, int confidence, int finale);

    List<Int> orderedScores(int articleId, Question question);

    void loadReview(int id, Map<Question, Int> map);
    
    double averageFinalScore(int id);
    
    Set<Int> acceptedArticles();
    
    List<Pair<Int, scala.Double>> sortedAcceptedArticles();
    
    Map<Int, scala.Double> averageWeightedFinalScoreMap();
}

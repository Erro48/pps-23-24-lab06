package ex2

object Pair:
  trait Pair[X,Y]:
    def x: X
    def y: Y

  object Pair:
    def apply[X,Y](x: X, y: Y): Pair[X, Y] = PairImpl(x,y)
    private case class PairImpl[X,Y](x: X, y: Y) extends Pair[X,Y]


case class ConferenceReviewingImpl() extends ConferenceReviewing:

  private val minimumAcceptableMean: Double = 5.0
  private val minimumAcceptableRelevance: Double = 8.0
  private val numberOfAcceptableRelevance: Int = 1

  private var reviews: Map[Int, List[Map[Question, Int]]] = Map()

  override def loadReview(id: Int, scores: Map[Question, Int]): Unit =
    val currentScores: List[Map[Question, Int]] = if reviews.contains(id) then reviews(id) else List()
    var newScores: List[Map[Question, Int]] = List()
    newScores = scores :: (if currentScores.isEmpty then newScores else currentScores)
    reviews = reviews + ((id, newScores))

  override def loadReview(id: Int, relevance: Int, significance: Int, confidence: Int, finale: Int): Unit =
    var scores: Map[Question, Int] = Map()
    scores = scores + ((Question.RELEVANCE, relevance), (Question.SIGNIFICANCE, significance), (Question.CONFIDENCE, confidence), (Question.FINAL, finale))
    loadReview(id, scores)

  override def averageFinalScore(id: Int): Double =
    val finalScores: List[Int] = orderedScores(id, Question.FINAL)
    val listSize: Double = finalScores.length
    finalScores.sum / listSize

  override def acceptedArticles(): Set[Int] =
    reviews
      .filter { case (id, list) => averageFinalScore(id) >= minimumAcceptableMean }
      .filter { case (id, list) => list.count(_(Question.RELEVANCE) >= minimumAcceptableRelevance) >= numberOfAcceptableRelevance }
      .map { case (id, _) => id }
      .toSet


  override def sortedAcceptedArticles(): List[Pair.Pair[Int, Double]] =
    this.acceptedArticles()
      .map(id => Pair.Pair(id, averageFinalScore(id)))
      .toList
      .reverse

  private def averageWeightedFinalScoreMap(articleId: Int): Double =
    val size: Double = reviews.filter((id, list) => articleId == id).flatMap((id, list) => list).size
    reviews
      .filter((id, list) => articleId == id)
      .flatMap((id, list) => list)
      .map(m => m(Question.FINAL).toDouble * m(Question.CONFIDENCE).toDouble / 10.0)
      .sum / size
  override def averageWeightedFinalScoreMap(): Map[Int, Double] =
    reviews.map((id, list) => (id, averageWeightedFinalScoreMap(id)))

  override def orderedScores(articleId: Int, question: Question): List[Int] =
    val scores: List[Map[Question, Int]] = reviews(articleId)
    scores.map(_(question)).sorted(_ - _)




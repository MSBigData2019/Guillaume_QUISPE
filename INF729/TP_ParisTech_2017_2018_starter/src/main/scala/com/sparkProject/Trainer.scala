package com.sparkProject

import org.apache.spark.SparkConf
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.evaluation.{BinaryClassificationEvaluator, MulticlassClassificationEvaluator, RegressionEvaluator}
import org.apache.spark.ml.feature._
import org.apache.spark.ml.tuning.{ParamGridBuilder, TrainValidationSplit}
import org.apache.spark.sql.SparkSession


object Trainer {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAll(Map(
      "spark.scheduler.mode" -> "FIFO",
      "spark.speculation" -> "false",
      "spark.reducer.maxSizeInFlight" -> "48m",
      "spark.serializer" -> "org.apache.spark.serializer.KryoSerializer",
      "spark.kryoserializer.buffer.max" -> "1g",
      "spark.shuffle.file.buffer" -> "32k",
      "spark.default.parallelism" -> "12",
      "spark.sql.shuffle.partitions" -> "12",
      "spark.driver.maxResultSize" -> "2g"
    ))


    val spark = SparkSession
      .builder
      .config(conf)
      .appName("TP_spark")
      .getOrCreate()

    // Loading data
    val df_dataset = spark
      .read
      .load("/Users/guillaumequispe/Downloads/prepared_trainingset/*.parquet")

    // random split pour training et test set
    val Array(training, test) = df_dataset.randomSplit(Array[Double](0.9, 0.1), 18)

    // on va maintenant définir les étapes de notre pipeline
    val tokenizer = new RegexTokenizer()
      .setPattern("\\W+")
      .setGaps(true)
      .setInputCol("text")
      .setOutputCol("tokens")

    val remover = new StopWordsRemover()
      .setInputCol("tokens")
      .setOutputCol("filtered")

    val countsvectorized = new CountVectorizer()
      .setInputCol("filtered").setOutputCol("rawFeatures")

    val idf = new IDF().setInputCol("rawFeatures").setOutputCol("tfidf")

    val stringindexcurrency =  new StringIndexer().setInputCol("currency2").setOutputCol("currency_indexed")
    val stringindexcountry = new StringIndexer().setInputCol("country2").setOutputCol("country_indexed")



    val encodedcurr = new OneHotEncoder().setInputCol("currency_indexed").setOutputCol("currency_onehot")
    val encodedcount = new OneHotEncoder().setInputCol("country_indexed").setOutputCol("country_onehot")


    val assembler = new VectorAssembler()
      .setInputCols(Array("tfidf", "days_campaign", "hours_prepa", "goal", "country_onehot", "currency_onehot"))
      .setOutputCol("features")

    // et une regression logistique sur les données regroupées dans features
    val lr = new LogisticRegression()
      .setElasticNetParam(0.0)
      .setFitIntercept(true)
      .setFeaturesCol("features")
      .setLabelCol("final_status")
      .setStandardization(true)
      .setPredictionCol("predictions")
      .setRawPredictionCol("raw_predictions")
      .setThresholds(Array(0.7, 0.3))
      .setTol(1.0e-6)
      .setMaxIter(300)

    // Définition du pipeline
    val pipeline = new Pipeline()
      .setStages(Array(tokenizer, remover, countsvectorized,idf, stringindexcurrency, stringindexcountry,encodedcurr , encodedcount,assembler, lr))

    // definition des paramètres du modèle à tester puis de la grid search
    val regParams:Array[Double] = (-4  to -1  map(x => scala.math.pow(10,2*x) )).toArray
    val minDFs:Array[Double] = Array(55,75,95)

    val paramGrid = new ParamGridBuilder()
      .addGrid(lr.regParam, regParams)
      .addGrid(countsvectorized.minDF,minDFs)
     // .addGrid(lr.elasticNetParam, Array(0.0, 0.5, 1.0))
     // .addGrid(lr.elasticNetParam, Array(0.0, 0.5, 1.0))
      .build()

    val evaluator=new MulticlassClassificationEvaluator().setLabelCol("final_status").setPredictionCol("predictions").setMetricName("f1")
    // definition du modele de train/validation
    val trainValidationSplit = new TrainValidationSplit()
      .setEstimator(pipeline)
      .setEvaluator(evaluator)
      .setEstimatorParamMaps(paramGrid)
      .setTrainRatio(0.7)
    val model = trainValidationSplit.fit(training)

    val df_WithPredictions=model.transform(test)
    val f1 = evaluator.evaluate(df_WithPredictions)
    println("F1 " + f1)

    //df_WithPredictions.select("project_id","predictions").show()
    df_WithPredictions.groupBy("final_status", "predictions").count.show()

    //sauvegarde du modèle
    model.write.overwrite().save("/Users/guillaumequispe/Downloads/telecom/TP_ParisTech_2017_2018_starter/pipeline")

    //if problems with unimported modules => sbt plugins update
  }
}

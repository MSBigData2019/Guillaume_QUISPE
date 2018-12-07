package com.sparkProject

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.functions.udf

object Preprocessor {

  def main(args: Array[String]): Unit = {

    // Des réglages optionels du job spark. Les réglages par défaut fonctionnent très bien pour ce TP
    // on vous donne un exemple de setting quand même
    val conf = new SparkConf().setAll(Map(
      "spark.scheduler.mode" -> "FIFO",
      "spark.speculation" -> "false",
      "spark.reducer.maxSizeInFlight" -> "48m",
      "spark.serializer" -> "org.apache.spark.serializer.KryoSerializer",
      "spark.kryoserializer.buffer.max" -> "1g",
      "spark.shuffle.file.buffer" -> "32k",
      "spark.default.parallelism" -> "12",
      "spark.sql.shuffle.partitions" -> "12"
    ))

    // Initialisation de la SparkSession qui est le point d'entrée vers Spark SQL (donne accès aux dataframes, aux RDD,
    // création de tables temporaires, etc et donc aux mécanismes de distribution des calculs.)
    val spark = SparkSession
      .builder
      .config(conf)
      .appName("TP_spark")
      .getOrCreate()

    import spark.implicits._
    /*******************************************************************************
      *
      *       TP 2
      *
      *       - Charger un fichier csv dans un dataFrame
      *       - Pre-processing: cleaning, filters, feature engineering => filter, select, drop, na.fill, join, udf, distinct, count, describe, collect
      *       - Sauver le dataframe au format parquet
      *
      *       if problems with unimported modules => sbt plugins update
      *
      ********************************************************************************/

    println("hello world ! from Preprocessor")
    val sc = spark.sparkContext
    val df = spark.read.format("csv").option("header", "true").load("train_clean.csv")
    println(df.columns.size)
    println(df.count())
    df.show()
    df.printSchema()
    df.head(5)

    val df2 = df.withColumn("goal",df("goal").cast(IntegerType)).withColumn("backers_count",df("backers_count").cast(IntegerType))

    df2.describe("goal","backers_count").show()
    val df3=df2.drop("disable_communication","backers_count","state_changed_at")

    val df_modified  = df3.withColumn("country2", udf_value($"country", $"currency")).withColumn("currency2", udf_valuecurrency( $"currency"))
    df_modified.show(4)

    val df4 = df_modified.filter($"country"==="False") .groupBy("currency").count.orderBy($"count".desc).show(50)

  }
  def udf_value = udf{(name: String, value: String) =>
    if (name == "False")
      value
    else if (name.length()!=2)
      null
    else
      name
  }

  def udf_valuecurrency = udf{(name: String) =>
    if (name.length()!=3)
      null
    else
      name
  }



}

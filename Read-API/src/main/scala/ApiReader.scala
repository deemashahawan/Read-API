import org.apache.hadoop.shaded.org.apache.http.client.methods.HttpGet
import org.apache.hadoop.shaded.org.apache.http.impl.client.HttpClients
import org.apache.log4j.BasicConfigurator
import org.apache.log4j.varia.NullAppender
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.execution.streaming.CommitMetadata.format
import org.json4s._
import org.json4s.jackson.JsonMethods._

object ApiReader {


  def main(args: Array[String]): Unit = {
    BasicConfigurator.configure(new NullAppender())
    // Create a SparkSession
    val spark = SparkSession.builder()
      .appName("Read API Data")
      .master("local[*]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    val url = "https://www.arbeitnow.com/api/job-board-api"
    val dataArray = fetchDataFromAPI(url)
    val df = spark.createDataFrame(dataArray.map(_.extract[Job]))

    df.show(3)

    spark.stop()
  }

  private case class Job(
                          slug: String,
                          company_name: String,
                          title: String,
                          description: String,
                          remote: Boolean,
                          url: String,
                          tags: List[String],
                          job_types: List[String],
                          location: String,
                          created_at: Long
                        )

  private def fetchDataFromAPI(url: String): List[JValue] = {
    val httpClient = HttpClients.createDefault()
    val httpGet = new HttpGet(url)
    val response = httpClient.execute(httpGet)
    val entity = response.getEntity
    val jsonStr = scala.io.Source.fromInputStream(entity.getContent).mkString
    val data = (parse(jsonStr) \ "data").asInstanceOf[JArray].arr
    httpClient.close()
    data
  }


}

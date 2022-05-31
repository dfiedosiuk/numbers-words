import java.util.Properties
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.serialization.Serdes.stringSerde
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig, Topology}
import scopt.OParser

object Stream extends App {

  import org.apache.kafka.streams.scala._

  case class Config(numbersTopic: String, textTopic: String, outputNumbers: String,outputText: String, multiply: Int)

  val myBuilder = OParser.builder[Config]

  val myParser = {
    import myBuilder._
    OParser.sequence(
      programName("daniel"),
      head("scopt", "1.0.0-alpha"),
      help("help").text("prints this usage text"),
      opt[String]('n', "numbersTopic")
        .action((value, cfg) => cfg.copy(numbersTopic = value))
        .text("My name property"),
      opt[String]('t', "textTopic")
        .action((value, cfg) => cfg.copy(textTopic = value))
        .text("My name property"),
      opt[String]('o', "outputTopic")
        .action((value, cfg) => cfg.copy(outputNumbers = value))
        .text("My name property"),
      opt[String]('p', "outputTopic")
        .action((value, cfg) => cfg.copy(outputText = value))
        .text("My name property"),
      opt[Int]('m', "multiply")
        .action((value, cfg) => cfg.copy(multiply = value))
        .text("My name property")

    )
  }

  val config = OParser.parse(myParser, args,
    Config(numbersTopic = "", textTopic = "", outputNumbers = "",outputText = "", multiply = 0)).getOrElse {
    println("Not enough or incorrect command-line arguments. Exiting...")
    sys.exit(-1)
  }

  val props = new Properties()
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-app")
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, ":9092")
  props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 0)

  def createTopologyWords(): Topology = {
    val builder = new StreamsBuilder
    builder
      .stream[String, String](config.textTopic)
      .flatMapValues(text => text.toUpperCase.split("\\W+"))
      .to(config.outputText)
    builder.build
  }

  def createTopologyNumbers(): Topology = {
    val builder = new StreamsBuilder
    builder
      .stream[String, String](config.numbersTopic)
      .mapValues(numbers => changeNumbers(numbers, config.multiply))
      .to(config.outputNumbers)
    builder.build
  }

  def changeNumbers(text: String, multiply: Int): String = {
    val numbersText = text.split(" ")
    numbersText.foreach(println)
    val numbers = numbersText.map(_.toInt)

    numbers.map {
      case n if (n > 0) => (n * multiply).toString
      case n if (n == 0) => "0"
      case n if (n < 0) => s"negative number: ${n}"
    }.mkString(" , ")

  }

  val streamsNumbers: KafkaStreams = new KafkaStreams(createTopologyNumbers(), props)
//  val streamsWords: KafkaStreams = new KafkaStreams(createTopologyWords(), props)
  streamsNumbers.start()
//  streamsWords.start()

  sys.ShutdownHookThread {
    streamsNumbers.close()
//    streamsWords.close()
  }

}
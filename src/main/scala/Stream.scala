import java.util.Properties
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.serialization.Serdes.stringSerde
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig, Topology}
import scopt.OParser

class Stream extends App {

  import org.apache.kafka.streams.scala._

  case class Config(numbersTopic: String, textTopic: String, outputTopic: String, multiply: Int)

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
        .action((value, cfg) => cfg.copy(outputTopic = value))
        .text("My name property"),
      opt[Int]('m', "multiply")
        .action((value, cfg) => cfg.copy(multiply = value))
        .text("My name property")

    )
  }

  val config = OParser.parse(myParser, args,
    Config(numbersTopic = "", textTopic = "", outputTopic = "", multiply = 0)).getOrElse {
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
      .to("output")
    builder.build
  }

  def createTopologyNumbers(): Topology = {
    val builder = new StreamsBuilder
    builder
      .stream[String, String](config.numbersTopic)
      .flatMapValues(numbers => numbers.split("\\W+"))
      .to("output")
    builder.build
  }

  def changeNumbers(text: String, multiply: Int) ={
    val numbersText = text.split("\\W+")
    val numbers = numbersText.map(_.toInt)

    numbers.map{
      case n if (n > 0) => (n*multiply).toString
      case n if (n == 0) => "0"
      case n if (n < 0)=> s"negative number: ${n}"
    }.mkString(" ")

  }

  val streams: KafkaStreams = new KafkaStreams(createTopologyWords(), props)
  streams.start()

  sys.ShutdownHookThread {
    streams.close()
  }

}
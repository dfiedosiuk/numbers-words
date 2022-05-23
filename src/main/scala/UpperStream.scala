import java.util.Properties
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.serialization.Serdes.stringSerde
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig, Topology}

class UpperStream {

  import org.apache.kafka.streams.scala._

  val props: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application")
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, ":9092")
    p.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 0)
    p
  }

  def createTopology(): Topology = {
    val builder = new StreamsBuilder
    val text = builder.stream[String, String]("input")
    val textUpper = text
      .flatMapValues(text => text.toUpperCase.split("\\W+"))

    println(textUpper)
    textUpper.to("output")

    builder.build
  }

  val streams: KafkaStreams = new KafkaStreams(createTopology(), props)
  streams.start()

  sys.ShutdownHookThread {
    streams.close()
  }
}
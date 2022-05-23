import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.apache.kafka.streams.{StreamsBuilder, TopologyTestDriver}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.jdk.CollectionConverters._

class UpperStreamSpec extends AnyFunSpec with Matchers {
  private val stringSer = new StringSerializer()
  private val stringDes = new StringDeserializer()

  describe("Running topology") {
  }
  it("generate events") {
    val target = new UpperStream

    val topology = target.createTopology()
    val driver = new TopologyTestDriver(topology, target.props)

    val inputTopic = driver.createInputTopic("input", stringSer, stringSer)
    val outputTopic = driver.createOutputTopic("output", stringDes, stringDes)

    inputTopic.pipeInput("key1", "value1")

    val results = outputTopic.readKeyValuesToMap().asScala

    results should be(Map("key1" -> "VALUE1"))
  }
}

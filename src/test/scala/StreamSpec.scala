import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.apache.kafka.streams.{StreamsBuilder, TopologyTestDriver}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should
import org.scalatest.matchers.should.Matchers

import scala.jdk.CollectionConverters._

class StreamSpec extends AnyFlatSpec with should.Matchers {

  behavior of "changeNumbers"

  it should "Multiplies positive numbers by a number given on command line, Adds “negative number: “ prefix to negative numbers " in {
    val myStream = new Stream
    val numbersText = "9 4 -3 2"
    val confMultiply = 5
    myStream.changeNumbers(numbersText, confMultiply) shouldBe "2"


  }
//  private val stringSer = new StringSerializer()
//  private val stringDes = new StringDeserializer()
//
//  it("generate events") {
//    val target = new UpperStream
//
//    val topology = target.createTopology()
//    val driver = new TopologyTestDriver(topology, target.props)
//
//    val inputTopic = driver.createInputTopic("input", stringSer, stringSer)
//    val outputTopic = driver.createOutputTopic("output", stringDes, stringDes)
//
//    inputTopic.pipeInput("key1", "value1")
//
//    val results = outputTopic.readKeyValuesToMap().asScala
//
//    results should be(Map("key1" -> "VALUE1"))
//  }
}

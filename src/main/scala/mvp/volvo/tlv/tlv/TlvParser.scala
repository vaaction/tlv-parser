package mvp.volvo.tlv.tlv

import java.nio.ByteBuffer

import scala.collection.mutable.ArrayBuffer
import TlvParser._

case class TlvParser(tag: Int, length: Int, value: Array[Byte],
                     children: ArrayBuffer[TlvParser] = ArrayBuffer[TlvParser](),
                     config: TlvConfig = TlvConfig()) {

  parse()

  private def parse(): Unit = {
    var i = 0
    val endIndex = length - 2 * config.size

    while (i < endIndex) {
      val tag = getNext
      val length = getNext
      try {
        val tlv = new TlvParser(tag, length, nextValue(i, length, value, config))
        children += tlv
        i += tlv.length
      } catch {
        case e: Throwable => throw new TlvException(s"Incorrect tlv structure: $this", value, e)
      }
    }

    def getNext: Int = {
      try {
        val n = next(i, value, config)
        i += config.size
        n
      } catch {
        case e: IndexOutOfBoundsException =>
          throw new TlvException(s"Cannot get data by offset: $i, tlv structure: $this", value, e)
      }
    }
  }

  override def toString: String = {
    import spray.json._
    import scala.collection.JavaConverters._

    val source = "{" +
      " \"tag\":" + tag +
      ", \"length\":" + length +
      ", \"value\":\"" + value.map("%02X" format _).mkString(" ") + "\"" +
      ", \"children\":" + children.toList.asJava +
    "}"
    source.parseJson.prettyPrint
  }
}

object TlvParser {

  def apply(valueHex: String): TlvParser = {
    try {
      val data = valueHex
        .replaceAll(" ", "")
        .replaceAll("\n", "")
        .replaceAll("\t", "")
        .trim
        .sliding(2, 2).toArray.map(v => Integer.parseInt(v, 16)).map(_.toByte)
      TlvParser(data, TlvConfig())
    } catch {
      case e: NumberFormatException => throw new TlvException("Not valid HEX data", Array.empty, e)
    }
  }

  def apply(value: Array[Byte], config: TlvConfig): TlvParser = {
    try {
      val tag = next(0, value, config)
      val length = next(config.size, value, config)
      val v = nextValue(2 * config.size, length, value, config)
      new TlvParser(tag, length, v, config = config)
    } catch {
      case e: TlvException => throw e
      case e: Throwable => throw new TlvException(s"Incorrect tlv structure in top", value, e)
    }
  }

  private def next(offset: Int, value: Array[Byte], config: TlvConfig) = {
    ByteBuffer.wrap(value, offset, config.size).order(config.order).getShort()
  }

  private def nextValue(offset: Int, length: Int, value: Array[Byte], config: TlvConfig) : Array[Byte] = {
    val nextValue = new Array[Byte](length)
    System.arraycopy(value, offset, nextValue, 0, length)
    nextValue
  }

}
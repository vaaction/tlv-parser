package mvp.volvo.tlv


import java.nio.ByteBuffer
import mvp.volvo.tlv.TlvParser.{next, nextValue}
import scala.collection.mutable.ArrayBuffer


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
      val tlv = new TlvParser(tag, length, nextValue(i, length, value, config))
      children += tlv
      i += tlv.length
    }

    def getNext: Int = {
      val n = next(i, value, config)
      i += config.size
      n
    }
  }

}

object TlvParser {
  def apply(value: Array[Byte], config: TlvConfig): TlvParser = {
    val tag = next(0, value, config)
    val length = next(config.size, value, config)
    val v = nextValue(2 * config.size, length, value, config)
    new TlvParser(tag, length, v, config = config)
  }

  private def next(offset: Int, value: Array[Byte], config: TlvConfig) = {
    try {
      ByteBuffer.wrap(value, offset, config.size).order(config.order).getShort()
    } catch {
      case e: IndexOutOfBoundsException =>
        throw new TlvException(s"Cannot get data by offset: $offset, tlv structure: $this", value)
    }
  }

  private def nextValue(offset: Int, length: Int, value: Array[Byte], config: TlvConfig) : Array[Byte] = {
    try {
      val nextValue = new Array[Byte](length)
      System.arraycopy(value, offset, nextValue, 0, length)
      nextValue
    } catch {
      case e: Throwable => throw new TlvException(s"Incorrect tlv structure: $this", value, e)
    }
  }

}
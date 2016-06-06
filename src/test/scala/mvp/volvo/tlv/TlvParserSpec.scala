package mvp.volvo.tlv

import org.scalatest._

class TlvParserSpec extends FlatSpec with Matchers {

  val tlvData = Array[Byte](0, 1, 0, 12, 0, 2, 0, 3, 'a', 'b', 'c', 0, 3, 0, 1, 'd')

  it should "parse tlvData" in {
    val parser = TlvParser(tlvData, TlvConfig())
    parser.children.size should be (2)
    parser.tag should be (1)
    parser.length should be (12)
    parser.value should be (Array(0, 2, 0, 3, 97, 98, 99, 0, 3, 0, 1, 100))

    parser.children(0).tag should be (2)
    parser.children(0).length should be (3)
    new String(parser.children(0).value) should be ("abc")
    parser.children(0).children.size should be (0)

    parser.children(1).tag should be (3)
    parser.children(1).length should be (1)
    new String(parser.children(1).value) should be ("d")
    parser.children(1).children.size should be (0)
  }
}

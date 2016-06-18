package mvp.volvo.tlv.model

import java.nio.ByteOrder

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

final case class TlvParseRequest(data: String, byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN)

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val format = jsonFormat2(TlvParseRequest)
}

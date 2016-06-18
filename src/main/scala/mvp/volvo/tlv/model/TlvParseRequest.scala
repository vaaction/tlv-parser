package mvp.volvo.tlv.model

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

final case class TlvParseRequest(data: String)

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val format = jsonFormat1(TlvParseRequest)
}

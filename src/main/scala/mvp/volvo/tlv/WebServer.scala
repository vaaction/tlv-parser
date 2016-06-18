package mvp.volvo.tlv

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import mvp.volvo.tlv.model.TlvParseRequest
import mvp.volvo.tlv.tlv.{TlvException, TlvParser}
import mvp.volvo.tlv.model.JsonSupport

import scala.util.Properties

class WebServer

object WebServer extends App with JsonSupport {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  val log = Logging(system, classOf[WebServer])
  implicit val executionContext = system.dispatcher

  val route =
    pathSingleSlash {
      get {
        getFromResource("assets/html/index.html")
      } ~
        post {
          entity(as[TlvParseRequest]) { request: TlvParseRequest =>
            try {
              val tlvParser = TlvParser(request.data)
              complete(tlvParser.toString)
            } catch {
              case e: TlvException =>
                log.warning(s"Error message: ${e.message}, data: " + e.data.mkString(","))
                complete {
                  HttpResponse(status = StatusCodes.BadRequest, entity = e.getMessage)
                }
            }
          }
        }
    } ~
      get {
        path("assets" / Remaining) { path =>
          val res = getFromResource("assets/" + path)
          res
        }
      }

  val port = System.getenv.getOrDefault("http.port", "8080")
  Http().bindAndHandle(route, "0.0.0.0", port.toInt)
  log.info(s"Server online at http://0.0.0.0:$port\n")
}
package mvp.volvo.tlv

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

class WebServer

object WebServer extends App {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  val log = Logging(system, classOf[WebServer])
  implicit val executionContext = system.dispatcher

  val route =
    get {
      pathSingleSlash {
        getFromResource("assets/html/index.html")
      } ~
        path("assets" / Remaining) { path =>
          val res = getFromResource("assets/" + path)
          log.debug(res.toString())
          res
        }
    }

  Http().bindAndHandle(route, "localhost", 8080)
  log.info(s"Server online at http://localhost:8080\n")
}
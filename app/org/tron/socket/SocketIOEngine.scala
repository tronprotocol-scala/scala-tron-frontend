package org.tron.socket

import javax.inject.{Inject, Named, Provider}

import akka.NotUsed
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.stream.{Materializer, OverflowStrategy}
import akka.util.Timeout
import org.tron.cluster.ClusterListener.RegisterListener
import org.tron.socket.SocketCodecs._
import play.api.libs.json.JsString
import play.engineio.EngineIOController
import play.socketio.scaladsl.SocketIO

import scala.concurrent.duration._

class SocketIOEngine @Inject() (
  socket: SocketIO,
  actorMaterializer: Materializer,
  actorSystem: ActorSystem,
  @Named("cluster-listener") clusterListener: ActorRef) extends Provider[EngineIOController] {

  implicit val materializer = actorMaterializer
  implicit val system = actorSystem
  implicit val timeout = Timeout(15.seconds)


  def flow(): Flow[Any, Any, NotUsed] = {
    var ref: ActorRef = null

    val receiver = Sink.foreach[Any] {
      case x =>
        println("unhandled", x)
    }

    val source = Source.actorRef[Any](16, OverflowStrategy.dropHead)
      .mapMaterializedValue { actorRef =>
        ref = actorRef
        clusterListener ! RegisterListener(ref)
      }

    Flow.fromSinkAndSourceCoupled(receiver, source)
  }

  def get() = {
    socket.builder
      .withErrorHandler {
        case x: Exception =>
          println("Exception!", x)
          JsString("Exception: " + x.getMessage)
      }
      .onConnect { (request, sessionId) =>
        ""
      }
//      .addNamespace(decoder, encoder) {
//        case (data, namespace) =>
//          flow()
//      }
      .defaultNamespace(decoder, encoder, flow())
      .createController()
  }

}

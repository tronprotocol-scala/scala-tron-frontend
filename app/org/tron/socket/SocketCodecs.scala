package org.tron
package socket

import io.circe.generic.auto._
import play.socketio.scaladsl.SocketIOEventCodec._
import CirceDecoder._
import CirceEncoder._
import akka.actor.ActorRef
import org.tron.cluster.ClusterListener.ClusterState
import org.tron.socket.SocketProtocol.{JoinNetwork, MemberChanged, RequestClusterState}

object SocketProtocol {
  case class MemberChanged(member: String, status: String)
  case class JoinNetwork(address: String)
  case class RequestClusterState()
}


object SocketCodecs {


  val decoder = decodeByName {
    case "client-connected" =>
      fromJson[String]

    case "join-network" =>
      fromJson[String] andThen {
        case (address) => JoinNetwork(address)
      }

    case "cluster-request-state" =>
      decodeNoArgs andThen { _ =>
        RequestClusterState()
      }

    case x: String =>
      fromJson[String]
  }

  val encoder = encodeByType[Any] {
    case _: MemberChanged =>
      "member-changed" -> toJson[MemberChanged]
    case _: ClusterState =>
      "cluster-state" -> toJson[ClusterState]

//    case _: PlayersChanged =>
//      "players-changed" ->
//        toJson[PlayersChanged]
//
//    case _: PlayerChanged =>
//      "player-changed" -> (toJson[Player] compose[PlayerChanged] {
//          case PlayerChanged(player) => player
//        })
//
//    case _: RequestScreenChange =>
//      "screen-change" -> (toJson[String] compose[RequestScreenChange] {
//        case RequestScreenChange(slide) => slide
//      })
//
//    case x: String =>
//      "msg" -> toJson[String]
  }

}

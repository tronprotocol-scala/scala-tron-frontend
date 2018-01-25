package org.tron
package socket

import io.circe.generic.auto._
import play.socketio.scaladsl.SocketIOEventCodec._
import CirceDecoder._
import CirceEncoder._

case class MemberChanged(member: String, status: String)

object SocketCodecs {


  val decoder = decodeByName {
    case "client-connected" =>
      fromJson[String]

    case x: String =>
      fromJson[String]
  }

  val encoder = encodeByType[Any] {
    case _: MemberChanged =>
      "member-changed" -> toJson[MemberChanged]

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

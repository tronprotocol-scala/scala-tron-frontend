package org

import io.circe.{Decoder, DecodingFailure, Encoder, Json}
import play.api.http.{ContentTypeOf, ContentTypes, Writeable}
import play.api.libs.json.{JsObject, JsValue, Json => PlayJson}
import play.api.mvc.Codec

import scala.language.experimental.macros

package object tron {

  // Circe Conversions
  implicit def circeToPlayJson(json: io.circe.Json): JsValue = PlayJson.parse(json.noSpaces)
  implicit def circeToPlayJsonObj(json: io.circe.Json): JsObject = PlayJson.parse(json.noSpaces).as[JsObject]
  implicit def playObjToCirce(json: JsObject): io.circe.JsonObject = io.circe.parser.parse(PlayJson.stringify(json)).right.toOption.flatMap(_.asObject).get
  implicit def playToCirce(json: JsValue): Json = io.circe.parser.parse(PlayJson.stringify(json)).right.get

  implicit def circeToPlayJsonWrapper(json: io.circe.Json): PlayJson.JsValueWrapper = PlayJson.parse(json.noSpaces)

  implicit val jsonPlayDecoder: Decoder[JsValue] = Decoder.instance { cursor =>
    cursor.focus match {
      case Some(json) =>
        Right(PlayJson.parse(json.noSpaces))
      case _ =>
        Left(DecodingFailure("Play Json", cursor.history))
    }
  }

  implicit val jsonPlayEncoder: Encoder[JsValue] = Encoder.instance { cursor =>
    io.circe.parser.parse(PlayJson.stringify(cursor)).right.get
  }

  implicit val jsonObjectPlayEncoder: Encoder[JsObject] = Encoder.instance { cursor =>
    io.circe.parser.parse(PlayJson.stringify(cursor)).right.get
  }


  implicit def writeableOfCirceJson(implicit codec: Codec): Writeable[io.circe.Json] = {
    Writeable(data => codec.encode(data.noSpaces))
  }

  implicit def contentTypeCirceJson(implicit codec: Codec): ContentTypeOf[io.circe.Json] = {
    ContentTypeOf(Some(ContentTypes.JSON))
  }

}

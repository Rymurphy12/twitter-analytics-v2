package com.github.rymurphy12.analyzer

import org.http4s._
import org.http4s.client.blaze._
import org.http4s.client.oauth1
import cats.effect._
import fs2.{Stream, StreamApp}
import fs2.StreamApp.ExitCode
import fs2.io.stdout
import fs2.text.{lines, utf8Encode}
import jawnfs2._
import io.circe.Json
import com.typesafe.config.ConfigFactory
import org.joda.time.DateTime
import com.vdurmont.emoji._
import com.github.nscala_time.time.Imports._

object TWStream extends TWStreamApp[IO]{
  

}

abstract class TWStreamApp[F[_]: Effect] extends StreamApp[F]{

  implicit val f = io.circe.jawn.CirceSupportParser.facade

  def sign(consumerKey: String, consumerSecret: String, accessToken: String, accessSecret: String)(req: Request[F]): F[Request[F]] = {
    val consumer = oauth1.Consumer(consumerKey, consumerSecret)
    val token = oauth1.Token(accessToken, accessSecret)
    oauth1.signRequest(req, consumer, callback = None, verifier = None, token = Some(token))
  }

  def jsonStream(consumerKey: String, consumerSecret: String, accessToken: String, accessSecret: String)(req: Request[F]): Stream[F, Json] =
    for {
      client <- Http1Client.stream[F]()
      sr <- Stream.eval(sign(consumerKey, consumerSecret, accessToken, accessSecret)(req))
      res <- client.streaming(sr)(resp => resp.body.chunks.parseJsonStream)
    } yield res

  override def stream(args: List[String], requestShutdown: F[Unit], consumerKey: String, consumerSecret: String, accessToken: String, accessSecret: String): Stream[F, ExitCode] = {
    val req = Request[F](Method.GET, Uri.uri("https://stream.twitter.com/1.1/statuses/sample.json"))
    val s = jsonStream(consumerKey, consumerSecret, accessToken, accessSecret)(req)
    s.map(_.spaces2).through(lines).through(utf8Encode).to(stdout) >> Stream.emit(ExitCode.Success)
  }
}

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

abstract class
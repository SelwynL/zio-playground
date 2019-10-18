package org.selwyn.zioplayground.service.logging

import com.typesafe.scalalogging.Logger
import zio.{IO, URIO}

trait Logging {
  def logging: Logging.Service[Any]
}

object Logging {

  trait Service[R] {
    // From environment R, create Logger (Nothing denotes this operation can not fail!)
    // Note this is the same as ZIO[R, Nothing, Logger]
    def logger: URIO[R, Logger]
  }

  trait Live extends Logging {
    private def getLogger(clazz: Class[_]): Logger =
      Logger(clazz)

    val logging: Service[Any] = new Service[Any] {
      override def logger: URIO[Any, Logger] =
        // Note this is the same as ZIO.succeed()
        IO.succeed(getLogger(getClass))
    }
  }

  object Live extends Live
}

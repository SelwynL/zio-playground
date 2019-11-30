package org.selwyn.zioplayground.service

import zio.ZIO
import zio.console.Console

sealed trait State
final case class Stop()  extends State
final case class Start() extends State

trait Manager {
  def manager: Manager.Service[Any]
}

object Manager {

  trait Service[R] {
    // gets the state from the manager
    def state(id: String): ZIO[R, Nothing, State]
  }

  @SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
  trait Live extends Manager {

    // List Dependencies, forcing a "Live" implementation of Files to be provided
    val files: Files.Service[Any]
    val console: Console.Service[Any]

    final val manager: Service[Any] = new Service[Any] {

      // Grab state from file, failure to grab anything but a START results in a STOP
      // TODO: leverage "id"
      @SuppressWarnings(Array("org.wartremover.warts.Any"))
      override def state(id: String): ZIO[Any, Nothing, State] =
        files
          .read("status")
          .fold(
            err => {
              console.putStrLn(s"[ERROR] Unable to read 'status' file: ${err.getMessage}")
              Stop()
            },
            content => {
              console.putStrLn(s"[INFO] Read status content: ${content}")
              if (content.equalsIgnoreCase("START")) Start() else Stop()
            }
          )
    }
  }

  object Live extends Live with Files.Live with Console.Live
}

// Makes Manager.Service methods available in the ZIO environment "R"
package object manager extends Manager.Service[Manager] {

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  final val managerService: ZIO[Manager, Nothing, Manager.Service[Any]] =
    ZIO.access(_.manager)

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  final def state(id: String): ZIO[Manager, Nothing, State] =
    ZIO.accessM(_.manager.state(id))
}

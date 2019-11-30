package org.selwyn.zioplayground

import org.selwyn.zioplayground.service.{manager, Files, Manager}

import zio.{Schedule, ZIO, Runtime => ZRuntime}
import zio.clock.Clock
import zio.console.{putStrLn, Console}
import zio.duration._
import zio.internal.PlatformLive

@SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.NonUnitStatements"))
object CustomRuntimeEntry extends App {

  // The leveraged modules by the program (the traits used)
  type AppEnv = Console with Clock with Manager with Files

  // Description of the program to run, only defined by traits for effects
  val program: ZIO[AppEnv, Nothing, Unit] =
    (for {
      state <- manager.state("abc123")
      _     <- putStrLn(s"State: $state")
    } yield ()).repeat(Schedule.spaced(4.seconds)).unit

  // Define the runtime effects implementation, which associates implementations of the traits defined in "AppEnv"
  val live: AppEnv              = new Console.Live with Clock.Live with Manager.Live with Files.Live
  val runtime: ZRuntime[AppEnv] = ZRuntime(live, PlatformLive.Default)

  // Run the entire program with the "Live" implementation
  runtime.unsafeRun(program.map(_ => 0))
}

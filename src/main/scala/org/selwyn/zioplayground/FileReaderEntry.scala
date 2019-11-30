package org.selwyn.zioplayground

import org.selwyn.zioplayground.service.Files
import org.selwyn.zioplayground.service.files
import zio.clock.Clock
import zio.duration._
import zio.{Schedule, ZIO, Runtime => ZRuntime}
import zio.console.{putStrLn, Console}
import zio.internal.PlatformLive

@SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.NonUnitStatements"))
object FileReaderEntry extends App {

  val filename = ".gitignore"
  val timeout = 4.seconds

  // The leveraged modules by the program (the traits used)
  type AppEnv = Console with Clock with Files

  // Description of the program to run, only defined by traits for effects
  // TODO: repeat this to read continuous file changes, fail on error
  val program: ZIO[AppEnv, Throwable, Unit] =
    (for {
      contents <- files.read(filename)
      _        <- putStrLn(s"Contents:\n$contents\n--------------------------------------------------")
    } yield ()).repeat(Schedule.spaced(timeout)).unit

  // Define the runtime effects implementation, which associates implementations of the traits defined in "AppEnv"
  val live: AppEnv              = new Console.Live with Clock.Live with Files.Live
  val runtime: ZRuntime[AppEnv] = ZRuntime(live, PlatformLive.Default)

  // Run the entire program with the "Live" implementation
  val result: Int = runtime.unsafeRun(program.fold(err => {
    System.err.println(s"Error: $err")
    1
  }, _ => 0))

  // Exit according to the result of the program
  sys.exit(result)
}

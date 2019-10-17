package org.selwyn.zioplayground

import java.util.concurrent.TimeUnit

import zio.clock.Clock
import zio.{IO, Schedule, UIO, ZEnv, ZIO, App => ZApp}
import zio.console._
import zio.duration._
import zio.duration.Duration

@SuppressWarnings(Array("org.wartremover.warts.Any"))
object ScheduleEntry extends ZApp {

  type System = Console with Clock

  override def run(args: List[String]): ZIO[ZEnv, Nothing, Int] =
    program.fold(_ => 1, _ => 0)

  def getTime: UIO[String] =
    // The "effectTotal" returns ZIO[Any, Nothing, String] meaning an error can't happen
    // Using "effect" returns a similar type that determines an error CAN occur: ZIO[Any, Throwable, String]
    IO.effectTotal(Duration(System.nanoTime(), TimeUnit.NANOSECONDS).render)

  val spacedSchedule: Schedule[Any, Int] =
    Schedule.spaced(2.seconds)

  val program: ZIO[System, Nothing, Unit] = (for {
    time <- getTime
    _    <- putStrLn(s"Current time: $time")
  } yield ()).repeat(spacedSchedule).unit // Drop the returned repeat index, and return Unit instead
}

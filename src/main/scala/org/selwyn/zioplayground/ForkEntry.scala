package org.selwyn.zioplayground

import java.util.concurrent.TimeUnit

import zio.{URIO, ZEnv, ZIO, App => ZApp}
import zio.console._
import zio.clock.Clock
import zio.duration.Duration

@SuppressWarnings(Array("org.wartremover.warts.Any"))
object ForkEntry extends ZApp {

  val latency: Long                 = 120l
  val tasks: Int                    = 500
  val expectedLinearExecution: Long = latency * tasks

  override def run(args: List[String]): ZIO[ZEnv, Nothing, Int] = {
    val start = System.currentTimeMillis()
    program.fold(
      _ => 1,
      _ => {
        System.out.println(s"SUCCESS: Actual task execution time: ${System.currentTimeMillis() - start}ms")
        0
      }
    )
  }

  def sleep(i: Int, latency: Long): URIO[Clock, Int] =
    ZIO.sleep(Duration(latency, TimeUnit.MILLISECONDS)).map(_ => i)

  type Env = Console with Clock

  val program: ZIO[Env, Throwable, Unit] = for {
    // 274ms (effectTotal: 100), 64508ms (effectTotal: 10000), 628ms (effectBlocking: 100)
    values <- ZIO.forkAll((1 to tasks).toList.map(sleep(_, latency)))
    _      <- values.join
  } yield ()

}

//  // FUTURES
//  @SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
//  def run(): Unit = {
//    val pool        = Executors.newFixedThreadPool(processors)
//    implicit val ec = ExecutionContext.fromExecutor(pool)
//
//    // Requires ExecutionContext
//    def lift[T](futures: Seq[Future[T]]) =
//      futures.map(_.map { Success(_) }.recover { case t => Failure(t) })
//
//    // Requires ExecutionContext
//    def future(i: Int, latency: Long): Future[Int] =
//      Future { Thread.sleep(latency); i }
//
//    val executions: List[Future[Int]] = (1 to tasks).toList.map(future(_, latency))
//    val program: Future[List[Int]]    = Future.sequence(executions)
//
//    val values: List[Int] = Await.result(program, Duration("30 sec"))
//    System.out.println(s"Values: $values")
//    pool.shutdownNow()
//  }
//
//  val start: Long = System.currentTimeMillis()
//  run()
//  System.out.println(s"SUCCESS: Actual task execution time: ${System.currentTimeMillis() - start}ms")

package org.selwyn.zioplayground

import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.{Serde, Serdes}
import zio.{App => ZApp}
import zio.console._
import zio.duration._
import zio.kafka.client._
import zio.kafka.client.Consumer

@SuppressWarnings(
  Array(
    "org.wartremover.warts.Any",
    "org.wartremover.warts.NonUnitStatements"
  )
)
object KafkaEntry extends ZApp {

  val consumerSettings: ConsumerSettings =
    ConsumerSettings(
      bootstrapServers = List("localhost:9092"),
      groupId = "zio-group-1",
      clientId = "selwyn-test-client",
      closeTimeout = 30.seconds,
      extraDriverSettings = Map(),
      pollInterval = 250.millis,
      pollTimeout = 50.millis,
      perPartitionChunkPrefetch = 2
    )

  implicit val stringSerde: Serde[String] = Serdes.String()

  override def run(args: List[String]) =
    // Creates a kafka consumer that runs forever
    Consumer
      .consumeWith[Console, String, String](Subscription.Topics(Set("test")), consumerSettings) {
        case (key, value) => putStrLn(s"Received ${key}: ${value}")
      }
      .fold(_ => 1, _ => 0)
}

// override def run(args: List[String]) =
//   program.fold(_ => 1, _ => 0)
//
// val program =
//   for {
//     _ <- Consumer.make[String, String](consumerSettings).use { c =>
//       for {
//         _ <- c.subscribe(Subscription.Topics(Set("test"))).orDie
//         _ <- c.plainStream.flattenChunks.run(ZSink.collectAllWhileM({ i: CommittableRecord[String, String] =>
//           for {
//             _ <- putStrLn(s"KEY: ${i.record.key()}| VALUE: ${i.record.value()}")
//           } yield true
//         }))
//       } yield ()
//     }
//   } yield ()
//
//
// type System = Clock with Blocking with Console
// val subscription = Subscription.Topics(Set("test")
//
// def consume[R, K: Serde, V: Serde](
//     subscription: Subscription,
//     settings: ConsumerSettings
// )(f: List[(K, V)] => ZIO[R, Nothing, Unit]): ZIO[R with Clock with Blocking with Console, Throwable, Unit] =
//   Consumer.make[K, V](settings).use { consumer =>
//     consumer.subscribe(subscription).flatMap { _ =>
//       val readToEnd = for {
//         endOffsets <- consumer.assignment
//           .repeat(Schedule.doUntil(_.nonEmpty))
//           .flatMap(consumer.endOffsets(_, 1000.millis))
//         _          <- consumer.seekToBeginning(endOffsets.keys.toSet)
//         _          <- putStrLn(s"Loop Start: ${endOffsets.toString}")
//         finishLine <- Ref.make[Map[TopicPartition, Boolean]](endOffsets.keys.map(_ -> false).toMap)
//         _          <- finishLine.get.flatMap(fl => putStrLn(s"Finish Line Is: $fl"))
//         log <- consumer.plainStream.flattenChunks
//           .run(ZSink.collectAllWhileM { i: CommittableRecord[K, V] =>
//             for {
//               updatedFinishingLine <- finishLine.update(_ + (i.offset.topicPartition -> endOffsets.exists(e =>
//                 i.offset.topicPartition.equals(e._1) && i.offset.offset + 1 == e._2)))
//               _ <- putStrLn(s"Current Item: $i")
//               _ <- putStrLn(s"Updated Finishing Line: $updatedFinishingLine")
//             } yield !updatedFinishingLine.values.forall(b => b)
//           })
//           .map(_.map(r => (r.record.key(), r.record.value())))
//         _ <- f(log)
//       } yield ()
//       readToEnd.forever
//     }
//   }
//
//
// val runtime = new DefaultRuntime {}
// runtime.unsafeRun(
//   consume[Console, String, String](subscription, consumerSettings)({ items =>
//     putStrLn(s"Got items: items")
//   }).fold(error => {
//     putStrLn(s"ERROR: $error")
//     1
//   }, _ => 0)
// )
//
//
// val consumer = Consumer.make[String, String](consumerSettings)
//
// val program: ZIO[System, Nothing, Unit] = for {
//   // Consume messages
//   done             <- Promise.make[Nothing, Unit]
//   messagesReceived <- Ref.make(List.empty[(String, String)])
//   fib <- Consumer
//     .consumeWith[Any, String, String](
//       subscription,
//       consumerSettings
//     ) { (key, value) =>
//       (for {
//         messagesSoFar <- messagesReceived.update(_ :+ (key -> value))
//         _             <- Task.when(messagesSoFar.size == nrMessages)(done.succeed(()))
//       } yield ()).orDie
//     }
//     .fork
//   _ <- done.await
//   _ <- fib.interrupt
//   _ <- fib.join.ignore
// } yield ()
//
//
// consume(subscription).runDrain.fold(_ => 1, _ => 0)
// private def consume(subscription: Subscription): ZStream[System, Throwable, Unit] =
//   ZStream
//     .managed(Consumer.make[String, String](consumerSettings))
//     .flatMap(
//       consumer =>
//         ZStream
//           .fromEffect(consumer.subscribe(subscription))
//           .flatMap(_ =>
//             consumer.plainStream.flattenChunks
//               .mapM {
//                 case CommittableRecord(record, offset) =>
//                   putStrLn(record.value()).as(offset) // Fails on "as"
//               }
//               .aggregate(ZSink.foldLeft[Offset, OffsetBatch](OffsetBatch.empty)(_ merge _))
//               .mapM(_.commit)))
//
//
//  val program: ZIO[Any, Nothing, Unit] = for {
//    // Consume messages
//    done             <- Promise.make[Nothing, Unit]
//    messagesReceived <- Ref.make(List.empty[(String, String)])
//    fib <- Consumer
//      .consumeWith[Any, String, String](
//        subscription,
//        consumerSettings
//      ) { (key, value) =>
//        (for {
//          messagesSoFar <- messagesReceived.update(_ :+ (key -> value))
//          _             <- Task.when(messagesSoFar.size == nrMessages)(done.succeed(()))
//        } yield ()).orDie
//      }
//      .fork
//    _ <- done.await
//    _ <- fib.interrupt
//    _ <- fib.join.ignore
//  } yield ()
//
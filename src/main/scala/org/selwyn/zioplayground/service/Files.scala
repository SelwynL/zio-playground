package org.selwyn.zioplayground.service

import zio.{IO, Task, UIO, ZIO}

import scala.io.{BufferedSource, Source}

trait Files {
  def files: Files.Service[Any]
}

object Files {

  trait Service[R] {
    def read(path: String): ZIO[R, Throwable, String]
    def write(path: String, content: String): ZIO[R, Throwable, Unit]
  }

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  trait Live extends Files {
    final val files: Service[Any] = new Service[Any] {
      override def read(path: String): Task[String] = {

        // Describe a read file effect that can expect to thrown an IOException, otherwise bubble the error up and kill the process
        val openFile: Task[BufferedSource] = IO.effect(Source.fromFile(path))

        // Leverage bracket to always close the buffered file source after use
        openFile.bracket(f => UIO.succeed(f.close()), f => UIO.effectTotal(f.getLines().mkString("\n")))
      }

      override def write(path: String, content: String): Task[Unit] =
        // TODO: write "contents" to "path"
        Task.succeed(())
    }
  }
}

package object files extends Files.Service[Files] {

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  final val filesService: ZIO[Files, Nothing, Files.Service[Any]] =
    ZIO.access(_.files)

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  override def read(path: String): ZIO[Files, Throwable, String] =
    ZIO.accessM(_.files.read(path))

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  override def write(path: String, content: String): ZIO[Files, Throwable, Unit] =
    ZIO.accessM(_.files.write(path, content))
}

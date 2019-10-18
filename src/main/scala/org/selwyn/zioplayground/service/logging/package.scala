import com.typesafe.scalalogging.Logger
import org.selwyn.zioplayground.service.logging.Logging
import org.selwyn.zioplayground.service.logging.Logging.Service
import zio.{URIO, ZIO}

package object logging extends Service[Logging] {
  val logger: URIO[Logging, Logger] = ZIO.accessM(_.logging.logger)
}

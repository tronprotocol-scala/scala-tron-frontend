import org.tron.cluster.ClusterListener
import com.google.inject.AbstractModule
import org.tron.socket.SocketIOEngine
import play.api.libs.concurrent.AkkaGuiceSupport
import play.engineio.EngineIOController

class Module extends AbstractModule with AkkaGuiceSupport {
  def configure() {
    bindActor[ClusterListener]("cluster-listener")
    bind(classOf[EngineIOController]).toProvider(classOf[SocketIOEngine])
  }
}

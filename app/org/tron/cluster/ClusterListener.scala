package org.tron.cluster

import akka.actor.{Actor, ActorLogging, ActorRef, Address, Terminated}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import org.tron.cluster.ClusterListener.{JoinCluster, RegisterListener}

object ClusterListener {
  case class JoinCluster(hostname: String, port: Int)
  case class RegisterListener(actorRef: ActorRef)
}

class ClusterListener extends Actor with ActorLogging {

  val cluster = Cluster(context.system)

  var listeners = Map[String, ActorRef]()

  override def preStart() = {
    cluster.subscribe(
      self,
      initialStateMode = InitialStateAsEvents,
      classOf[MemberEvent],
      classOf[UnreachableMember])
  }

  override def postStop() = {
    cluster.unsubscribe(self)
  }

  def receive = {
    case MemberUp(member) ⇒
      log.info("Member is Up: {}", member.address)
    case UnreachableMember(member) ⇒
      log.info("Member detected as unreachable: {}", member)
    case MemberRemoved(member, previousStatus) ⇒
      log.info(
        "Member is Removed: {} after {}",
        member.address, previousStatus)
    case _: MemberEvent ⇒ // ignore

    case Terminated(ref) =>
      listeners -= ref.path.name

    case RegisterListener(ref) =>
      listeners += ref.path.name -> ref
      context.watch(ref)
    case JoinCluster(hostname, port) =>
      val seedNodeAddress = Address("akka.tcp", "TronCluster", hostname, port)

      println("JOINING CLUSTER AS FRONTEND")
      cluster.joinSeedNodes(List(seedNodeAddress))
  }
}

package org.tron.cluster

import akka.actor.{Actor, ActorLogging, ActorRef, Address, Terminated}
import akka.cluster.ClusterEvent._
import akka.cluster.MemberStatus.WeaklyUp
import akka.cluster.{Cluster, Member, MemberStatus}
import org.tron.cluster.ClusterListener._

object ClusterListener {
  case class JoinCluster(hostname: String, port: Int)
  case class RegisterListener(actorRef: ActorRef)
  case class MemberState(name: String, state: String, roles: Seq[String])
  case class RequestClusterState(ref: ActorRef)
  case class ClusterState(members: List[MemberState])
}

class ClusterListener extends Actor with ActorLogging {

  val cluster = Cluster(context.system)

  var listeners = Map[String, ActorRef]()

  var memberState = Map[String, Member]()

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

  def buildState = ClusterState(
    members = memberState.filter(_._2.status != WeaklyUp). map {
      case (address, member)=>
        MemberState(address, member.status.toString, member.roles.filter(_ != "dc-default").toList)
    }.toList
  )

  def broadcastState() = {
    val clusterState = buildState
    listeners.values.foreach(_ ! clusterState)
  }

  def updateMember(member: Member) = {
    memberState += member.address.toString -> member
    broadcastState()
  }

  def receive = {
    case event: MemberRemoved =>
      memberState -= event.member.address.toString
      broadcastState()

    case event: MemberEvent ⇒
      updateMember(event.member)

    case Terminated(ref) =>
      listeners -= ref.path.name

    case RegisterListener(ref) =>
      listeners += ref.path.name -> ref
      context.watch(ref)
      broadcastState()

    case JoinCluster(hostname, port) =>
      val seedNodeAddress = Address("akka.tcp", "TronCluster", hostname, port)

      println("JOINING CLUSTER AS FRONTEND")
      cluster.joinSeedNodes(List(seedNodeAddress))

    case RequestClusterState(ref) =>
      ref ! buildState
  }
}

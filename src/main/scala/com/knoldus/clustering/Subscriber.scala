package com.knoldus.clustering

import akka.actor.{Actor, ActorLogging}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{InitialStateAsEvents, MemberDowned, MemberEvent, MemberExited, MemberJoined, MemberLeft, MemberRemoved, MemberUp, MemberWeaklyUp, UnreachableMember}

class Subscriber extends Actor with ActorLogging {
  val cluster = Cluster(context.system)

  override def preStart(): Unit = {
    cluster.subscribe(
      self,
      initialStateMode =  InitialStateAsEvents,
      classOf[MemberEvent],
      classOf[UnreachableMember]
    )
  }

  override def postStop(): Unit = cluster.unsubscribe(self)

  override def receive: Receive = {
    case MemberJoined(member) => log.info(s"Member is joining with address ${member.address}")
    case MemberUp(member) => log.info(s"New member is up with address ${member.address}")
    case MemberWeaklyUp(member) => log.info(s"Member is Weakly up with address ${member.address}")
    case MemberLeft(member) => log.info(s"Member left the cluster with address ${member.address}")
    case MemberExited(member) => log.info(s"Member with status ${member.status} exited the cluster with address ${member.address}")
    case MemberDowned(member) => log.info(s"Member down with address ${member.address}")
    case MemberRemoved(member, previousState) => log.info(s"Member with address ${member.address} removed from cluster with previous state ${previousState.toString}")
    case _ => log.info("Unknown event found")
  }
}

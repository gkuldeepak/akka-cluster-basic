package com.knoldus.clustering

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

class ClusterInstantiator extends Actor with ActorLogging {

  override def receive: Receive = {
    case StartCluster(ports) =>
      log.info("\n\n\nInstantiating Cluster\n\n\n")
      def startCluster(ports: List[Int]) : Unit = {
        ports.foreach{
          port =>
            val config = ConfigFactory.parseString(
              s"""
                 |akka.remote.artery.canonical.port = $port
                 |""".stripMargin
            ).withFallback(ConfigFactory.load("clustering.conf"))
            val system = ActorSystem("knoldus", config) // Note: This actor name should be same with mentioned in conf file
            system.actorOf(Props[Subscriber])
        }
      }
      startCluster(ports)
  }

}

case class StartCluster(ports: List[Int])

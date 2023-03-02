package com.knoldus.clustering

import akka.actor.{ActorSystem, Props}

object Clusters_Basics extends App{

  val system = ActorSystem("Cluster_System")
  val actor= system.actorOf(Props[ClusterInstantiator])
  actor ! StartCluster(List(2551, 2552, 0, 0)) // Note: '0' is a magic number used to get the un-used ports

}
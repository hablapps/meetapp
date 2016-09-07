package org.hablapps.meetup
package fun
package mysql

import scala.concurrent.{Await, Future, duration, ExecutionContext}
import ExecutionContext.Implicits.global
import duration._

import logic._
import common.logic.Domain._
import common.mysql.Domain._

object FutureStore extends Store[Future] {
  import dbConfig._, driver.api._
  
  // Operadores de Store

  def getGroup(gid: Int): Future[Group] =
    db.run(group_table.byID(Some(gid)).result.head)  

  def getUser(uid: Int): Future[User] =
    db.run(user_table.byID(Some(uid)).result.head)
  
  def putJoin(join: JoinRequest): Future[JoinRequest] =
    db.run((join_table returning join_table.map(_.jid)
      into ((req,id) => req.copy(jid = id))) += join)  

  def putMember(member: Member): Future[Member] =
    db.run((member_table returning member_table.map(_.mid)
      into ((mem,id) => mem.copy(mid = id))) += member)

  // Operadores de composición
  
  def returns[A](a: A): Future[A] = Future(a)
  
  def doAndThen[A, B](fa: Future[A])(f: A => Future[B]): Future[B] = fa.flatMap(f)
}

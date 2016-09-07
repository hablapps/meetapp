package org.hablapps.meetup.oo.mysql

import scala.concurrent.{Await, Future, duration, ExecutionContext}
import ExecutionContext.Implicits.global
import duration._

import org.hablapps.meetup.oo.logic, logic._
import org.hablapps.meetup.common.logic.Domain._
import org.hablapps.meetup.common.mysql.Domain._

trait Store extends logic.Store{
  import dbConfig._, driver.api._

  def getGroup(gid: Int): Future[Group] = 
    db.run(group_table.byID(Some(gid)).result.head)
      .transform(identity, {
        case _: java.util.NoSuchElementException => 
          NonExistentEntity(gid)
      })
  
  def getGroup2(gid: Int): Group = {
    val f = db.run(group_table.byID(Some(gid)).result.head)
    Await.result(f, Duration.Inf)
  }
   
  def getUser(uid: Int): Future[User] =  
    db.run(user_table.byID(Some(uid)).result.head)
      .transform(identity, {
        case _: java.util.NoSuchElementException => 
          NonExistentEntity(uid)
      })
    
  def putJoin(join: JoinRequest): Future[JoinRequest] = 
    db.run((join_table returning join_table.map(_.jid)
                into ((req,id) => req.copy(jid = id))) += join)

  def putMember(member: Member): Future[Member] =
    db.run((member_table returning member_table.map(_.mid)
                into ((mem,id) => mem.copy(mid = id))) += member)









}



package org.hablapps.meetup.oo.logic

import scala.concurrent.Future

import org.hablapps.meetup.common.logic.Domain._

trait Store{

  def getGroup(gid: Int): Future[Group]
  

  def getUser(uid: Int): Future[User]
  

  def putJoin(join: JoinRequest): Future[JoinRequest]
  

  def putMember(member: Member): Future[Member]

}


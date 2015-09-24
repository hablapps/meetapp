package org.hablapps.meetup.oo.logic


import org.hablapps.meetup.common.logic.Domain._

trait Store{

  def getGroup(gid: Int): Group
  

  def getUser(uid: Int): User
  

  def putJoin(join: JoinRequest): JoinRequest
  

  def putMember(member: Member): Member

}




























sealed class StoreError(val msg: String) extends RuntimeException

case class NonExistentEntity(id: Int) extends StoreError(s"Non-existent entity $id")
case class GenericError(override val msg: String) extends StoreError(msg)


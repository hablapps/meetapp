package org.hablapps.meetup.store

import org.hablapps.meetup.domain._


trait Store{
  def getGroup(gid: Int): Group
  def getUser(uid: Int): User
  def putJoin(join: JoinRequest): JoinRequest
  def putMember(member: Member): Member
  def isMember(uid: Int, gid: Int): Boolean
  def isPending(uid: Int, gid: Int): Boolean
}

sealed class StoreError(val msg: String) extends RuntimeException
case class NonExistentEntity(id: Int) extends StoreError(s"Non-existent entity $id")
case class ConstraintFailed(constraint: String) extends StoreError(s"Constraint failed: $constraint")
case class GenericError(override val msg: String) extends StoreError(msg)

package org.hablapps.meetup.common.logic

object Domain{

  case class User(
    uid: Option[Int],
    name: String
  )

  case class Group(
    id: Option[Int], 
    name: String, 
    city: String,
    must_approve: Boolean
  )

  case class Member(
    mid: Option[Int],
    uid: Int,
    gid: Int
  )

  case class JoinRequest(
    jid: Option[Int],
    uid: Int,
    gid: Int
  )

  type JoinResponse = Either[JoinRequest, Member]

  sealed class StoreError(val msg: String) extends RuntimeException

  case class NonExistentEntity(id: Int) extends StoreError(s"Non-existent entity $id")
  case class GenericError(override val msg: String) extends StoreError(msg)

}
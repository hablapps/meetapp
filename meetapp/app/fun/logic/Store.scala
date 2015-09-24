package org.hablapps.meetup.fun.logic

import org.hablapps.meetup.common.logic.Domain._

object Store{
  
  def getGroup(id: Int): Store[Group] = 
    GetGroup(id, t => Return(t))
  
  def getUser(id: Int): Store[User] =  
    GetUser(id, t => Return(t))

  def putJoin(t: JoinRequest): Store[JoinRequest] = 
    PutJoin(t, t1 => Return(t1))

  def putMember(t: Member): Store[Member] = 
    PutMember(t, t1 => Return(t1))

  def If[U,V](cond: => Boolean)(_then: Store[V], _else: Store[U]): Store[Either[V,U]] = 
    if (cond) 
      _then map (u => Left(u))
    else
      _else map (v => Right(v))

  implicit class StoreOps[U](store: Store[U]){

    def flatMap[V](f: U => Store[V]): Store[V] = store match {
      case GetUser(id, next) => GetUser(id, next andThen (_ flatMap f))
      case GetGroup(id, next) => GetGroup(id, next andThen (_ flatMap f))
      case PutJoin(t, next) => PutJoin(t, next andThen (_ flatMap f))
      case PutMember(t, next) => PutMember(t, next andThen (_ flatMap f))
      case Return(t) => f(t)
    }

    def map[V](f: U => V): Store[V] = store match {
      case GetUser(id, next) => GetUser(id, next andThen (_ map f))
      case GetGroup(id, next) => GetGroup(id, next andThen (_ map f))
      case PutJoin(t, next) => PutJoin(t, next andThen (_ map f))
      case PutMember(t, next) => PutMember(t, next andThen (_ map f))
      case Return(t) => Return(f(t)) 
    }

  }

}

sealed trait Store[+U]

case class GetGroup[U](id: Int, next: Group => Store[U]) extends Store[U]
case class GetUser[U](id: Int, next: User => Store[U]) extends Store[U]
case class PutJoin[U](join: JoinRequest, next: JoinRequest => Store[U]) extends Store[U]
case class PutMember[U](id: Member, next: Member => Store[U]) extends Store[U]
case class Return[U](t: U) extends Store[U]

sealed class StoreError(val msg: String) extends RuntimeException

case class NonExistentEntity(id: Int) extends StoreError(s"Non-existent entity $id")
case class GenericError(override val msg: String) extends StoreError(msg)

  
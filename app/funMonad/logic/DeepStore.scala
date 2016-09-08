package org.hablapps.meetup
package funmonad
package logic

import common.logic.Domain._

sealed abstract class StoreProgram[A]
case class PutUser(user: User) extends StoreProgram[Int]
case class PutGroup(group: Group) extends StoreProgram[Int]
case class GetUser(uid: Int) extends StoreProgram[User]
case class GetGroup(gid: Int) extends StoreProgram[Group]
case class PutJoin(join: JoinRequest) extends StoreProgram[JoinRequest]
case class PutMember(member: Member) extends StoreProgram[Member]
case class Returns[U](value: U) extends StoreProgram[U]
case class Sequence[U,V](inst: StoreProgram[U], next: U => StoreProgram[V])
  extends StoreProgram[V]

object StoreProgram{

  implicit object StoreDeep extends Store[StoreProgram] {
    // Operadores de Store
    def putUser(user: User): StoreProgram[Int] = PutUser(user)
    def putGroup(group: Group): StoreProgram[Int] = PutGroup(group)
    def getGroup(gid: Int): StoreProgram[Group] = GetGroup(gid)
    def getUser(uid: Int): StoreProgram[User] = GetUser(uid)
    def putJoin(join: JoinRequest): StoreProgram[JoinRequest] = PutJoin(join)
    def putMember(member: Member): StoreProgram[Member] = PutMember(member)
  }

  implicit object StoreMonad extends Monad[StoreProgram]{
    // Operadores de Monad
    def pure[A](a: A): StoreProgram[A] = Returns(a)
    def flatMap[A, B](fa: StoreProgram[A])(f: A => StoreProgram[B]): StoreProgram[B] = Sequence(fa, f)
  }
}
  
  
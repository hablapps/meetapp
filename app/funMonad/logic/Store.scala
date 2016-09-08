package org.hablapps.meetup
package funmonad
package logic

import common.logic.Domain._

trait Store[F[_]]{
  def putUser(user: User): F[Int]
  def putGroup(group: Group): F[Int]
  def getGroup(gid: Int): F[Group]
  def getUser(uid: Int): F[User]
  def putJoin(join: JoinRequest): F[JoinRequest]
  def putMember(member: Member): F[Member]
}

object Store {

  object Syntax{
    def putUser[F[_]](user: User)(implicit S: Store[F]): F[Int] = S.putUser(user)
    def putGroup[F[_]](group: Group)(implicit S: Store[F]): F[Int] = S.putGroup(group)
    def getGroup[F[_]](gid: Int)(implicit S: Store[F]): F[Group] = S.getGroup(gid)
    def getUser[F[_]](uid: Int)(implicit S: Store[F]): F[User] = S.getUser(uid)
    def putJoin[F[_]](join: JoinRequest)(implicit S: Store[F]): F[JoinRequest] = S.putJoin(join)
    def putMember[F[_]](member: Member)(implicit S: Store[F]): F[Member] = S.putMember(member)
  }

}

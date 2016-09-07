package org.hablapps.meetup
package fun
package logic

import common.logic.Domain._

trait Store[F[_]]{
  def getGroup(gid: Int): F[Group]
  def getUser(uid: Int): F[User]
  def putJoin(join: JoinRequest): F[JoinRequest]
  def putMember(member: Member): F[Member]
  // combinators
  def doAndThen[A, B](f: F[A])(cont: A => F[B]): F[B]
  def returns[A](a: A): F[A]
  // derived combinators
  def map[A,B](f: F[A])(m: A=>B): F[B] = 
    doAndThen(f)(m andThen returns)
}

object Store {

  object Syntax{
    def getGroup[F[_]](gid: Int)(implicit S: Store[F]): F[Group] = S.getGroup(gid)
    def getUser[F[_]](uid: Int)(implicit S: Store[F]): F[User] = S.getUser(uid)
    def putJoin[F[_]](join: JoinRequest)(implicit S: Store[F]): F[JoinRequest] = S.putJoin(join)
    def putMember[F[_]](member: Member)(implicit S: Store[F]): F[Member] = S.putMember(member)
    def returns[F[_], A](a: A)(implicit S: Store[F]): F[A] = S.returns(a)
    
    // combinators
    implicit class StoreOps[F[_], A](f: F[A])(implicit S: Store[F]){
      def flatMap[B](cont: A => F[B]): F[B] = S.doAndThen(f)(cont)
      def map[B](m: A=>B): F[B] = S.doAndThen(f)(m andThen S.returns)
    }
  }

  import Syntax._
  
  def cond[F[_]: Store, X, Y](
    test: => Boolean,
    left: => F[X],
    right: => F[Y]): F[Either[X,Y]] = 
    if (test)
      left map (Left(_))
    else 
      right map (Right(_))
}

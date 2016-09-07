package org.hablapps.meetup
package fun
package logic
package templates

import common.logic.Domain._


/**
 * Reimplementación funcional de la versión convencional de la API Store
 * La primera diferencia es la parametrización del trait `Store`. 
 */
trait Store{
  
  // Los métodos de la API son ahora agnósticos respecto a la forma en la 
  // que se conseguirán sus resultados (Group, User, etc.)

  def getGroup(gid: Int): Group
  def getUser(uid: Int): User
  def putJoin(join: JoinRequest): JoinRequest
  def putMember(member: Member): Member

  // Además de los métodos propios de la interfaz, necesitamos
  // combinadores


  // Estos combinadores adicionales se pueden implementar a partir
  // de los primitivos


}

object Store {

  // Estos métodos nos permitirán acceder a la API sin necesidad de 
  // utilizar explícitamente una instancia de la API

  object Syntax{

    // API

    // def putUser[F[_]](user: User)(implicit S: Store[F]): F[Int] = ???
    // def putGroup[F[_]](group: Group)(implicit S: Store[F]): F[Int] = ???
    // def getGroup[F[_]](gid: Int)(implicit S: Store[F]): F[Group] = ???
    // def getUser[F[_]](uid: Int)(implicit S: Store[F]): F[User] = ???
    // def putJoin[F[_]](join: JoinRequest)(implicit S: Store[F]): F[JoinRequest] = ???
    // def putMember[F[_]](member: Member)(implicit S: Store[F]): F[Member] = ???

    // Combinadores

    // def returns[F[_], A](a: A)(implicit S: Store[F]): F[A] = ???
    
    // implicit class StoreOps[F[_], A](f: F[A])(implicit S: Store[F]){
    //   def flatMap[B](cont: A => F[B]): F[B] = ???
    //   def map[B](m: A=>B): F[B] = ???
    // }

    // def cond[F[_], X, Y](
    //   test: => Boolean,
    //   left: => F[X],
    //   right: => F[Y])(implicit S: Store[F]) = ???
  }

}

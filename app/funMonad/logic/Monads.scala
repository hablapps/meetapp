package org.hablapps.meetup
package funmonad
package logic

// Type class definition

trait Monad[M[_]]{
  def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]
  def pure[A](value: A): M[A]

  // derived

  def map[A, B](ma: M[A])(f: A => B): M[B] =
    flatMap(ma)(f andThen pure)

  def flatten[A](mma: M[M[A]]): M[A] =
    flatMap(mma)(identity)

  def cond[X, Y](test: => Boolean,
    left: => M[X], right: => M[Y]): M[Either[X,Y]] = 
    if (test)
      map(left)(Left(_))
    else 
      map(right)(Right(_))

}

object Monad {

  def apply[M[_]](implicit M: Monad[M]): Monad[M] = M

  // Syntax for the type class

  object Syntax{

    def pure[M[_],A](a: A)(implicit M: Monad[M]) = M.pure(a)

    def cond[M[_],A,B](test: => Boolean)(left: => M[A], right: => M[B])(
      implicit M: Monad[M]) = M.cond(test,left, right)

    implicit class InfixSyntax[M[_], A](ma: M[A])(implicit M: Monad[M]){

      def flatMap[B](f: A => M[B]): M[B] = 
        M.flatMap(ma)(f)

      def map[B](f: A => B): M[B] = 
        M.flatMap(ma)(s => M.pure(f(s)))
    }
  }

  // Common instances

  implicit object IdMonad extends Monad[Id]{
    def pure[A](a: A): A = a
    def flatMap[A,B](a: A)(f: A => B) = f(a)
  }

}

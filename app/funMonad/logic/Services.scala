package org.hablapps.meetup
package funmonad
package logic

import scalaz.syntax.monad._

import common.logic.Domain._

object Services{ 
  import Monad.Syntax._, Store.Syntax._

  def join[F[_]: Monad: Store](request: JoinRequest): F[JoinResponse] =
    for {
      _      <- getUser(request.uid) 
      group  <- getGroup(request.gid) 
      result <- cond(group.must_approve)( 
        left = putJoin(request), 
        right = putMember(Member(None, request.uid, request.gid)) 
      )
    } yield result

}


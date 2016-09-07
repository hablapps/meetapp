package org.hablapps.meetup
package fun
package logic

import common.logic.Domain._

object Services{ 
  
  import Store.Syntax._

  def join[F[_]: Store](request: JoinRequest): F[JoinResponse] =
    for {
      _      <- getUser(request.uid) 
      group  <- getGroup(request.gid) 
      result <- Store.cond(
        test = group.must_approve, 
        left = putJoin(request), 
        right = putMember(Member(None, request.uid, request.gid)) 
      )
    } yield result

}


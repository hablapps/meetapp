package org.hablapps.meetup.funz.logic

import org.hablapps.meetup.common.logic.Domain._

object Services{ 
  
  def join(request: JoinRequest): Store[JoinResponse] = for {
    _      <- Store.getUser(request.uid)
    group  <- Store.getGroup(request.gid)
    result <- Cond(
      test = group.must_approve,
      left = Store.putJoin(request),
      right = Store.putMember(Member(None, request.uid, request.gid))
    )
  } yield result 

}


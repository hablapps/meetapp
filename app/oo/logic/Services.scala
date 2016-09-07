package org.hablapps.meetup.oo.logic

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import org.hablapps.meetup.common.logic.Domain._

trait Services{ Store: Store => 
    
  def join(request: JoinRequest): Future[JoinResponse] = for {
    _ <- Store.getUser(request.uid)
    group <- Store.getGroup(request.gid)
    result <- if (group.must_approve)
      Store.putJoin(request) map (r => Left(r))
    else 
      Store.putMember(Member(None, request.uid, request.gid)) map (m => Right(m))
  } yield result

}


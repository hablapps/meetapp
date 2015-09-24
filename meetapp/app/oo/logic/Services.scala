package org.hablapps.meetup.oo.logic

import org.hablapps.meetup.common.logic.Domain._

trait Services{ Store: Store => 
    
  def join(request: JoinRequest): JoinResponse = {
    val _     = Store.getUser(request.uid)
    val group = Store.getGroup(request.gid)
    val result = Either.cond(
      test = group.must_approve, 
      left = Store.putJoin(request),
      right = Store.putMember(Member(None, request.uid, request.gid))
    )
    result
  }

}


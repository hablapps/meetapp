package org.hablapps.meetup.oo.logic

import org.hablapps.meetup.common.logic.Domain._

trait Services{ Store: Store => 
  
  def join(request: JoinRequest): JoinResponse = {
    val JoinRequest(_, uid, gid) = request
    
    val _ = Store.getUser(uid)
    val group = Store.getGroup(gid)
    val joinOrMember = 
      if (group.must_approve) 
        Left(Store.putJoin(request))
      else
        Right(Store.putMember(Member(None, uid, gid)))
    joinOrMember
  }

}


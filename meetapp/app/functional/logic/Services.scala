package org.hablapps.meetup.functional.logic

import Domain._

object Services{ 
  
  def join(request: JoinRequest): Store[JoinResponse] = {
    val JoinRequest(_, uid, gid) = request
    for{
      _ <- Store.getUser(uid)
      group <- Store.getGroup(gid)
      joinOrMember <- 
        Store.If(group.must_approve)(
          _then = Store.putJoin(request),
          _else = Store.putMember(Member(None, uid, gid)) 
        )
    } yield joinOrMember
  }

}


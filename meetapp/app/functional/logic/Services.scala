package org.hablapps.meetup.functional.logic

import Domain._

object Services{ 
  
  def join(request: JoinRequest): Store[JoinResponse] = {
    val JoinRequest(_, uid, gid) = request
    for{
      user <- Store.getUser(uid)
      group <- Store.getGroup(gid)
      joinOrMember <- 
        Store.If(!group.must_approve)(
          _then = Store.putMember(Member(None, uid, gid)), 
          _else = Store.putJoin(request) unless Store.isPending(uid, gid)
        ) unless Store.isMember(uid, gid)
    } yield joinOrMember
  }

}


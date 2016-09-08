package org.hablapps.meetup
package oo
package logic

import common.logic.Domain._

trait Services { S: Store =>
    
  def join(request: JoinRequest): JoinResponse = {
    val _      = S.getUser(request.uid)
    val group  = S.getGroup(request.gid) 
    val result = Either.cond(
      test  = group.must_approve, 
      left  = S.putJoin(request), 
      right = S.putMember(Member(None, request.uid, request.gid))
    )
    result
  }

  
  // Esta versión muestra más claramente la correspondencia
  // con la primera versión funcional (ServicesWithoutSugar)
  def joinv2(request: JoinRequest): JoinResponse = {
    val user   = S.getUser(request.uid);
    val group  = S.getGroup(request.gid);
    if (group.must_approve) {
      val regJoin = S.putJoin(request)
      Left(regJoin)
    } else {
      val regMember = S.putMember(Member(None, request.uid, request.gid))
      Right(regMember)
    }
  }


}

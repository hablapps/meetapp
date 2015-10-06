package org.hablapps.meetup.fun.logic

import org.hablapps.meetup.common.logic.Domain._

object Services{ 
  
  def joinRaw(request: JoinRequest): Store[JoinResponse] = 
    StoreAndThen(GetUser(request.uid), (user: User) => 
      StoreAndThen(GetGroup(request.gid), (group: Group) => 
        if (group.must_approve)
          StoreAndThen(PutJoin(request), (request: JoinRequest) => 
            Return(Left(request))
          )
        else {
          val new_member = Member(None, request.uid, request.gid)
          StoreAndThen(PutMember(new_member), (member: Member) => 
            Return(Right(member))
          )
        }
      )
    )

  def join(request: JoinRequest): Store[JoinResponse] = for {
    _      <- Store.getUser(request.uid)
    group  <- Store.getGroup(request.gid)
    result <- Store.Cond(
      test = group.must_approve,
      left = Store.putJoin(request),
      right = Store.putMember(Member(None, request.uid, request.gid))
    )
  } yield result 

}


package org.hablapps.meetup
package oo
package logic

import common.logic.Domain._

trait Store {

  def getGroup(gid: Int): Group
  def getUser(uid: Int): User
  def putJoin(join: JoinRequest): JoinRequest
  def putMember(member: Member): Member

}


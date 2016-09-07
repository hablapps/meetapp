package org.hablapps.meetup
package fun
package play

import fun.logic, logic._
import common.logic.Domain._
import common.mysql.MySqlDomain._

import _root_.play.api.db.slick.DB
import _root_.play.api.Play.current

import scala.slick.driver.MySQLDriver.simple.{Sequence => _, _}


object DeepIdInterpreter{

  def run[U](program: StoreProgram[U]): U = 
    program match {
      case PutUser(user: User)        => IdStore.putUser(user)
      case PutGroup(group: Group)     => IdStore.putGroup(group)
      case GetGroup(gid: Int)         => IdStore.getGroup(gid)
      case GetUser(uid: Int)          => IdStore.getUser(uid)
      case PutJoin(join: JoinRequest) => IdStore.putJoin(join)
      case PutMember(member: Member)  => IdStore.putMember(member)
      case Returns(value)             => IdStore.returns(value)
      case Sequence(program, next)    => IdStore.doAndThen(run(program))(next andThen run)
    }

}

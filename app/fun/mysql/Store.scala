package org.hablapps.meetup.fun.mysql

import org.hablapps.meetup.fun.logic, logic._
import org.hablapps.meetup.common.logic.Domain._
import org.hablapps.meetup.common.mysql.Domain._

import play.api.db.slick.DB
import play.api.Play.current

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import scala.slick.driver.MySQLDriver.simple._


object Interpreter{

  def runInstruction[U](instruction: StoreInstruction[U]): U =
    instruction match {
      
      case GetGroup(gid: Int) => 
        DB.withSession { implicit session =>
          group_table.byID(Some(gid)).firstOption.get
        }
      
      case GetUser(uid: Int) =>
        DB.withSession { implicit session =>
          user_table.byID(Some(uid)).firstOption.get
        }
    
      case PutJoin(join: JoinRequest) => 
        DB.withSession { implicit session =>
          val maybeId = join_table returning join_table.map(_.jid) += join
          join.copy(jid = maybeId)
        }

      case PutMember(member: Member) =>
        DB.withSession { implicit session =>
          val maybeId = member_table returning member_table.map(_.mid) += member
          member.copy(mid = maybeId)
        }

    }

  def run[U](store: Store[U]): U = store match {
    case Return(value) => 
      value
    case StoreAndThen(instruction, next) => 
      val result = runInstruction(instruction)
      run(next(result))
  }

}

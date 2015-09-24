package org.hablapps.meetup.fun.mysql

import org.hablapps.meetup.fun.logic, logic._
import org.hablapps.meetup.common.logic.Domain._
import org.hablapps.meetup.common.mysql.Domain._

import play.api.db.slick.DB
import play.api.Play.current

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import scala.slick.driver.MySQLDriver.simple._


object Interpreter{

  def run[U](store: Store[U]): U = store match {

    case GetGroup(gid: Int, next: (Group => Store[U])) => 
      DB.withSession { implicit session =>
        val maybeGroup = (for { 
          group <- group_table if group.gid === gid
        } yield group).firstOption
        run(next(maybeGroup.get))
      }

    case GetUser(uid: Int, next: (User => Store[U])) => 
      DB.withSession { implicit session =>
        val maybeUser = (for {
          user <- user_table if user.uid === uid
        } yield user).firstOption
        run(next(maybeUser.get))
      }

    case PutJoin(join: JoinRequest, next: (JoinRequest => Store[U])) => 
      DB.withSession { implicit session =>
        val maybeId = join_table returning join_table.map(_.jid) += join
        run(next(join.copy(jid = maybeId)))
      }

    case PutMember(member: Member, next: (Member => Store[U])) => 
      DB.withSession { implicit session =>
        val maybeId = member_table returning member_table.map(_.mid) += member
        run(next(member.copy(mid = maybeId)))
      }

    case Return(result) => 
      result
  }

}

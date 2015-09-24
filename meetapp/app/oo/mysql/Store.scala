package org.hablapps.meetup.oo.mysql

import org.hablapps.meetup.oo.logic, logic._
import org.hablapps.meetup.common.logic.Domain._
import org.hablapps.meetup.common.mysql.Domain._

import play.api.Play.current
import play.api.db.slick.DB

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import scala.slick.driver.MySQLDriver.simple._


trait Store extends logic.Store{



  def getGroup(gid: Int): Group = 
    DB.withSession { implicit session =>
      val maybeGroup = (for { 
        group <- group_table if group.gid === gid
      } yield group).firstOption
      maybeGroup.get
    }
   
  def getUser(uid: Int): User =  
    DB.withSession { implicit session =>
      val maybeUser = (for {
        user <- user_table if user.uid === uid
      } yield user).firstOption
      maybeUser.get
    }
    
  def putJoin(join: JoinRequest): JoinRequest = 
    DB.withSession { implicit session =>
      val maybeId = join_table returning join_table.map(_.jid) += join
      join.copy(jid = maybeId)
    }

  def putMember(member: Member): Member = 
    DB.withSession { implicit session =>
      val maybeId = member_table returning member_table.map(_.mid) += member
      member.copy(mid = maybeId)
    }



}



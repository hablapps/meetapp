package org.hablapps.meetup.store.mysql

import org.hablapps.meetup.{domain, store}, domain._, store._
import domain.mysql._

import play.api.Play.current
import play.api.db.slick.DB

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import scala.slick.driver.MySQLDriver.simple._


trait MySQLStore extends Store{

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

  def isMember(uid: Int, gid: Int): Boolean = 
    DB.withSession { implicit session =>
      true
    }

  def isPending(uid: Int, gid: Int): Boolean = 
    DB.withSession { implicit session =>
      true
    }
}



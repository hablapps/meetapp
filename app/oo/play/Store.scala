package org.hablapps.meetup
package oo
package play

import logic._
import common.logic.Domain._
import common.mysql.MySqlDomain._

import _root_.play.api.Play.current
import _root_.play.api.db.slick.DB

import scala.slick.driver.MySQLDriver.simple._


trait Store extends logic.Store {

  def getGroup(gid: Int): Group = 
    DB.withSession { implicit session =>
      group_table.byID(Some(gid)).firstOption.get
    }
   
  def getUser(uid: Int): User =  
    DB.withSession { implicit session =>
      user_table.byID(Some(uid)).firstOption.get
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

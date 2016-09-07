package org.hablapps.meetup
package fun
package play
package templates

import logic._
import common.logic.Domain._
import common.mysql.MySqlDomain._

import _root_.play.api.db.slick.DB
import _root_.play.api.Play.current

import scala.slick.driver.MySQLDriver.simple.{Sequence => _, _}

/**
 * Los únicos cambios que hay que realizar al intérprete tienen 
 * que ver con la parametrización de la API, y la implementación 
 * de los combinadores
 */
object Interpreter extends oo.logic.Store{
  
  // API

  def putUser(user: User): Int =
    DB.withSession { implicit session =>
      val maybeId = user_table returning user_table.map(_.uid) += user
      maybeId.get
    }
  
  def putGroup(group: Group): Int =
    DB.withSession { implicit session =>
      val maybeId = group_table returning group_table.map(_.gid) += group
      maybeId.get
    }

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

  // Operadores de composición
  
  
}


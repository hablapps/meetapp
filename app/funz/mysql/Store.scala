package org.hablapps.meetup.funz.mysql

import org.hablapps.meetup.funz.logic, logic._
import org.hablapps.meetup.common.logic.Domain._
import org.hablapps.meetup.common.mysql.Domain._

import play.api.db.slick.DB
import play.api.Play.current

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import scala.slick.driver.MySQLDriver.simple._

import scalaz.{Store => ScalazStore, _}, Scalaz._

object Interpreter{

  object runInstruction extends (StoreInstruction ~> Id) {
    def apply[T](instruction: StoreInstruction[T]): T = 
      DB.withSession { implicit session =>
        instruction match {
          case GetGroup(gid: Int) => 
            group_table.byID(Some(gid)).firstOption.get
        
          case GetUser(uid: Int) =>
            user_table.byID(Some(uid)).firstOption.get
      
          case PutJoin(join: JoinRequest) => 
            val maybeId = join_table returning join_table.map(_.jid) += join
            join.copy(jid = maybeId)
        
          case PutMember(member: Member) =>
            val maybeId = member_table returning member_table.map(_.mid) += member
            member.copy(mid = maybeId)
        }
      } 
  }

  def run[U](store: Store[U]): U = 
    store.foldMap(runInstruction)

}

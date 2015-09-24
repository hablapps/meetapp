package org.hablapps.meetup.funz.mysqlErrors

import org.hablapps.meetup.common.logic.Domain._
import org.hablapps.meetup.common.mysql.Domain._
import org.hablapps.meetup.funz.logic, logic._

import play.api.db.slick.DB
import play.api.Play.current

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import scala.slick.driver.MySQLDriver.simple._

import scalaz.{Store => ScalazStore, _}, Scalaz._

object Interpreter{

  type Error[x] = \/[StoreError,x]

  case class RunInstruction(implicit session: Session) 
    extends (StoreInstruction ~> Error) {
  
    def apply[T](instruction: StoreInstruction[T]): \/[StoreError,T] = instruction match {
      
      case GetGroup(gid: Int) => 
        group_table.byID(Some(gid)).firstOption.fold[\/[StoreError,Group]](
          -\/(NonExistentEntity(gid))
        )(\/.right)

      case GetUser(uid: Int) =>
        user_table.byID(Some(uid)).firstOption.fold[\/[StoreError,User]](
          -\/(NonExistentEntity(uid))
        )(\/.right)
    
      case PutJoin(join: JoinRequest) => 
        val maybeId = join_table returning join_table.map(_.jid) += join
        \/-(join.copy(jid = maybeId))

      case PutMember(member: Member) =>
        val maybeId = member_table returning member_table.map(_.mid) += member
        \/-(member.copy(mid = maybeId))

    }
  }

  def run[T](store: Store[T]): \/[StoreError, T] = 
    DB.withSession { implicit session =>
      store.foldMap(RunInstruction.apply)
    }

}

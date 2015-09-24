package org.hablapps.meetup.fun.mysql

import scala.reflect.{ClassTag, classTag}
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.lifted.CompiledFunction
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException

import play.api.db.slick.DB
import play.api.Play.current

import org.hablapps.meetup.fun.logic
import logic._
import org.hablapps.meetup.common.logic.Domain._
import org.hablapps.meetup.common.mysql.Domain._


object Interpreter{

  def run[U](store: Store[U]): Either[StoreError, U] = store match {
    case Return(result) => 
      Right(result)
    case Fail(error) => 
      Left(error)
    case GetGroup(id: Int, next: (Group => Store[U])) => 
      DB.withSession { implicit session =>
        group_table.byID(Some(id)).firstOption.fold[Either[StoreError,U]](
          Left[StoreError,U](NonExistentEntity(id))
        ){
          entity => run(next(entity))
        }
      }
    case GetUser(id: Int, next: (User => Store[U])) => 
      // Thread.sleep(100)
      DB.withSession { implicit session =>
        user_table.byID(Some(id)).firstOption.fold[Either[StoreError,U]](
          Left[StoreError,U](NonExistentEntity(id))
        ){
          entity => run(next(entity))
        }
      }
    case PutMember(member: Member, next: (Member => Store[U])) => 
      DB.withSession { implicit session =>
        val mid: Either[StoreError, Int] = try{
          val maybeId = member_table returning member_table.map(_.mid) += member
          maybeId.fold[Either[StoreError,Int]](Left(GenericError(s"Could not put new member $member")))(Right(_))
        } catch {
          case e : MySQLIntegrityConstraintViolationException => 
            Left(GenericError(s"Could not put new member $member"))
        }
        mid.right
          .map(id => run(next(member.copy(mid = Some(id)))))
          .joinRight
      }
    case PutJoin(join: JoinRequest, next: (JoinRequest => Store[U])) => 
      DB.withSession { implicit session =>
        val jid: Either[StoreError, Int] = try{
          val maybeId = join_table returning join_table.map(_.jid) += join
          maybeId.fold[Either[StoreError,Int]](
            Left(GenericError(s"Could not put new join $join")))(
            Right(_)
          )
        } catch {
          case e : MySQLIntegrityConstraintViolationException => 
            Left(GenericError(s"Constraint violation: $e"))
        }
        jid.right
          .map(id => run(next(join.copy(jid = Some(id)))))
          .joinRight
      }
    case _ => 
      Left(new StoreError("unevaluated yet"))
  }

}
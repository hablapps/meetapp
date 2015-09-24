package org.hablapps.meetup.funz.mysqlAsync

import scala.concurrent.{ExecutionContext, Future}

import org.hablapps.meetup.common.logic.Domain._
import org.hablapps.meetup.common.mysql.Domain._
import org.hablapps.meetup.funz.logic, logic._

import play.api.db.slick.DB
import play.api.Play.current

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import scala.slick.driver.MySQLDriver.simple._

import scalaz.{Store => ScalazStore, _}, Scalaz._

object Interpreter{

  type WrongOrRight[x] = \/[StoreError, x]
  type AsyncResponse[x] = EitherT[Future,StoreError,x]

  case class RunInstruction(implicit session: Session, ec: ExecutionContext) 
    extends (StoreInstruction ~> AsyncResponse) {
  
    def apply[T](instruction: StoreInstruction[T]): AsyncResponse[T] = instruction match {
      
      case GetGroup(gid: Int) => 
        EitherT(
          Future(group_table
            .byID(Some(gid))
            .firstOption
            .fold[\/[StoreError,Group]](-\/(NonExistentEntity(gid)))(\/.right)
          )
        )

      case GetUser(uid: Int) =>
        EitherT(
          Future(user_table
            .byID(Some(uid))
            .firstOption
            .fold[WrongOrRight[User]](-\/(NonExistentEntity(uid)))(\/.right)
          )
        )
    
      case PutJoin(join: JoinRequest) => 
        val maybeId = join_table returning join_table.map(_.jid) += join
        EitherT(
          Future[\/[StoreError,JoinRequest]](
            \/-(join.copy(jid = maybeId))
          )
        )

      case PutMember(member: Member) =>
        val maybeId = member_table returning member_table.map(_.mid) += member
        EitherT(
          Future[\/[StoreError, Member]](
            \/-(member.copy(mid = maybeId))
          )
        )

    }
  }

  def run[T](store: Store[T]): Future[\/[StoreError, T]] = 
    DB.withSession { implicit session =>
      implicit val ec = akka.actor.ActorSystem().dispatchers.lookup("blocking-dispatcher")
      store.foldMap(RunInstruction.apply).run
    }

}

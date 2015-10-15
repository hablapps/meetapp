package org.hablapps.meetup.funz.mysqlAsync

import scala.util.{Try, Success, Failure}
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global

import scalaz._, Scalaz._

import play.api._
import play.api.mvc._
import play.api.db.slick._
import play.api.libs.json._

import play.api.db.slick.DB
import play.api.Play.current

import org.hablapps.meetup.common.logic.Domain._
import org.hablapps.meetup.funz.logic, logic._

object Members extends Controller{

  implicit val ec = akka.actor.ActorSystem().dispatchers.lookup("blocking-dispatcher")
  
  def add(gid: Int): Action[Int] =
    DB.withSession( implicit s => 
      Action.async(parse.json[Int]) { 
        fromHTTP(gid)         andThen 
        logic.Services.join   andThen
        Interpreter.run       andThen
        toHTTP
      }
    )

  def fromHTTP(gid: Int): Request[Int] => JoinRequest = 
    request => JoinRequest(None, request.body, gid)

  def toHTTP(response: Future[JoinResponse]): Future[Result] = ???
  //   response match {
  //     case Failure(error@NonExistentEntity(id)) => 
  //       NotFound(s"${error.msg}")
  //     case Failure(error) => 
  //       InternalServerError(error.toString)
  //     case Success(response) => response fold(
  //       joinRequest => 
  //         Accepted(s"Join request $joinRequest, left pending for futher processing"),
  //       member => 
  //         Created(Json.toJson(member)(Json.writes[Member]))
  //     )
  //   }

}
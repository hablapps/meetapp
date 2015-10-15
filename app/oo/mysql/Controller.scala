package org.hablapps.meetup.oo.mysql

import scala.util.{Try, Success, Failure}

import play.api._
import play.api.mvc._
import play.api.libs.json._

import org.hablapps.meetup.common.logic.Domain._
import org.hablapps.meetup.oo.logic, logic._

object Members extends Controller{

  def add(gid: Int): Action[Int] =
    Action(parse.json[Int]) { 
      fromHTTP(gid) andThen
      (joinRequest => 
        Try(Services.join(joinRequest))) andThen
      toHTTP
    }

  def fromHTTP(gid: Int): Request[Int] => JoinRequest = 
    request => JoinRequest(None, request.body, gid)

  def toHTTP(response: Try[JoinResponse]): Result = 
    response match {
      case Failure(error@NonExistentEntity(id)) => 
        NotFound(s"${error.msg}")
      case Failure(error) => 
        InternalServerError(error.toString)
      case Success(response) => response fold(
        joinRequest => 
          Accepted(s"Join request $joinRequest, left pending for futher processing"),
        member => 
          Created(Json.toJson(member)(Json.writes[Member]))
      )
    }

}
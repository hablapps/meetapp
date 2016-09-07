package org.hablapps.meetup.common

import scala.util.{Try, Success, Failure}

import play.api._
import play.api.mvc._
import play.api.libs.json._

import logic.Domain._

trait CommonController extends Controller{

  def fromHTTP(gid: Int): Request[Int] => JoinRequest = 
    request => JoinRequest(None, request.body, gid)

  def toHTTP(response: Try[JoinResponse]): Result = 
    response match {
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
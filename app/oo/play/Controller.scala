package org.hablapps.meetup
package oo
package play

import scala.util.{Try, Success, Failure}

import _root_.play.api._, mvc._, libs.json._

import common.logic.Domain._, logic._

object Members extends Controller with common.CommonController{

  def add(gid: Int): Action[Int] =
    Action(parse.json[Int]) { 
      fromHTTP(gid) andThen
      (joinRequest => Try(Services.join(joinRequest))) andThen
      toHTTP
    }
    
}
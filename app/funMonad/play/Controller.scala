package org.hablapps.meetup
package funmonad
package play

import scala.util.{Try, Success, Failure}

import _root_.play.api._, mvc._, libs.json._

import common.logic.Domain._
import logic._

object Members extends Controller with common.CommonController{

  // Deep embedding

  def add(gid: Int): Action[Int] =
    Action(parse.json[Int]) {
      fromHTTP(gid)                                    andThen
      Services.join[StoreProgram]                      andThen
      (program => Try(DeepIdInterpreter.run(program))) andThen
      toHTTP
    }

  // Shallow embedding

  implicit val _ = IdStore

  def add2(gid: Int): Action[Int] =
    Action(parse.json[Int]) {
      fromHTTP(gid)                                andThen
      (request => Try(Services.join[Id](request))) andThen
      toHTTP
    }

}
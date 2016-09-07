package org.hablapps.meetup
package oo
package mysql

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import play.api._
import play.api.mvc._
import play.api.libs.json._

import common.logic.Domain._, common.mysql.CommonController
import logic._

object Members extends Controller with CommonController{

  def add(gid: Int): Action[Int] =
    Action.async(parse.json[Int]) { 
      fromHTTP(gid) andThen
      Services.join andThen
      toHTTP
    }
}
// package org.hablapps.meetup.fun.mysqlError

// import play.api._
// import play.api.mvc._
// import play.api.libs.json._
// import play.api.Play.current
// import play.api.db.slick.DB

// import scala.concurrent.Future
// import scala.concurrent.ExecutionContext
// import play.api.libs.concurrent.Akka

// import org.hablapps.meetup.common.logic.Domain._
// import org.hablapps.meetup.fun.logic, logic._

// object Members extends Controller{

//   def add(gid: Int): Action[Int] =
//     Action(parse.json[Int]) { 
//       fromHTTP(gid)         andThen 
//       logic.Services.join   andThen
//       Interpreter.run andThen
//       toHTTP
//     }

//   def fromHTTP(gid: Int): Request[Int] => JoinRequest = 
//     request => JoinRequest(None, request.body, gid)

//   def toHTTP(response: Either[StoreError, JoinResponse]): Result = 
//     response fold(
//       error => error match {
//         case error@NonExistentEntity(id) => 
//           NotFound(s"${error.msg}")
//         case error => 
//           InternalServerError(error.msg)
//       },
//       success => success fold(
//         joinRequest => 
//           Accepted(s"Join request $joinRequest, left pending for futher processing"),
//         member => 
//           Created(Json.toJson(member)(Json.writes[Member]))
//       )
//     )

// }
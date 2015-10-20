package org.hablapps.meetup.fun.logic.testing

import org.scalatest._

import org.hablapps.meetup.common.logic._, Domain._
import org.hablapps.meetup.fun.logic._, Services._

class LogicSpec(tester: Store.Interpreter) extends FlatSpec with Matchers {

  "unirse a un grupo" should "devolver un error si el usuario no existe" in {
    tester.run(join(JoinRequest(None, 1, 2))) should be(Left(NonExistentEntity(1)))
  }

//   it should "devolver un error si el grupo no existe" in {
//     val store1 = Interpreter.MapStore(
//       User(Some(1), "user 1")
//     )

//     Interpreter.run(store1)(join(JoinRequest(None, 1, 2))) should be(Left(NonExistentEntity(2))) 
//   }
  
//   "unirse a un grupo sin restricciones de entrada" should     
//     "realizarse inmediatamente si el usuario no pertenece ya al mismo" in {

//     val store1 = Interpreter.MapStore(
//       User(Some(1), "user 1"),
//       Group(Some(2), "group 1", "CR", false)
//     )

//     Interpreter.run(store1)(join(JoinRequest(None, 1, 2))) should 
//       be(Right(Right(Member(Some(3),1,2))))
//   }

//   it should "prohibirse si el usuario pertenece ya" in {

//     val store1 = Interpreter.MapStore(
//       User(Some(1), "user 1"),
//       Group(Some(2), "group 1", "CR", false),
//       Member(Some(3), 1, 2)
//     )

//     Interpreter.run(store1)(join(JoinRequest(None, 1, 2))) should 
//       be(Left(GenericError(s"already exists Member(None,1,2)")))
//   }

//   "unirse a un grupo con restricciones de entrada" should   
//     "dejarse pendiente si el usuario no pertenece ya y no estaba pendiente ninguna otra solicitud" in {

//     val store1 = Interpreter.MapStore(
//       User(Some(1), "user 1"),
//       Group(Some(2), "group 1", "CR", true)
//     )

//     Interpreter.run(store1)(join(JoinRequest(None, 1, 2))) should 
//       be(Right(Left(JoinRequest(Some(3),1,2))))
//   }

//   it should "prohibirse si el usuario no pertenece ya, pero tiene una solicitud pendiente" in {

//     val store1 = Interpreter.MapStore(
//       User(Some(1), "user 1"),
//       Group(Some(2), "group 1", "CR", true),
//       JoinRequest(Some(3), 1, 2)
//     )

//     Interpreter.run(store1)(join(JoinRequest(None, 1, 2))) should
//       be(Left(GenericError(s"already pending JoinRequest(None,1,2)")))
//   }

// // "unirse a un grupo" should "devolver un error si el usuario no existe" in {
//   //   val store1 = Interpreter.MapStore()
  
//   //   an[NonExistentEntity] should be 
//   //     thrownBy(Interpreter.run(store1)(join(JoinRequest(None, 1, 2))))
//   // }

//   // it should "devolver un error si el grupo no existe" in {
//   //   val store1 = Interpreter.MapStore(
//   //     User(Some(1), "user 1")
//   //   )

//   //   an[NonExistentEntity] should be 
//   //     thrownBy(Interpreter.run(store1)(join(JoinRequest(None, 1, 2)))) 
//   //   //   Interpreter.run(store1)(join(JoinRequest(None, 1, 2))) must_== Left(NonExistentEntity(2))
//   // }

}

package org.hablapps.meetup.fun.testing

import scala.reflect.{ClassTag, classTag}
import org.hablapps.meetup.fun.logic._
import org.hablapps.meetup.common.logic._, Domain._

object Interpreter{

  sealed abstract class MapError(val msg: String) extends Exception
  
  case class WrongType(id: Int) extends MapError(
    s"Entity $id exists but its type is not right")

  case class MapStore(map: Map[Int, Any], next: Int){

    def get[T: ClassTag](id: Int): Either[StoreError, T] = 
      map.get(id).fold[Either[StoreError,T]](Left(NonExistentEntity(id))){
        entity => if (!classTag[T].runtimeClass.isInstance(entity))
          Left(WrapError(WrongType(id)))
        else 
          Right(entity.asInstanceOf[T])
      }

    def checkNotMember(member: Member): Either[StoreError, Unit] = {
      val exists = map.exists{ _ match{
        case (_, Member(_, member.uid, member.gid)) => true
        case _ => false
      }}
      if (exists) Left(GenericError(s"already exists $member"))
      else Right(())
    }

    def addMember(member: Member): (MapStore, Member) = {
      val memberWithId = member.copy(mid = Some(next))
      val newStore = new MapStore(
        map + (next -> memberWithId), 
        next + 1
      )
      (newStore, memberWithId)
    }
  
    def checkNoPending(request: JoinRequest): Either[StoreError, Unit] = {
      val exists = map.exists{ _ match{
        case (_, JoinRequest(_, request.uid, request.gid)) => true
        case _ => false
      }}
      if (exists) Left(GenericError(s"already pending $request"))
      else Right(())
    }

    def addJoinRequest(request: JoinRequest): (MapStore, JoinRequest) = {
      val joinWithId = request.copy(jid = Some(next))
      val newStore = new MapStore(
        map + (next -> joinWithId), 
        next + 1
      )
      (newStore, joinWithId)
    }
    
  }

  object MapStore{

    def apply(entities: Any*): MapStore = 
      MapStore(
        (1.to(entities.size) zip entities).toMap, 
        entities.size+1)

  }

  def run[U](store: MapStore)(program: Store[U]): Either[StoreError, U] = program match {
    case Return(value) => 
      Right(value)
    case StoreAndThen(instruction, next) => 
      val (newStore, result) = runInstruction(store)(instruction)
      result.right.flatMap{ 
        result => run(newStore)(next(result))
      }
  }

  def runInstruction[U](store: MapStore)(inst: StoreInstruction[U]): (MapStore, Either[StoreError, U]) = 
    inst match {
      
      case GetUser(id) => 
        println(s"--> GetUser($id)")
        (store, store.get[User](id))
      
      case GetGroup(id) => 
        println(s"--> GetGroup($id)")
        (store, store.get[Group](id))
      
      case PutMember(member) => 
        println(s"--> PutMember($member)")
        val result: Either[StoreError, (MapStore, Member)] = 
          for {
            _ <- store.get[User](member.uid).right
            _ <- store.get[Group](member.gid).right
            _ <- store.checkNotMember(member).right
            result <- Right(store.addMember(member)).right
          } yield result
        result.fold(
          error => (store, Left(error)),
          pair => (pair._1, Right(pair._2))
        )
  
      case PutJoin(request) => 
        println(s"--> PutJoin($request)")
        val result: Either[StoreError, (MapStore, JoinRequest)] = 
          for {
            _ <- store.get[User](request.uid).right
            _ <- store.get[Group](request.gid).right
            _ <- store.checkNoPending(request).right
            result <- Right(store.addJoinRequest(request)).right
          } yield result
        result.fold(
          error => (store, Left(error)),
          pair => (pair._1, Right(pair._2))
        )
  
      case instruction => 
        (store, Left(GenericError(s"No interpretation for $instruction")))
    }

}

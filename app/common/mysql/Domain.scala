package org.hablapps.meetup.common.mysql

object Domain{

  import org.hablapps.meetup.common.logic.Domain._

  import play.api.Play.current
  import play.api.db.slick.DB

  import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
  import scala.slick.driver.MySQLDriver.simple._

  class GroupTable(tag: Tag) extends Table[Group](tag, "Groups") {
    def gid = column[Option[Int]]("gid", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def city = column[String]("city")
    def must_approve = column[Boolean]("must_approve")
    def * = (gid, name, city, must_approve) <> (Group.tupled, Group.unapply)
  }

  object group_table extends TableQuery[GroupTable](new GroupTable(_)){
    val byID = this.findBy(_.gid)
  }

  class UserTable(tag: Tag) extends Table[User](tag, "Users") {
    def uid = column[Option[Int]]("uid", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def * = (uid, name) <> (User.tupled, User.unapply)
  }

  object user_table extends TableQuery[UserTable](new UserTable(_)){
    val byID = this.findBy(_.uid)
  }

  class MemberTable(tag: Tag) extends Table[Member](tag, "Members") {
    def mid = column[Option[Int]]("mid", O.PrimaryKey, O.AutoInc)
    def uid = column[Int]("uid")
    def gid = column[Int]("gid")
    def * = (mid, uid, gid) <> (Member.tupled, Member.unapply)
  }

  val member_table = TableQuery[MemberTable]

  class JoinTable(tag: Tag) extends Table[JoinRequest](tag, "Joins") {
    def jid = column[Option[Int]]("jid", O.PrimaryKey, O.AutoInc)
    def uid = column[Int]("uid")
    def gid = column[Int]("gid")
    def * = (jid, uid, gid) <> (JoinRequest.tupled, JoinRequest.unapply)
  }

  val join_table = TableQuery[JoinTable]

}
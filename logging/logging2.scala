package ref

/* 
  Reference module with a choice of impure functions.
*/
object Impure{

  def add(x: Int, y: Int): Int = 
    x + y

  def impureSum(x: Int, y: Int): Int = {
    println(s"Beginning addition ...")
    val z = x + y 
    println(s"Adding $x + $y = $z")
    z
  }

  def minusOne(x: Int): Int = {
    println(s"Substracting one to $x")
    x - 1 
  }

  def composing(x: Int, y: Int): Int = 
    minusOne(add(x,y))

}

/*

Each of the following modules represents a small step towards the full implementation
of logging programs: 

- Instructions.       Introduces the (definitive) set of instructions of logging programs. 
- Programs.           The type of logging programs is explicitly introduced.
- PurePrograms.       Introduces a new version of logging programs that allow us to write pure programs.
- SequencingPrograms. New version of logging programs that provides for sequencing multiple instructions.
- DependentPrograms.  Sequencing of instructions can be now context-dependent.
- SequencingOperator. Adds a sequencing operator for concatenation of programs (not instructions).
- SmartPrograms.      Defines atomic logging programs in a one-to-one correspondence with instructions.
- ValuePrograms.      Adds a sequencing operator for concatenating programs and pure functions.
- SweetPrograms.      Syntactic sugar for logging programs using "for-comprehensions"
- ScalazPrograms.     Rewriting of the logging program definition using the scalaz library.

Each of the previous modules are structured into four sections: 
- Instructions.         The type of instructions logging programs are made of.
- Programs.             The type of logging programs. 
- Sample programs.      Simple programs to test the type of logging programs.
- Sample interpreters.  Simple interpreters of logging programs.

*/

object OnInstructions{

  /* Instructions */
  
  trait LoggingInstruction
  case class Warn(msg: String) extends LoggingInstruction
  case class Debug(msg: String) extends LoggingInstruction

  /* Sample program */

  def add(x: Int, y: Int): (LoggingInstruction, Int) = {
    val z = x + y
    (Warn(s"Adding: $x + $y = $z"), z)
  }

  /* Smaple interpreter */

  object IOLogging{

    def runInstruction(instruction: LoggingInstruction): Unit = 
      instruction match {
        case Warn(msg) => println(s"WARN: $msg")
        case Debug(msg) => println(s"DEBUG: $msg")
      }

    def run(program: (LoggingInstruction, Int)): Int = {
      val (instruction, value) = program
      runInstruction(instruction)
      value
    }
  }
}

object OnPrograms{

  /* Instructions */

  trait LoggingInstruction
  case class Warn(msg: String) extends LoggingInstruction
  case class Debug(msg: String) extends LoggingInstruction
  
  /* Programs */

  type LoggingProgram[T] = (LoggingInstruction, T)

  /* Sample program */

  def add(x: Int, y: Int): LoggingProgram[Int] = {
    val z = x + y
    (Warn(s"Adding: $x + $y = $z"), z)
  }

  /* Sample interpreter */

  object IOLogging{

    def runInstruction(instruction: LoggingInstruction): Unit = 
      instruction match {
        case Warn(msg) => println(s"WARN: $msg")
        case Debug(msg) => println(s"DEBUG: $msg")
      }

    def run(program: LoggingProgram[Int]): Int = {
      val (instruction, value) = program
      runInstruction(instruction)
      value
    }
  }
}

object OnPurePrograms{

  /* Instructions */

  trait LoggingInstruction
  case class Warn(msg: String) extends LoggingInstruction
  case class Debug(msg: String) extends LoggingInstruction
  
  /* Programs */

  trait LoggingProgram[T] 
  case class LogAndThen[T](inst: LoggingInstruction, value: T)
    extends LoggingProgram[T]
  case class Return[T](value: T) extends LoggingProgram[T]

  /* Sample program */

  def add(x: Int, y: Int): LoggingProgram[Int] = {
    Return(x + y)
  }

  /* Sample interpreter */

  object IOLogging{

    def runInstruction(instruction: LoggingInstruction): Unit = 
      instruction match {
        case Warn(msg) => println(s"WARN: $msg")
        case Debug(msg) => println(s"DEBUG: $msg")
      }

    def run[T](program: LoggingProgram[T]): T = 
      program match {
        case Return(value) => value
        case LogAndThen(inst, value) => 
          runInstruction(inst)
          value
      }
  }
}

object PlainSequencing{

  /* Instructions */

  trait LoggingInstruction
  case class Warn(msg: String) extends LoggingInstruction
  case class Debug(msg: String) extends LoggingInstruction

  /* Programs */

  trait LoggingProgram[T]
  case class LogAndThen[T](inst: LoggingInstruction, cont: LoggingProgram[T])
    extends LoggingProgram[T]
  case class Return[T](value: T) extends LoggingProgram[T]

  /* Sample program */

  def add(x: Int, y: Int): LoggingProgram[Int] = 
    LogAndThen(Warn(s"Beginning addition ..."), {
      val z = x + y 
      LogAndThen(Debug(s"Adding: $x + $y = $z"), 
        Return(z)
      )
    })

  /* Sample interpreter */

  object IOLogging{

    def runInstruction(instruction: LoggingInstruction): Unit = 
      instruction match {
        case Warn(msg) => println(s"WARN: $msg")
        case Debug(msg) => println(s"DEBUG: $msg")
      }

    def run[T](program: LoggingProgram[T]): T = 
      program match {
        case Return(value) => value
        case LogAndThen(inst, value) => 
          runInstruction(inst)
          run(value)
      }
  }
}

object ContextDependentSequencing{

  /* Instructions */

  trait LoggingInstruction[_]
  case class Warn(msg: String) extends LoggingInstruction[Unit]
  case class Debug(msg: String) extends LoggingInstruction[Unit]

  /* Programs */

  trait LoggingProgram[T]
  case class LogAndThen[U,T](inst: LoggingInstruction[U], cont: U => LoggingProgram[T])
    extends LoggingProgram[T]
  case class Return[T](value: T) extends LoggingProgram[T]

  /* Sample program */

  def add(x: Int, y: Int): LoggingProgram[Int] = 
    LogAndThen(Warn(s"Beginning addition ..."), (_: Unit) => {
      val z = x + y 
      LogAndThen(Debug(s"Adding: $x + $y = $z"), 
        (_: Unit) => Return(z)
      )
    })

  /* Sample interpreter */

  object IOLogging{

    def runInstruction[T](instruction: LoggingInstruction[T]): T = 
      instruction match {
        case Warn(msg) => println(s"WARN: $msg")
        case Debug(msg) => println(s"DEBUG: $msg")
      }

    def run[T](program: LoggingProgram[T]): T = 
      program match {
        case Return(value) => value
        case LogAndThen(inst, next) => 
          runInstruction(inst)
          run(next(()))
      }
  }

}


object SequencingPrograms{

  /* Instructions */

  trait LoggingInstruction[_]
  case class Warn(msg: String) extends LoggingInstruction[Unit]
  case class Debug(msg: String) extends LoggingInstruction[Unit]

  /* Programs */

  trait LoggingProgram[T]{
    def andThenM[U](cont: T => LoggingProgram[U]): LoggingProgram[U] = 
      this match {
        case Return(value) => cont(value) 
        case LogAndThen(inst, next) => 
          LogAndThen(inst, next andThen (_ andThenM cont))
      }
  }
  case class LogAndThen[U,T](inst: LoggingInstruction[U], cont: U => LoggingProgram[T])
    extends LoggingProgram[T]
  case class Return[T](value: T) extends LoggingProgram[T]

  /* Sample program: RAW */

  def add(x: Int, y: Int): LoggingProgram[Int] = 
    LogAndThen(Warn(s"Beginning addition ..."), (_: Unit) => {
      val z = x + y 
      LogAndThen(Debug(s"Adding: $x + $y = $z"), (_: Unit) => 
        Return(z)
      )
    })

  def minusOne(x: Int): LoggingProgram[Int] = {
    LogAndThen(Warn(s"Substracting one to $x"), (_: Unit) => 
      Return(x-1)
    )
  }

  def composing(x: Int, y: Int): LoggingProgram[Int] = 
    add(x,y) andThenM { (z: Int) => minusOne(z) }

  /* Sample interpreter */

  object IOLogging{
    
    def runInstruction[T](instruction: LoggingInstruction[T]): T = 
      instruction match {
        case Warn(msg) => println(s"WARN: $msg")
        case Debug(msg) => println(s"DEBUG: $msg")
      }

    def run[T](program: LoggingProgram[T]): T = 
      program match {
        case Return(value) => value
        case LogAndThen(inst, next) => 
          runInstruction(inst)
          run(next(()))
      }
  }

}

object SmartPrograms{

  /* Instructions */

  trait LoggingInstruction[_]
  case class Warn(msg: String) extends LoggingInstruction[Unit]
  case class Debug(msg: String) extends LoggingInstruction[Unit]

  /* Programs */

  trait LoggingProgram[T]{
    def andThenM[U](cont: T => LoggingProgram[U]): LoggingProgram[U] = 
      this match {
        case Return(value) => cont(value) 
        case LogAndThen(inst, next) => 
          LogAndThen(inst, next andThen (_ andThenM cont))
      }
  }
  case class LogAndThen[U,T](inst: LoggingInstruction[U], cont: U => LoggingProgram[T])
    extends LoggingProgram[T]
  case class Return[T](value: T) extends LoggingProgram[T]


  object LoggingProgram{

    /* Atomic programs: smart constructors */
    def warn(msg: String): LoggingProgram[Unit] = 
      LogAndThen(Warn(msg), (_: Unit) => Return(()))

    def debug(msg: String): LoggingProgram[Unit] = 
      LogAndThen(Debug(msg), (_: Unit) => Return(()))
  }

  import LoggingProgram._

  /* Sample program */

  def add(x: Int, y: Int): LoggingProgram[Int] = 
    warn(s"Beginning addition ...") andThenM ( (_: Unit) => {
      val z = x + y 
      debug(s"Adding: $x + $y = $z") andThenM ( (_: Unit) => 
        Return(z)
      )
    })
  
  /* Sample interpreter */

  object IOLogging{

    def runInstruction[T](instruction: LoggingInstruction[T]): T = 
      instruction match {
        case Warn(msg) => println(s"WARN: $msg")
        case Debug(msg) => println(s"DEBUG: $msg")
      }

    def run[T](program: LoggingProgram[T]): T = 
      program match {
        case Return(value) => value
        case LogAndThen(inst, next) => 
          runInstruction(inst)
          run(next(()))
      }
  }

}

object ValuePrograms{

  /* Instructions */

  trait LoggingInstruction[_]
  case class Warn(msg: String) extends LoggingInstruction[Unit]
  case class Debug(msg: String) extends LoggingInstruction[Unit]

  /* Programs */

  trait LoggingProgram[T]{
    def andThenM[U](cont: T => LoggingProgram[U]): LoggingProgram[U] = 
      this match {
        case Return(value) => cont(value) 
        case LogAndThen(inst, next) => 
          LogAndThen(inst, next andThen (_ andThenM cont))
      }
    def andThenV[U](f: T => U): LoggingProgram[U] = 
      this andThenM (t => Return(f(t)))
  }
  case class LogAndThen[U,T](inst: LoggingInstruction[U], cont: U => LoggingProgram[T])
    extends LoggingProgram[T]
  case class Return[T](value: T) extends LoggingProgram[T]


  object LoggingProgram{

    /* Atomic programs: smart constructors */
    def warn(msg: String): LoggingProgram[Unit] = 
      LogAndThen(Warn(msg), (_: Unit) => Return(()))

    def debug(msg: String): LoggingProgram[Unit] = 
      LogAndThen(Debug(msg), (_: Unit) => Return(()))
  }

  import LoggingProgram._

  /* Sample program */

  def add(x: Int, y: Int): LoggingProgram[Int] = 
    warn(s"Beginning addition ...") andThenM ( (_: Unit) => {
      val z = x + y 
      debug(s"Adding: $x + $y = $z") andThenV ( (_: Unit) => 
        z
      )
    })
  
  /* Sample interpreter */

  object IOLogging{

    def runInstruction[T](instruction: LoggingInstruction[T]): T = 
      instruction match {
        case Warn(msg) => println(s"WARN: $msg")
        case Debug(msg) => println(s"DEBUG: $msg")
      }

    def run[T](program: LoggingProgram[T]): T = 
      program match {
        case Return(value) => value
        case LogAndThen(inst, next) => 
          runInstruction(inst)
          run(next(()))
      }
  }

}

object SweetPrograms{

  /* Instructions */

  trait LoggingInstruction[_]
  case class Warn(msg: String) extends LoggingInstruction[Unit]
  case class Debug(msg: String) extends LoggingInstruction[Unit]

  /* Programs */

  trait LoggingProgram[T]{
    def flatMap[U](cont: T => LoggingProgram[U]): LoggingProgram[U] = 
      this match {
        case Return(value) => cont(value) 
        case LogAndThen(inst, next) => 
          LogAndThen(inst, next andThen (_ flatMap cont))
      }
    def map[U](f: T => U): LoggingProgram[U] = 
      this flatMap (t => Return(f(t)))
  }
  case class LogAndThen[U,T](inst: LoggingInstruction[U], cont: U => LoggingProgram[T])
    extends LoggingProgram[T]
  case class Return[T](value: T) extends LoggingProgram[T]


  object LoggingProgram{
    
    /* Atomic programs: smart constructors */
    def warn(msg: String): LoggingProgram[Unit] = 
      LogAndThen(Warn(msg), (_: Unit) => Return(()))

    def debug(msg: String): LoggingProgram[Unit] = 
      LogAndThen(Debug(msg), (_: Unit) => Return(()))
  }

  import LoggingProgram._

  /* Sample program */

  def add(x: Int, y: Int): LoggingProgram[Int] = for {
    _ <- warn(s"Beginning addition ...")
    z = x + y 
    _ <- debug(s"Adding: $x + $y = $z")
  } yield z
  
  /* Sample interpreter */

  object IOLogging{

    def runInstruction[T](instruction: LoggingInstruction[T]): T = 
      instruction match {
        case Warn(msg) => println(s"WARN: $msg")
        case Debug(msg) => println(s"DEBUG: $msg")
      }

    def run[T](program: LoggingProgram[T]): T = 
      program match {
        case Return(value) => value
        case LogAndThen(inst, next) => 
          runInstruction(inst)
          run(next(()))
      }

  }
}


object ScalazPrograms{
  // libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.0-M3"
  import scalaz._, Scalaz._, Free._

  /* Instructions */

  trait LoggingInstruction[_]
  case class Warn(msg: String) extends LoggingInstruction[Unit]
  case class Debug(msg: String) extends LoggingInstruction[Unit]

  /* Programs */

  type LoggingProgram[T] = Free[LoggingInstruction, T]


  object LoggingProgram{
    /* Atomic programs: smart constructors */
    def warn(msg: String): LoggingProgram[Unit] = 
      liftF(Warn(msg))

    def debug(msg: String): LoggingProgram[Unit] = 
      liftF(Debug(msg))
  }

  import LoggingProgram._

  /* Sample program */

  def add(x: Int, y: Int): LoggingProgram[Int] = for {
    _ <- warn(s"Beginning addition ...")
    z = x + y 
    _ <- debug(s"Adding: $x + $y = $z")
  } yield z
  
  /* Sample interpreter */

  object IOLogging{

    object runInstruction extends (LoggingInstruction ~> Id) {
      def apply[T](instruction: LoggingInstruction[T]): T = 
        instruction match {
          case Warn(msg) => println(s"WARN: $msg")
          case Debug(msg) => println(s"DEBUG: $msg")
        }
    }

    def run[T](program: LoggingProgram[T]): T = 
      program.foldMap(runInstruction)

  }
}

/*

Object-oriented implementation

*/

object OOProgram{
  
  /* Instructions */

  trait Logging{
  
    def warn(msg: String): Unit

    def debug(msg: String): Unit

  }

  /* Sample program */

  trait AbstractProgram{ self: Logging => 

    def add(x: Int, y: Int): Int = {
      warn(s"Beginning addition ...")
      val z = x + y 
      debug(s"Adding: $x + $y = $z")
      z
    }
  
  }
  
  object SampleProgram extends AbstractProgram with IOLogging 

  /* Sample interpreter */

  trait IOLogging extends Logging{

    def warn(msg: String): Unit = 
      println(s"WARN: $msg")

    def debug(msg: String): Unit =
      println(s"DEBUG: $msg")

  }

}


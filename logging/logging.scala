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

object AdHocProgram{

  /* Programs */
  
  trait LoggingInstruction
  case class Warn(msg: String) extends LoggingInstruction
  case class Debug(msg: String) extends LoggingInstruction

  type Logging[x] = (LoggingInstruction, x)

  /* Sample program */
  
  def suma(x: Int, y: Int): Logging[Int] = {
    val z = x + y 
    (Warn(s"Adding $x + $y = $z"),z)
  }

  /* Smaple interpreter */

  def runInstruction(inst: LoggingInstruction): Unit = 
    inst match {
      case Warn(msg) => println(s"WARN: $msg")
      case Debug(msg) => println(s"DEBUG: $msg")
    }
    
  def interpreter[X](program: Logging[X]): X = {
    val (instruction, value) = program
    runInstruction(instruction)
    value
  }


}

object MultiProgram{

  /* Programs */
  
  trait LoggingInstruction
  case class Warn(msg: String) extends LoggingInstruction
  case class Debug(msg: String) extends LoggingInstruction

  trait Logging[X]
  case class LogAndThen[X](inst: LoggingInstruction, 
    cont: Logging[X]) extends Logging[X]
  case class Return[X](value: X) extends Logging[X]

  /* Sample program */
  
  def suma(x: Int, y: Int): Logging[Int] = 
    LogAndThen(Warn(s"Beginning addition ..."), {
      val z = x + y 
      LogAndThen(Debug(s"Adding $x + $y = $z"),
        Return(z)
      )
    })
  
  /* Smaple interpreter */

  def runInstruction(inst: LoggingInstruction): Unit = 
    inst match {
      case Warn(msg) => println(s"WARN: $msg")
      case Debug(msg) => println(s"DEBUG: $msg")
    }
    
  def interpreter[X](program: Logging[X]): X =
    program match {
      case LogAndThen(inst, cont) => 
        runInstruction(inst)
        interpreter(cont)
      case Return(value) =>
        value
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


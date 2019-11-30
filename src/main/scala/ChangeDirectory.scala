import sys.process._

object ChangeDirectoryBuilder{

  sealed trait State

  object PossibleStates{
    sealed trait EmptyCommand extends State
    sealed trait NoPath extends State
    sealed trait HasPath extends State
    type EmptyCdObject = EmptyCommand with NoPath
  }
}

class ChangeDirectoryBuilder[S <: ChangeDirectoryBuilder.State](command: Seq[String]=Seq()){
  import ChangeDirectoryBuilder.PossibleStates._
  /*
For the following functions, implicit constraints restrict the calling of methods to a specific schematic as explained in the documentation
setPath: implicit constraint only allows NoPath state to pass. Returns HasPath type which restricts function from being called again
build: can not be called unless path has been provided
 */
  def SetPath(path:String)(implicit ev: S =:= EmptyCdObject): ChangeDirectoryBuilder[EmptyCommand with HasPath] = new ChangeDirectoryBuilder[EmptyCommand with HasPath](command :+ path)
  def build(implicit ev: S =:= EmptyCommand with HasPath) = Process("cmd.exe /c cd "+ command.mkString(" ")).!
}
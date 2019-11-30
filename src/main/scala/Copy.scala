import sys.process._

object CopyBuilder{

  sealed trait State

  object PossibleStates{
    sealed trait EmptyCommand extends State
    sealed trait NoArgs extends State
    sealed trait HasArgs extends State
    sealed trait NoFile extends State
    sealed trait NoPath extends State
    sealed trait HasFile extends State
    sealed trait HasPath extends State
    type EmptyCpObject = EmptyCommand with NoArgs with NoFile with NoPath
    type CpObjectWithArg = EmptyCommand with HasArgs with NoFile with NoPath
    type CpObjectWithFile = EmptyCommand with HasArgs with HasFile with NoPath
    type FullCpObject = EmptyCommand with HasArgs with HasFile with HasPath
    type CompleteCpObject = EmptyCommand with HasFile with HasPath
  }
}

class CopyBuilder[S <: CopyBuilder.State](command: Seq[String]=Seq()){
  import CopyBuilder.PossibleStates._
  /*
For the following functions, implicit constraints restrict the calling of methods to a specific schematic as explained in the documentation
setArgs: implicit constraint only allows NoArgs state to pass and has to be invoked as first function to be invoked. Returns HasArgs type which restricts function from being called again
setFile: Can be run without calling set args but can not be called again if called once due to type constraints
setPath: Can not be run without calling SetFile but can not be called again if called once due to type constraints
build: will not build without at least calling SetFile and SetPath
*/
  def SetArgs(args:String)(implicit ev: S =:= EmptyCommand with NoArgs with NoFile with NoPath): CopyBuilder[CpObjectWithArg] = new CopyBuilder[CpObjectWithArg](command :+ args)
  def SetFile(filePath:String)(implicit ev: S <:< EmptyCommand with NoFile with NoPath): CopyBuilder[CpObjectWithFile] = new CopyBuilder[CpObjectWithFile](command :+ filePath)
  def SetPath(dir:String)(implicit ev: S <:< EmptyCommand with HasFile with NoPath): CopyBuilder[FullCpObject] = new CopyBuilder[FullCpObject](command:+ dir)
  def build(implicit ev: S <:< CompleteCpObject) = Process("cmd.exe /c copy " + command.mkString(" ")).!

}
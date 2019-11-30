import sys.process._

object NewViewDirectoryBuilder{

  sealed trait State

  object PossibleStates{
    sealed trait EmptyCommand extends State
    sealed trait NoArgs extends State
    sealed trait NoDir extends State
    sealed trait HasArgs extends State
    sealed trait HasDir extends State
    type EmptyVDObject = EmptyCommand with NoArgs with NoDir
    type VDObjectWithArg = EmptyCommand with HasArgs with NoDir
    type FullVDObject = EmptyCommand with HasArgs with HasDir
    type CompleteVDObject = EmptyCommand with HasDir
  }
}

class NewViewDirectoryBuilder[S <: NewViewDirectoryBuilder.State](command: Seq[String]=Seq()){
  import NewViewDirectoryBuilder.PossibleStates._
  /*
For the following functions, implicit constraints restrict the calling of methods to a specific schematic as explained in the documentation
setArgs: implicit constraint only allows NoArgs state to pass and has to be invoked as first function to be invoked. Returns HasArgs type which restricts function from being called again
setDir: Can be run without calling set args but can not be called again if called once due to type constraints
build: can not be built unless at least directory has been provided
*/
  def SetArgs(args:String)(implicit ev: S =:= EmptyVDObject): NewViewDirectoryBuilder[VDObjectWithArg] = new NewViewDirectoryBuilder[VDObjectWithArg](command :+ args)
  def SetDir(dir:String)(implicit ev: S <:< EmptyCommand with NoDir): NewViewDirectoryBuilder[FullVDObject] = new NewViewDirectoryBuilder[FullVDObject](command:+check(dir))
  def check(dum: String):String = dum match{
    case "" => "."
    case _ => dum
  }
  def build(implicit ev: S <:< CompleteVDObject) = ("cmd.exe /c dir " + command.mkString(" ")).!!

}
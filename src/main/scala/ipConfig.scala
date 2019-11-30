import sys.process._

object ipConfigBuilder{
  //Implementing Phantom types
  sealed trait State

  object PossibleStates{
    //Each method has a full or empty states
    sealed trait EmptyCommand extends State
    sealed trait NoOptions extends State
    sealed trait NoAdapterName extends State
    sealed trait HasOptions extends State
    sealed trait HasAdapterName extends State

    //Create types according to semantic restrictions
    type EmptyIpcObject = EmptyCommand with NoOptions with NoAdapterName
    type IpcObjectWithOption = EmptyCommand with HasOptions with NoAdapterName
    type FullIpcObject = EmptyCommand with HasOptions with HasAdapterName
  }
}

class ipConfigBuilder[S <: ipConfigBuilder.State](command: Seq[String]=Seq()){
  import ipConfigBuilder.PossibleStates._
/*
For the following functions, implicit constraints restrict the calling of methods to a specific schematic as explained in the documentation
setOptions: implicit constraint only allows NoOptions state to pass. Returns HasOptions type which restricts function from being called again
setAdapter: Follows similar restrictions as above
 */
def SetOptions(opt:String)(implicit ev: S =:= EmptyIpcObject): ipConfigBuilder[IpcObjectWithOption] = new ipConfigBuilder[IpcObjectWithOption](command :+ opt)
def SetAdapter(adapt:String)(implicit ev: S =:= IpcObjectWithOption): ipConfigBuilder[FullIpcObject] = new ipConfigBuilder[FullIpcObject](command:+adapt)
def build(implicit ev: S <:< EmptyCommand) = ("cmd.exe /c ipconfig "+ command.mkString(" ")).!!

}
import sys.process._

object PingBuilder{

  sealed trait State

  object PossibleStates{
    sealed trait EmptyCommand extends State
    sealed trait NoArgs extends State
    sealed trait NoHost extends State
    sealed trait HasArgs extends State
    sealed trait HasHost extends State
    type EmptyPingObject = EmptyCommand with NoArgs with NoHost
    type PingObjectWithArg = EmptyCommand with HasArgs with NoHost
    type FullPingObject = EmptyCommand with HasArgs with HasHost
    type CompletePingObject = EmptyCommand with HasHost
  }
}

class PingBuilder[S <: PingBuilder.State](command: Seq[String]=Seq()){
  import PingBuilder.PossibleStates._
  /*
For the following functions, implicit constraints restrict the calling of methods to a specific schematic as explained in the documentation
setArgs: implicit constraint only allows NoArgs state to pass and has to be invoked as first function to be invoked. Returns HasArgs type which restricts function from being called again
setHost: Can be run without calling set args but can not be called again if called once due to type constraints
build: will not build without at least calling setting the host
*/
  def SetArgs(args:String)(implicit ev: S =:= EmptyPingObject): PingBuilder[PingObjectWithArg] = new PingBuilder[PingObjectWithArg](command :+ args)
  def SetHost(host:String)(implicit ev: S <:< EmptyCommand with NoHost): PingBuilder[FullPingObject] = new PingBuilder[FullPingObject](command:+host)
  def build(implicit ev: S <:< CompletePingObject) = Process("cmd.exe /c ping " + command.mkString(" ")).!
}
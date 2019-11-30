import sys.process._

object NewFindStringBuilder{
//Implementing Phantom types
  sealed trait State

  object PossibleStates{
    //Each method has a full or empty states
    sealed trait EmptyCommand extends State
    sealed trait NoArgs extends State
    sealed trait HasArgs extends State
    sealed trait NoString extends State
    sealed trait HasString extends State
    sealed trait NoPath extends State
    sealed trait HasPath extends State
    sealed trait NoInput extends State
    sealed trait HasInput extends State

    //Create types according to semantic restrictions
    type EmptyFsObject = EmptyCommand with NoArgs with NoString with NoPath with NoInput //Defines the state of an empty object
    type FsObjectWithArg = EmptyCommand with HasArgs with NoString with NoPath with NoInput //When SetArgs is called, the state is changed. Helps us keep track of whether args have been set
    type FsObjectWithString = EmptyCommand with HasArgs with HasString with NoPath with NoInput //Follows a similar logic as the above
    type FsObjectWithPath = EmptyCommand with HasArgs with HasString with HasPath with NoInput
    type FsObjectWithInput = EmptyCommand with HasArgs with HasString with NoPath with HasInput //For pipelined, input should be present instead of a path
  }
}

class NewFindStringBuilder[S <: NewFindStringBuilder.State](command: Seq[String]=Seq(), Input: String=""){
  import NewFindStringBuilder.PossibleStates._
  /*
  For the following functions, implicit constraints restrict the calling of methods to a specific schematic as explained in the documentation
  setArgs: implicit constraint only allows NoArgs state to pass. Returns HasArgs type which restricts function from being called again
  setString: implicit constraint only allows NoString state to pass. Also does not raise an issue if args have been set or not as they are optional.
              Returns HasString type with HasArgs as well which restricts the setting of string or arg again
   setPath: Only call if command is to be used to find a string in a particular path. Follows similar restrictions as above
   setInput: Only call if pipelining is taking place. Input refers to the result of another object being pipelined. follows similar restrictions as above
   */
  def SetArgs(args:String)(implicit ev: S =:= EmptyFsObject): NewFindStringBuilder[FsObjectWithArg] = new NewFindStringBuilder[FsObjectWithArg](command :+ args)

  def SetString(dir:String)(implicit ev: S <:< EmptyCommand with NoString with NoPath with NoInput): NewFindStringBuilder[FsObjectWithString] =
    new NewFindStringBuilder[FsObjectWithString](command:+dir)
  def SetPath(path:String)(implicit  ev: S <:< EmptyCommand with HasString with NoPath with NoInput): NewFindStringBuilder[FsObjectWithPath] =
    new NewFindStringBuilder[FsObjectWithPath](command:+ path)
  def SetInput(input:String)(implicit  ev: S <:< EmptyCommand with HasString with NoPath with NoInput): NewFindStringBuilder[FsObjectWithInput] =
    new NewFindStringBuilder[FsObjectWithInput](command,Input = input)
  /*
  If input is non empty, the command has been pipelined. If it is empty, a simple findstr is called on the path provided
   */
  def build(implicit ev: S <:< EmptyCommand with HasString) = Input match {
    case "" => ("cmd.exe /c findstr " + command.mkString(" ")).!!
    case _ => Input.split("\n").map(x =>Process("cmd.exe /c echo \"" + x.trim + "\" | findstr " + command.mkString(" ")).lazyLines_!.mkString)
      .filter(x => x.trim().length() > 0).mkString("\n")
  }

}
import com.typesafe.config.ConfigFactory
import org.scalatest.FunSuite

class CompileTest extends FunSuite {

  val conf = ConfigFactory.load()
  test("This should not compile as SetArgs is called after SetDir "){
    assertDoesNotCompile("new NewViewDirectoryBuilder[NewViewDirectoryBuilder.PossibleStates.EmptyVDObject]().SetDir(conf.getString(\"vd-config.dir\")).SetArgs(conf.getString(\"vd-config.args\")).build")
  }

  test("This should not compile as SetPath for copy is missing"){
    assertDoesNotCompile("val copyObject = new CopyBuilder[CopyBuilder.PossibleStates.EmptyCpObject]().SetFile(conf.getString(\"copy-config.file\")).build")
  }

  test("This should not compile as SetPath is missing"){
    assertDoesNotCompile("val CdObject = new ChangeDirectoryBuilder[ChangeDirectoryBuilder.PossibleStates.EmptyCdObject]().build")
  }

  test("This should compile as it is a valid findstr command"){
    assertCompiles("val FsObject = new NewFindStringBuilder[NewFindStringBuilder.PossibleStates.EmptyFsObject]().SetArgs(conf.getString(\"fs-config.args\")).SetString(conf.getString(\"fs-config.stringToSearch\")).SetPath(conf.getString(\"fs-config.path\")).build")
  }

  test("This should compile as it is a valid ping command"){
    assertCompiles("val pingObject = new PingBuilder[PingBuilder.PossibleStates.EmptyPingObject]().SetArgs(conf.getString(\"ping-config.args\")).SetHost(conf.getString(\"ping-config.site\")).build")
  }

  test("This should compile as this is a valid command"){
    assertCompiles("val pingObject = new PingBuilder[PingBuilder.PossibleStates.EmptyPingObject]().SetHost(conf.getString(\"ping-config.site\")).build")
  }
}

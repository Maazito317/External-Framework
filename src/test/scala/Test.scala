import sys.process._
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

object Test {
  def main(args: Array[String]):Unit = {

    val logger = LoggerFactory.getLogger("Test")
    val conf = ConfigFactory.load()
    logger.info("Starting Tests...")

    logger.info("Running dir command")
    val VdObject = new NewViewDirectoryBuilder[NewViewDirectoryBuilder.PossibleStates.EmptyVDObject]().SetArgs(conf.getString("vd-config.args")).SetDir(conf.getString("vd-config.dir")).build
    println(VdObject)

    logger.info("Running ipconfig command")
    val ipcObject = new ipConfigBuilder[ipConfigBuilder.PossibleStates.EmptyIpcObject]().build
    println(ipcObject)

    logger.info("Running findstr command")
    val FsObject = new NewFindStringBuilder[NewFindStringBuilder.PossibleStates.EmptyFsObject]().SetArgs(conf.getString("fs-config.args")).SetString(conf.getString("fs-config.stringToSearch")).SetPath(conf.getString("fs-config.path")).build
    println(FsObject)
    logger.info("Running pipelined command")
    val FsObject2 = new NewFindStringBuilder[NewFindStringBuilder.PossibleStates.EmptyFsObject]().SetString(conf.getString("fs-config.stringToSearch")).SetInput(VdObject).build
    println(FsObject2)

    logger.info("Running ping command")
    val pingObject = new PingBuilder[PingBuilder.PossibleStates.EmptyPingObject]().SetArgs(conf.getString("ping-config.args")).SetHost(conf.getString("ping-config.site")).build

    logger.info("Running cd command")
    val CdObject = new ChangeDirectoryBuilder[ChangeDirectoryBuilder.PossibleStates.EmptyCdObject]().SetPath(conf.getString("cd-config.path")).build

    logger.info("Running copy command")
    val copyObject = new CopyBuilder[CopyBuilder.PossibleStates.EmptyCpObject]().SetFile(conf.getString("copy-config.file")).SetPath(conf.getString("copy-config.location")).build
    }
}

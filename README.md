# External Command Execution Framework

## Author
<Maaz Khan, 667601660>

## Overview
The framework allows for the usage of the following windows commands using the builder pattern:

 - Change Directory: cd
 - View Directory: dir
 - Find String: findstr
 - ipconfig
 - ping
 - copy
 
 The framework makes use of phantom types to deal with function states and help with restricting the semantics of the framework. The commands are built in a similar way as they would be written in a command line. In this, the orders of the method calls are important and the command may not be built if certain methods are not invoked.

 ## Change Directory
The command is meant to change our current directory and is executed in the command line as follows: `cd <path>`

Using the framework, this is how we execute the command: 

    val CdObject = new ChangeDirectoryBuilder[ChangeDirectoryBuilder.PossibleStates.EmptyCdObject]().SetPath("C:\\Users\\mmaaz\\OneDrive\\Desktop").build

Through the use of phantom types, we have restricted the builder to not be able to call the build function until the SetPath method has been used. The method, in addition to setting the path, also changes the state of our builder to one where the path has been set. Hence allowing for the build function to be invoked.

## Ping
**Ping** helps determine TCP/IP networks IP address, as well as issues with the network and assists with resolving them. The command is executed in the command line as follows: `ping <args> <website>`

Using the framework, this is how we execute the command:

    val pingObject = new PingBuilder[PingBuilder.PossibleStates.EmptyPingObject]().SetArgs("-n 3").SetHost("www.google.com").build

The SetArgs function here sets the number of packets sent to be equal to 3. And the SetHost sets the name of the website. Through the use of phantom types, we have restricted the builder to be unable to call the build function without calling the SetHost function. The SetArg function may or may not be used and it will not effect the build at compile time. Furthermore, if args are to be set, the method must be invoked before we set the host. This is equivalent to the command: `ping -n 3 www.google.com`

## Ipconfig

Used without parameters, **ipconfig** displays Internet Protocol version 4 (IPv4) and IPv6 addresses, subnet mask, and default gateway for all adapters. It further displays all current TCP/IP network configuration values and refreshes Dynamic Host Configuration Protocol (DHCP) and Domain Name System (DNS) settings. The command is executed in the command line as follows: `ipconfig <Options> <Adapter>`

Using the framework, this is how we execute the command: 

    val ipcObject = new ipConfigBuilder[ipConfigBuilder.PossibleStates.EmptyIpcObject]().SetOptions("/showclassid").SetAdapter("Local*").build

This is equivalent to the command: `ipconfig /showclassid Local*` This displays the DHCP class ID for all adapters with names that start with Local.

For this command, invoking SetOptions or SetAdapter is not necessary to be able to buil the command. But if used, SetOptions must be invoked before SetAdapter.

## Copy

Copies one or more files from one location to another. The command is executed in the command line as follows: `copy <fileToCopy> <LocationToBeCopiedTo>`

Using the framework, this is how we execute the command:

    val copyObject = new CopyBuilder[CopyBuilder.PossibleStates.EmptyCpObject]().SetFile("C:\\Users\\mmaaz\\OneDrive\\Desktop\\Lecture5.pdf").SetPath("C:\\Users\\mmaaz\\OneDrive\\Desktop\\HW456").build
This is equivalent to the command: `copy Lecture5.pdf C:\Users\mmaaz\OneDrive\Desktop\HW456` if we are present in the desktop directory.

For this command, invoking both SetFile and SetPath is necessary for the build function to be invoked. Also, SetFile must be invoked before SetPath.

## Dir
Displays a list of a directory's files and subdirectories. If used without parameters, **dir** displays the disk's volume label and serial number, followed by a list of directories and files on the disk (including their names and the date and time each was last modified). For files, **dir** displays the name extension and the size in bytes. **Dir** also displays the total number of files and directories listed, their cumulative size, and the free space (in bytes) remaining on the disk. The command is executed in the command line as follows: `dir <args> <directory>`

Using the framework, this is how we execute the command:

    val VdObject = new NewViewDirectoryBuilder[NewViewDirectoryBuilder.PossibleStates.EmptyVDObject]().SetArgs("/Q").SetDir("C:\\Users\\mmaaz\\OneDrive\\Desktop").build

This is equivalent to the command `dir /Q C:\\Users\\mmaaz\\OneDrive\\Desktop`

For this command, if we want to use multiple arguments we can do so like this: SetArgs(/Q/A/.....). Also, it is not necessary to invoke the SetArgs method for the build method to be invoked. However, the SetArgs method must be invoked before the SetDir method.

## Findstr

Searches for patterns of text in files. The command is executed in the command line as follows: `findstr <args> <stringToSearch> <path>`

Using the framework, this is how we execute the command:

    val FsObject = new NewFindStringBuilder[NewFindStringBuilder.PossibleStates.EmptyFsObject]().SetArgs("/M").SetString("Michael").SetPath("C:\\Users\\mmaaz\\OneDrive\\Desktop\\*").build
This is equivalent to the command: `findstr /M "Michael" "C:\Users\mmaaz\OneDrive\Desktop\*"`

For this command, if we want to use multiple arguments we can do so like this: SetArgs(/M/X/....). Also, it is not necessary to invoke the SetArgs method for the build method to be invoked. However, the methods must be invoked in the sequence as shown above. The build method does not compile without calling both SetString and SetPath. 

This command can further be pipelined with the **dir** command as shown: 

    val FsObject2 = new NewFindStringBuilder[NewFindStringBuilder.PossibleStates.EmptyFsObject]().SetString("Michael").SetInput(VdObject).build
This is equivalent to the command: `dir C:\Users\mmaaz\OneDrive\Desktop\ | findstr "Michael"` 
Pipelining is implemented using the SetInput method. Again, it is optional to set any args and the sequence of invocation must be followed. 

## Setup
The source code can be imported into IntelliJ  IDEA and used directly. 

## Logging and Config
Logging is done at various levels and configuration file is present in the resources folder.
# Eternal-Command-Execution-Framework

# FileSystemTraverser

This program will take as input an absolute or relative folder and traverse all directories and sub directories for files named with the extension found at config file. Each file will be split into words and information about them will be written in the below format.

<Directory src/test/resources/testdata>
**** LONG FILES ****
	<sub-directory a>
		<sub-directory b>
			<file #1 e.txt> <#words>  <wordthe 118> <wordand 59> <wordof 119>
			<file #2 Wigan.txt> <#words>  <worda 110> <wordgame 50> <wordin 64> <wordfor 60> <wordretrieved 50> <word2009 96> <wordthe 260> <word0 68> <word2008 109> <word1 52> <wordjanuary 82> <wordand 73> <wordof 62> <wordreport 53> <wordfrom 71> <wordup 75> <wordto 103> <wordwigan 130> <wordon 106> <wordjump 68>
			<file #3 lamar.txt> <#words>  <wordthe 122> <wordin 62> <wordand 63> <wordof 66>
			<sub-directory d>
				<file #2 Buchdruck.txt> <#words>  <wordder 178> <worddie 131> <wordin 95> <worddas 52> <wordund 113>
<Directory src/test/resources/testdata>
**** SHORT FILES ****
	<sub-directory a>
		<sub-directory b>
			<sub-directory d>
				<file #1 Talmadge.txt> <#words> 

What it does is write all text file names, if text file contains more than threshold number of words, it is considered a long file. Long files containing more words than the lower amount at config file, those words and their repetitions are also printed to the console. Otherwise, just the filename and index within directory is printed. All directories will be printed by default. Proper indentation shows the user the tree structure. The important thing to keep in mind is the encoding and locale settings at config file.

This console application can be built with the below command.
  mvn clean assembly:assembly.

The create jar file with dependencies can be run with the below commands.
  java -jar target/app-0.0.1-SNAPSHOT-jar-with-dependencies.jar
  java -jar target/app-0.0.1-SNAPSHOT-jar-with-dependencies.jar -config_file_location_with_dashes-

First command will use the default config and log4j configuration found at src/main/resources.

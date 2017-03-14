# FileSystemTraverser

This program will take as input an absolute or relative folder and traverse all directories and sub directories for files named with the extension found at config file. Each file will be split into words and information about them will be written by indenting each folder and file with one tab relative to their parents.


What it does is write all text file names, if text file contains more than threshold number of words, it is considered a long file. Long files containing more words than the lower amount at config file, those words and their repetitions are also printed to the console. Otherwise, just the filename and index within directory is printed. All directories will be printed by default. Proper indentation shows the user the tree structure. The important thing to keep in mind is the encoding and locale settings at config file.

This console application can be built with the below command.
  mvn clean assembly:assembly.

The create jar file with dependencies can be run with the below commands.
  java -jar target/app-0.0.1-SNAPSHOT-jar-with-dependencies.jar
  java -jar target/app-0.0.1-SNAPSHOT-jar-with-dependencies.jar -config_file_location_with_dashes-

First command will use the default config and log4j configuration found at src/main/resources.

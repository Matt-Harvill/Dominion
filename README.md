# Dominion
### Install Java
Download an appropriate JDK for your operating system. The latest JDK can be downloaded from the official [OpenJDK](http://jdk.java.net/16/) website.

Once installed, type `java --version` from your command line to verify installation was successful.

You need to set the JAVA_HOME environment variable to the JDK installation directory. You can follow [this guide](https://www.baeldung.com/java-home-on-windows-7-8-10-mac-os-x-linux) to set JAVA_HOME for your platform.

### Install Maven
Download the [zip or tar binaries](https://maven.apache.org/download.cgi) for you operating system.

Extract the contents to a directory of your choice and then add the bin directory `directory-to-maven\apache-maven-3.X.X\bin` to your PATH environment variable.

Similarly to Java, type `mvn --version` from your command line to verify installation was successful.

If you have any issues installing maven, go [here](https://maven.apache.org/install.html) for more details.

## Running Dominion
*After installing Java and Maven*

In the command line navigate to the directory where you cloned/downloaded this repository.

Run `mvn javafx:run` and the dominion login should appear!

*If you receive errors, make sure you're in the directory with the pom.xml file*

## Playing Dominion
To play with multiple players, have each player run a single instance on their machine.
To play with multiple players on the same machine, open a new terminal window for each instance.

One player will be the host and the other players will join the host's game. The host can find their IP and Port in the top right of the application once the game has been created.
Players joining the game should be on the same local area network to connect to the host's game.

### Rules of Dominion
Go [here](https://www.ultraboardgames.com/dominion/game-rules.php) to read the basic rules of Dominion.

Now you're ready to play!


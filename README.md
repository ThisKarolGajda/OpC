# OpC
**ThisOpKarol`s Core and API**

# Features
- Simple and quick to use,
- Removes additional code and need for other libraries that are included in plugin such as:
  - PlaceHolderAPI (with creating own placeholders)
  - BStats (with registering own stats)
  - Vault (with economy methods)
- Easy command manager and event registration,
- CenterText and OpComponent with String component built-in,
- MySql and Flat database implementation,
- Configuration files with custom methods,
- HeadDatabase,
- From and to String Inventory and Item builder,
- Language manager, with languages for each player,
- Own Map and mapping system for easier use of java maps,
- Useful classes:
  - BiOptional, which holds two optional objects,
  - OpActionBar,
  - OpBossBar,
  - OpLocation,
  - OpParticle,
  - OpRunnable,
  - OpSound,
  - OpTitle,
  - OpText
- Abstract JavaPlugin implementation which provided added features and Configuration file to plugin,
- String based storage system with builder built-in supporting multiple java variables (double, int, String, boolean, (custom)) with supportive reflection,
- Useful util classes
- Custom Configuration system with easy-to-add own configuration objects,
- Head database and retrieving from players 

You can learn everything that this core have in the [wiki](https://github.com/ThisKarolGajda/OpC/wiki).

# Implementation:
## Version
Jitppack version: ![](https://jitpack.io/v/ThisKarolGajda/OpC.svg)
## Maven
```maven
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>

<dependency>
    <groupId>com.github.ThisKarolGajda</groupId>
    <artifactId>OpC</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```
## Gradle
```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

dependencies {
        implementation 'com.github.ThisKarolGajda:OpC:master-SNAPSHOT'
}
``` 

## Jitpack
You can see other options, versions and build logs of OpC under this [link](https://jitpack.io/#ThisKarolGajda/OpC).

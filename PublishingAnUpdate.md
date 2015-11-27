# Publishing an update #

Publishing an update for your Java application is a simple task and the Twinkle framework provides you with some tools to make the process automatic.

## Generating keys ##

Your updates must be cryptographically signed to prevent man-in-the-middle attacks to the users of your applications. This is different from code-signing your app through a company such as Verisign (which you are encouraged to do, but it's not required). To sign your update, you must generate a private and a public key. The private key will be used by you alone to sign the update package, while the public key will be distributed with your application and will be used by the Twinkle framework to verify the integrity of the downloaded data.

A simple script, **genkeys.sh**, is provided in the **tools** directory, and allows for an easy generation of the keys on Unix derived systems. The only requirement needed is **openssl** installed.

On Unix-like system, you can simply type:

```
 cd your-project-dir/tools
 ./genkeys.sh keys-dest-dir/
```

The script will generate two files in your _keys-dest-dir_ directory, namely **dsa\_priv.pem** (your private key) and **dsa\_pub.pem** (your public key).

You are now ready to go!

## Signing and distributing your update package with Twinkle ##

Distribution is accomplished following these steps:
  1. Sign your update package with your private key
  1. Generate or assemble an **appcast** feed based on a [template](http://andymatuschak.org/files/sparkletestcast.xml)
  1. Host your generated appcast feed somewhere, where it is publicly accessible (on your webserver, ecc)

The Twinkle framework provides you with a sample ant build script to automate the first two steps of the process. You can find this script in the _tests/simpleapp/_ directory. You will need a **Unix-derived** system to use them and the latest version of **openssl**.

The script uses an external property file, **info.properties**, that you must fill with your application information.

On most occasions, you will only need to touch few of the properties it contains. Probably you'll need to edit at least the **project.name**, **info.software.name**, **project.appcast.host** and **info.version** if you keep the directory structure intact.

For completion, here is a brief description of the properties contained in the **info.properties** file:

| **project.name** | The name of your project |
|:-----------------|:-------------------------|
| **project.source** | The directory containing the source of your application, relative to the location of the **build.xml** file |
| **project.main** | The class containing the main of your application. This will be used when building the jar package. |
| **project.libdir** | Location of the external dependencies of your application |
| **project.libs** | Ant pattern matching your dependencies |
| **project.antdir** | Location of any additional Ant library |
| **project.bin**  | Directory where the compiled class file will be generated |
| **project.res.dir** | Directory containing the **appcast.xml** template file |
| **project.tools.dir** | Directory where the tools scripts are located |
| **project.appcast.file** | Name of the appcast template file |
| **project.appcast.host** | Public location of your **appcast** feed, without the appcast file name. (eg: _http://www.barrysoft.it/twinkle/simpleapp_) |
| **project.appcast.dsakey** | Location of your private key, that will be used to sign the update package |
| **project.appcast.dsasign** | Location of the **sign\_archive.sh** script |
| **project.deploy** | Directory where your project will be deployed to |
| **project.deploy.jar** | Directory where the jar of your project will be deployed to |
| **project.deploy.jarname** | Name of the jar file that will be build for your application |
| **project.deploy.jarlibs** | Directory where the dependencies of your application will be deployed to |
| **project.deploy.web** | Directory where web-related files will be generated. **The appcast feed will be generated here** , along with your update package, that will be copied inside the **dist** directory |
| **project.buildfile** | Name of the file to be used to keep track of the current build number |
| **info.software.name** | The name of your application, will be used in the appcast feed |
| **info.version** | The actual version string of your project (eg: _0.0.1_) |

When you have succesfully edited the build file you can simply run, in your project's root:

```
  ant app-cast
```

If everything goes well, you will find in the directory addressed by the **project.deploy.web** property, an **appcast.xml** file and a **dist** directory containing a zip of the jar of your application.

At this point you can manually edit the **appcast.xml** file with your application changelog.

When you are done, simply upload the content of your **project.deploy.web** directory to your server, to the URL pointed by **project.appcast.host**.

Congrats! You have just published your first application update!

To release subsequent updates you'll just need to run ant with the app-cast target again and upload the generated files.

## Manually signing and distributing your update package ##

If you want to use your own build evironment or your own build scripts you can sign your updates manually. You'll also need to create an appcast file by yourself, probably starting from a [template](http://andymatuschak.org/files/sparkletestcast.xml).

For further information you can read the official Sparkle documentation at https://github.com/andymatuschak/Sparkle/wiki.
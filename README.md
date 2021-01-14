# file-worker

<div align="center">

[![Build Status](https://travis-ci.org/lightblueseas/file-worker.svg?branch=develop)](https://travis-ci.org/lightblueseas/file-worker) 
[![Coverage Status](https://codecov.io/gh/lightblueseas/file-worker/branch/develop/graph/badge.svg)](https://codecov.io/gh/lightblueseas/file-worker)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=lightblueseas_file-worker&metric=alert_status)](https://sonarcloud.io/dashboard?id=lightblueseas_file-worker)
[![Open Issues](https://img.shields.io/github/issues/lightblueseas/file-worker.svg?style=flat)](https://github.com/lightblueseas/file-worker/issues)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.astrapi69/file-worker/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.astrapi69/file-worker)
[![Javadocs](http://www.javadoc.io/badge/io.github.astrapi69/file-worker.svg)](http://www.javadoc.io/doc/io.github.astrapi69/file-worker)
[![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](http://opensource.org/licenses/MIT)
[![Donate](https://img.shields.io/badge/donate-❤-ff2244.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=GVBTWLRAZ7HB8)

</div>

Project that holds utility class for file operations.

If you like this project put a ⭐ and donate

## License

The source code comes under the liberal MIT License, making file-worker great for all types of applications.

## Maven dependency

Maven dependency is now on sonatype.
Check out [sonatype repository](https://oss.sonatype.org/index.html#nexus-search;gav~io.github.astrapi69~file-worker~~~) for latest snapshots and releases.

Add the following maven dependency to your project `pom.xml` if you want to import the core functionality of file-worker:

	<properties>
			...
		<!-- FILE-WORKER version -->
		<file-worker.version>6</file-worker.version>
			...
	</properties>
			...
		<dependencies>
			...
			<!-- FILE-WORKER DEPENDENCY -->
			<dependency>
				<groupId>io.github.astrapi69</groupId>
				<artifactId>file-worker</artifactId>
				<version>${file-worker.version}</version>
			</dependency>
			...
		</dependencies>
	
			
## gradle dependency

You can first define the version in the ext section and add than the following gradle dependency to 
your project `build.gradle` if you want to import the core functionality of file-worker:


define version in file gradle.properties
```
fileWorkerVersion=6
```

or in build.gradle ext area

```
ext {
			...
    fileWorkerVersion = "6"
			...
}
```

and than add the dependency to the dependencies area

```
dependencies {
			...
	implementation("io.github.astrapi69:file-worker:$fileWorkerVersion")
			...
}
```

## Semantic Versioning

The versions of file-worker are maintained with the Semantic Versioning guidelines.

Release version numbers will be incremented in the following format:

`<major>.<minor>.<patch>`

For detailed information on versioning you can visit the [wiki page](https://github.com/lightblueseas/mvn-parent-projects/wiki/Semantic-Versioning).


## Want to Help and improve it? ###

The source code for file-worker are on GitHub. Please feel free to fork and send pull requests!

Create your own fork of [lightblueseas/file-worker/fork](https://github.com/lightblueseas/file-worker/fork)

To share your changes, [submit a pull request](https://github.com/lightblueseas/file-worker/pull/new/develop).

Don't forget to add new units tests on your changes.

## Contacting the Developers

Do not hesitate to contact the file-worker developers with your questions, concerns, comments, bug reports, or feature requests.
- Feature requests, questions and bug reports can be reported at the [issues page](https://github.com/lightblueseas/file-worker/issues).

## Note

No animals were harmed in the making of this library.

# Donations

This project is kept as an open source product and relies on contributions to remain being developed. 
If you like this project, please consider a donation through paypal: <a href="https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=B37J9DZF6G9ZC" target="_blank">
<img src="https://www.paypalobjects.com/en_US/GB/i/btn/btn_donateCC_LG.gif" alt="PayPal this" title="PayPal – The safer, easier way to pay online!" border="0" />
</a>

or over bitcoin or bitcoin-cash with:

36JxRRDfRazLNqUV6NsywCw1q7TK38ukpC

or over ether with:

0x588Aa02De98B1Ef70afeDC3ec5290130a3E5e273

or over the donation buttons at the top.

## Similar projects

Here is a list of awesome similar projects:

Open Source:

 * [ziputils](https://bitbucket.org/matulic/ziputils) Simple library for reading and writing password-protected zip files in Java

## Credits

|**Travis CI**|
|     :---:      |
|[![Travis CI](https://travis-ci.com/images/logos/TravisCI-Full-Color.png)](https://coveralls.io/github/lightblueseas/file-worker?branch=develop)|
|Special thanks to [Travis CI](https://travis-ci.org) for providing a free continuous integration service for open source projects|
|     <img width=1000/>     |

|**Nexus Sonatype repositories**|
|     :---:      |
|[![sonatype repository](https://img.shields.io/nexus/r/https/oss.sonatype.org/io.github.astrapi69/file-worker.svg?style=for-the-badge)](https://oss.sonatype.org/index.html#nexus-search;gav~io.github.astrapi69~file-worker~~~)|
|Special thanks to [sonatype repository](https://www.sonatype.com) for providing a free maven repository service for open source projects|
|     <img width=1000/>     |

|**coveralls.io**|
|     :---:      |
|[![Coverage Status](https://coveralls.io/repos/github/lightblueseas/file-worker/badge.svg?branch=develop)](https://coveralls.io/github/lightblueseas/file-worker?branch=develop)|
|Special thanks to [coveralls.io](https://coveralls.io) for providing a free code coverage for open source projects|
|     <img width=1000/>     |

|**javadoc.io**|
|     :---:      |
|[![Javadocs](http://www.javadoc.io/badge/io.github.astrapi69/file-worker.svg)](http://www.javadoc.io/doc/io.github.astrapi69/file-worker)|
|Special thanks to [javadoc.io](http://www.javadoc.io) for providing a free javadoc documentation for open source projects|
|     <img width=1000/>     |





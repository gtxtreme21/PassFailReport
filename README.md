PassFailReport
==============

This is a simple report plugin for showing pass/fail jmeter results

This plug in reads the .jtl files that are created by the jmeter plugin: http://jmeter.lazerycode.com/.
This plug in will create this html file in a project's build directory: jmeter_fail_results.html.

This is an example of where the report gets created:  
C:\workspace_gary\gary_project\target\jmeter\results\reports\jmeter_fail_results.html

This is where it reads the .jtl files from:
C:\workspace_gary\gary_project\target\jmeter\results\

In order for this plugin to work, you must first run the jmeter plugin to create the .jtl files.

These are examples of how the jmeter plugin and this report plugin are defined in your maven pom:

  <properties>
		<jmeter.dir>${project.build.directory}/jmeter/results</jmeter.dir>
	</properties>

	<plugin>
		<groupId>com.lazerycode.jmeter</groupId>
		<artifactId>jmeter-maven-plugin</artifactId>
		<version>1.8.1</version>
		<configuration>
			<testResultsTimestamp>false</testResultsTimestamp>
			<ignoreResultFailures>true</ignoreResultFailures>
			<testFilesDirectory>test/jmeter</testFilesDirectory>
			<testFilesIncluded>
				<jMeterTestFile>**/*.jmx</jMeterTestFile>
			</testFilesIncluded>	
			<propertiesUser>
				<test.host>localhost</test.host>
				<test.port>6789</test.port>
			</propertiesUser>
			<!-- <overrideRootLogLevel>debug</overrideRootLogLevel> -->
		</configuration>
		<executions>
			<execution>
				<id>jmeter-tests</id>
				<phase>integration-test</phase>
				<goals>
					<goal>jmeter</goal>
				</goals>
			</execution>
		</executions>
		<dependencies>
			<dependency>
				<groupId>oracle</groupId>
				<artifactId>oracle-jdbc</artifactId>
				<version>10.1.0.2.0</version>					
			</dependency>
		</dependencies>
	</plugin>

	<plugin>
		<groupId>com.github.gtxtreme21</groupId>
		<artifactId>passfailreport-maven-plugin</artifactId>
		<version>1.1</version>
		<executions>
			<execution>
				<id>passfailreport</id>
				<goals>
					<goal>report</goal>
				</goals>
			</execution>
		</executions>
	</plugin>
	
*** Alternate configuration if you don't use default directories, you can define them ***
	<plugin>
		<groupId>com.github.gtxtreme21</groupId>
		<artifactId>passfailreport-maven-plugin</artifactId>
		<version>1.1</version>
		<configuration>
			<inputDirectory>${project.build.directory}/jmeter/results</inputDirectory>
			<outputDirectory>${project.build.directory}/jmeter/results/reports</outputDirectory>					
        </configuration>				
		<executions>
			<execution>
				<id>passfailreport</id>
				<goals>
					<goal>report</goal>
				</goals>
			</execution>
		</executions>
	</plugin>	
	
This is how to run the report: mvn pom.xml jmeter:jmeter passfailreport:report

This is a batch script that I wrote for our windows environment to run the report in the background 
and open the report in the default browser when it completes. To use this batch script, 
copy the following code into a run_jmeter_report.bat file and place this file in the 
root directory of where your jmeter reports are in your project. For example,
the bat file is in this dir on my PC: C:\workspace_gary\gary_project\test\jmeter\run_jmeter_report.bat

rem *** Start of script
chdir ..\..
echo Jmeter will take a while for all the tests to run. 
echo This is the path to the report. You can open it after all the jmeter tests have finished:
echo .\target\jmeter\results\reports\jmeter_fail_results.html
call mvn pom.xml jmeter:jmeter passfailreport:report
.\target\jmeter\results\reports\jmeter_fail_results.html
echo The report should be opened in your default browser, click enter to finish
pause
rem *** End of script

NOTE: If passfailreport:report doesn't work, try the fully qualified version com.github.gtxtreme21:passfailreport:report

LICENSE
This repository respects the GNU GENERAL PUBLIC LICENSE distribution rules. No original files were modified
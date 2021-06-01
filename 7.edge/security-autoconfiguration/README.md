1. Make sure the pom doesn't have springboot maven plugin
2. If spring boot maven plugin is used to build the jar, then
`jar tvf target/security-autoconfiguration-0.0.1-SNAPSHOT.jar | grep TokenRelayAutoConfiguration`
`output:`
`(main) security-autoconfiguration $ jar tvf target/security-autoconfiguration-0.0.1-SNAPSHOT.jar | grep TokenRelayAutoConfiguration
  2861 Mon May 31 12:54:36 IST 2021 BOOT-INF/classes/relay/TokenRelayAutoConfiguration$FeignAutoconfiguration.class
  1131 Mon May 31 12:54:36 IST 2021 BOOT-INF/classes/relay/TokenRelayAutoConfiguration.class
  1453 Mon May 31 12:54:36 IST 2021 BOOT-INF/classes/relay/TokenRelayAutoConfiguration$SecureRestTemplateConfiguration.class
   981 Mon May 31 12:54:36 IST 2021 BOOT-INF/classes/relay/TokenRelayAutoConfiguration$RestTemplateConfiguration.class`

<em>Here, I can see that the class is under BOOT-INF/classes root.</em>

3. Apparently, auto configuration classes cannot be found under BOOT-INF/classes root.

4. Comment the section in pom

`<!--	<build>-->
<!--		<plugins>-->
<!--			<plugin>-->
<!--				<groupId>org.springframework.boot</groupId>-->
<!--				<artifactId>spring-boot-maven-plugin</artifactId>-->
<!--			</plugin>-->
<!--		</plugins>-->
<!--	</build>-->`

4. Build the jar
`./mvnw clean verify`

5. Check the TokenRelayAutoConfiguration class

`jar tvf target/security-autoconfiguration-0.0.1-SNAPSHOT.jar | grep TokenRelayAutoConfiguration`

`output:`

` 2861 Mon May 31 13:01:26 IST 2021 relay/TokenRelayAutoConfiguration$FeignAutoconfiguration.class
   1131 Mon May 31 13:01:26 IST 2021 relay/TokenRelayAutoConfiguration.class
   1453 Mon May 31 13:01:26 IST 2021 relay/TokenRelayAutoConfiguration$SecureRestTemplateConfiguration.class
    981 Mon May 31 13:01:26 IST 2021 relay/TokenRelayAutoConfiguration$RestTemplateConfiguration.class`





 
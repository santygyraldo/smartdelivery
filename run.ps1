$env:JAVA_HOME = "C:\Program Files\Java\jdk-23"
$env:Path = "C:\Program Files\Java\jdk-23\bin;" + $env:Path
& ".\mvnw.cmd" spring-boot:run

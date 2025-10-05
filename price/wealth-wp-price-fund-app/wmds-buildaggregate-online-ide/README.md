
if need to deployed the project to PCF, reference below cmd:
 eg: mvn clean install -f  oes_pom.xml -Dmaven.test.skip=true -P releaseVersion,DEV_OES

  param oes_pom.xml: mapping below wmds-pcf oes_pom.xml
  param PSE_AMH_UTB: PCF config param
  eg:wmds-oes-1.0.0-OES-SIT-SNAPSHOT.war
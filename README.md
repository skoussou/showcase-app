# Installation & Start des Showcases
1. JBoss 7.1 von \\filer17l\IT170L\project\KI-HUB.A8133\03_Analyse_und_Design\12_Architektur\04_CUS@Cloud\00_JBossRedHat\JBoss_Installation\jboss-eap-7.1.0.zip 
   nach **D:\\devsbb\\** entpacken

1. Verzeichnis umbenennen von D:\devsbb\jboss-eap-7.1 nach **D:\devsbb\jboss-eap**

1. Adapter für Websphere MQ von \\filer17l\IT170L\project\KI-HUB.A8133\03_Analyse_und_Design\12_Architektur\04_CUS@Cloud\00_JBossRedHat\JBoss_Installation\wmq.jmsra.rar 
   nach **D:\devsbb\jboss-eap\standalone\deployments\wmq.jmsra.rar** kopieren

1. src/main/resources/standalone-full.xml nach **D:\devsbb\jboss-eap\standalone\configuration\standalone-full.xml** 
kopieren (vorher sinnvollerweise das Original backupen)


## Start via Cmdline
2. Im standalone-full.xml UXXXXXX ersetzen durch die eigene UNummer

2. JBoss starten mittels D:\devsbb\jboss-eap\bin\standalone.bat -c standalone-full.xml

2. Datei anpassen: src/main/resources/oracle_module.cli. Der relative Pfad von ojdbc6 muss auf ein 
   passendes lokales jar zeigen

2. D:/devsbb/jboss-eap/bin/jboss-cli.bat --connect --file=src/main/resources/oracle_module.cli

2. Das ojdbc-jar nach D:\devsbb\jboss-eap\standalone\deployments kopieren

2. Deployen von diesem Showcase mittels mvn clean install wildfly:deploy

2. Manuelles Einspielen einer Meldung mittels MQ Explorer auf die eigene PPLAN-Queue

2. Es wird eine Nachricht auf die Prognose-Queue geschickt und eine auf das Event-Topic und die Inhalte der 
   Property-Tabelle werden geloggt


## Start aus IntelliJ
3. File > Settings > Build, Execution, Deployment > Application Servers > + > JBoss Server

3. Installationspfad auswählen (D:\devsbb\jboss-eap) > OK > OK

3. Run > Edit Configurations... > + > JBoss Server > local

3. Deployments > + > Hier nun das exploded war auswählen

3. Ggf. PreBuildStep von "Build" auf "Run Maven-Goal" mit Wert "clean package" ändern

3. Im Reiter _Startup/Connection_ bei allen drei Einträgen das Feld Startup script wie folgt anpassen:
   D:\devsbb\jboss-eap\bin\standalone.bat -c ../../../workspaces/kihub-jboss-showcase/src/main/resources/standalone-full.xml
   Der Pfad zum standalone-full.xml ist relativ zum JBoss-Installation-Folder anzugeben!
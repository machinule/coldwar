# coldwar

Install steps:

1. Download eclipse for Java devs: https://eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/mars/1/eclipse-java-mars-1-win32-x86_64.zip
2. File > Import > Projects from Git
3. Clone URI
4. URI = https://github.com/machinule/coldwar.git
5. Import as General Project
6. Finish
7. Right click on project > Delete
8. Make sure "Delete Project Contents on Disk" is NOT checked and click OK
9. File > Import > Gradle > Gradle Project
10. Select the root directory of the project (it should have gradle.build in it)
11. Finish. It should create three projects: coldwar, coldwar-game-core and coldwar-game-desktop
12. Right click on coldwar-game-core > Properties > Resource > Resource Filters : Delete "Project relative path matches 'build'"
13. Java Build Path > Add Folder : "build/generated/source/proto/main/java"
14. Open cold-war-desktop > src > DesktopLauncher.java
15. Hit the run button
16. If it has an error about finding a file: Right click on cold-war-desktop > Run As > Run Configurations > 
17. Under "Arguments" > Working Directory set it to "${workspace_loc:coldwar-game-desktop/assets}"


cd RobotCode/snobot2018
call gradlew cleanEclipse
call gradlew eclipse
cd ../..


cd NetworkTableViewer/2018SmartDashboardWidgets
call gradlew cleanEclipse
call gradlew eclipse
cd ../..


cd NetworkTableViewer/2018ShuffleboardWidgets
call gradlew cleanEclipse
call gradlew eclipse
cd ../..

:: Legacy, shouldn't be there anymore
cd NetworkTableViewer/2018NetworkTableViewer
DEL .classpath
DEL .project
DEL 2018NetworkTableViewer.iml
DEL 2018NetworkTableViewer.ipr
DEL 2018NetworkTableViewer.iws
cd ../..

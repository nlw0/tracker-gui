
lazy val root = (project in file(".")).aggregate(scalavision, tracker_gui)

lazy val scalavision = project

lazy val tracker_gui = project.in(file("tracker-gui")).dependsOn(scalavision)

javaOptions in run += "-Djava.library.path=/usr/local/share/OpenCV/java"
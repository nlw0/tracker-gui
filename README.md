# tracker-gui
GUI to my Scala+OpenCV based tracker library (scalavision).

The project contains an interactive GUI application that feeds images from the webcam to the tracker, and presents the results as a regular desktop app using ScalaFX. There is also a Web version, a server that takes images captured by a browser, and return the tracking results using HTTP. They both use the same underlying tracker code that receives a stream of images as input.

We use AKAZE features, do some very basic and traditional matching, estimate translation from the matching points, and then just plot the estimated direction as a thick green line on the top left. Next planned development steps is to improve on the tracking, and then start some basic applications such as:
  - Stitching images to create panoramas.
  - Extract texture from flat object or labels. "Scan" photographs and documents.

[[https://github.com/nlw0/tracker-gui/blob/master/scalavision_demo.png|alt=Matched features and estimated translation.]]

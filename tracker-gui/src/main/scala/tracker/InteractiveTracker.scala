package tracker

import org.opencv.core._
import org.opencv.videoio.{VideoCapture, Videoio}
import visionlib.TestKeypointExtractor

import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.image.{ImageView, PixelFormat, WritableImage}
import scalafx.scene.layout.BorderPane
import scalafx.scene.{Group, Scene}

class WebcamProcessor(transformImage: Mat => Option[Mat]) {

  val capture = new VideoCapture(0)

  capture.set(Videoio.CAP_PROP_MODE, Videoio.CAP_MODE_RGB)

  val w: Int = capture.get(Videoio.CAP_PROP_FRAME_WIDTH).toInt
  val h: Int = capture.get(Videoio.CAP_PROP_FRAME_HEIGHT).toInt

  def next = if (!capture.isOpened) None else {
    val cameraInput = new MatOfByte()
    capture.read(cameraInput)
    if (cameraInput.empty()) None else Some(cameraInput) flatMap transformImage
  }
}

object InteractiveTracker extends JFXApp {
  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

  val frameFreq = new FrequencyMeter()

  // val webcam = new WebcamProcessor(TestKeypointExtractor.findAndDrawFeatures)
  val webcam = new WebcamProcessor(Tracker.track)

  val imagePane = new Group
  val wi = new WritableImage(webcam.w, webcam.h)
  val iv = new ImageView(wi)
  imagePane.children = List(iv)


  stage = new PrimaryStage {
    title = "scalavision"

    scene = new Scene {
      root = new BorderPane {
        center = imagePane
      }

      val timer = AnimationTimer(nextFrame)
      timer.start
    }
  }

  def nextFrame(t: Long): Unit = {
    println(f"${frameFreq.update(t)} fps")
    webcam.next foreach displayImage
  }

  def displayImage(mat: Mat): Unit = {
    val CHANNELS = 3
    val STRIDE = mat.cols * CHANNELS
    val byteArray = new Array[Byte](mat.total().toInt * CHANNELS)
    mat.get(0, 0, byteArray)
    wi.pixelWriter.setPixels(0, 0, mat.cols, mat.rows, PixelFormat.getByteRgbInstance, byteArray, 0, STRIDE)
  }

}

object Tracker {
  var lastImage: Option[Mat] = None

  def track(image: Mat) = {
    val out = lastImage map (li => TestKeypointExtractor.findAndDrawTracks(li, image))

    lastImage = Some(image)
    out
  }
}

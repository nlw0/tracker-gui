package tracker

import org.opencv.core._
import org.opencv.videoio.{VideoCapture, Videoio}
import visionlib.{ExtractedKeypoints, ImageAndDescriptors, TestKeypointExtractor, Tracker}

import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.image.{ImageView, PixelFormat, WritableImage}
import scalafx.scene.layout.BorderPane
import scalafx.scene.{Group, Scene}

object WebcamStream extends Iterator[Option[Mat]] {

  val capture = new VideoCapture(0)

  capture.set(Videoio.CAP_PROP_MODE, Videoio.CAP_MODE_RGB)

  val w: Int = capture.get(Videoio.CAP_PROP_FRAME_WIDTH).toInt
  val h: Int = capture.get(Videoio.CAP_PROP_FRAME_HEIGHT).toInt

  def hasNext = true

  def next = if (!capture.isOpened) None else {
    val cameraInput = new MatOfByte()
    capture.read(cameraInput)
    if (cameraInput.empty()) None else Some(cameraInput)
  }
}

object InteractiveTracker extends JFXApp {
  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

  val frameFreq = new FrequencyMeter()

  val imgPairs = WebcamStream.flatten map TestKeypointExtractor.extractFeatures sliding 2

  val imagePane = new Group
  val wi = new WritableImage(WebcamStream.w, WebcamStream.h)
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
    val ii = imgPairs.next
    Tracker.track(ii(0), ii(1)) foreach displayImage
  }

  def displayImage(mat: Mat): Unit = {
    val CHANNELS = 3
    val STRIDE = mat.cols * CHANNELS
    val byteArray = new Array[Byte](mat.total().toInt * CHANNELS)
    mat.get(0, 0, byteArray)
    wi.pixelWriter.setPixels(0, 0, mat.cols, mat.rows, PixelFormat.getByteRgbInstance, byteArray, 0, STRIDE)
  }

}




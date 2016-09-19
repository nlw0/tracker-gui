package tracker

import org.opencv.core._
import org.opencv.imgproc.Imgproc
import org.opencv.videoio.{VideoCapture, Videoio}

import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.image.{ImageView, PixelFormat, WritableImage}
import scalafx.scene.layout.BorderPane
import scalafx.scene.{Group, Scene}

object OpenCameraTest extends JFXApp {
  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

  val matOrig = new MatOfByte()

  val capture = new VideoCapture(0)

  capture.set(Videoio.CAP_PROP_MODE, Videoio.CAP_MODE_RGB)

  val w: Int = capture.get(Videoio.CAP_PROP_FRAME_WIDTH).toInt
  val h: Int = capture.get(Videoio.CAP_PROP_FRAME_HEIGHT).toInt

  val wi = new WritableImage(w, h)
  val iv = new ImageView(wi)
  val rootPane = new Group
  rootPane.children = List(iv)

  val was = Array.fill[Byte](h * w * 3)(0.toByte)
  wi.pixelWriter.setPixels(0, 0, w, h, PixelFormat.getByteRgbInstance, was, 0, w)

  def displayImage(mat: Mat): Unit = {
    val CHANNELS = 3
    val STRIDE = mat.cols * CHANNELS
    val byteArray = new Array[Byte](mat.total().toInt * CHANNELS)
    mat.get(0, 0, byteArray)
    wi.pixelWriter.setPixels(0, 0, mat.cols, mat.rows, PixelFormat.getByteRgbInstance, byteArray, 0, STRIDE)
  }

  def processImage(matOrig: Mat) = {
    Imgproc.circle(matOrig, new Point(10, 10), 3, new Scalar(255, 0, 0), -1)
    matOrig
  }

  def captureImage(capture: VideoCapture) =
    if (capture.isOpened) {
      capture.read(matOrig)
      if (!matOrig.empty()) Some(matOrig) else None
    } else None

  val frameFreq = new FrequencyMeter()

  stage = new PrimaryStage {
    title = "scalavision"

    scene = new Scene(w, h) {
      root = rootPane

      val timer = AnimationTimer { t =>
        frameFreq.update(t)
        println(f"$frameFreq fps")

        captureImage(capture) map processImage foreach displayImage
      }

      timer.start
    }
  }
}

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


object InteractiveTracker extends JFXApp with TestKeypointExtractor {
  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

  val frameFreq = new FrequencyMeter()

  val imgPairs = WebcamStream.flatten map extractFeatures sliding 2

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
    Tracker.track(imgPairs.next: _*) foreach displayImage
  }

  def displayImage(mat: Mat): Unit = {
    val CHANNELS = 3
    val STRIDE = mat.cols * CHANNELS
    val byteArray = new Array[Byte](mat.total().toInt * CHANNELS)
    mat.get(0, 0, byteArray)
    wi.pixelWriter.setPixels(0, 0, mat.cols, mat.rows, PixelFormat.getByteRgbInstance, byteArray, 0, STRIDE)
  }

}
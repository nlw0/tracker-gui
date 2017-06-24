package tracker

import org.opencv.core.{Mat, MatOfByte}
import org.opencv.videoio.{VideoCapture, Videoio}
import org.opencv.imgproc.Imgproc

object WebcamStream extends Iterator[Option[Mat]] {

  val capture = new VideoCapture(0)

  capture.set(Videoio.CAP_PROP_MODE, Videoio.CAP_MODE_RGB)

  val w: Int = capture.get(Videoio.CAP_PROP_FRAME_WIDTH).toInt
  val h: Int = capture.get(Videoio.CAP_PROP_FRAME_HEIGHT).toInt

  def hasNext = true

  def next = if (!capture.isOpened) None else {
    val cameraInput = new MatOfByte()
    capture.read(cameraInput)
    if (cameraInput.empty()) None else {
      val matrgb = new Mat()
      Imgproc.cvtColor(cameraInput, matrgb, Imgproc.COLOR_BGR2RGB)
      Some(matrgb)
    }
  }
}

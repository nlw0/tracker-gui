package trackerhello

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.image.{ImageView, PixelFormat, WritableImage}
import scalafx.scene.{Group, Scene}

/**
  * Example adapted from code showed in [[http://docs.oracle.com/javafx/2/canvas/jfxpub-canvas.htm]].
  */
object ScalaFXHelloWorld extends JFXApp {

  val wi = new WritableImage(8, 8)
  val iv = new ImageView(wi)
  val rootPane = new Group
  rootPane.children = List(iv)
  stage = new PrimaryStage {
    title = "Canvas Doodle Test"
    scene = new Scene(8, 8) {
      root = rootPane
    }
  }
  val pw = wi.pixelWriter
  val pixels = Array.fill[Byte](3 * 8 * 8)(0)
  val pp = pixels.update(2, 255.toByte)
  pw.setPixels(0, 0, 8, 8, PixelFormat.getByteRgbInstance, pixels, 0, 8)
}
package tracker

/** Class to measure frequency of some event. Either use the `tick` method to get system time of each event, or
  * provide it explicitly with the `update` method. Time since last even is stored, as well as a current estimate of
  * period between events. Estimate is updated using a IIR filter, and rate of change can be controlled by the
  * `damping` factor.
  */
class FrequencyMeter(damping: Long = 16L) {
  var period = 100000L
  var lastTime = System.nanoTime

  def update(time: Long): Unit = {
    period += (time - lastTime - period) / damping
    lastTime = time
  }

  def tick() = update(System.nanoTime)

  override def toString = f"${1e9 / period}%.1f"
}

package sizeof

import com.madhukaraphatak.sizeof.SizeEstimator
import org.slf4j.LoggerFactory

import java.util
import java.util.ArrayList

object Main extends App {
  private val logger = LoggerFactory.getLogger(Main.getClass)

  class DummyClass1 {}

  class DummyClass2 {
    val x: Int = 0
  }

  class DummyClass3 {
    val x: Int = 0
    val y: Double = 0.0
  }

  class DummyClass4(val d: DummyClass3) {
    val x: Int = 0
  }

  object DummyString {
    def apply(str: String): DummyString = new DummyString(str.toArray)
  }

  class DummyString(val arr: Array[Char]) {
    override val hashCode: Int = 0
    // JDK-7 has an extra hash32 field http://hg.openjdk.java.net/jdk7u/jdk7u6/jdk/rev/11987e85555f
    @transient val hash32: Int = 0
  }

  checkSizesMatch(DummyString(""))
  checkSizesMatch(DummyString("a"))
  checkSizesMatch(DummyString("ab"))
  checkSizesMatch(DummyString("abcdefgh"))
  checkSizesMatch(Array.fill(10)(new DummyClass1))
  checkSizesMatch(Array.fill(10)(new DummyClass2))
  checkSizesMatch(Array.fill(10)(new DummyClass3))
  checkSizesMatch(Array(new DummyClass1, new DummyClass2))
  checkSizesMatch(Array.fill(1000)(new DummyClass1))
  checkSizesMatch(Array.fill(1000)(new DummyClass2))
  checkSizesMatch(Array.fill(1000)(new DummyClass3))

  val javaList = new util.ArrayList[Long]()
  for (i <- 0 until 10) {
    javaList.add(java.lang.Long.valueOf(i))
  }
  checkSizesMatch(javaList)
  for (i <- 0 until 10) {
    javaList.add(java.lang.Long.valueOf(i))
  }
  checkSizesMatch(javaList)

  def checkSizesMatch[T](t: T): Unit = {
    val psize = JavaSizeEstimator.estimate(t)
    val ssize = SparkSizeEstimator.estimate(t.asInstanceOf[AnyRef])
    val msize = SizeEstimator.estimate(t.asInstanceOf[AnyRef])
    if (psize != ssize) {
      logger.warn(s"values do no match: $psize (JavaNew) and $ssize (Spark)")
    }
    if (msize != ssize) {
      logger.warn(s"values do no match: $msize (madhukaraphatak) and $ssize (Spark)")
    }
  }

}

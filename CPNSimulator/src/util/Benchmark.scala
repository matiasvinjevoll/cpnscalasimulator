package util

import java.lang.management._

object Benchmark {
  
  /**
   * "CPU time" is user time plus system time. It's the total time spent using a CPU for your application.
   * Returns time in ms.
   */
  def getCpuTime() = {
    val bean = ManagementFactory.getThreadMXBean
    if (bean.isCurrentThreadCpuTimeSupported)
      bean.getCurrentThreadCpuTime / 1000000
    else 0
  }
  
  /**
   * "User time" is the time spent running your application's own code.
   * Returns time in ms.
   */
  def getUserTime() = {
  	val bean = ManagementFactory.getThreadMXBean
  			if (bean.isCurrentThreadCpuTimeSupported)
  				bean.getCurrentThreadUserTime / 1000000
  				else 0
  }
  
  /**
   * "System time" is the time spent running OS code on behalf of your application (such as for I/O).
   * Returns time in ms.
   */
  def getSystemTime() = {
  	val bean = ManagementFactory.getThreadMXBean
  			if (bean.isCurrentThreadCpuTimeSupported)
  				(bean.getCurrentThreadCpuTime - bean.getCurrentThreadUserTime) / 1000000
  				else 0
  }
  

}
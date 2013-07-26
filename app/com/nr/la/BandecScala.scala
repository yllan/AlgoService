
package com.nr.la


import com.nr.NRUtil._
import java.lang.Math._


/**
 * Band-Diagonal Systems - PTC
 * Object for solving linear equations A*x = b for a band-diagonal matrix A,
 * using LU decomposition
 */ 
object BandecScala {

def banmul( a: Array[Array[Double]], m1: Int, m2: Int, x: Array[Double], b: Array[Double])  =
    {
      com.nr.la.Bandec.banmul(a, m1, m2, x, b)
    }
    
  
}

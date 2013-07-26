
package com.nr.la

/*
 // Example of solving a system with NR LU
 var A = D2D(3, 3, 4.5, 6.7, -0.5, 0.5, 4.5, 3.4, -0.56, 0.6, 0.3)
 var b = D2D(3, 1, -0.3, 1.2, 0.4)
 var x = com.nr.la.LUnr.LUSolve(A, b)
 var shoulsBeZero = A*x-b
 
 var b1 = D1D(-0.3, 1.2, 0.4)   // as Array[Double]
 var x1 = com.nr.la.LUnr.LUSolve(A, b1)
 var shouldBeZero1 = A*x1-b1
 
 var detLU = com.nr.la.LUnr.LUdet(A)  // compute the determinant
 var invLU = com.nr.la.LUnr.LUinv(A)  // inverse the matrix
 var shouldBeIdentity = A*invLU
 */
object LUnr {
  def LUSolve( A: Array[Array[Double]], b: Array[Array[Double]]) =   {
    var LUObj = new com.nr.la.LUdcmp(A)   // construct the LU object and perform LU decomposition of matrix A
    var brows = b.length
    var bcols = b(0).length
    var x = Array.ofDim[Double](brows, bcols)
    LUObj.solve(b, x)
    x
    }
   
  def LUSolve( A: Array[Array[Double]], b: Array[Double]) =   {
    var LUObj = new com.nr.la.LUdcmp(A)   // construct the LU object and perform LU decomposition of matrix A
    var brows = b.length
    var bcols = 1
    var x = new Array[Double](brows)
    LUObj.solve(b, x)
    x
    }
    
  def LUdet(A: Array[Array[Double]]) = {
    var LUObj = new com.nr.la.LUdcmp(A) // construct the LU object and perform LU decomposition of matrix A
    LUObj.det()
    }
  
  def LUinv(A: Array[Array[Double]]) = {
    var LUObj = new com.nr.la.LUdcmp(A) // construct the LU object and perform LU decomposition of matrix A
    LUObj.inverse()
    }
    
    
  
   
}

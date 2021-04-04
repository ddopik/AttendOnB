package com.spidersholidays.attendonb

import android.annotation.SuppressLint
import android.util.Log

class TestCases {


    @SuppressLint("LongLogTag")
    fun getPager() {
        Log.e("TestCases Value before---->", TestSingletonClass.getValue().toString())
        TestSingletonClass.setValX(19)

        Log.e("TestCases Value after---->", TestSingletonClass.getValue().toString())

        solution(arrayOf(1,2,5,6,77,9))
    }


    fun solution(mArra: Array<Int>) {

/// get the biggest number

/// loop throug supplied array
// if number exsist decrese it by 1 --> repeat the loop
// if not found this ---> this is the desired number


        for (mNumber in mArra) {
            for ((index, arrayNum) in  mArra.withIndex()) {
                if (mNumber<arrayNum ){
                  break
              }
                if(index == mArra.size-1){
                    println("$mNumber is the biggest number")
                }
            }
        }


    }

}

class TestCases2 {


    @SuppressLint("LongLogTag")
    fun getPager() {
        val cc = NetworkStatus.customstatus("ss")


        Log.e("TestCases2 Value after---->", TestSingletonClass.getValue().toString())
    }

}


object TestSingletonClass {

    var x = 9


    fun setValX(mValue: Int) {
        x = mValue
    }

    fun getValue(): Int {
        return x
    }


}


sealed class NetworkStatus {

    data class Loading(var loading: Boolean) : NetworkStatus()

    data class CustomSignal(var signal: String) : NetworkStatus()

//    data class CustomSignalDetailed(var signal: ErrorCaseData) : NetworkStatus()

    data class Failure(val e: Throwable) : NetworkStatus()

    companion object {

        fun loading(isLoading: Boolean): NetworkStatus = Loading(isLoading)

        fun customstatus(signal: String): NetworkStatus = CustomSignal(signal)

//        fun customStatusDetailed(signals: ErrorCaseData): NetworkStatus = CustomSignalDetailed(signals)

        fun failure(e: Throwable): NetworkStatus = Failure(e)
    }

}
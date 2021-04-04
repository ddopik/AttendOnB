package com.spidersholidays.attendonb.ui.vacation.newvacation.viewmodel

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.spidersholidays.attendonb.network.BaseNetWorkApi
import com.spidersholidays.attendonb.ui.vacation.newvacation.model.NewVacationDataResponse
import io.reactivex.Maybe
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.api.mockito.PowerMockito.`when`
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.robolectric.annotation.Config


//@RunWith(PowerMockRunner::class)
//@PrepareForTest(BaseNetWorkApi::class)


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class NewVacationViewModelTest {



    /**
     * Rule ---> can do useful things before and/or after the  test execution
     * as the annotated obj should have scripts that run according to before/Start logic
     * InstantTaskExecutorRule is a JUnit Rule. When you use it with the @get:Rule annotation, it causes some code in the InstantTaskExecutorRule
     * */
    @get:Rule
    val instantTaskExceptedRule = InstantTaskExecutorRule()


    lateinit var newVacationViewModel: NewVacationViewModel
    var greet: String.() -> Unit = {
        println("Hello $this")
        buildString{

        }
    }

    fun buildString(action: StringBuilder.() -> Unit): String {
        val stringBuilder = StringBuilder()
        action(stringBuilder)
        return stringBuilder.toString()
    }
    @Before
    fun setup() {

        /**
         * Required to initialize and inject variables with @Mock Annotation
         * */
        MockitoAnnotations.initMocks(this)

        /**
         *Installing dependencies with Activity Context
         * */
//        ActivityScenario.launch(NewVacationActivity::class.java).onActivity { activity ->
//            newVacationViewModel = NewVacationViewModel.getInstance(activity)!!
//        }
        greet("also my extintion  text")

    }
    @Test
    fun main(){
        solution(arrayOf(1,2,5,6,77,9))
     }
    fun solution(mArra: Array<Int>) {

/// get the biggest number

/// loop throug supplied array
// if number exsist decrese it by 1 --> repeat the loop
// if not found this ---> this is the desired number

var biggestNum =0
        for (mNumber in mArra) {
            for ((index, arrayNum) in  mArra.withIndex()) {
                if (mNumber<arrayNum ){
                    break
                }
                if(index == mArra.size-1){
                    biggestNum = mNumber
                    println("$mNumber is the biggest number")
                }
            }
        }

        for (mNumber in mArra) {
            for ((index, arrayNum) in  mArra.withIndex()) {
                if ((biggestNum-1) >= arrayNum  ){
                    biggestNum -= 1
                    break
                }
                if(index == mArra.size-1){
                    biggestNum = mNumber
                    println("$mNumber is the smallest not exist number")
                }
            }
        }
    }
    @Test
    fun getVacationFormData() {
        /**
         * When you need to mock static  dependencies
         * */
        PowerMockito.mockStatic(BaseNetWorkApi::class.java)




        `when`(BaseNetWorkApi.getNewVacationData()).thenAnswer {
            return@thenAnswer Maybe.just(ArgumentMatchers.any<NewVacationDataResponse>())
        }

        //        Assert.assertNotNull( newVacationViewModel.getVacationFormData())
    }


    @Test
    fun testProgressStateLiveData() {
        /*
        To test LiveData it's recommended you do two things:
        1-- Use InstantTaskExecutorRule
        2-- Ensure LiveData observatio
        ***/
        /**
         * Your next step is to make sure the LiveData you're testing is observed
         * */
        val observer = Mockito.mock(Observer::class.java) as Observer<Boolean>  //<---- another valid experation

        try {


            /**
             * When you use LiveData, you commonly have an activity or fragment (LifecycleOwner) observe the LiveData.
             * To get the expected LiveData behavior for your view model's LiveData, you need to observe the LiveData with a LifecycleOwner.
             * Solve ----> you can use the "observeForever"
             *  as :   ensures the LiveData is constantly observed, without needing a LifecycleOwner
             * */
//        val observe = Observer<Boolean> {}  //<---- another valid experation

            newVacationViewModel = NewVacationViewModel()

            newVacationViewModel.onVacationFormProgressChanged().observeForever(observer)

            /**
             * Find action that notify of do any operation to your (Observed/selected ) varuable
             * */
            newVacationViewModel.testtest()

            Assert.assertEquals(this.newVacationViewModel.onVacationFormProgressChanged().value, true)
        } finally {

            newVacationViewModel.onVacationFormProgressChanged().removeObserver(observer)
        }

/**
 *         /// what next ---> verify() example
 *                       -->  involve Hamcrest
 *         \             -->  involve Robolectric Testing library
 *                       -->  involve espresso: of Ui Test
 * */


    }

}
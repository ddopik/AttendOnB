package com.spidersholidays.attendonb.ui.vacation.newvacation.viewmodel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule

 import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
 import com.spidersholidays.attendonb.ui.vacation.newvacation.NewVacationActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.Robolectric
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class NewVacationViewModelTest {


    /**
     * Rule ---> can do useful things before and/or after the  test execution
     * as the annotated obj should have scripts that run according to before/Start logic
     * */
    @get:Rule
    val instantTaskExceptedRule  = InstantTaskExecutorRule()

    lateinit var newVacationViewModel :NewVacationViewModel
//    val activityRule = ActivityTestRule(NewVacationActivity::class.java, true, true)

    val viewModel = mock(NewVacationViewModel::class.java)


    @Rule
    @JvmField
    val activityRule = ActivityTestRule<NewVacationActivity>(NewVacationActivity::class.java, true, false)
    @Before
    fun setup(){
      val activity: NewVacationActivity = Robolectric.setupActivity(NewVacationActivity::class.java)
//         val newVacationViewModel = NewVacationViewModel.getInstance(activity)
//        newVacationViewModel =  NewVacationViewModel(ApplicationProvider.getApplicationContext<NewVacationActivity>())!!
     }
    @Test
    fun getVacationFormData() {
      newVacationViewModel.getVacationFormData()
    }



}
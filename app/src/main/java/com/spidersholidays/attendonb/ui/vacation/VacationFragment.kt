package com.spidersholidays.attendonb.ui.vacation

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
 import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.base.ViewPagerAdapter
import com.spidersholidays.attendonb.base.ViewPagerFragment
import com.spidersholidays.attendonb.ui.vacation.approved.ApprovedFragment
import com.spidersholidays.attendonb.ui.vacation.newvacation.NewVacationActivity
import com.spidersholidays.attendonb.ui.vacation.pending.PendingFragment
import com.spidersholidays.attendonb.ui.vacation.rejected.RejectFragment
import kotlinx.android.synthetic.main.fragment_vacation.*

class VacationFragment : ViewPagerFragment() {



    companion object {
        fun getInstance(): VacationFragment {

            return VacationFragment()
        }
    }



    override fun getToolbar(): Toolbar? {
        return null
    }

    override val mainLayout: Int
        get() = R.layout.fragment_vacation
    override val fragments: List<Fragment>
        get() {
            "asdas".capitalize()
            return mutableListOf(PendingFragment.getInstance(), ApprovedFragment.getInstance(), RejectFragment.getInstance())
        }
    override val fragmentsTitles: ArrayList<String>
        get() {
            val titles = ArrayList<String>(3)
            titles.add(resources.getString(R.string.pending))
            titles.add(resources.getString(R.string.approved))
            titles.add(resources.getString(R.string.rejected))
            return titles
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_add_new_vacation.setOnClickListener {
            val intent =Intent(activity,NewVacationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

    }

    override val mainView: Int
        get() =  R.layout.fragment_vacation
    override val tabLayout: Int
        get() = R.id.vacation_tab_layout
    override val viewPager: Int
        get() =  R.id.vacation_view_pager
    override val viewUpButton: Boolean?
        get() = false
    override val viewPagerAdapter: ViewPagerAdapter
        get() = ViewPagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)


}
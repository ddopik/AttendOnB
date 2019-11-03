package com.attendance735.attendonb.ui.vacation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.viewpager.widget.ViewPager
import com.attendance735.attendonb.R
import com.attendance735.attendonb.base.ViewPagerAdapter
import com.attendance735.attendonb.base.ViewPagerFragment
import com.attendance735.attendonb.ui.vacation.approved.ApprovedFragment
import com.attendance735.attendonb.ui.vacation.pending.PedingFragment
import com.attendance735.attendonb.ui.vacation.rejected.RejectFragment
import com.google.android.material.tabs.TabLayout
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
            return mutableListOf(PedingFragment.getInstance(), ApprovedFragment.getInstance(), RejectFragment.getInstance())
        }
    override val fragmentsTitles: ArrayList<String>
        get() {
            val titles = ArrayList<String>(3)
            titles.add(resources.getString(R.string.pending))
            titles.add(resources.getString(R.string.approved))
            titles.add(resources.getString(R.string.rejected))
            return titles
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
        get() = ViewPagerAdapter(activity?.supportFragmentManager!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
}
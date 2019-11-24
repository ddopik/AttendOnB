package com.spidersholidays.attendonb.ui.vacationmangment.ui

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.base.ViewPagerAdapter
import com.spidersholidays.attendonb.base.ViewPagerFragment

class ManagementVacationFragment : ViewPagerFragment() {


    companion object {
        fun getInstance(): ManagementVacationFragment {

            return ManagementVacationFragment()
        }
    }


    override fun getToolbar(): Toolbar? {
        return null
    }

    override val mainLayout: Int
        get() = R.layout.fragment_management_vacation
    override val fragments: List<Fragment>
        get() {
            return mutableListOf(MangmentPendingVacationFragment.getInstance(), ManagementApprovedVacationFragment.getInstance(), ManagementRejectedVacationFragment.getInstance())
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
        get() = R.layout.fragment_management_vacation
    override val tabLayout: Int
        get() = R.id.management_vacation_tab_layout
    override val viewPager: Int
        get() = R.id.management_vacation_view_pager
    override val viewUpButton: Boolean?
        get() = false
    override val viewPagerAdapter: ViewPagerAdapter
        get() = ViewPagerAdapter(childFragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)

}
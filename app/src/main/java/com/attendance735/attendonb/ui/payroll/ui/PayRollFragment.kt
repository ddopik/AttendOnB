package com.attendance735.attendonb.ui.payroll.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import bases.BaseFragment
import com.attendance735.attendonb.R
import com.attendance735.attendonb.base.commonModel.PayRoll
import com.attendance735.attendonb.ui.payroll.viewmodel.PayRollViewModel
import kotlinx.android.synthetic.main.fragment_pay_roll.*

class PayRollFragment : BaseFragment() {


    private lateinit var payRollAdapter: PayRollAdapter
    private val payRollList: MutableList<PayRoll> = mutableListOf()
    private var payRollViewModel: PayRollViewModel? = null

    companion object {
        private var INSTANCE: PayRollFragment? = null

        fun getInstance(): PayRollFragment {


            if (INSTANCE == null) {
                INSTANCE = PayRollFragment()
            }

            return INSTANCE!!
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return layoutInflater.inflate(R.layout.fragment_pay_roll, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        payRollViewModel = PayRollViewModel.getInstance(this)

        val payRollAdapterListener = object : PayRollAdapter.PayRollAdapterClickListener {
            override fun onPayRollItemClickListener(payRoll: PayRoll) {
                val intent =Intent(activity,PayRollDetailsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.putExtra(PayRollDetailsActivity.PAY_ROL_OBJ,payRoll)
                startActivity(intent)
            }
        }
        payRollAdapter = PayRollAdapter(payRollList, payRollAdapterListener)
        rv_pay_roll.adapter = payRollAdapter

        initObservers()


        payRollViewModel?.getPayRollData()
    }

    override fun initObservers() {
        payRollViewModel?.onPayRollProgressChanged()?.observe(viewLifecycleOwner, Observer {
            if (it) {
                payroll_progress.visibility = View.VISIBLE
            } else {
                payroll_progress.visibility = View.GONE

            }

        })

        payRollViewModel?.onPayRollDataChanged()?.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.size >0){
                    no_pay_roll_stats_msg.visibility =View.GONE
                    payRollList.clear()
                    payRollList.addAll(it)
                    payRollAdapter.notifyDataSetChanged()
                }else{
                    no_pay_roll_stats_msg.visibility =View.VISIBLE
                }
            }.apply {
                if (it == null)
                    no_pay_roll_stats_msg.visibility =View.VISIBLE

            }



        })

    }
}
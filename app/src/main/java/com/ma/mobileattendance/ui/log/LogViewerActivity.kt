package com.ma.mobileattendance.ui.log

import android.os.Bundle
import com.ma.mobileattendance.BaseActivity
import com.ma.mobileattendance.databinding.ActivityLogViewerBinding


class LogViewerActivity : BaseActivity() {
    private lateinit var binding: ActivityLogViewerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val log_summary = intent.getStringExtra("log_summary")
        if (!log_summary.isNullOrEmpty()) {
            binding.textLog.text = log_summary
        }
    }
}
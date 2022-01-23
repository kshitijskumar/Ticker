package com.example.ticker.core.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.ticker.R
import com.example.ticker.core.adapter.TickerTimeAdapter
import com.example.ticker.core.adapter.ZoomCenterItemLayoutManager
import com.example.ticker.databinding.LayoutTickerBinding
import kotlin.math.max

class Ticker @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    private val defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val HOURS_12_FORMAT = 12
        private const val HOURS_24_FORMAT = 24
    }

    private val binding: LayoutTickerBinding =
        LayoutTickerBinding.inflate(LayoutInflater.from(context), this, true)

    private val hoursAdapter: TickerTimeAdapter by lazy {
        TickerTimeAdapter()
    }

    private val minutesAdapter: TickerTimeAdapter by lazy {
        TickerTimeAdapter()
    }

    private var shouldBeIn12HourFormat: Boolean = true
    private var isAmSelected: Boolean = true
    private var currentlySelectedHour: String = ""
    private var currentlySelectedMinute: String = ""

    private var minutesInterval: Int = 1

    init {
        initConfigurations()
        initViews()
        initHoursAndMinutesList()
    }

    private fun initConfigurations() {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Ticker,
            defStyleAttr,
            0
        ).apply {
            try {
                val hoursFormat = this.getInt(R.styleable.Ticker_hoursFormat, HOURS_12_FORMAT)
                shouldBeIn12HourFormat = hoursFormat == HOURS_12_FORMAT
                minutesInterval = max(this.getInt(R.styleable.Ticker_minutesInterval, 1), 1)
                isAmSelected = this.getBoolean(R.styleable.Ticker_shouldAmSelected, true)
            } finally {
                recycle()
            }
        }
    }

    private fun initViews() {
        binding.apply {
            tvAm.isVisible = shouldBeIn12HourFormat
            tvPm.isVisible = shouldBeIn12HourFormat
        }
        binding.tvAm.setOnClickListener {
            isAmSelected = true
            binding.tvAm.setTextColor(ResourcesCompat.getColor(resources, R.color.black, null))
            binding.tvPm.setTextColor(ResourcesCompat.getColor(resources, R.color.grey, null))
        }
        binding.tvPm.setOnClickListener {
            isAmSelected = false
            binding.tvPm.setTextColor(ResourcesCompat.getColor(resources, R.color.black, null))
            binding.tvAm.setTextColor(ResourcesCompat.getColor(resources, R.color.grey, null))
        }
        if (isAmSelected) {
            binding.tvAm.performClick()
        } else {
            binding.tvPm.performClick()
        }
    }

    private fun initHoursAndMinutesList() {
        val minutesList = (0 until 60 step minutesInterval).toList()
        val hoursList = if (shouldBeIn12HourFormat) {
            (1..12).toList()
        } else {
            (0..24).toList()
        }
        initTickerRecyclerViews(
            binding.rvHours,
            hoursAdapter,
            hoursList,
            true
        )
        initTickerRecyclerViews(
            binding.rvMinutes,
            minutesAdapter,
            minutesList,
            false
        )
        setupTopBottomPadding()
    }

    private fun initTickerRecyclerViews(
        rv: RecyclerView,
        adapter: TickerTimeAdapter,
        unitsList: List<Int>,
        isHours: Boolean,
    ) {
        rv.apply {
            layoutManager = ZoomCenterItemLayoutManager(context)
            this.adapter = adapter
        }
        val linearSnapHelper = LinearSnapHelper()
        linearSnapHelper.attachToRecyclerView(rv)

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    updateTextColorForSelectedTimeText(recyclerView, R.color.time_unit_selected_color, isHours)
                } else {
                    updateTextColorForSelectedTimeText(recyclerView, R.color.time_unit_unselected_color, isHours)
                }
            }

            private fun updateTextColorForSelectedTimeText(
                recyclerView: RecyclerView,
                @ColorRes textColorRes: Int,
                isHours: Boolean
            ) {
                val snappedChildView =
                    linearSnapHelper.findSnapView(recyclerView.layoutManager) ?: return
                val snappedViewHolder = rv.getChildViewHolder(snappedChildView)
                if (snappedViewHolder is TickerTimeAdapter.DefaultTickerViewHolder) {
                    snappedViewHolder.binding.root.setTextColor(
                        ResourcesCompat.getColor(
                            resources,
                            textColorRes,
                        null
                        )
                    )
                    val timeString = snappedViewHolder.binding.root.text.toString()
                    updateCurrentlySelectedValues(isHours, timeString, textColorRes)

                }
            }
        })

        adapter.submitList(unitsList)
        scrollToCurrentTime(rv, isHours)
    }

    private fun updateCurrentlySelectedValues(isHours: Boolean, timeString: String, @ColorRes appliedColorRes: Int) {
        if (appliedColorRes == R.color.time_unit_selected_color) {
            if (isHours) {
                currentlySelectedHour = timeString
            } else {
                currentlySelectedMinute = timeString
            }
        }
    }

    private fun setupTopBottomPadding() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val height = binding.root.height
                val padding = height / 2 - 20
                binding.rvHours.setPadding(0, padding, 0, padding)
                binding.rvMinutes.setPadding(0, padding, 0, padding)

                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun scrollToCurrentTime(rv: RecyclerView, isHours: Boolean) {
        val scrollRunnable = if (isHours) {
            val currentTimeIntPosition = currentlySelectedHour.toIntOrNull()
            currentTimeIntPosition?.let { timeInInt ->
                Runnable {
                    val position = if (shouldBeIn12HourFormat) {
                        timeInInt - 1
                    } else {
                        timeInInt
                    }
                    rv.smoothScrollToPosition(position)
                }
            } ?: Runnable {
                rv.smoothScrollBy(0, 20)
            }
        } else {
            val currentTimeIntPosition = currentlySelectedMinute.toIntOrNull()
            currentTimeIntPosition?.let {
                Runnable {
                    rv.smoothScrollToPosition(it)
                }
            } ?: Runnable {
                rv.smoothScrollBy(0, 20)
            }
        }

        rv.postDelayed(scrollRunnable, 100)
    }

    /**
     * Returns the currently selected time
     * @param format: Return format for time, default HH:MM FORMAT
     */
    fun getCurrentlySelectedTime(format: String = "HH:MM FORMAT") : String{
        return format.replace("HH", currentlySelectedHour)
            .replace("MM", currentlySelectedMinute)
            .replace("FORMAT", if (isAmSelected) "Am" else "Pm")
    }

    /**
     * Sets the currently selected time for the time picker
     * Format HH:MM FORMAT
     */
    fun setInitialSelectedTime(initialTime: String) {
        val timeAndAMPmSplit = initialTime.split(" ")
        if (timeAndAMPmSplit.size < 2) return
        val hourAndMinuteSplit = timeAndAMPmSplit[0].split(":")
        if (hourAndMinuteSplit.size < 2) return
        currentlySelectedHour = hourAndMinuteSplit[0]
        currentlySelectedMinute = hourAndMinuteSplit[1]
        isAmSelected = timeAndAMPmSplit[1].equals("Am", true)
        if (isAmSelected) binding.tvAm.performClick() else binding.tvPm.performClick()
        scrollToCurrentTime(binding.rvHours, true)
        scrollToCurrentTime(binding.rvMinutes, false)
    }

}
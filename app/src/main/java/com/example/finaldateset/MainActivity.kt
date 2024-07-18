package com.example.finaldateset



import CustomAnimation

import android.os.Bundle

import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.LinearSnapHelper

import androidx.recyclerview.widget.RecyclerView

import java.util.Calendar



class MainActivity : AppCompatActivity() {



    private lateinit var yearRecyclerView: RecyclerView

    private lateinit var monthRecyclerView: RecyclerView

    private lateinit var dayRecyclerView: RecyclerView

    private lateinit var selectedDateTextView: TextView

    private val calendar: Calendar = Calendar.getInstance()

    private val customAnimation = CustomAnimation()



    private var selectedYear: Int = -1

    private var selectedMonth: Int = -1

    private var selectedDay: Int = -1



    private fun updateDays(year: Int, month: Int): List<String> {

        calendar.set(Calendar.YEAR, year)

        calendar.set(Calendar.MONTH, month - 1)

        val maxDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        return (1..maxDaysInMonth).map { it.toString().padStart(2, '0') }

    }



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)



        yearRecyclerView = findViewById(R.id.yearRecyclerView)

        monthRecyclerView = findViewById(R.id.monthRecyclerView)

        dayRecyclerView = findViewById(R.id.dayRecyclerView)

        selectedDateTextView = findViewById(R.id.selectedDateTextView)



        val firstItemTopSpace = resources.getDimensionPixelSize(R.dimen.first_item_top_space)

        val lastItemBottomSpace = resources.getDimensionPixelSize(R.dimen.last_item_bottom_space)



        yearRecyclerView.addItemDecoration(ItemDecoration(firstItemTopSpace, lastItemBottomSpace))

        monthRecyclerView.addItemDecoration(ItemDecoration(firstItemTopSpace, lastItemBottomSpace))

        dayRecyclerView.addItemDecoration(ItemDecoration(firstItemTopSpace, lastItemBottomSpace))



        LinearSnapHelper().attachToRecyclerView(yearRecyclerView)

        LinearSnapHelper().attachToRecyclerView(monthRecyclerView)

        LinearSnapHelper().attachToRecyclerView(dayRecyclerView)



        // 年

        val years = (1924..2024).map { "$it" }.toMutableList()

        val yearAdapter = YearAdapter(yearRecyclerView, { position -> }, { centerPosition ->

            selectedYear = years[centerPosition].toIntOrNull() ?: return@YearAdapter

            val selectedMonthPosition = adjustForTopSpace(monthRecyclerView)

            val currentDayItemCount = (dayRecyclerView.adapter as YearAdapter).itemCount

            val newDays = updateDays(selectedYear, selectedMonthPosition + 1)

            updateDaysAndScroll(newDays, currentDayItemCount)

            updateSelectedDateTextView(selectedYear, selectedMonthPosition + 1, selectedDay)

        })

        yearRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        yearRecyclerView.adapter = yearAdapter

        yearAdapter.submitList(years)



        // 月

        val months = (1..12).map { it.toString().padStart(2, '0') }.toMutableList()

        val monthAdapter = YearAdapter(monthRecyclerView, { position -> }, { centerPosition ->

            selectedMonth = months[centerPosition].toIntOrNull() ?: return@YearAdapter

            val selectedYearPosition = adjustForTopSpace(yearRecyclerView)

            selectedYear = years[selectedYearPosition].toIntOrNull() ?: return@YearAdapter

            val currentDayItemCount = (dayRecyclerView.adapter as YearAdapter).itemCount

            val newDays = updateDays(selectedYear, selectedMonth)

            updateDaysAndScroll(newDays, currentDayItemCount)

            updateSelectedDateTextView(selectedYear, selectedMonth, selectedDay)

        })

        monthRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        monthRecyclerView.adapter = monthAdapter

        monthAdapter.submitList(months)



        // 日

        val days = updateDays(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1).toMutableList()

        val dayAdapter = YearAdapter(dayRecyclerView, { position -> }, { centerPosition ->

            selectedDay = days[centerPosition].toIntOrNull() ?: return@YearAdapter

            updateSelectedDateTextView(selectedYear, selectedMonth, selectedDay)

        })

        dayRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        dayRecyclerView.adapter = dayAdapter

        dayAdapter.submitList(days)



        addScrollListeners()

    }



    private fun updateDaysAndScroll(newDays: List<String>, currentDayItemCount: Int) {

        val dayAdapter = dayRecyclerView.adapter as YearAdapter

        dayAdapter.updateData(newDays)

        if (newDays.size < currentDayItemCount) {

            dayRecyclerView.scrollToPosition(0)

        }

    }



    private fun updateSelectedDateTextView(year: Int, month: Int, day: Int) {

        val yearText = if (year == -1) "" else year.toString()

        val monthText = if (month == -1) "" else month.toString().padStart(2, '0')

        val dayText = if (day == -1) "" else day.toString().padStart(2, '0')

        selectedDateTextView.text = "Selected Date: $yearText-$monthText-$dayText"

    }



    private fun adjustForTopSpace(recyclerView: RecyclerView): Int {

        val firstVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

        val firstVisibleItemView = (recyclerView.layoutManager as LinearLayoutManager).findViewByPosition(firstVisibleItemPosition)

        val firstItemTopSpace = resources.getDimensionPixelSize(R.dimen.first_item_top_space)

        val isPartiallyVisible = firstVisibleItemView?.top ?: 0 < firstItemTopSpace

        return if (isPartiallyVisible) firstVisibleItemPosition + 1 else firstVisibleItemPosition

    }



    private fun addScrollListeners() {

        yearRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)

                customAnimation.applyAnimation(recyclerView)

            }

        })

        monthRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)

                customAnimation.applyAnimation(recyclerView)

            }

        })

        dayRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)

                customAnimation.applyAnimation(recyclerView)

            }

        })

    }

}

/*
 * Copyright (C) 2016 Álinson Santos Xavier <isoron@gmail.com>
 *
 * This file is part of Loop Habit Tracker.
 *
 * Loop Habit Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Loop Habit Tracker is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.isoron.uhabits.activities.habits.show.views

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import org.isoron.uhabits.R
import org.isoron.uhabits.core.ui.screens.habits.show.views.TargetCardViewModel
import org.isoron.uhabits.databinding.ShowHabitTargetBinding
import org.isoron.uhabits.utils.toThemedAndroidColor

class TargetCardView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private val binding = ShowHabitTargetBinding.inflate(LayoutInflater.from(context), this)
    fun update(data: TargetCardViewModel) {
        val androidColor = data.color.toThemedAndroidColor(context)
        binding.targetChart.setValues(data.values)
        binding.targetChart.setTargets(data.targets)
        binding.targetChart.setLabels(data.intervals.map { intervalToLabel(resources, it) })
        binding.title.setTextColor(androidColor)
        binding.targetChart.setColor(androidColor)
        postInvalidate()
    }

    companion object {
        fun intervalToLabel(resources: Resources, interval: Int) = when (interval) {
            1 -> resources.getString(R.string.today)
            7 -> resources.getString(R.string.week)
            30 -> resources.getString(R.string.month)
            91 -> resources.getString(R.string.quarter)
            else -> resources.getString(R.string.year)
        }
    }
}

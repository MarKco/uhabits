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

package org.isoron.uhabits.widgets

import android.app.PendingIntent
import android.content.Context
import android.view.View
import org.isoron.uhabits.activities.common.views.HistoryChart
import org.isoron.uhabits.core.models.Habit
import org.isoron.uhabits.core.ui.screens.habits.show.views.HistoryCardPresenter
import org.isoron.uhabits.utils.toThemedAndroidColor
import org.isoron.uhabits.widgets.views.GraphWidgetView

class HistoryWidget(
    context: Context,
    id: Int,
    private val habit: Habit,
    private val firstWeekday: Int
) : BaseWidget(context, id) {

    override fun getOnClickPendingIntent(context: Context): PendingIntent {
        return pendingIntentFactory.showHabit(habit)
    }

    override fun refreshData(view: View) {
        val widgetView = view as GraphWidgetView
        widgetView.setBackgroundAlpha(preferedBackgroundAlpha)
        if (preferedBackgroundAlpha >= 255) widgetView.setShadowAlpha(0x4f)
        val model = HistoryCardPresenter().present(
            habit = habit,
            isSkipEnabled = prefs.isSkipEnabled,
            firstWeekday = prefs.firstWeekday,
        )
        (widgetView.dataView as HistoryChart).apply {
            setFirstWeekday(model.firstWeekday)
            setSkipEnabled(model.isSkipEnabled)
            setColor(model.color.toThemedAndroidColor(context))
            setEntries(model.entries)
            setNumerical(model.isNumerical)
        }
    }

    override fun buildView() =
        GraphWidgetView(context, HistoryChart(context)).apply {
            setTitle(habit.name)
        }

    override fun getDefaultHeight() = 250
    override fun getDefaultWidth() = 250
}

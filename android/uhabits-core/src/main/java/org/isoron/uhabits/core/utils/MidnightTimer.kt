/*
 * Copyright (C) 2017 Álinson Santos Xavier <isoron@gmail.com>
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
package org.isoron.uhabits.core.utils

import org.isoron.uhabits.core.AppScope
import java.util.LinkedList
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * A class that emits events when a new day starts.
 */
@AppScope
open class MidnightTimer @Inject constructor() {
    private val listeners: MutableList<MidnightListener> = LinkedList()
    private lateinit var executor: ScheduledExecutorService

    @Synchronized fun addListener(listener: MidnightListener) {
        this.listeners.add(listener)
    }

    @Synchronized fun onPause() = executor.shutdownNow()

    @Synchronized fun onResume() {
        executor = Executors.newSingleThreadScheduledExecutor()
        executor.scheduleAtFixedRate(
            { notifyListeners() },
            DateUtils.millisecondsUntilTomorrowWithOffset() + 1000,
            DateUtils.DAY_LENGTH,
            TimeUnit.MILLISECONDS
        )
    }

    @Synchronized fun removeListener(listener: MidnightListener) = this.listeners.remove(listener)

    @Synchronized private fun notifyListeners() {
        for (l in listeners) {
            l.atMidnight()
        }
    }

    interface MidnightListener {
        fun atMidnight()
    }
}

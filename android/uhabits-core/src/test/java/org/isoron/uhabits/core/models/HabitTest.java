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

package org.isoron.uhabits.core.models;

import org.isoron.uhabits.core.*;
import org.junit.*;
import org.junit.rules.*;

import nl.jqno.equalsverifier.*;

import static org.hamcrest.CoreMatchers.*;
import static org.isoron.uhabits.core.utils.DateUtils.*;
import static org.junit.Assert.*;

public class HabitTest extends BaseUnitTest
{
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
    }

    @Test
    public void testUuidGeneration()
    {
        Habit habit1 = modelFactory.buildHabit();
        Habit habit2 = modelFactory.buildHabit();
        assertNotNull(habit1.getUuid());
        assertNotNull(habit2.getUuid());
        assertNotEquals(habit1.getUuid(), habit2.getUuid());
    }

    @Test
    public void test_copyAttributes()
    {
        Habit model = modelFactory.buildHabit();
        model.setArchived(true);
        model.setColor(new PaletteColor(0));
        model.setFrequency(new Frequency(10, 20));
        model.setReminder(new Reminder(8, 30, new WeekdayList(1)));

        Habit habit = modelFactory.buildHabit();
        habit.copyFrom(model);
        assertThat(habit.isArchived(), is(model.isArchived()));
        assertThat(habit.getColor(), is(model.getColor()));
        assertThat(habit.getFrequency(), equalTo(model.getFrequency()));
        assertThat(habit.getReminder(), equalTo(model.getReminder()));
    }


    @Test
    public void test_hasReminder()
    {
        Habit h = modelFactory.buildHabit();
        assertThat(h.hasReminder(), is(false));

        h.setReminder(new Reminder(8, 30, WeekdayList.EVERY_DAY));
        assertThat(h.hasReminder(), is(true));
    }

    @Test
    public void test_isCompleted() throws Exception
    {
        Habit h = modelFactory.buildHabit();
        assertFalse(h.isCompletedToday());
        h.getOriginalEntries().add(new Entry(getToday(), Entry.YES_MANUAL));
        h.recompute();
        assertTrue(h.isCompletedToday());
    }

    @Test
    public void test_isCompleted_numerical() throws Exception
    {
        Habit h = modelFactory.buildHabit();
        h.setType(Habit.NUMBER_HABIT);
        h.setTargetType(Habit.AT_LEAST);
        h.setTargetValue(100.0);
        assertFalse(h.isCompletedToday());

        h.getOriginalEntries().add(new Entry(getToday(), 200_000));
        h.recompute();
        assertTrue(h.isCompletedToday());

        h.getOriginalEntries().add(new Entry(getToday(), 100_000));
        h.recompute();
        assertTrue(h.isCompletedToday());

        h.getOriginalEntries().add(new Entry(getToday(), 50_000));
        h.recompute();
        assertFalse(h.isCompletedToday());

        h.setTargetType(Habit.AT_MOST);
        h.getOriginalEntries().add(new Entry(getToday(), 200_000));
        h.recompute();
        assertFalse(h.isCompletedToday());

        h.getOriginalEntries().add(new Entry(getToday(), 100_000));
        h.recompute();
        assertTrue(h.isCompletedToday());

        h.getOriginalEntries().add(new Entry(getToday(), 50_000));
        h.recompute();
        assertTrue(h.isCompletedToday());
    }

    @Test
    public void testURI() throws Exception
    {
        assertTrue(habitList.isEmpty());
        Habit h = modelFactory.buildHabit();
        habitList.add(h);
        assertThat(h.getId(), equalTo(0L));
        assertThat(h.getUriString(),
            equalTo("content://org.isoron.uhabits/habit/0"));
    }
}
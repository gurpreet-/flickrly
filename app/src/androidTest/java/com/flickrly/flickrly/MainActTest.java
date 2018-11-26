package com.flickrly.flickrly;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.flickrly.flickrly.activities.MainActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActTest {


    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, true, false);

    @Before
    public void before() {
        Context context = getInstrumentation().getTargetContext();
        SharedPreferences prefs = context.getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        prefs.edit()
            .putString(MainActivity.PREF_SORT, MainActivity.PREF_SORT_CREATION)
            .commit();
        activityRule.launchActivity(new Intent(context, MainActivity.class));
    }

    @Test
    public void testCheckBlackBackgroundAppearsOnSortButtonClick() {
        onView(withId(R.id.sort_btn)).perform(click());
        SystemClock.sleep(800);
        onView(withId(R.id.black_bg)).check(matches(withAlpha(1)));
    }

    @Test
    public void testCheckAdapterFills() {
        // Wait for load
        SystemClock.sleep(2000);
        onView(withId(R.id.photos_rv)).check(matches(hasMinimumChildCount(1)));
    }

    @Test
    public void testToggleWorks() {
        // Wait for load
        onView(withId(R.id.sort_btn)).perform(click());
        SystemClock.sleep(800);
        onView(withId(R.id.publication_btn)).perform(click());
        onView(withId(R.id.sort_btn)).check(matches(withText("Publication")));
    }

}

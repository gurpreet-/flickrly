package com.flickrly.flickrly;

import android.os.SystemClock;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.flickrly.flickrly.activities.SearchActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isFocusable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchActTest {


    @Rule
    public ActivityTestRule<SearchActivity> activityRule = new ActivityTestRule<>(SearchActivity.class);

    @Test
    public void testCheckSearchIsAvailable() {
        // Wait for load
        SystemClock.sleep(100);
        onView(withId(R.id.search_box)).check(matches(isFocusable()));
    }

}

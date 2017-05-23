package com.place.finder;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;

import com.place.finder.view.activity.PlaceFinderActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.pressKey;
import static org.junit.Assert.*;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * PlaceFinderActivityInstrumentedTest  which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PlaceFinderActivityInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.place.finder", appContext.getPackageName());
    }

    @Rule
    public ActivityTestRule<PlaceFinderActivity> mActivityRule =
            new ActivityTestRule<>(PlaceFinderActivity.class);

    @Test
    public void ensureTextChangesWork() {
        // Type text and then press the button.
        onView(withId(R.id.searchView))
                .perform(typeText("Sayaji Hotel"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.searchView)).perform(click());

        // Check that the text was changed.
        onView(withId(R.id.searchView)).check(matches(withText("Taj hotel Mumbai")));
    }


}

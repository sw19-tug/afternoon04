package at.tugraz.ist.swe;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static org.junit.Assert.*;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class ToolbarTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testToolbarButtons()
    {
        onView(withId(R.id.smileyFlyoutMenu)).check(matches(isDisplayed()));
    }
}

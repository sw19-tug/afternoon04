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
public class ColorPickerDialogTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testDialogVisible()
    {
        onView(withText(R.string.color_picker_title)).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    @Test
    public void testDialogButtonsVisible()
    {
        onView(withText(R.string.color_picker_button_apply)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText(R.string.color_picker_button_reset)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText(R.string.color_picker_button_cancel)).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    @Test
    public void testDialogSliderVisible()
    {
        //SeekBar = Slider
        onView(withId(R.id.color_picker_seekbar_red)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withId(R.id.color_picker_seekbar_green)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withId(R.id.color_picker_seekbar_blue)).inRoot(isDialog()).check(matches(isDisplayed()));
    }
}

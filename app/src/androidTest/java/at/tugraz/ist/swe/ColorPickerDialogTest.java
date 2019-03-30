package at.tugraz.ist.swe;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.hasTextColor;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


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

    @Test
    public void testButtonColors(){
        onView(withText("Cancel")).inRoot(isDialog()).check(matches(hasTextColor(R.color.colorCancelButtons)));
        onView(withText("Reset")).inRoot(isDialog()).check(matches(hasTextColor(R.color.colorResetButtons)));
        onView(withText("Apply")).inRoot(isDialog()).check(matches(hasTextColor(R.color.colorAcceptButtons)));
    }

    @Test
    public void testTextViewVisible(){
        onView(withId(R.id.textView_red_color)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withId(R.id.textView_green_color)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withId(R.id.textView_blue_color)).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    @Test
    public void testTextBoxShowsSeekBarProgress() {
        onView(withId(R.id.color_picker_seekbar_red)).perform(setProgress(50));
        onView(withId(R.id.textView_red_color)).check(matches(withText("50")));
    }


}

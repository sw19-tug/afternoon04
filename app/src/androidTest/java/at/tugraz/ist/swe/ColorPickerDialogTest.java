package at.tugraz.ist.swe;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.SeekBar;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.UiController;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.widget.EditText;
import android.view.View;
import org.hamcrest.Matcher;
import org.hamcrest.Description;

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
import android.support.test.espresso.matcher.ViewMatchers;


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
        onView(withId(R.id.color_picker_seekbar_red)).perform(setProgress(0));
        onView(withId(R.id.textView_red_color)).check(matches(withText("0")));

        onView(withId(R.id.color_picker_seekbar_green)).perform(setProgress(100));
        onView(withId(R.id.textView_green_color)).check(matches(withText("100")));

        onView(withId(R.id.color_picker_seekbar_blue)).perform(setProgress(256));
        onView(withId(R.id.textView_blue_color)).check(matches(withText("255")));
    }

    @Test
    public void testSeekbarShowsTextBoxValue() {
        onView(withId(R.id.textView_red_color)).perform(setText("0"));
        onView(withId(R.id.color_picker_seekbar_red)).check(matches(withProgress(0)));
        onView(withId(R.id.textView_green_color)).perform(setText("100"));
        onView(withId(R.id.color_picker_seekbar_green)).check(matches(withProgress(100)));
        onView(withId(R.id.textView_blue_color)).perform(setText("256"));
        onView(withId(R.id.color_picker_seekbar_blue)).check(matches(withProgress(255)));
    }

    @Test
    public void testTextBoxHexShown() {
        onView(withId(R.id.textView_hex_color)).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    // helper function to set value on seekbar
    public static ViewAction setProgress(final int progress) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                ((SeekBar) view).setProgress(progress);
            }

            @Override
            public String getDescription() {
                return "Set Progress of Seekbar";
            }

            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }
        };
    }

    // helper function to set value on edit text
    public static ViewAction setText(final String value){
        return new ViewAction() {
            @SuppressWarnings("unchecked")
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(EditText.class);
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((EditText) view).setText(value);
            }

            @Override
            public String getDescription() {
                return "sets text of Edit Text";
            }
        };
    }

    //helper function to check progress of seekbar
    public static Matcher<View> withProgress(final int expectedProgress) {
        return new BoundedMatcher<View, SeekBar>(SeekBar.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("expected: ");
                description.appendText(""+expectedProgress);
            }

            @Override
            public boolean matchesSafely(SeekBar seekBar) {
                return seekBar.getProgress() == expectedProgress;
            }
        };
    }


}

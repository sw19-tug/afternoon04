package at.tugraz.ist.swe;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.test.espresso.Root;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.MotionEvents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.MotionEvent;
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
import org.zakariya.flyoutmenu.FlyoutMenuView;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.isTouchable;
import static android.support.test.espresso.matcher.ViewMatchers.hasTextColor;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;


import android.support.test.espresso.matcher.ViewMatchers;

import java.util.ArrayList;
import java.util.List;


@RunWith(AndroidJUnit4.class)
public class ColorPickerDialogTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testDialogVisible()
    {
        onView(withId(R.id.toolFlyoutMenu)).perform(click());
        activityTestRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int amount_items = activityTestRule.getActivity().toolFlyoutMenu.getAdapter().getCount();
                for(int counter = 0; 0 < amount_items; counter++)
                {
                    FlyoutMenuView.MenuItem result = activityTestRule.getActivity().toolFlyoutMenu.getAdapter().getItem(counter);
                    if(((FlyoutToolbar.MenuItemImage)result).getID() == R.drawable.ic_outline_color_lens_24px)
                    {
                        activityTestRule.getActivity().toolFlyoutMenu.setSelectedMenuItem(result);
                        break;
                    }
                }
            }
        });
        onView(withText(R.string.color_picker_title)).check(matches(isDisplayed()));
    }

    /*
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
    public void testRGBLabelsVisible(){
        onView(withId(R.id.color_picker_label_r)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withId(R.id.color_picker_label_g)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withId(R.id.color_picker_label_b)).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    @Test
    public void testColorPreviewVisible(){
        onView(withId(R.id.color_picker_preview)).check(matches(isDisplayed()));
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
    public void testCorrectColorPreview() {
        onView(withId(R.id.color_picker_seekbar_red)).perform(setProgress(0));
        onView(withId(R.id.color_picker_seekbar_green)).perform(setProgress(100));
        onView(withId(R.id.color_picker_seekbar_blue)).perform(setProgress(255));

        onView(withId(R.id.color_picker_preview)).check(matches(withBackgroundColor(Color.argb(255, 0, 100, 255))));

    }

    @Test
    public void testResetButton() {
        onView(withId(R.id.color_picker_seekbar_red)).perform(setProgress(0));
        onView(withId(R.id.color_picker_seekbar_green)).perform(setProgress(100));
        onView(withId(R.id.color_picker_seekbar_blue)).perform(setProgress(255));

        onView(withText("Apply")).inRoot(isDialog()).perform(click());

        onView(withId(R.id.color_picker_seekbar_red)).perform(setProgress(200));
        onView(withId(R.id.color_picker_seekbar_green)).perform(setProgress(0));
        onView(withId(R.id.color_picker_seekbar_blue)).perform(setProgress(20));

        onView(withText("Reset")).inRoot(isDialog()).perform(click());

        onView(withId(R.id.color_picker_seekbar_red)).check(matches(withProgress(0)));
        onView(withId(R.id.color_picker_seekbar_green)).check(matches(withProgress(100)));
        onView(withId(R.id.color_picker_seekbar_blue)).check(matches(withProgress(255)));

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

    @Test
    public void testTextBoxHexShowsSeekbarValues()
    {
        onView(withId(R.id.color_picker_seekbar_red)).perform(setProgress(0));
        onView(withId(R.id.color_picker_seekbar_green)).perform(setProgress(100));
        onView(withId(R.id.color_picker_seekbar_blue)).perform(setProgress(255));

        onView(withId(R.id.textView_hex_color)).check(matches(withText("0064FF")));
    }

    @Test
    public void testSeekbarShowsTextBoxHexValues()
    {
        onView(withId(R.id.textView_hex_color)).perform(setText("64FF32"));

        onView(withId(R.id.color_picker_seekbar_red)).check(matches(withProgress(100)));
        onView(withId(R.id.color_picker_seekbar_green)).check(matches(withProgress(255)));
        onView(withId(R.id.color_picker_seekbar_blue)).check(matches(withProgress(50)));
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

    public static Matcher<View> withBackgroundColor(final int expectedColor) {
        return new BoundedMatcher<View, View>(View.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("expected: ");
                description.appendText(""+expectedColor);
            }

            @Override
            public boolean matchesSafely(View view) {
                return expectedColor == ((ColorDrawable) view.getBackground()).getColor();
            }
        };
    }
    */

}

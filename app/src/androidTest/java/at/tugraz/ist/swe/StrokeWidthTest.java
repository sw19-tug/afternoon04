package at.tugraz.ist.swe;

import android.graphics.drawable.ColorDrawable;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.zakariya.flyoutmenu.FlyoutMenuView;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class StrokeWidthTest {

        @Rule
        public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

        @Test
        public void testStrokeWidthElementsVisible(){
            openDialog();
            onView(withId(R.id.strokeWidthLayout)).check(matches(isDisplayed()));
            onView(withId(R.id.strokewidth_left)).check(matches(isDisplayed()));
            onView(withId(R.id.strokewidth_right)).check(matches(isDisplayed()));
            onView(withId(R.id.strokewidth_text)).check(matches(isDisplayed()));
        }
        
        @Test
        public void testSeekbarShowsTextBoxValue() {
            openDialog();
            onView(withId(R.id.textView_red_color)).perform(replaceText("0"), pressImeActionButton());
            onView(withId(R.id.color_picker_seekbar_red)).check(matches(withProgress(0)));
            onView(withId(R.id.textView_green_color)).perform(replaceText("100"), pressImeActionButton());
            onView(withId(R.id.color_picker_seekbar_green)).check(matches(withProgress(100)));
            onView(withId(R.id.textView_blue_color)).perform(replaceText("255"), pressImeActionButton());
            onView(withId(R.id.color_picker_seekbar_blue)).check(matches(withProgress(255)));
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

        public void openDialog()
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
        }
    }


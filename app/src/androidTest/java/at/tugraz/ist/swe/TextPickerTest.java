package at.tugraz.ist.swe;

import android.graphics.Bitmap;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.MotionEvents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.zakariya.flyoutmenu.FlyoutMenuView;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.hasTextColor;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class TextPickerTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void testDialogVisible()
    {
        openDialog();
        onView(withText(R.string.text_picker_title)).check(matches(isDisplayed()));
    }


    @Test
    public void testDialogButtonsVisible()
    {
        openDialog();
        onView(withText(R.string.text_picker_button_apply)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText(R.string.text_picker_button_cancel)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText(R.string.text_picker_button_reset)).inRoot(isDialog()).check(matches(isDisplayed()));
    }


    @Test
    public void testButtonColors(){
        openDialog();
        onView(withText("Cancel")).inRoot(isDialog()).check(matches(hasTextColor(R.color.colorCancelButtons)));
        onView(withText("Reset")).inRoot(isDialog()).check(matches(hasTextColor(R.color.colorResetButtons)));
        onView(withText("Apply")).inRoot(isDialog()).check(matches(hasTextColor(R.color.colorAcceptButtons)));
    }

    @Test
    public void testTextViewVisible(){
        openDialog();
        onView(withId(R.id.textPicker_view)).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    @Test
    public void testTextDrawn(){
        activityTestRule.getActivity().drawingArea.resetCanvas();
        openDialog();
        onView(withId(R.id.textPicker_text)).perform(setText("HALLO"));
        onView(withText("Apply")).inRoot(isDialog()).perform(click());
        onView(withId(R.id.main_canvas_view)).perform(performTouch(150, 150));
        onView(withId(R.id.main_canvas_view)).perform(performTouch(150, 150));
        Bitmap bitmap_old = activityTestRule.getActivity().drawingArea.getBitmap();
        onView(withId(R.id.main_canvas_view)).perform(performTouch(700, 700));
        Bitmap bitmap_new = activityTestRule.getActivity().drawingArea.getBitmap();
        onView(withId(R.id.main_canvas_view)).check(matches(checkBitmap(bitmap_old, bitmap_new)));

    }

    @Test
    public void testResetButton(){
        openDialog();
        onView(withId(R.id.textPicker_text)).perform(setText("HALLO"));
        onView(withText("Reset")).inRoot(isDialog()).perform(click());
        onView(withId(R.id.textPicker_text)).check(matches(withText("")));
    }

    @Test
    public void testResetButtonAfterApply(){
        openDialog();
        onView(withId(R.id.textPicker_text)).perform(setText("HALLO"));
        onView(withText("Apply")).inRoot(isDialog()).perform(click());
        onView(withId(R.id.main_canvas_view)).perform(performTouch(150, 150));
        openDialog();
        onView(withId(R.id.textPicker_text)).perform(setText("ABBC"));
        onView(withText("Reset")).inRoot(isDialog()).perform(click());
        onView(withId(R.id.textPicker_text)).check(matches(withText("HALLO")));
    }

    @Test
    public void testCancelButton(){

        openDialog();
        onView(withId(R.id.textPicker_text)).perform(setText("HALLO"));
        onView(withText("Cancel")).inRoot(isDialog()).perform(click());
        onView(withText(R.string.text_picker_title)).check(doesNotExist());
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

    public static ViewAction performTouch(final float x, final float y) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "perform touch down";
            }

            @Override
            public void perform(UiController uiController, final View view) {

                int[] location = new int[2];
                view.getLocationOnScreen(location);

                float[] coordinates = new float[] { x + location[0], y + location[1] };
                float[] precision = new float[] { 1f, 1f };

                MotionEvent down = MotionEvents.sendDown(uiController, coordinates, precision).down; // go down
                uiController.loopMainThreadForAtLeast(200); // wait
                MotionEvents.sendUp(uiController, down, coordinates); // go up
                uiController.loopMainThreadForAtLeast(1000); // wait
            }
        };
    }

    public static Matcher<View> checkBitmap(final Bitmap bitmap_old, final Bitmap bitmap_new) {
        return new Matcher<View>() {
            @Override
            public void describeTo(Description description) {

            }

            @Override
            public boolean matches(Object item) {

                for(int x = 1; x < bitmap_new.getWidth(); x++)
                {
                    for(int y = 1; y < bitmap_new.getHeight(); y++)
                    {
                        if(bitmap_new.getPixel(x, y) != bitmap_old.getPixel(x, y))
                        {
                            return true;
                        }
                    }
                }

                return false;
            }

            @Override
            public void describeMismatch(Object item, Description mismatchDescription) {
            }

            @Override
            public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {

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
                    if(((FlyoutToolbar.MenuItemImage)result).getID() == R.drawable.ic_baseline_text_fields_24px)
                    {
                        activityTestRule.getActivity().toolFlyoutMenu.setSelectedMenuItem(result);
                        break;
                    }
                }
            }
        });
    }


}

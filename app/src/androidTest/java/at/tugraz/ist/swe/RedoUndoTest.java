package at.tugraz.ist.swe;

import android.graphics.Bitmap;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.MotionEvents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.MotionEvent;
import android.view.View;

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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
@RunWith(AndroidJUnit4.class)
public class RedoUndoTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testSelectedTool()
    {
        openDialog(R.drawable.ic_baseline_swap_horiz_24px);
        onView(withId(R.id.strokeWidthLayout)).check(doesNotExist());
        onView(withId(R.id.strokewidth_left)).check(doesNotExist());
        onView(withId(R.id.strokewidth_right)).check(doesNotExist());
        onView(withId(R.id.strokewidth_text)).check(doesNotExist());
        onView(withId(R.id.undoLayout)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonUndo)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonRedo)).check(matches(isDisplayed()));
    }

    @Test
    public void testUndoFunction()
    {
        Bitmap oldBitmap = (Bitmap)activityTestRule.getActivity().drawingArea.getBitmap();
        openDialog(R.drawable.ic_si_glyph_circle);
        onView(withId(R.id.main_canvas_view)).perform(performTouch(150, 150));
        onView(withId(R.id.main_canvas_view)).perform(performTouch(50, 50));
        openDialog(R.drawable.ic_baseline_swap_horiz_24px);
        onView(withId(R.id.buttonUndo)).perform(click());
        Bitmap newBitmap = (Bitmap)activityTestRule.getActivity().drawingArea.getBitmap();
        onView(withId(R.id.main_canvas_view)).check(matches(checkBitmap(oldBitmap,newBitmap)));
    }

    @Test
    public void testRedoFunction()
    {
        openDialog(R.drawable.ic_si_glyph_circle);
        onView(withId(R.id.main_canvas_view)).perform(performTouch(150, 150));
        onView(withId(R.id.main_canvas_view)).perform(performTouch(50, 50));
        Bitmap oldBitmap = (Bitmap)activityTestRule.getActivity().drawingArea.getBitmap();
        openDialog(R.drawable.ic_baseline_swap_horiz_24px);
        onView(withId(R.id.buttonUndo)).perform(click());
        onView(withId(R.id.buttonRedo)).perform(click());
        Bitmap newBitmap = (Bitmap)activityTestRule.getActivity().drawingArea.getBitmap();
        onView(withId(R.id.main_canvas_view)).check(matches(checkBitmap(oldBitmap, newBitmap)));
    }

    public void openDialog(final int tool)
    {
        onView(withId(R.id.toolFlyoutMenu)).perform(click());
        activityTestRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int amount_items = activityTestRule.getActivity().toolFlyoutMenu.getAdapter().getCount();
                for(int counter = 0; 0 < amount_items; counter++)
                {
                    FlyoutMenuView.MenuItem result = activityTestRule.getActivity().toolFlyoutMenu.getAdapter().getItem(counter);
                    if(((FlyoutToolbar.MenuItemImage)result).getID() == tool)
                    {
                        activityTestRule.getActivity().toolFlyoutMenu.setSelectedMenuItem(result);
                        break;
                    }
                }
            }
        });
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
    public static Matcher<View> checkBitmap(final Bitmap oldBitmap, final Bitmap newBitmap)
    {
        return new Matcher<View>() {
            @Override
            public boolean matches(Object item) {
                for(int x = 1; x < newBitmap.getWidth(); x++)
                {
                    for(int y = 1; y < newBitmap.getHeight(); y++)
                    {
                        if(newBitmap.getPixel(x, y) != oldBitmap.getPixel(x, y))
                        {
                            return false;
                        }
                    }
                }
                return true;
            }

            @Override
            public void describeMismatch(Object item, Description mismatchDescription) {

            }

            @Override
            public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {

            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

}
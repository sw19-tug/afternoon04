package at.tugraz.ist.swe;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import android.view.View;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.UiController;
import android.support.test.espresso.action.MotionEvents;
import org.hamcrest.Matcher;
import org.zakariya.flyoutmenu.FlyoutMenuView;
import android.view.MotionEvent;

@RunWith(AndroidJUnit4.class)
public class DrawPointTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testDrawboardVisible()
    {
        onView(withId(R.id.main_canvas_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testDrawboardUserAction()
    {
        activityTestRule.getActivity().drawingArea.resetCanvas();
        openDialog();
        onView(withId(R.id.main_canvas_view)).perform(performTouch(150, 150)); // click to close menu

        onView(withId(R.id.main_canvas_view)).perform(performTouch(50, 50)); // click to draw
        onView(withId(R.id.main_canvas_view)).perform(performTouch(150, 150)); // click to draw
        onView(withId(R.id.main_canvas_view)).perform(performTouch(300, 300)); // click to draw
        onView(withId(R.id.main_canvas_view)).perform(performTouch(500, 900)); // click to draw

        onView(withId(R.id.draw_point_view)).check(matches(checkCoordinates(50,50)));
        onView(withId(R.id.draw_point_view)).check(matches(checkCoordinates(150,150)));
        onView(withId(R.id.draw_point_view)).check(matches(checkCoordinates(300,300)));
        onView(withId(R.id.draw_point_view)).check(matches(checkCoordinates(500,900)));

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
                    if(((FlyoutToolbar.MenuItemImage)result).getID() == R.drawable.ic_si_glyph_circle)
                    {
                        activityTestRule.getActivity().toolFlyoutMenu.setSelectedMenuItem(result);
                        break;
                    }
                }
            }
        });
    }

    public static Matcher<View> checkCoordinates(final float x, final float y)
    {
        return new Matcher<View>() {
            @Override
            public void describeTo(Description description) {

            }

            @Override
            public boolean matches(Object item) {
                DrawArea temp = (DrawArea) item;
                Bitmap tool_bitmap = temp.getBitmap();

                int alpha_color = Color.alpha(tool_bitmap.getPixel((int )x, (int) y));
                int red_color = Color.red(tool_bitmap.getPixel((int) x,(int) y));
                int green_color = Color.green(tool_bitmap.getPixel((int) x,(int) y));
                int blue_color = Color.blue(tool_bitmap.getPixel((int) x,(int) y));

                if(Color.BLACK == Color.argb(alpha_color, red_color, green_color, blue_color))
                    return true;


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
}
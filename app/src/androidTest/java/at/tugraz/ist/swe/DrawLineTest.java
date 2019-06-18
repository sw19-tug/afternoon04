package at.tugraz.ist.swe;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.MotionEvents;
import android.support.test.espresso.action.Swipe;
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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DrawLineTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void testSelectedTool()
    {
        onView(withId(R.id.toolFlyoutMenu)).perform(click());
        activityTestRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int amount_items = activityTestRule.getActivity().toolFlyoutMenu.getAdapter().getCount();
                for(int counter = 0; 0 < amount_items; counter++)
                {
                    FlyoutMenuView.MenuItem result = activityTestRule.getActivity().toolFlyoutMenu.getAdapter().getItem(counter);
                    if(((FlyoutToolbar.MenuItemImage)result).getID() == R.drawable.ic_si_glyph_line_two_angle_point)
                    {
                        activityTestRule.getActivity().toolFlyoutMenu.setSelectedMenuItem(result);
                        break;
                    }
                }
                PaintingTool tool = activityTestRule.getActivity().drawingArea.getPaintingTool();

                assertEquals(Line.class.toString(), tool.getClass().toString());
            }
        });
    }

    @Test
    public void testDrawboardVisible()
    {
        onView(withId(R.id.main_canvas_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testDrawboardUserAction()
    {
        openDialog();
        activityTestRule.getActivity().drawingArea.resetCanvas();
        onView(withId(R.id.main_canvas_view)).perform(performTouch(50, 50, 300, 50));//click to hide toolbar
        onView(withId(R.id.main_canvas_view)).perform(performTouch(50, 50, 300, 50)); // click to draw
        onView(withId(R.id.main_canvas_view)).perform(performTouch(300, 200, 700, 200)); // click to draw

        onView(withId(R.id.draw_point_view)).check(matches(checkCoordinates(50,50, 300, 50)));
        onView(withId(R.id.draw_point_view)).check(matches(checkCoordinates(300,200, 700, 200)));
    }

    public static Matcher<View> checkCoordinates(final float x_down, final float y_down, final float x_up, final float y_up)
    {
        return new Matcher<View>() {
            @Override
            public void describeTo(Description description) {

            }

            @Override
            public boolean matches(Object item) {
                DrawArea temp = (DrawArea) item;
                Bitmap tool_bitmap = temp.getBitmap();


                for(int i = (int) x_down; i < x_up; i++)
                {
                        int alpha_color = Color.alpha(tool_bitmap.getPixel(i, (int) y_down));
                        int red_color = Color.red(tool_bitmap.getPixel(i, (int) y_down));
                        int green_color = Color.green(tool_bitmap.getPixel(i, (int) y_down));
                        int blue_color = Color.blue(tool_bitmap.getPixel(i, (int) y_down));

                        if (Color.BLACK != Color.argb(alpha_color, red_color, green_color, blue_color))
                        {
                            return false;
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
                    if(((FlyoutToolbar.MenuItemImage)result).getID() == R.drawable.ic_si_glyph_line_two_angle_point)
                    {
                        activityTestRule.getActivity().toolFlyoutMenu.setSelectedMenuItem(result);
                        break;
                    }
                }
            }
        });
    }

    public static ViewAction performTouch(final float x_down, final float y_down, final float x_up, final float y_up) {

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

                float[] coordinates_down = new float[] { x_down + location[0], y_down + location[1] };
                float[] coordinates_up = new float[] { x_up + location[0], y_up + location[1] };
                float[] precision = new float[] { 1f, 1f };

                MotionEvent down = MotionEvents.sendDown(uiController, coordinates_down, precision).down;
                uiController.loopMainThreadForAtLeast(500); // wait
                MotionEvents.sendUp(uiController, down, coordinates_down);
                uiController.loopMainThreadForAtLeast(500); // wait
                Swipe.FAST.sendSwipe(uiController, coordinates_down, coordinates_up, precision); // go down
            }
        };
    }
}
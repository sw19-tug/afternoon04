package at.tugraz.ist.swe;

import android.graphics.Bitmap;
import android.graphics.Color;
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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DrawPathTest {

    private static MotionEvent down_;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testSelectedTool() {
        onView(withId(R.id.toolFlyoutMenu)).perform(click());
        activityTestRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int amount_items = activityTestRule.getActivity().toolFlyoutMenu.getAdapter().getCount();
                for (int counter = 0; 0 < amount_items; counter++) {
                    FlyoutMenuView.MenuItem result = activityTestRule.getActivity().toolFlyoutMenu.getAdapter().getItem(counter);
                    if (((FlyoutToolbar.MenuItemImage) result).getID() == R.drawable.ic_outline_brush_24px) {
                        activityTestRule.getActivity().toolFlyoutMenu.setSelectedMenuItem(result);
                        break;
                    }
                }
                PaintingTool tool = activityTestRule.getActivity().drawingArea.getPaintingTool();
                assertEquals(PathTool.class.toString(), tool.getClass().toString());
            }
        });
    }

    @Test
    public void testDrawboardVisible() {
        onView(withId(R.id.main_canvas_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testDrawboardUserAction() {
        activityTestRule.getActivity().drawingArea.resetCanvas();
        openDialog(R.drawable.ic_outline_brush_24px);

        onView(withId(R.id.main_canvas_view)).perform(performTouchDown(50, 50)); // click to close menu
        onView(withId(R.id.main_canvas_view)).perform(performTouchUp(50, 50)); // click to close menu

        onView(withId(R.id.main_canvas_view)).perform(performTouchDown(50, 50)); // click to go down
        onView(withId(R.id.main_canvas_view)).perform(performMove(100, 100)); // click to move
        onView(withId(R.id.main_canvas_view)).perform(performMove(200, 50)); // click to move
        onView(withId(R.id.main_canvas_view)).perform(performMove(700, 700)); // click to move
        onView(withId(R.id.main_canvas_view)).perform(performTouchUp(700, 700));

        onView(withId(R.id.draw_point_view)).check(matches(checkCoordinates(50, 50, Color.BLACK)));
        onView(withId(R.id.draw_point_view)).check(matches(checkCoordinates(75, 75, Color.BLACK)));
        onView(withId(R.id.draw_point_view)).check(matches(checkCoordinates(150, 75, Color.BLACK)));
        onView(withId(R.id.draw_point_view)).check(matches(checkCoordinates(450, 375, Color.BLACK)));
        onView(withId(R.id.draw_point_view)).check(matches(checkCoordinates(699, 699, Color.BLACK)));

    }

    @Test
    public void testEraser() {
        activityTestRule.getActivity().drawingArea.resetCanvas();
        openDialog(R.drawable.ic_outline_brush_24px);

        onView(withId(R.id.main_canvas_view)).perform(performTouchDown(50, 50)); // click to close menu
        onView(withId(R.id.main_canvas_view)).perform(performTouchUp(50, 50)); // click to close menu

        onView(withId(R.id.main_canvas_view)).perform(performTouchDown(50, 50)); // click to go down
        onView(withId(R.id.main_canvas_view)).perform(performMove(100, 100)); // click to move
        onView(withId(R.id.main_canvas_view)).perform(performMove(200, 50)); // click to move
        onView(withId(R.id.main_canvas_view)).perform(performMove(700, 700)); // click to move
        onView(withId(R.id.main_canvas_view)).perform(performTouchUp(700, 700));

        openDialog(R.drawable.ic_si_glyph_erase);

        onView(withId(R.id.main_canvas_view)).perform(performTouchDown(50, 50)); // click to close menu
        onView(withId(R.id.main_canvas_view)).perform(performTouchUp(50, 50)); // click to close menu

        onView(withId(R.id.main_canvas_view)).perform(performTouchDown(50, 50)); // click to go down
        onView(withId(R.id.main_canvas_view)).perform(performMove(100, 100)); // click to move
        onView(withId(R.id.main_canvas_view)).perform(performMove(200, 50)); // click to move
        onView(withId(R.id.main_canvas_view)).perform(performMove(700, 700)); // click to move
        onView(withId(R.id.main_canvas_view)).perform(performTouchUp(700, 700));

        onView(withId(R.id.draw_point_view)).check(matches(checkCoordinates(50, 50, Color.WHITE)));
        onView(withId(R.id.draw_point_view)).check(matches(checkCoordinates(75, 75, Color.WHITE)));
        onView(withId(R.id.draw_point_view)).check(matches(checkCoordinates(150, 75, Color.WHITE)));
        onView(withId(R.id.draw_point_view)).check(matches(checkCoordinates(450, 375, Color.WHITE)));
        onView(withId(R.id.draw_point_view)).check(matches(checkCoordinates(699, 699, Color.WHITE)));

    }

    public static Matcher<View> checkCoordinates(final float x_check, final float y_check, final int color) {
        return new Matcher<View>() {
            @Override
            public void describeTo(Description description) {

            }

            @Override
            public boolean matches(Object item) {
                DrawArea temp = (DrawArea) item;
                Bitmap tool_bitmap = temp.getBitmap();


                int alpha_color = Color.alpha(tool_bitmap.getPixel((int) x_check, (int) y_check));
                int red_color = Color.red(tool_bitmap.getPixel((int) x_check, (int) y_check));
                int green_color = Color.green(tool_bitmap.getPixel((int) x_check, (int) y_check));
                int blue_color = Color.blue(tool_bitmap.getPixel((int) x_check, (int) y_check));

                if (color != Color.argb(alpha_color, red_color, green_color, blue_color)) {
                    return false;
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

    public void openDialog(final int tool) {
        onView(withId(R.id.toolFlyoutMenu)).perform(click());
        activityTestRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int amount_items = activityTestRule.getActivity().toolFlyoutMenu.getAdapter().getCount();
                for (int counter = 0; 0 < amount_items; counter++) {
                    FlyoutMenuView.MenuItem result = activityTestRule.getActivity().toolFlyoutMenu.getAdapter().getItem(counter);
                    if (((FlyoutToolbar.MenuItemImage) result).getID() == tool) {
                        activityTestRule.getActivity().toolFlyoutMenu.setSelectedMenuItem(result);
                        break;
                    }
                }
            }
        });
    }

    public static ViewAction performTouchUp(final float x_up, final float y_up) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "perform touch up";
            }

            @Override
            public void perform(UiController uiController, final View view) {

                int[] location = new int[2];
                view.getLocationOnScreen(location);

                float[] coordinates_up = new float[]{x_up + location[0], y_up + location[1]};

                MotionEvents.sendUp(uiController, down_, coordinates_up);
                uiController.loopMainThreadForAtLeast(500); // wait
            }
        };
    }

    public static ViewAction performMove(final float x_move, final float y_move) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "perform move";
            }

            @Override
            public void perform(UiController uiController, final View view) {

                int[] location = new int[2];
                view.getLocationOnScreen(location);

                float[] coordinates_move = new float[]{x_move + location[0], y_move + location[1]};

                MotionEvents.sendMovement(uiController, down_, coordinates_move);
                uiController.loopMainThreadForAtLeast(500); // wait
            }
        };
    }

    public static ViewAction performTouchDown(final float x_down, final float y_down) {
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

                float[] coordinates_down = new float[]{x_down + location[0], y_down + location[1]};
                float[] precision = new float[]{1f, 1f};

                down_ = MotionEvents.sendDown(uiController, coordinates_down, precision).down;
                uiController.loopMainThreadForAtLeast(500); // wait
            }
        };
    }

}
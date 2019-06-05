package at.tugraz.ist.swe;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.MotionEvents;
import android.support.test.espresso.matcher.BoundedMatcher;
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
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class StrokeWidthTest {

        @Rule
        public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

        @Test
        public void testStrokeWidthElementsVisible(){
            selectTool(R.drawable.ic_si_glyph_circle);
            onView(withId(R.id.strokeWidthLayout)).check(matches(isDisplayed()));
            onView(withId(R.id.strokewidth_left)).check(matches(isDisplayed()));
            onView(withId(R.id.strokewidth_right)).check(matches(isDisplayed()));
            onView(withId(R.id.strokewidth_text)).check(matches(isDisplayed()));
        }

        @Test
        public void testCorrectPointSize() {
            selectTool(R.drawable.ic_si_glyph_circle);
            onView(withId(R.id.main_canvas_view)).perform(performTouch(150, 150)); // click to close menu

            onView(withId(R.id.strokewidth_text)).perform(setText("30"), pressImeActionButton());

            onView(withId(R.id.main_canvas_view)).perform(performTouch(150, 150)); // click to draw
            onView(withId(R.id.main_canvas_view)).perform(performTouch(50, 50)); // click to draw

            onView(withId(R.id.draw_point_view)).check(matches(checkCoordinates(50,50, 30)));
            onView(withId(R.id.draw_point_view)).check(matches(checkCoordinates(150,150, 30)));
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

        public void selectTool(final int id)
        {
            onView(withId(R.id.toolFlyoutMenu)).perform(click());
            activityTestRule.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int amount_items = activityTestRule.getActivity().toolFlyoutMenu.getAdapter().getCount();
                    for(int counter = 0; 0 < amount_items; counter++)
                    {
                        FlyoutMenuView.MenuItem result = activityTestRule.getActivity().toolFlyoutMenu.getAdapter().getItem(counter);
                        if(((FlyoutToolbar.MenuItemImage)result).getID() == id)
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

    public static Matcher<View> checkCoordinates(final float x, final float y, final int size)
    {
        return new Matcher<View>() {
            @Override
            public void describeTo(Description description) {

            }

            @Override
            public boolean matches(Object item) {
                DrawArea temp = (DrawArea) item;
                Bitmap tool_bitmap = temp.getBitmap();

                int alpha_color_top = Color.alpha(tool_bitmap.getPixel((int )x, (int) y - size));
                int red_color_top = Color.red(tool_bitmap.getPixel((int )x , (int) y - size));
                int green_color_top = Color.green(tool_bitmap.getPixel((int )x, (int) y - size));
                int blue_color_top = Color.blue(tool_bitmap.getPixel((int )x, (int) y - size));

                int top = Color.argb(alpha_color_top, red_color_top, green_color_top, blue_color_top);

                int alpha_color_bottom = Color.alpha(tool_bitmap.getPixel((int )x, (int) y + size - 1));
                int red_color_bottom = Color.red(tool_bitmap.getPixel((int )x, (int) y + size - 1));
                int green_color_bottom = Color.green(tool_bitmap.getPixel((int )x, (int) y + size - 1));
                int blue_color_bottom = Color.blue(tool_bitmap.getPixel((int )x, (int) y + size - 1));

                int bottom = Color.argb(alpha_color_bottom, red_color_bottom, green_color_bottom, blue_color_bottom);

                int alpha_color_left = Color.alpha(tool_bitmap.getPixel((int )x - size, (int) y));
                int red_color_left = Color.red(tool_bitmap.getPixel((int )x - size, (int) y));
                int green_color_left = Color.green(tool_bitmap.getPixel((int )x - size, (int) y));
                int blue_color_left = Color.blue(tool_bitmap.getPixel((int )x - size, (int) y));

                int left = Color.argb(alpha_color_left, red_color_left, green_color_left, blue_color_left);

                int alpha_color_right = Color.alpha(tool_bitmap.getPixel((int )x + size - 1, (int) y));
                int red_color_right = Color.red(tool_bitmap.getPixel((int )x + size - 1, (int) y));
                int green_color_right = Color.green(tool_bitmap.getPixel((int )x + size - 1, (int) y));
                int blue_color_right = Color.blue(tool_bitmap.getPixel((int )x + size - 1, (int) y));

                int right = Color.argb(alpha_color_right, red_color_right, green_color_right, blue_color_right);

                if(Color.BLACK == top && Color.BLACK == bottom && Color.BLACK == left && Color.BLACK == right)
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


package at.tugraz.ist.swe;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import android.view.View;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.UiController;
import android.support.test.espresso.action.MotionEvents;
import org.hamcrest.Matcher;
import android.view.MotionEvent;
import java.util.List;

import android.graphics.Point;


@RunWith(AndroidJUnit4.class)
public class DrawPathTest {

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
        onView(withId(R.id.main_canvas_view)).perform(performTouch(50, 50, 55, 55));
        onView(withId(R.id.draw_path_view)).check(matches(checkCoordinates(50,50)));
        onView(withId(R.id.draw_path_view)).check(matches(checkCoordinates(51,51)));
        onView(withId(R.id.draw_path_view)).check(matches(checkCoordinates(52,52)));
        onView(withId(R.id.draw_path_view)).check(matches(checkCoordinates(53,53)));
        onView(withId(R.id.draw_path_view)).check(matches(checkCoordinates(54,54)));
        onView(withId(R.id.draw_path_view)).check(matches(checkCoordinates(55,55)));
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

                MotionEvent down = MotionEvents.sendDown(uiController, coordinates_down, precision).down; // go down
                uiController.loopMainThreadForAtLeast(200); // wait
                MotionEvents.sendUp(uiController, down, coordinates_up); // go up
            }
        };
    }

    public static Matcher<View> checkCoordinates(final float x, final float y)
    {
        return new Matcher<View>() {
            @Override
            public void describeTo(Description description) {

            }

            @Override
            public boolean matches(Object item) {
                DrawPointView temp = (DrawPointView) item;
                List<Point> temp_array = temp.getPoint_list();

                for(int i = 0; i< temp_array.size(); i++)
                {
                    if (temp_array.get(i).x == x)
                        if (temp_array.get(i).y == y)
                            return true;
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
}

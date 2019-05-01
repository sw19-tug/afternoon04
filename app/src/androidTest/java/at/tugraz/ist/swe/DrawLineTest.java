package at.tugraz.ist.swe;

import android.graphics.Paint;
import android.support.test.espresso.core.internal.deps.guava.base.Preconditions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.zakariya.flyoutmenu.FlyoutMenuView;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.equalTo;

@RunWith(AndroidJUnit4.class)
public class DrawLineTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testSelectedTool()
    {
        selectLineTool();
        PaintingTool tool = activityTestRule.getActivity().drawingArea.getPaintingTool();

        //assert(tool.toString().equals(Line.class.toString()));
        isLineClass(tool);
    }

    public void selectLineTool()
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
    public static Matcher<Object> isLineClass(PaintingTool tool) {
        // use preconditions to fail fast when a test is creating an invalid matcher.
        Preconditions.checkArgument(!(tool.equals(null)));
        return isLineClass(equalTo(tool));
    }

    public static Matcher<Object> isLineClass (final Matcher<PaintingTool> objectToMatch) {
        Preconditions.checkNotNull(objectToMatch);
        return new TypeSafeMatcher<Object>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Expected class: " + Line.class.toString());
            }

            @Override
            public void describeMismatchSafely(Object item, Description description) {
                description.appendText("Actual class: " + item.getClass().toString());
            }

            @Override
            public boolean matchesSafely(Object item) {


                return false;
            }

        };
    }

}

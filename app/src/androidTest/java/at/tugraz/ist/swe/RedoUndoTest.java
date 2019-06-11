package at.tugraz.ist.swe;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

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
        openDialog();
        onView(withId(R.id.strokeWidthLayout)).check(doesNotExist());
        onView(withId(R.id.strokewidth_left)).check(doesNotExist());
        onView(withId(R.id.strokewidth_right)).check(doesNotExist());
        onView(withId(R.id.strokewidth_text)).check(doesNotExist());
        onView(withId(R.id.undoLayout)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonUndo)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonRedo)).check(matches(isDisplayed()));
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
                    if(((FlyoutToolbar.MenuItemImage)result).getID() == R.drawable.ic_baseline_swap_horiz_24px)
                    {
                        activityTestRule.getActivity().toolFlyoutMenu.setSelectedMenuItem(result);
                        break;
                    }
                }
            }
        });
    }

}
package at.tugraz.ist.swe;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.zakariya.flyoutmenu.FlyoutMenuView;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
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

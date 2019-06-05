package at.tugraz.ist.swe;

import android.Manifest;
import android.os.Environment;
import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import org.zakariya.flyoutmenu.FlyoutMenuView;


import java.io.File;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SaveImageTest {

    @Rule public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testImageSave()
    {

        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/");
        int len_before_save = dir.listFiles().length;

        openDialog();
        SystemClock.sleep(1000);

        assertTrue(dir.listFiles().length == (len_before_save + 1));
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
                    if(((FlyoutToolbar.MenuItemImage)result).getID() == R.drawable.ic_si_save)
                    {
                        activityTestRule.getActivity().toolFlyoutMenu.setSelectedMenuItem(result);
                        break;
                    }
                }
            }
        });
    }

}
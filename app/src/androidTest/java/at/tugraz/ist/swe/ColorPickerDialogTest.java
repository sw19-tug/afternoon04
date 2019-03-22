package at.tugraz.ist.swe;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class ColorPickerDialogTest {

    @Test
    public void testDialogVisible()
    {
        onView(withId(R.id.dlg_color)).check(matches(isDisplayed()));
    }
}

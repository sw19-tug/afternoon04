package at.tugraz.ist.swe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.MotionEvents;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.zakariya.flyoutmenu.FlyoutMenuView;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class ImportImageTest {

    private static Bitmap bitmap;
    private static Bitmap canvas;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testDrawboardVisible()
    {
        onView(withId(R.id.main_canvas_view)).check(matches(isDisplayed()));
    }

    @Test
    public void openPicture() {

        try {
            InputStream is = activityTestRule.getActivity().getResources().getAssets().open("demo_img.png");
            bitmap = BitmapFactory.decodeStream(is);

            activityTestRule.getActivity().drawingArea.setTool(new ImageImportTool(bitmap, null));
            canvas = activityTestRule.getActivity().drawingArea.getBitmap();

            float finger_x = canvas.getWidth() / (float) 2.f;
            float finger_y = canvas.getHeight() / (float) 2.f;
            performTouch(finger_x, finger_y);


            int height = bitmap.getHeight()-1;
            int width = bitmap.getWidth()-1;

            int begin_x =  (int)finger_x - (width / 2);
            int begin_y = (int)finger_y - (height /2);

            int end_x = (int)finger_x + (width / 2);
            int end_y = (int)finger_y + (height /2);

            for(int i = 0;i < 100; i++) {
                Random randomGenerator = new Random();
                int x = randomGenerator.nextInt((end_x - begin_x) + 1) + begin_x;
                int y = randomGenerator.nextInt((end_y - begin_y) + 1) + begin_y;

                int x_bitmap = x - begin_x;
                int y_bitmap = y - begin_y;
                onView(withId(R.id.draw_point_view)).check(matches(checkCoordinates(x_bitmap,y_bitmap, x, y)));
            }


            }
        catch (IOException e){
            e.printStackTrace();
        }



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
                    if(((FlyoutToolbar.MenuItemImage)result).getID() == R.drawable.ic_outline_add_photo_alternate_24px)
                    {
                        activityTestRule.getActivity().toolFlyoutMenu.setSelectedMenuItem(result);
                        break;
                    }
                }
            }
        });
    }

    public static Matcher<View> checkCoordinates(final float x_bitmap, final float y_bitmap, final int x_global, final int y_global)
    {
        return new Matcher<View>() {
            @Override
            public void describeTo(Description description) {

            }

            @Override
            public boolean matches(Object item) {



                int alpha_color_bitmap = Color.alpha(bitmap.getPixel((int )x_bitmap, (int) y_bitmap));
                int red_color_bitmap = Color.red(bitmap.getPixel((int) x_bitmap,(int) y_bitmap));
                int green_color_bitmap = Color.green(bitmap.getPixel((int) x_bitmap,(int) y_bitmap));
                int blue_color_bitmap = Color.blue(bitmap.getPixel((int) x_bitmap,(int) y_bitmap));

                int alpha_color_global = Color.alpha(canvas.getPixel((int )x_global, (int) y_global));
                int red_color_global = Color.red(canvas.getPixel((int) x_global,(int) y_global));
                int green_color_global = Color.green(canvas.getPixel((int) x_global,(int) y_global));
                int blue_color_global = Color.blue(canvas.getPixel((int) x_global,(int) y_global));

                if(Color.argb(alpha_color_bitmap, red_color_bitmap, green_color_bitmap, blue_color_bitmap) == Color.argb(alpha_color_global, red_color_global, green_color_global, blue_color_global))
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

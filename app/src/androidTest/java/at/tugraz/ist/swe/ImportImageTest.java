package at.tugraz.ist.swe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.MotionEvents;
import android.support.test.rule.ActivityTestRule;
import android.view.MotionEvent;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.assertTrue;


public class ImportImageTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void openPicture() {
        activityTestRule.getActivity().drawingArea.resetCanvas();
        try {
            InputStream is = activityTestRule.getActivity().getResources().getAssets().open("demo_img.png");
            Bitmap bitmap = BitmapFactory.decodeStream(is);

            activityTestRule.getActivity().drawingArea.setTool(new ImageImportTool(bitmap, null));
            Bitmap canvas = activityTestRule.getActivity().drawingArea.getBitmap();

            float finger_x = canvas.getWidth() / 2.f;
            float finger_y = canvas.getHeight() / 2.f;
            onView(withId(R.id.main_canvas_view)).perform(performTouch(finger_x, finger_y));

            Bitmap canvas_with_pic = activityTestRule.getActivity().drawingArea.getBitmap();

            int height = bitmap.getHeight();
            int width = bitmap.getWidth();

            int begin_x = (int) finger_x - (width / 2);
            int begin_y = (int) finger_y - (height / 2);
            Bitmap pic_on_canvas = Bitmap.createBitmap(canvas_with_pic, begin_x, begin_y, width, height);

            assertTrue(pic_on_canvas.sameAs(bitmap));


        } catch (IOException e) {
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

                float[] coordinates = new float[]{x + location[0], y + location[1]};
                float[] precision = new float[]{1f, 1f};

                MotionEvent down = MotionEvents.sendDown(uiController, coordinates, precision).down; // go down
                uiController.loopMainThreadForAtLeast(200); // wait
                MotionEvents.sendUp(uiController, down, coordinates); // go up
                uiController.loopMainThreadForAtLeast(1000); // wait
            }
        };
    }

}


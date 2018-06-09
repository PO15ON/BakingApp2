package com.example.ahmed.bakingapp;


import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

@RunWith(AndroidJUnit4.class)
public class MainActivity2Test {

    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule<Main2Activity>(Main2Activity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent result = new Intent(context, Main2Activity.class);
            result.putExtra("bundle", MainActivity.createResultData());
            return result;
        }
    };

    //    @Before
//    public void stubIntent(){
//        intending(isInternal()).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
//    }
    @Test
    public void buttonExistTest() {
//        intended(hasExtra("bundle", BundleMatchers.hasKey("bundle")));
        onView(ViewMatchers.withId(R.id.next_btn))
                .perform(click());
    }
}

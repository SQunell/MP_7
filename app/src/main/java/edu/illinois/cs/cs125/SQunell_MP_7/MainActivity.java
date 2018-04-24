package edu.illinois.cs.cs125.SQunell_MP_7;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Main class for MP.
 */
public final class MainActivity extends AppCompatActivity {

    /**
     * Opens up the camera to take a picture
     */
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public static final int PICK_IMAGE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

   //Sets the image view to the photo that was just taken
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "On Activity Result Ran");

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            pic.setImageBitmap(imageBitmap);
        }


      /*  if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Log.d(TAG, "Selected image is about to be placed.");
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            pic.setImageBitmap(imageBitmap);
        } */

     /*   if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {

            // Let's read picked image data - its URI
           Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            // Now we need to set the GUI ImageView data with data read from the picked file.
            pic.setImageBitmap(BitmapFactory.decodeFile(imagePath));

            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();
        } */



    }








    ImageView pic;
    Button submit;
    Button photo;
    TextView recs;

    //Temporary variable- Just used to switch between two preset recommended food options
    int counter;


    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "MP_7:";

    /** Request queue for our API requests. */
    private static RequestQueue requestQueue;

    /**
     * Run when this activity comes to the foreground.
     *
     * @param savedInstanceState unused
     */

    private static String latest;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the queue for our API requests
        requestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_main);

        final Button importz = findViewById(R.id.Import);
        importz.setVisibility(View.VISIBLE);
        importz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Import photo button clicked");
                //IMPORT PHOTO METHOD
/*
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
                Log.d(TAG, "Import photo button finished"); */
            }
        });

        final Button newfood = findViewById(R.id.Refood);
        newfood.setVisibility(View.INVISIBLE);
        newfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "New food button clicked");
                //NEW FOOD METHOD
                if (counter % 2 == 0) {
                    recs.setText("Emotion: Sadness    Recommended Food: Mac and Cheese");
                } else {
                    recs.setText("Emotion: Sadness    Recommended Food: Ice Cream");
                }
                counter++;
            }
        });

        final Button restaurant = findViewById(R.id.Explore);
        restaurant.setVisibility(View.INVISIBLE);
        restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Explore restaurant button clicked");

                if (counter % 2 == 0) {
                    recs.setText("Recommended Restaurant: Oberweis Dairy");
                } else {
                    recs.setText("Recommended Restaurant: Noodles & Company");
                }
            }
        });

        final Button redo = findViewById(R.id.Redo);
        redo.setVisibility(View.INVISIBLE);
        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "redo photo button clicked");
                //RETURN TO STARTING STATE/VISIBILITY

                newfood.setVisibility(View.INVISIBLE);
                restaurant.setVisibility(View.INVISIBLE);
                redo.setVisibility(View.INVISIBLE);
                recs.setVisibility(View.INVISIBLE);
                importz.setVisibility(View.VISIBLE);
                photo.setVisibility(View.VISIBLE);
                submit.setVisibility(View.INVISIBLE);
                pic.setVisibility(View.INVISIBLE);
                recs.setText("Emotion: Sadness    Recommended Food: Ice Cream");
                counter = 0;
            }
        });

        recs = findViewById(R.id.Recommend);
        recs.setVisibility(View.INVISIBLE);

        photo = findViewById(R.id.Photo);
        photo.setVisibility(View.VISIBLE);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "take photo button clicked");
                //TAKE PHOTO METHOD
                dispatchTakePictureIntent();
                pic.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);

            }
        });

        submit = findViewById(R.id.Submit);
        submit.setVisibility(View.INVISIBLE);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Submit photo button clicked");
                //ANALYZE PHOTO METHOD
                newfood.setVisibility(View.VISIBLE);
                restaurant.setVisibility(View.VISIBLE);
                redo.setVisibility(View.VISIBLE);
                recs.setVisibility(View.VISIBLE);
                importz.setVisibility(View.INVISIBLE);
                photo.setVisibility(View.INVISIBLE);
                submit.setVisibility(View.INVISIBLE);
                photoAPI();

            }
        });



        pic = findViewById(R.id.Image);
        pic.setVisibility(View.INVISIBLE);






    }

    /**
     * Run when this activity is no longer visible.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }
    /**parse data from face api. */
    // NEED TO DEAL WITH NOT 1 FACE
    static String faceParse(final String json) {
        return null;
    }

    /**
     * Make a call to the face API. NOT COMPLETE
     */
    void photoAPI() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://westcentralus.api.cognitive.microsoft.com/face/v1.0"
                            + BuildConfig.API_KEY1,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                Log.d(TAG, response.toString(2));
                                latest = response.toString();
                            } catch (JSONException ignored) { }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            Log.e(TAG, error.toString());
                        }
                    });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /** Call eatstreet API. NOT COMPLETE **/
    void foodAPI() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://api.eatstreet.com/publicapi/v1"
                            + BuildConfig.API_KEY2,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                Log.d(TAG, response.toString(2));
                                latest = response.toString();
                            } catch (JSONException ignored) { }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

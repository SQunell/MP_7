package edu.illinois.cs.cs125.SQunell_MP_7;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Random;

import com.microsoft.projectoxford.face.*;
import com.microsoft.projectoxford.face.contract.*;


/**
 * Main class for MP.
 */
public final class MainActivity extends AppCompatActivity {

    private FaceServiceClient faceServiceClient = new FaceServiceRestClient("https://westcentralus.api.cognitive.microsoft.com/face/v1.0", BuildConfig.API_KEY1);

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

    Bitmap imageBitmap;

   //Sets the image view to the photo that was just taken
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "On Activity Result Ran");


        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
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
    Button newfood;
    Button redo;
    Button importz;
    Button restaurant;

    //Temporary variable- Just used to switch between two preset recommended food options
    int counter;


    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "MP_7:";
    /** current emotion. */
    String emotion;
    /**current food. */
    String food;

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

        importz = findViewById(R.id.Import);
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

        newfood = findViewById(R.id.Refood);
        newfood.setVisibility(View.INVISIBLE);
        newfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                emoto(emotion);
            }
        });
        //Still need to figure out what this even gonna do
        restaurant = findViewById(R.id.Explore);
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

        redo = findViewById(R.id.Redo);
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
                detectAndFrame(imageBitmap);

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
    // not tested
    void faceParse(final String json) {
        JsonParser parser = new JsonParser();
        try {
            JsonArray peepz = parser.parse(json).getAsJsonArray();
            if (peepz.size() == 0 ) {
                recs.setText("Sorry, we need a clear photo of a human face.");
                return;
            } else if (peepz.size() > 1) {
                recs.setText("Please submit a photo of just one face.");
                return;
            } else {
                JsonObject emotions = peepz.get(0).getAsJsonObject().getAsJsonObject("faceAttributes")
                        .getAsJsonObject("emotion");
                double anger = emotions.get("anger").getAsDouble();
                double contempt = emotions.get("contempt").getAsDouble();
                double disgust = emotions.get("disgust").getAsDouble();
                double fear = emotions.get("fear").getAsDouble();
                double happiness = emotions.get("happiness").getAsDouble();
                double neutral = emotions.get("neutral").getAsDouble();
                double sadness = emotions.get("sadness").getAsDouble();
                double surprise = emotions.get("surprise").getAsDouble();
                double biggest = Math.max(Math.max(Math.max(Math.max(Math.max(Math.max(Math.max(anger,contempt),disgust),fear),happiness),neutral),sadness),surprise);
                String finalemot;
                if (anger == biggest) {
                    finalemot = "anger";
                    //Intentionally merging contempt and disgust
                } else if (contempt == biggest) {
                    finalemot = "disgust";
                } else if (disgust == biggest) {
                    finalemot = "disgust";
                } else if (fear == biggest) {
                    finalemot = "fear";
                } else if (happiness == biggest) {
                    finalemot = "happiness";
                } else if (neutral == biggest) {
                    finalemot = "neutral";
                } else if (sadness == biggest) {
                    finalemot = "sadness";
                } else {
                    //Intentionally merging fear and surprise
                    finalemot = "fear";
                }
                emoto(finalemot);
                //not sure why these visibilities are flagging checkstyle
                newfood.setVisibility(View.VISIBLE);
                restaurant.setVisibility(View.VISIBLE);
                redo.setVisibility(View.VISIBLE);
                recs.setVisibility(View.VISIBLE);
                importz.setVisibility(View.INVISIBLE);
                photo.setVisibility(View.INVISIBLE);
                submit.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            recs.setText("We really didn't like that image. Git gud.");
        }
    }

    /** set emotion. needs actual foods */
    void emoto(final String emotionk) {
        String newfood = new String(food);
        emotion = emotionk;
        Random index = new Random();
        if (emotionk.equals("anger")) {
            String[] foodz = {"anger1", "anger2"};
            while(newfood.equals(food)) {
                newfood = foodz[index.nextInt(foodz.length)];
            }
            recs.setText("We detected anger. You could use something soothing to calm you down. " +
                    "Perhaps some " + newfood+" will quell your rage.");
        } else if(emotionk.equals("disgust")) {
            String[] foodz = {"disgust1", "disgust2"};
            while(newfood.equals(food)) {
                newfood = foodz[index.nextInt(foodz.length)];
            }
            recs.setText("We detected disgust. Perhaps something fancy will satisfy? " +
                    "We suggest " + newfood+".");
        } else if(emotionk.equals("fear")) {
            String[] foodz = {"fear1", "fear2"};
            while(newfood.equals(food)) {
                newfood = foodz[index.nextInt(foodz.length)];
            }
            recs.setText("We detected fear. Something simple could help you relax. " +
                    "Chill out for a bit with some " + newfood+"!");

        } else if(emotionk.equals("neutral")) {
            String[] foodz = {"neutral1", "neutral2"};
            while(newfood.equals(food)) {
                newfood = foodz[index.nextInt(foodz.length)];
            }
            recs.setText("We didn't detect much emotion. Something new and exciting could spice up your day. " +
                    "We recommend " + newfood+".");
        } else if(emotionk.equals("happiness")) {
            String[] foodz = {"happiness1", "happiness2"};
            while(newfood.equals(food)) {
                newfood = foodz[index.nextInt(foodz.length)];
            }
            recs.setText("We detected happiness. If it ain't broke, don't fix it! " +
                    "Classic " + newfood+" certainly won't hurt your mood.");
        } else {
            String[] foodz = {"sadness1", "sadness2"};
            while(newfood.equals(food)) {
                newfood = foodz[index.nextInt(foodz.length)];
            }
            recs.setText("We detected sadness. Something sweet would perk you up! " +
                    "We bet you'd love some " + newfood+"!");
        }
        food = newfood;
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
                                faceParse(latest);
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



    // Detect faces by uploading face images
    // Frame faces after detection

    private void detectAndFrame(final Bitmap imageBitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(outputStream.toByteArray());
        AsyncTask<InputStream, String, Face[]> detectTask =
                new AsyncTask<InputStream, String, Face[]>() {
                    @Override
                    protected Face[] doInBackground(InputStream... params) {
                        try {
                            publishProgress("Detecting...");
                            Face[] result = faceServiceClient.detect(
                                    params[0],
                                    true,         // returnFaceId
                                    false,        // returnFaceLandmarks
                                    null           // returnFaceAttributes: a string like "age, gender"
                            );
                            if (result == null)
                            {
                                publishProgress("Detection Finished. Nothing detected");
                                return null;
                            }
                            publishProgress(
                                    String.format("Detection Finished. %d face(s) detected",
                                            result.length));
                            return result;
                        } catch (Exception e) {
                            publishProgress("Detection failed");
                            return null;
                        }
                    }
                    @Override
                    protected void onPreExecute() {
                        //TODO: show progress dialog
                    }
                    @Override
                    protected void onProgressUpdate(String... progress) {
                        //TODO: update progress
                    }
                    @Override
                    protected void onPostExecute(Face[] result) {
                        //TODO: update face frames
                    }
                };
        detectTask.execute(inputStream);
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

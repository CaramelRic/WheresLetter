package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.BaseActivity;
import com.example.project.Fake;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends BaseActivity {
    Thread shining;
    MediaPlayer mediaPlayer;
    MediaPlayer letterPlayer;
    Thread timing;
    List<Integer> letters;
    List<Integer> voices;
    List<String> names;
    int current_id;
    Bitmap current;
    String[]tags = new String[3];
    Thread vision;
    BarChart barChart;
    Integer[] times_array = new Integer[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    List<Integer> times = Arrays.asList(times_array);
    Integer[]  average_array = new Integer[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    List<Integer> average = new ArrayList(Arrays.asList(average_array));
    Integer[] reference_array = new Integer[]{R.id.a,R.id.b,R.id.c,R.id.d,R.id.e,R.id.f,R.id.g,R.id.h,R.id.i,R.id.j,R.id.k,R.id.l,R.id.m,R.id.n,R.id.o,R.id.p,R.id.q,R.id.r,R.id.s,R.id.t,R.id.u,R.id.v,R.id.w,R.id.x,R.id.y,R.id.z};
    List<Integer> reference = new ArrayList<>(Arrays.asList(reference_array));
    int duration;
    String[] names_array;
    List<List<Bitmap>> pictures;
    List<List<String[]>> labels;
    List<List<String>> takenTimes;
    List<List<Integer>> durations;
    long takenTime;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(timing != null){
            timing.interrupt();
        }
        if(shining != null){
            shining.interrupt();
        }
        if(mediaPlayer!= null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if(letterPlayer!= null){
            letterPlayer.stop();
            letterPlayer.release();
        }

        pictures = new ArrayList<>();
        labels = new ArrayList<>();
        takenTimes = new ArrayList<>();
        durations = new ArrayList<>();



        for(int i=0;i<26;i++){
            pictures.add(new ArrayList<Bitmap>());
            labels.add(new ArrayList<>());
            takenTimes.add(new ArrayList<>());
            durations.add(new ArrayList<>());

        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    public void click(View v) {
        switch (v.getId()) {
            case R.id.start:
                setMusicType(MusicType.FIRST);
                toast("Game Start");
                break;
            case R.id.history:
                setMusicType(MusicType.FIRST);
                toast("Viewing History");
                break;
        }
    }

    public void start(View view){
        onClick(view);
        setContentView(R.layout.game_layout);

        mediaPlayer = MediaPlayer.create(getBaseContext(),R.raw.background_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        Integer[] letters_array = {R.id.a,R.id.b,R.id.c,R.id.d,R.id.e,R.id.f,R.id.g,R.id.h,R.id.i,R.id.j,R.id.k,R.id.l,R.id.m,R.id.n,R.id.o,R.id.p,R.id.q,R.id.r,R.id.s,R.id.t,R.id.u,R.id.v,R.id.w,R.id.x,R.id.y,R.id.z};
        Integer[] voices_array = {R.raw.a,R.raw.b,R.raw.c,R.raw.d,R.raw.e,R.raw.f,R.raw.g,R.raw.h,R.raw.i,R.raw.j,R.raw.k,R.raw.l,R.raw.m,R.raw.n,R.raw.o,R.raw.p,R.raw.q,R.raw.r,R.raw.s,R.raw.t,R.raw.u,R.raw.v,R.raw.w,R.raw.x,R.raw.y,R.raw.z};
        names_array = new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
        letters = new ArrayList<>(Arrays.asList(letters_array));
        voices = new ArrayList<>(Arrays.asList(voices_array));
        names = new ArrayList<>(Arrays.asList(names_array));

        nextRound();
    }


    public void history(View view) {
        onClick(view);
        setContentView(R.layout.history_layout);
        createBar();
        createHistory();
    }

    public void time(){
        TextView time = (TextView) findViewById(R.id.time);
        if(null!=timing){
            timing.interrupt();
        }

        timing = new Thread(){
            @Override
            public void run(){
                try{
                    long startTime = System.currentTimeMillis();
                    while(!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                long endTime = System.currentTimeMillis();
                                takenTime = endTime;
                                duration = (int) (endTime - startTime)/1000;
                                String text = "Elapsed Time: " + duration+ "s";
                                time.setText(text);
                            }
                        });

                    }
                }
                catch(Exception e){
                }
            }
        };
        timing.start();
    }

    public void shine(View view) {

        GradientDrawable sd = (GradientDrawable) view.getBackground().mutate();
        shining = new Thread(){
            @Override
            public void run(){
                try{
                    while(!isInterrupted()){
                        Thread.sleep(500);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                    sd.setColor(Color.YELLOW);
                                    sd.invalidateSelf();
                                    letterPlayer = MediaPlayer.create(getBaseContext(),voices.get(letters.indexOf(view.getId())));
                                    letterPlayer.start();

                            }
                        });
                        Thread.sleep(500);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                letterPlayer.stop();
                                letterPlayer.release();
                                sd.setColor(Color.WHITE);
                                    sd.invalidateSelf();
                            }
                        });

                    }
                }
                catch(Exception e){
                }


            }
        };
        shining.start();

    }

    public void exit(View view) {
        if(timing != null){
            timing.interrupt();
        }
        if(shining != null){
            shining.interrupt();
        }
        if(mediaPlayer!= null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        setContentView(R.layout.activity_main);
    }

    public void capture(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 8888);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 8888 && resultCode == RESULT_OK) {
            shining.interrupt();
            timing.interrupt();
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap copy = photo.copy(Bitmap.Config.ARGB_8888, true);
            current = copy;
            if(null!=vision){
                vision.interrupt();
            }

            vision = new Thread(){
                @Override
                public void run(){
                    try{
                            classify();
                            String name = names.get(letters.indexOf(current_id)).toUpperCase();
                            String first_letter0 = tags[0].substring(0,1);
                            String first_letter1 = tags[1].substring(0,1);
                            String first_letter2 = tags[2].substring(0,1);

                            String[] labels = tags.clone();
                            String letter = name;

                            //I used the first method in the discussion in class, this method is defined in Fake class;
                            boolean fake = Fake.isFake(letter,labels);

                            if(!fake&&(first_letter0.equals(name)||first_letter1.equals(name)||first_letter2.equals(name))) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //wins
                                        findViewById(current_id).setBackground(new BitmapDrawable(getResources(), photo));
                                        int index = reference.indexOf(current_id);
                                        setHistory(index,photo,labels,takenTime,duration);
                                        letterPlayer = MediaPlayer.create(getBaseContext(),R.raw.cheering);
                                        letterPlayer.start();
                                        voices.remove(letters.indexOf(current_id));
                                        names.remove(letters.indexOf(current_id));
                                        letters.remove(letters.indexOf(current_id));
                                        nextRound();
                                    }
                                });
                            }
                            else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //loses
                                        GradientDrawable sd = (GradientDrawable) findViewById(current_id).getBackground().mutate();
                                        sd.setColor(Color.RED);
                                        sd.invalidateSelf();
                                        letterPlayer = MediaPlayer.create(getBaseContext(),R.raw.booing);
                                        letterPlayer.start();
                                        voices.remove(letters.indexOf(current_id));
                                        names.remove(letters.indexOf(current_id));
                                        letters.remove(letters.indexOf(current_id));
                                        nextRound();
                                    }
                                });
                            }
                            int new_average = (average.get(reference.indexOf(current_id))*(times.get(reference.indexOf(current_id))) + duration) / (times.get(reference.indexOf(current_id))+1);
                            System.out.println(duration);
                            average.set(reference.indexOf(current_id),new_average);
                            times.set(reference.indexOf(current_id),times.get(reference.indexOf(current_id))+1);
                            System.out.println(average.get(reference.indexOf(current_id)));


                    }
                    catch(Exception e){
                    }
                }
            };
            vision.start();
            //Canvas canvas = new Canvas(copy);
            //GradientDrawable gd = (GradientDrawable) findViewById(current_id).getBackground().mutate();
            //GradientDrawable gd = new GradientDrawable();
            //Drawable gd = new GradientDrawable();
            //gd.setBounds(0, 0, 200, 180);
            //gd.setAlpha(255);
            //gd.draw(canvas);
            //findViewById(letters.get(current_id)).setBackground(gd);

        }
    }
        private void classify() {
            //step 1
            Bitmap bitmap = current;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bout);
            Image myimage = new Image();
            myimage.encodeContent(bout.toByteArray());
            //step 2
            AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();
            annotateImageRequest.setImage(myimage);
            Feature f = new Feature();
            f.setType("LABEL_DETECTION");
            f.setMaxResults(5);
            List<Feature> lf = new ArrayList<Feature>();
            lf.add(f);
            annotateImageRequest.setFeatures(lf);
            //step 3
            HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
            GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
            builder.setVisionRequestInitializer(new VisionRequestInitializer("AIzaSyDFn_YBOEe41UOowakp91flzFO_wLZbexA"));
            Vision vision = builder.build();
            //step 4
            BatchAnnotateImagesRequest request = new BatchAnnotateImagesRequest();
            List<AnnotateImageRequest> list = new ArrayList<AnnotateImageRequest>();
            list.add(annotateImageRequest);
            request.setRequests(list);
            try {
                Vision.Images.Annotate task = vision.images().annotate(request);
                BatchAnnotateImagesResponse response = task.execute();
                List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
                int i = 1;
                int length = labels.size();
                tags[0] = labels.get(0).getDescription();
                Log.v("MYTAG", tags[0]);
                while (i < 3 && i < length) {
                    tags[i] = labels.get(i).getDescription();
                    Log.v("MYTAG", tags[i]);
                    i++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            ;

        }

        private void nextRound(){
            if(letters.size()>0) {
                int randomNum = ThreadLocalRandom.current().nextInt(0, letters.size());
                current_id = letters.get(randomNum);
                Button a = (Button) findViewById(letters.get(randomNum));
                shine(a);
                time();
            }
            else{
                if(timing != null){
                    timing.interrupt();
                }
                if(shining != null){
                    shining.interrupt();
                }
                if(mediaPlayer!= null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                if(letterPlayer!= null){
                    letterPlayer.stop();
                    letterPlayer.release();
                }
                toast("Game Ended");
                setContentView(R.layout.activity_main);
            }
        }

        private void createBar(){
            barChart = findViewById(R.id.myBar);
            List<BarEntry> list = new ArrayList();
            for(int i=0;i<26;i++){
                list.add(new BarEntry(i, average.get(i)));
            }
            BarDataSet barDataSet = new BarDataSet(list, "Letters");
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(11f);
            BarData barData = new BarData(barDataSet);
            barChart.setData(barData);
            barChart.setFitBars(true);
            barChart.setMinimumHeight(500);
            barChart.getDescription().setText("average time");
            //barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(names_array));
            barChart.animateY(1000);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setLabelCount(26);
            xAxis.setGranularityEnabled(true);
            xAxis.setGranularity(0);
            xAxis.setDrawGridLines(false);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            String[] copy = names_array.clone();
            for(int i=0;i<26;i++){
                copy[i] = copy[i].toUpperCase();
            }
            xAxis.setValueFormatter(new IndexAxisValueFormatter(copy));
            xAxis.setAxisMaximum(27);
            xAxis.setAxisMinimum(-1);
        }

        private void setHistory(int index, Bitmap picture,String[] tags, long takenTime, int duration){
            List<Bitmap> pics = pictures.get(index);
            List<String[]> tagList = labels.get(index);
            List<String> times = takenTimes.get(index);
            List<Integer> usedTimes = durations.get(index);

            if(pics.size() == 3){
                pics.add(picture);
                pics.remove(0);
            }
            else{
                pics.add(picture);
            }

            if(tagList.size() == 3){
                tagList.add(tags);
                tagList.remove(0);
            }
            else{
                tagList.add(tags);
            }

            SimpleDateFormat formatter= new SimpleDateFormat("MM/dd 'at' HH:mm");
            Date date = new Date(takenTime);
            String dateString = formatter.format(date);

            if(times.size() == 3){
                times.add(dateString);
                times.remove(0);
            }
            else{
                times.add(dateString);
            }

            if(usedTimes.size() == 3){
                usedTimes.add(duration);
                usedTimes.remove(0);
            }
            else{
                usedTimes.add(duration);
            }
        }

        private void createHistory(){
            Integer[][] imageViewID = {{R.id.a1,R.id.a2,R.id.a3},{R.id.b1,R.id.b2,R.id.b3},
                    {R.id.c1,R.id.c2,R.id.c3},{R.id.d1,R.id.d2,R.id.d3},{R.id.e1,R.id.e2,R.id.e3},
                    {R.id.f1,R.id.f2,R.id.f3},{R.id.g1,R.id.g2,R.id.g3},{R.id.h1,R.id.h2,R.id.h3},
                    {R.id.i1,R.id.i2,R.id.i3},{R.id.j1,R.id.j2,R.id.j3},{R.id.k1,R.id.k2,R.id.k3},
                    {R.id.l1,R.id.l2,R.id.l3},{R.id.m1,R.id.m2,R.id.m3},{R.id.n1,R.id.n2,R.id.n3},
                    {R.id.o1,R.id.o2,R.id.o3},{R.id.p1,R.id.p2,R.id.p3},{R.id.q1,R.id.q2,R.id.q3},
                    {R.id.r1,R.id.r2,R.id.r3},{R.id.s1,R.id.s2,R.id.s3},{R.id.t1,R.id.t2,R.id.t3},
                    {R.id.u1,R.id.u2,R.id.u3},{R.id.v1,R.id.v2,R.id.v3},{R.id.w1,R.id.w2,R.id.w3},
                    {R.id.x1,R.id.x2,R.id.x3},{R.id.y1,R.id.y2,R.id.y3},{R.id.z1,R.id.z2,R.id.z3}};
            Integer[][] labelsID = {{R.id.labelsA1,R.id.labelsA2,R.id.labelsA3},{R.id.labelsB1,R.id.labelsB2,R.id.labelsB3},
                    {R.id.labelsC1,R.id.labelsC2,R.id.labelsC3},{R.id.labelsD1,R.id.labelsD2,R.id.labelsD3},
                    {R.id.labelsE1,R.id.labelsE2,R.id.labelsE3},{R.id.labelsF1,R.id.labelsF2,R.id.labelsF3},
                    {R.id.labelsG1,R.id.labelsG2,R.id.labelsG3},{R.id.labelsH1,R.id.labelsH2,R.id.labelsH3},
                    {R.id.labelsI1,R.id.labelsI2,R.id.labelsI3},{R.id.labelsJ1,R.id.labelsJ2,R.id.labelsJ3},
                    {R.id.labelsK1,R.id.labelsK2,R.id.labelsK3},{R.id.labelsL1,R.id.labelsL2,R.id.labelsL3},
                    {R.id.labelsM1,R.id.labelsM2,R.id.labelsM3},{R.id.labelsN1,R.id.labelsN2,R.id.labelsN3},
                    {R.id.labelsO1,R.id.labelsO2,R.id.labelsO3},{R.id.labelsP1,R.id.labelsP2,R.id.labelsP3},
                    {R.id.labelsQ1,R.id.labelsQ2,R.id.labelsQ3},{R.id.labelsR1,R.id.labelsR2,R.id.labelsR3},
                    {R.id.labelsS1,R.id.labelsS2,R.id.labelsS3},{R.id.labelsT1,R.id.labelsT2,R.id.labelsT3},
                    {R.id.labelsU1,R.id.labelsU2,R.id.labelsU3},{R.id.labelsV1,R.id.labelsV2,R.id.labelsV3},
                    {R.id.labelsW1,R.id.labelsW2,R.id.labelsW3},{R.id.labelsX1,R.id.labelsX2,R.id.labelsX3},
                    {R.id.labelsY1,R.id.labelsY2,R.id.labelsY3},{R.id.labelsZ1,R.id.labelsZ2,R.id.labelsZ3}};
            Integer[][] durationID = {{R.id.durationA1,R.id.durationA2,R.id.durationA3},{R.id.durationB1,R.id.durationB2,R.id.durationB3},
                    {R.id.durationC1,R.id.durationC2,R.id.durationC3},{R.id.durationD1,R.id.durationD2,R.id.durationD3},
                    {R.id.durationE1,R.id.durationE2,R.id.durationE3},{R.id.durationF1,R.id.durationF2,R.id.durationF3},
                    {R.id.durationG1,R.id.durationG2,R.id.durationG3},{R.id.durationH1,R.id.durationH2,R.id.durationH3},
                    {R.id.durationI1,R.id.durationI2,R.id.durationI3},{R.id.durationJ1,R.id.durationJ2,R.id.durationJ3},
                    {R.id.durationK1,R.id.durationK2,R.id.durationK3},{R.id.durationL1,R.id.durationL2,R.id.durationL3},
                    {R.id.durationM1,R.id.durationM2,R.id.durationM3},{R.id.durationN1,R.id.durationN2,R.id.durationN3},
                    {R.id.durationO1,R.id.durationO2,R.id.durationO3},{R.id.durationP1,R.id.durationP2,R.id.durationP3},
                    {R.id.durationQ1,R.id.durationQ2,R.id.durationQ3},{R.id.durationR1,R.id.durationR2,R.id.durationR3},
                    {R.id.durationS1,R.id.durationS2,R.id.durationS3},{R.id.durationT1,R.id.durationT2,R.id.durationT3},
                    {R.id.durationU1,R.id.durationU2,R.id.durationU3},{R.id.durationV1,R.id.durationV2,R.id.durationV3},
                    {R.id.durationW1,R.id.durationW2,R.id.durationW3},{R.id.durationX1,R.id.durationX2,R.id.durationX3},
                    {R.id.durationY1,R.id.durationY2,R.id.durationY3},{R.id.durationZ1,R.id.durationZ2,R.id.durationZ3}};
            Integer[][] timeID = {{R.id.timeA1,R.id.timeA2,R.id.timeA3},{R.id.timeB1,R.id.timeB2,R.id.timeB3},
                    {R.id.timeC1,R.id.timeC2,R.id.timeC3},{R.id.timeD1,R.id.timeD2,R.id.timeD3},
                    {R.id.timeE1,R.id.timeE2,R.id.timeE3},{R.id.timeF1,R.id.timeF2,R.id.timeF3},
                    {R.id.timeG1,R.id.timeG2,R.id.timeG3},{R.id.timeH1,R.id.timeH2,R.id.timeH3},
                    {R.id.timeI1,R.id.timeI2,R.id.timeI3},{R.id.timeJ1,R.id.timeJ2,R.id.timeJ3},
                    {R.id.timeK1,R.id.timeK2,R.id.timeK3},{R.id.timeL1,R.id.timeL2,R.id.timeL3},
                    {R.id.timeM1,R.id.timeM2,R.id.timeM3},{R.id.timeN1,R.id.timeN2,R.id.timeN3},
                    {R.id.timeO1,R.id.timeO2,R.id.timeO3},{R.id.timeP1,R.id.timeP2,R.id.timeP3},
                    {R.id.timeQ1,R.id.timeQ2,R.id.timeQ3},{R.id.timeR1,R.id.timeR2,R.id.timeR3},
                    {R.id.timeS1,R.id.timeS2,R.id.timeS3},{R.id.timeT1,R.id.timeT2,R.id.timeT3},
                    {R.id.timeU1,R.id.timeU2,R.id.timeU3},{R.id.timeV1,R.id.timeV2,R.id.timeV3},
                    {R.id.timeW1,R.id.timeW2,R.id.timeW3},{R.id.timeX1,R.id.timeX2,R.id.timeX3},
                    {R.id.timeY1,R.id.timeY2,R.id.timeY3},{R.id.timeZ1,R.id.timeZ2,R.id.timeZ3}};

            for(int i=0; i<26;i++){
                for(int j=0;j<pictures.get(i).size();j++){
                    System.out.println("elements created");
                    //setting images
                    ImageView imageView = findViewById(imageViewID[i][j]);
                    Bitmap photo = pictures.get(i).get(j);
                    imageView.setImageBitmap(photo);

                    //setting labels
                    TextView label = findViewById(labelsID[i][j]);
                    String temp = "";
                    String[] tags = labels.get(i).get(j);
                    for(int k=0;k<3;k++){
                        if(k!=0){
                            temp += ";";
                        }
                        temp += tags[k];
                    }
                    label.setText(temp);

                    //setting duration
                    TextView usedTime = findViewById(durationID[i][j]);
                    int usedTimeInput = durations.get(i).get(j);
                    usedTime.setText(usedTimeInput+"s");

                    //setting photo taken time
                    TextView takenTime = findViewById(timeID[i][j]);
                    String takenTimeInput = takenTimes.get(i).get(j);
                    takenTime.setText(takenTimeInput);
                }
            }



        }
}
package essential.esl.ui.fragment;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import essential.esl.R;
import essential.esl.app.BasePage;
import essential.esl.app.MyAnimation;
import essential.esl.app.MyBaseActivity;
import essential.esl.app.MyBaseFragment;
import essential.esl.data.Conversation;
import essential.esl.data.DataSource;
import essential.esl.ui.activity.MainActivity;
import essential.esl.ui.page.DescriptionPage;
import essential.esl.ui.page.QuizzesPage;
import essential.esl.widget.HelpfulTip;

/**
 * Created by admin on 5/25/2016.
 */
public class QuizzeFragment extends MyBaseFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {
    private final int SEEK_TIME = 5000;
    private LinearLayout btnBack, btnShare;
    private TextView tvTitle;
    private ViewPager viewPager;
    public HelpfulTip helpfulTip;
    public Conversation conversation;
    public MyViewPagerAdapter adapter;
    private ArrayList<ImageView> dots;
    private MediaPlayer mediaPlayer;
    private Handler mHandler;
    private TextView tvCurrentTime, tvDuration;
    private ImageView btnPlay;
    private LinearLayout btnBackWard, btnForWard;
    private SeekBar seekBar;
    private boolean isLoading = false;
    private boolean isSetUpAudio = false;


    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_quizze;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        this.conversation = (Conversation) getArguments().getSerializable("converstation");
        helpfulTip = new HelpfulTip(this, rootView, conversation);
        mediaPlayer = new MediaPlayer();
        mHandler = new Handler();
        init(rootView);
        setupViewPager();
        selectPage(0);

    }

    private Runnable mUpdateTimeTask = new Runnable() {

        public void run() {

            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();


            tvCurrentTime.setText(getStringTime(currentDuration));
            int progress = (int) (getProgressPercentage(currentDuration, totalDuration));
            seekBar.setProgress(progress);

            mHandler.postDelayed(this, 30);
        }
    };

    public int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);
        percentage = (((double) currentSeconds) / totalSeconds) * 100;
        return percentage.intValue();
    }

    private void setupViewPager() {
        adapter = new MyViewPagerAdapter((MyBaseActivity) getActivity(), this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void selectPage(int position) {
        for (int i = 0; i < dots.size(); i++) {
            dots.get(i).setColorFilter(getResources().getColor(R.color.secondaryTextColor));
        }
        dots.get(position).setColorFilter(getResources().getColor(R.color.colorPrimaryDark));
    }

    private void init(View rootView) {
        btnBack = (LinearLayout) rootView.findViewById(R.id.btn_Back);
        btnShare = (LinearLayout) rootView.findViewById(R.id.btn_Share);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tvCurrentTime = (TextView) rootView.findViewById(R.id.curentLength);
        tvDuration = (TextView) rootView.findViewById(R.id.duration);
        btnPlay = (ImageView) rootView.findViewById(R.id.btn_play);
        btnBackWard = (LinearLayout) rootView.findViewById(R.id.btn_backWard);
        btnForWard = (LinearLayout) rootView.findViewById(R.id.btn_forWard);
        seekBar = (SeekBar) rootView.findViewById(R.id.seekbar);
        tvDuration.setText(conversation.length);
        dots = new ArrayList<>();
        dots.add((ImageView) rootView.findViewById(R.id.dot1));
        dots.add((ImageView) rootView.findViewById(R.id.dot2));
        dots.add((ImageView) rootView.findViewById(R.id.dot3));
        btnBack.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnBackWard.setOnClickListener(this);
        btnForWard.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);

        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        tvTitle.setText(conversation.title);
    }

    @Override
    public void onBackPress() {
        if (helpfulTip.isTipsShowing()) helpfulTip.hideTips();
        else {
            mediaPlayer.reset();
            mHandler.removeCallbacks(mUpdateTimeTask);
            super.onBackPress();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id != R.id.btn_play)
            MyAnimation.animZoomWhenOnClick(v, this, 1, 1.3f, 1, 1.3f);
        else MyAnimation.animZoomWhenOnClick(v, this, 1, 1.1f, 1, 1.1f);
        switch (id) {
            case R.id.btn_Back:
                MainActivity activity = (MainActivity) getActivity();
                activity.onBackPressed();
                break;
            case R.id.btn_Share:
                break;
            case R.id.btn_backWard:
                seekBackward();
                break;
            case R.id.btn_forWard:
                seekForward();
                break;
            case R.id.btn_play:
                playAudio();
                break;
        }
    }

    private void playAudio() {
        if (!isSetUpAudio) {
            if (!isLoading) {
                btnPlay.setImageResource(R.drawable.pause);
                if (DataSource.isFileExists(conversation.id + "")) {
                    playAudioFromSdCard();
                } else {

                    Ion.with(this)
                            .load(conversation.audioUrl)
                            .progress(new ProgressCallback() {
                                @Override
                                public void onProgress(long downloaded, long total) {
                                    if (isLoading == false) isLoading = true;
                                }
                            })// write to a file
                            .write(new File("/sdcard/Essential/ESLAudios/" + conversation.id + ".mp3"))
                            // run a callback on completion
                            .setCallback(new FutureCallback<File>() {
                                @Override
                                public void onCompleted(Exception e, File result) {
                                    isLoading = false;

                                    playAudioFromSdCard();
                                }
                            });
                }
            } else {
                btnPlay.setImageResource(R.drawable.pause);
            }
        } else {
            if (!isLoading) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.play);
                } else {
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.pause);
                }
            }
        }
    }

    public void seekForward() {
        int currentPosition = mediaPlayer.getCurrentPosition();
        if (currentPosition + SEEK_TIME <= mediaPlayer.getDuration()) {
            mediaPlayer.seekTo(currentPosition + SEEK_TIME);
        } else mediaPlayer.seekTo(mediaPlayer.getDuration());
    }

    public void seekBackward() {
        int currentPosition = mediaPlayer.getCurrentPosition();
        if (currentPosition - SEEK_TIME >= 0) {
            mediaPlayer.seekTo(currentPosition - SEEK_TIME);
        } else mediaPlayer.seekTo(0);
    }

    public void playAudioFromSdCard() {
        mediaPlayer.reset();
        String mediaPath = "/sdcard/Essential/ESLAudios/" + conversation.id + ".mp3";
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Uri uri = Uri.parse(mediaPath);
        try {
            mediaPlayer.setDataSource(getContext().getApplicationContext(), uri);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);
            updateSeekBar();
        } catch (IOException ae) {
        }
        isSetUpAudio = true;
    }

    private String getStringTime(long duration) {
        String newTime;
        long totalSec = duration / 1000;
        long minute = totalSec / 60;
        long sec = totalSec % 60;
        if (minute > 100) return ("--:--");
        if (minute < 10) {
            if (sec < 10) {
                newTime = "0" + minute + ":0" + sec;
                return newTime;
            } else {
                newTime = "0" + minute + ":" + sec;
                return newTime;
            }
        } else {
            if (sec < 10) {
                newTime = minute + ":0" + sec;
                return newTime;
            } else {
                newTime = minute + ":" + sec;
                return newTime;
            }
        }


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int totalDuration = mediaPlayer.getDuration();
        int currentDuration = progressToTimer(seekBar.getProgress(), totalDuration);
        mediaPlayer.seekTo(currentDuration);
        updateSeekBar();
    }

    public void updateSeekBar() {
        mHandler.postDelayed(mUpdateTimeTask, 30);
    }

    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        return currentDuration * 1000;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        btnPlay.setImageResource(R.drawable.play);
        mediaPlayer.seekTo(0);
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private ArrayList<BasePage> pages;

        public MyViewPagerAdapter(MyBaseActivity activity, MyBaseFragment fragment) {
            pages = new ArrayList<>();
            QuizzesPage quizzesPage = new QuizzesPage(fragment, activity, conversation.id);
            DescriptionPage scriptPage = new DescriptionPage(fragment, activity, conversation.script);
            DescriptionPage keyVocabularyPage = new DescriptionPage(fragment, activity, conversation.keyVocabulary);
            pages.add(quizzesPage);
            pages.add(scriptPage);
            pages.add(keyVocabularyPage);

        }

        @Override
        public int getCount() {
            return pages.size();
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View layout = null;
            layout = pages.get(position).getContent();
            container.addView(layout);
            return layout;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            pages.get(position).destroy();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


    }
}

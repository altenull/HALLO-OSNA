package com.altenull.hallo_osna;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.github.florent37.viewanimator.ViewAnimator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import java.util.ArrayList;


public class StoryViewActivity extends BaseActivity {
    protected DisplayImageOptions displayImageOptions;
    protected ArrayList<Integer> savedPosition = new ArrayList();
    protected ArrayList<View> savedViews = new ArrayList();

    private ViewPager viewPager;
    private int CURRENT_ID;
    private int photoTotal;

    private ImageButton logoYellow;
    private ImageButton helpYellow;
    private ImageButton mapButton;
    private ArrayList<Boolean> isThereMap = new ArrayList();
    private ImageButton emailButton;
    private String emailAddress;

    private ArrayList<RelativeLayout> indicators = new ArrayList();

    private RelativeLayout photoTitleLayout;
    private RelativeLayout photoCommentLayout;

    private TextView photoTitle;
    private TextView photoComment;
    private TextView studentName;
    private TextView studentEnglishName;
    private TextView studentMajor;
    private TextView studentPeriod;

    private int offsetX;
    private int offsetY;
    protected double myScale;
    private boolean isZoom = false;

    private ArrayList<RelativeLayout> topBottomLayouts = new ArrayList();
    private ArrayList<Animation> slideAnimations = new ArrayList();
    private ArrayList<Animation> slideBackAnimations = new ArrayList();


    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.view_exit);
    }


    private void setIndicator(int paramInt) {
        int i = 0;

        if ( i >= this.photoTotal ) {
            return;
        }

        if ( this.photoTotal > 5 ) {
            for (  ;  i < this.photoTotal;  i++ ) {
                if ( i == paramInt ) {
                    ((RelativeLayout)this.indicators.get(this.photoTotal -1 - i)).animate().alpha(1.0F).setDuration(200L).setListener(null);
                }
                else {
                    ((RelativeLayout)this.indicators.get(this.photoTotal -1 - i)).animate().alpha(0.4F).setDuration(200L).setListener(null);
                }
            }
        }
        else if ( this.photoTotal <= 5 ) {
            for (  ;  i < this.photoTotal;  i++ ) {
                if ( i == paramInt ) {
                    ((RelativeLayout)this.indicators.get(this.photoTotal -1 - i + 5)).animate().alpha(1.0F).setDuration(200L).setListener(null);
                }
                else {
                    ((RelativeLayout)this.indicators.get(this.photoTotal -1 - i + 5)).animate().alpha(0.4F).setDuration(200L).setListener(null);
                }
            }
        }
    }


    private void zoomClick() {
        int photosNum = this.savedViews.size();

        if ( !this.isZoom ) {
            this.myScale = 1.0D;

            for ( int i = 0;  ;  i++ ) {
                if ( i >= photosNum ) {
                    for ( int j = 0;  j < this.topBottomLayouts.size();  j++ ) {
                        ((RelativeLayout)this.topBottomLayouts.get(j)).startAnimation(this.slideAnimations.get(j));
                    }

                    this.photoTitleLayout.animate().y(0).setInterpolator(new DecelerateInterpolator(2.0F)).setDuration(1000L).setListener(null);
                    this.photoCommentLayout.setVisibility(View.VISIBLE);
                    ViewAnimator.animate(this.photoCommentLayout).newsPaper().duration(1000L).start();

                    getWindow().getDecorView().setBackgroundColor(Color.parseColor("#000000"));

                    this.myScale = this.imageScale;
                    this.isZoom = true;
                    return;
                }
                ((View)this.savedViews.get(i)).animate().scaleX((float)this.myScale).scaleY((float)this.myScale).setInterpolator(new DecelerateInterpolator(2.0F)).setDuration(1000L).setListener(null);
            }
        }
        else if ( this.isZoom ) {
            this.myScale = this.imageScale;

            for ( int i = 0;  ;  i++ ) {
                if ( i >= photosNum ) {
                    for ( int j = 0;  j < this.topBottomLayouts.size();  j++ ) {
                        ((RelativeLayout)this.topBottomLayouts.get(j)).startAnimation(this.slideBackAnimations.get(j));
                    }

                    this.photoTitleLayout.animate().y(-this.topMovement).setDuration(0L).setListener(null);
                    this.photoCommentLayout.setVisibility(View.INVISIBLE);

                    getWindow().getDecorView().setBackgroundColor(Color.parseColor("#FFFFFF"));

                    this.isZoom = false;
                    return;
                }
                ((View)this.savedViews.get(i)).animate().scaleX((float)this.myScale).scaleY((float)this.myScale).setInterpolator(new DecelerateInterpolator(2.0F)).setDuration(200L).setListener(null);
            }
        }
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_story);

        this.displayImageOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_empty).showImageOnFail(R.drawable.empty_photo).resetViewBeforeLoading(false).cacheInMemory(false).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).build();
        this.loadingAnimation = AnimationUtils.loadAnimation(this, R.anim.loading);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#FFFFFF"));

        Intent localIntent = getIntent();
        Bundle localBundle = localIntent.getExtras();
        assert ( localBundle != null );
        setResult(this.RESULT_OK, localIntent);

        this.CURRENT_ID = localBundle.getInt("CURRENT_ID", 0);
        this.photoTotal = ((DataGetterSetters)DataHandler.getInstance().getData().get(this.CURRENT_ID)).getPhoto().size();

        this.imageScale = DataHandler.getInstance().getImageScale();
        this.myScale = this.imageScale;

        this.logoYellow = (ImageButton)findViewById(R.id.logo_yellow);
        this.helpYellow = (ImageButton)findViewById(R.id.help_yellow);

        this.logoYellow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                ViewAnimator.animate(paramAnonymousView).tada().duration(1000L).start();
            }
        });

        this.helpYellow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                StoryViewActivity.this.showHelp();
            }
        });

        for ( int i = 0;  i < this.photoTotal;  i++ ) {
            if ( !((DataGetterSetters)DataHandler.getInstance().getData().get(this.CURRENT_ID)).getPhotoMap().get(i).equals("EMPTY") ) {
                this.isThereMap.add(Boolean.TRUE);
            }
            else {
                this.isThereMap.add(Boolean.FALSE);
            }
        }

        this.mapButton = (ImageButton)findViewById(R.id.story_photo_map);

        if ( this.isThereMap.get(0) ) {
            this.mapButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW);
                    mapIntent.setData(Uri.parse(((DataGetterSetters)DataHandler.getInstance().getData().get(StoryViewActivity.this.CURRENT_ID)).getPhotoMap().get(0)));
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if ( mapIntent.resolveActivity(getPackageManager()) != null ) {
                        startActivity(mapIntent);
                    }
                }
            });
            this.mapButton.setVisibility(View.VISIBLE);
        }
        else {
            this.mapButton.setVisibility(View.INVISIBLE);
        }

        this.emailButton = (ImageButton)findViewById(R.id.story_student_email);
        this.emailAddress = ((DataGetterSetters)DataHandler.getInstance().getData().get(this.CURRENT_ID)).getEmail();

        if ( !this.emailAddress.equals("EMPTY") ) {
            this.emailButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:" + StoryViewActivity.this.emailAddress));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                                    "[HALLO-OSNA] "
                                    + "Ich habe eine Frage für \'"
                                    + ((DataGetterSetters) DataHandler.getInstance().getData().get(StoryViewActivity.this.CURRENT_ID)).getName()
                                    + "\'");
                    try {
                        startActivity(Intent.createChooser(emailIntent, "E-Mail senden"));
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(StoryViewActivity.this, "There are no email clients installed.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else {
            StoryViewActivity.this.emailButton.setVisibility(View.GONE);
        }

        this.indicators.add((RelativeLayout)findViewById(R.id.indicator_1));
        this.indicators.add((RelativeLayout)findViewById(R.id.indicator_2));
        this.indicators.add((RelativeLayout)findViewById(R.id.indicator_3));
        this.indicators.add((RelativeLayout)findViewById(R.id.indicator_4));
        this.indicators.add((RelativeLayout)findViewById(R.id.indicator_5));
        this.indicators.add((RelativeLayout)findViewById(R.id.indicator_6));
        this.indicators.add((RelativeLayout)findViewById(R.id.indicator_7));
        this.indicators.add((RelativeLayout)findViewById(R.id.indicator_8));
        this.indicators.add((RelativeLayout)findViewById(R.id.indicator_9));
        this.indicators.add((RelativeLayout)findViewById(R.id.indicator_10));

        if ( this.photoTotal > 5 ) {
            for ( int k = 1;  k < 10;  k++ ) {
                if ( k >= this.photoTotal ) {
                    ((RelativeLayout)this.indicators.get(k)).animate().alpha(0.0F).setDuration(0L).setListener(null);
                }
            }
        }
        else if ( this.photoTotal <= 5 ) {
            for ( int k = 0;  k < 10;  k++ ) {
                ((RelativeLayout)this.indicators.get(k)).animate().alpha(0.0F).setDuration(0L).setListener(null);
            }

            for ( int k = 0;  k < this.photoTotal;  k++ ) {
                ((RelativeLayout)this.indicators.get(k + 5)).animate().alpha(1.0F).setDuration(0L).setListener(null);
            }
        }

        this.photoTitle = (TextView)findViewById(R.id.story_photo_title);
        this.photoComment = (TextView)findViewById(R.id.story_photo_comment);
        this.studentName = (TextView)findViewById(R.id.story_student_name);
        this.studentEnglishName = (TextView)findViewById(R.id.story_student_english_name);
        this.studentMajor = (TextView)findViewById(R.id.story_student_major);
        this.studentPeriod = (TextView)findViewById(R.id.story_student_period);

        this.photoTitle.setText(((DataGetterSetters)DataHandler.getInstance().getData().get(this.CURRENT_ID)).getPhotoTitle().get(0));
        this.photoComment.setText(((DataGetterSetters)DataHandler.getInstance().getData().get(this.CURRENT_ID)).getPhotoComment().get(0).replace(" ", "\u00A0"));
        this.studentName.setText(((DataGetterSetters)DataHandler.getInstance().getData().get(this.CURRENT_ID)).getName());
        this.studentEnglishName.setText("(" + ((DataGetterSetters)DataHandler.getInstance().getData().get(this.CURRENT_ID)).getEnglishName() + ")");
        this.studentMajor.setText(((DataGetterSetters)DataHandler.getInstance().getData().get(this.CURRENT_ID)).getMajor());
        this.studentPeriod.setText(((DataGetterSetters)DataHandler.getInstance().getData().get(this.CURRENT_ID)).getPeriod());

        if (DataHandler.getInstance().getScreenSize() == this.SCREEN_SIZE_LARGE) {
            this.photoTitle.setTextSize(18.0F);
            this.photoComment.setTextSize(16.0F);
            this.studentName.setTextSize(18.0F);
            this.studentEnglishName.setTextSize(14.0F);
            this.studentMajor.setTextSize(12.0F);
            this.studentPeriod.setTextSize(10.0F);
        }

        this.photoTitleLayout = (RelativeLayout)findViewById(R.id.story_title_layout);
        this.photoCommentLayout = (RelativeLayout)findViewById(R.id.story_comment_layout);

        this.photoTitleLayout.animate().y(-this.topMovement).setDuration(0L).setListener(null);
        this.photoCommentLayout.setVisibility(View.INVISIBLE);

        this.viewPager = (ViewPager)findViewById(R.id.story_viewpager);

        ViewPager localViewPager1 = this.viewPager;
        ImagePagerAdapter localViewPagerModule1 = new ImagePagerAdapter(this.CURRENT_ID);
        localViewPager1.setAdapter(localViewPagerModule1);
        this.viewPager.setCurrentItem(0, true);
        setIndicator(0);

        ViewPager localViewPager2 = this.viewPager;
        ViewPager.PageTransformer localViewPagerModule2 = new ViewPager.PageTransformer() {
            public void transformPage(View paramAnonymousPage, float paramAnonymousPosition) {
                paramAnonymousPage.setRotationY(paramAnonymousPosition * -30.0F);
            }
        };
        localViewPager2.setPageTransformer(true, localViewPagerModule2);

        ViewPager localViewPager3 = this.viewPager;
        ViewPager.OnPageChangeListener localViewPagerModule3 = new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int paramAnonymousInt) {}
            public void onPageScrolled(int paramAnonymousInt1, float paramAnonymousFloat, int paramAnonymousInt2) {}

            public void onPageSelected(final int paramAnonymousInt) {
                StoryViewActivity.this.photoTitle.setText(((DataGetterSetters)DataHandler.getInstance().getData().get(StoryViewActivity.this.CURRENT_ID)).getPhotoTitle().get(paramAnonymousInt));
                StoryViewActivity.this.photoComment.setText(((DataGetterSetters)DataHandler.getInstance().getData().get(StoryViewActivity.this.CURRENT_ID)).getPhotoComment().get(paramAnonymousInt).replace(" ", "\u00A0"));
                StoryViewActivity.this.setIndicator(paramAnonymousInt);

                if ( StoryViewActivity.this.isThereMap.get(paramAnonymousInt) ) {
                    StoryViewActivity.this.mapButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View paramAnonymousView) {
                            Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW);
                            mapIntent.setData(Uri.parse(((DataGetterSetters)DataHandler.getInstance().getData().get(StoryViewActivity.this.CURRENT_ID)).getPhotoMap().get(paramAnonymousInt)));
                            mapIntent.setPackage("com.google.android.apps.maps");
                            if ( mapIntent.resolveActivity(getPackageManager()) != null ) {
                                startActivity(mapIntent);
                            }
                        }
                    });
                    StoryViewActivity.this.mapButton.setVisibility(View.VISIBLE);
                }
                else {
                    StoryViewActivity.this.mapButton.setVisibility(View.INVISIBLE);
                }
            }
        };
        localViewPager3.addOnPageChangeListener(localViewPagerModule3);

        ViewPager localViewPager4 = this.viewPager;
        View.OnTouchListener localViewPagerModule4 = new View.OnTouchListener() {
            public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent) {
                if (paramAnonymousMotionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    StoryViewActivity.this.offsetX = (int)paramAnonymousMotionEvent.getX();
                    StoryViewActivity.this.offsetY = (int)paramAnonymousMotionEvent.getY();
                }

                for (  ;  ;  ) {
                    if (paramAnonymousMotionEvent.getAction() == MotionEvent.ACTION_UP) {
                        int lastOffsetX = (int)paramAnonymousMotionEvent.getX();
                        int lastOffsetY = (int)paramAnonymousMotionEvent.getY();
                        int gapX = Math.abs(StoryViewActivity.this.offsetX - lastOffsetX);
                        int gapY = Math.abs(StoryViewActivity.this.offsetY - lastOffsetY);

                        if ((gapX < 10) && (gapY < 10))  {
                            StoryViewActivity.this.zoomClick();
                        }
                    }
                    return false;
                }
            }
        };
        localViewPager4.setOnTouchListener(localViewPagerModule4);

        this.topBottomLayouts.add((RelativeLayout)findViewById(R.id.story_top_layout_left));
        this.topBottomLayouts.add((RelativeLayout)findViewById(R.id.story_top_layout_right));
        this.topBottomLayouts.add((RelativeLayout)findViewById(R.id.story_bottom_layout_left));
        this.topBottomLayouts.add((RelativeLayout)findViewById(R.id.story_bottom_layout_right));

        this.slideAnimations.add((Animation)AnimationUtils.loadAnimation(this, R.anim.slide_top_left));
        this.slideAnimations.add((Animation)AnimationUtils.loadAnimation(this, R.anim.slide_top_right));
        this.slideAnimations.add((Animation)AnimationUtils.loadAnimation(this, R.anim.slide_bottom_left));
        this.slideAnimations.add((Animation)AnimationUtils.loadAnimation(this, R.anim.slide_bottom_right));
        this.slideBackAnimations.add((Animation)AnimationUtils.loadAnimation(this, R.anim.slide_back_top_left));
        this.slideBackAnimations.add((Animation)AnimationUtils.loadAnimation(this, R.anim.slide_back_top_right));
        this.slideBackAnimations.add((Animation)AnimationUtils.loadAnimation(this, R.anim.slide_back_bottom_left));
        this.slideBackAnimations.add((Animation)AnimationUtils.loadAnimation(this, R.anim.slide_back_bottom_right));

        for ( int i = 0;  i < this.slideAnimations.size();  i++ ) {
            final int finalI = i;
            this.slideAnimations.get(i).setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationEnd(Animation paramAnimation) {
                    ((RelativeLayout)StoryViewActivity.this.topBottomLayouts.get(finalI)).setVisibility(View.INVISIBLE);
                }
                public void onAnimationRepeat(Animation paramAnimation) { }
                public void onAnimationStart(Animation paramAnimation) { }
            });
        }

        for ( int i = 0;  i < this.slideBackAnimations.size();  i++ ) {
            final int finalI = i;
            this.slideBackAnimations.get(i).setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationEnd(Animation paramAnimation) {
                    ((RelativeLayout)StoryViewActivity.this.topBottomLayouts.get(finalI)).setVisibility(View.VISIBLE);
                }
                public void onAnimationRepeat(Animation paramAnimation) { }
                public void onAnimationStart(Animation paramAnimation) { }
            });
        }

        ViewPropertyAnimator localViewPropertyAnimator1 = this.viewPager.animate().alpha(0.0F).setDuration(0L);

        AnimatorListenerAdapter localViewPropertyAnimatorModule1 = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator paramAnonymousAnimator) {
                StoryViewActivity.this.viewPager.animate().alpha(1.0F).setDuration(200L).setListener(null);
                ((RelativeLayout)StoryViewActivity.this.topBottomLayouts.get(2)).startAnimation(StoryViewActivity.this.slideBackAnimations.get(2));
                ((RelativeLayout)StoryViewActivity.this.topBottomLayouts.get(3)).startAnimation(StoryViewActivity.this.slideBackAnimations.get(3));
            }
        };
        localViewPropertyAnimator1.setListener(localViewPropertyAnimatorModule1);
    }


    private class ImagePagerAdapter extends PagerAdapter {
        private int currentID;
        private LayoutInflater inflater;


        ImagePagerAdapter(int paramInt) {
            this.currentID = paramInt;
            this.inflater = StoryViewActivity.this.getLayoutInflater();
        }


        public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject) {
            int destroyIndex = StoryViewActivity.this.savedPosition.indexOf(Integer.valueOf(paramInt));
            StoryViewActivity.this.savedPosition.remove(destroyIndex);
            StoryViewActivity.this.savedViews.remove(destroyIndex);
            paramViewGroup.removeView((View)paramObject);
        }


        public int getCount() { return ((DataGetterSetters)DataHandler.getInstance().getData().get(this.currentID)).getPhoto().size(); }


        public Object instantiateItem(ViewGroup paramViewGroup, int paramInt) {
            View localView = this.inflater.inflate(R.layout.item_image_view, paramViewGroup, false);
            assert ( localView != null );
            ImageView localImageView = (ImageView)localView.findViewById(R.id.photoview);
            final ProgressBar localProgressBar = (ProgressBar)localView.findViewById(R.id.progressview);
            localProgressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(StoryViewActivity.this, R.color.osna_yellow), PorterDuff.Mode.SRC_IN);
            localProgressBar.startAnimation(StoryViewActivity.this.loadingAnimation);
            StoryViewActivity.this.savedViews.add(localView);
            StoryViewActivity.this.savedPosition.add(Integer.valueOf(paramInt));

            if ( !StoryViewActivity.this.isZoom ) {
                localView.animate().scaleX((float)StoryViewActivity.this.myScale).scaleY((float)StoryViewActivity.this.myScale).setDuration(0L).setListener(null);
            }
            else {
                localView.animate().scaleX((float)1.0D).scaleY((float)1.0D).setDuration(0L).setListener(null);
            }


            StoryViewActivity.this.imageLoader.displayImage((String)((DataGetterSetters)DataHandler.getInstance().getData().get(this.currentID)).getPhoto().get(paramInt), localImageView, StoryViewActivity.this.displayImageOptions, new SimpleImageLoadingListener() {
                public void onLoadingComplete(String paramAnonymousString, View paramAnonymousView, Bitmap paramAnonymousBitmap) {
                    localProgressBar.clearAnimation();
                    localProgressBar.setVisibility(View.GONE);
                }

                public void onLoadingFailed(String paramAnonymousString, View paramAnonymousView, FailReason paramAnonymousFailReason) {
                    FailReason.FailType localFailType = paramAnonymousFailReason.getType();
                    Object localObject = null;

                    switch(localFailType) {
                        case IO_ERROR:
                            localObject = "I/O 에러가 발생했습니다.";
                            break;

                        case DECODING_ERROR:
                            localObject = "이미지 디코딩에 실패했습니다.";
                            break;

                        case NETWORK_DENIED:
                            localObject = "네트워크 연결이 불가능합니다.";
                            break;

                        case OUT_OF_MEMORY:
                            localObject = "사용 가능한 메모리가 부족합니다.";
                            break;

                        case UNKNOWN:
                            localObject = "알 수 없는 에러가 발생했습니다.";
                            break;

                        default:
                            break;
                    }

                    Toast.makeText(StoryViewActivity.this, (CharSequence)localObject, Toast.LENGTH_LONG).show();
                    localProgressBar.clearAnimation();
                    localProgressBar.setVisibility(View.GONE);
                    return;
                }

                public void onLoadingStarted(String paramAnonymousString, View paramAnonymousView) {
                    localProgressBar.setVisibility(View.VISIBLE);
                }
            });
            paramViewGroup.addView(localView, 0);
            return localView;
        }

        public boolean isViewFromObject(View paramView, Object paramObject) {
            return paramView == paramObject;
        }

        public void restoreState(Parcelable paramParcelable, ClassLoader paramClassLoader) {}

        public Parcelable saveState() {
            return null;
        }
    }
}
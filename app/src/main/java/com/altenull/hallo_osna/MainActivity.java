package com.altenull.hallo_osna;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.Process;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.viewanimator.ViewAnimator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import java.util.ArrayList;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import io.github.francoiscampbell.circlelayout.CircleLayout;
import it.sephiroth.android.library.viewrevealanimator.ViewRevealAnimator;


public class MainActivity extends BaseActivity {
    protected DisplayImageOptions displayImageOptions;
    private ColorMatrixColorFilter grayScaleFilter;
    private long backKeyPressedTime = 0L;
    private int currentAPIVersion;
    private Toast toast;

    private ImageView logoWhite;
    private ImageView helpWhite;
    private ImageButton logoBlack;
    private ImageButton helpBlack;

    private RelativeLayout verticalScrollbar;
    private int verticalScrollbarHeight;
    private final int VERTICAL_SCROLLBAR_TRACK_WIDTH = 10;
    private final int VERTICAL_SCROLLBAR_WIDTH = 8;

    private int ImageHeight;
    private double dpWidth;
    private double dpHeight;
    protected int rowHH;

    private int xNum = 1;
    private int yNum = 2;

    private RelativeLayout slideLayout;

    private ImageButton storyButton;
    private ImageButton categoryButton;

    private int isCurrentMode;
    private final int STORY_MODE = 0;
    private final int CATEGORY_MODE = 1;

    private int currentID;
    private int pageTotal;
    private int aPageTotal;
    private int studentTotal;

    private ArrayList<Boolean> isSelectedStudent = new ArrayList();
    private VerticalViewPager verticalViewPager;
    private GridView gridview;

    private String currentCategory;
    private ViewRevealAnimator viewRevealAnimator;
    private io.github.francoiscampbell.circlelayout.CircleLayout categoryLayout;
    private ArrayList<ImageButton> categoryList = new ArrayList();
    private ArrayList<String> categoryTags = new ArrayList();


    private void resetComponents() {
        this.viewRevealAnimator.setAnimationDuration(0);
        this.viewRevealAnimator.setDisplayedChild(0);

        this.logoWhite.setVisibility(View.INVISIBLE);
        this.helpWhite.setVisibility(View.INVISIBLE);
        this.logoBlack.animate().alpha(1.0F).setDuration(0L).setListener(null);
        this.helpBlack.animate().alpha(1.0F).setDuration(0L).setListener(null);

        if ( this.isCurrentMode == STORY_MODE ) {
            this.categoryButton.setVisibility(View.VISIBLE);
        }
        else if ( this.isCurrentMode == CATEGORY_MODE ) {
            this.storyButton.setVisibility(View.VISIBLE);
        }

        this.verticalViewPager.animate().alpha(1.0F).setDuration(0L).setListener(null);
        this.categoryLayout.animate().alpha(1.0F).setDuration(0L).setListener(null);

        this.slideLayout.setVisibility(View.INVISIBLE);
        this.slideLayout.animate().alpha(0.0F).setDuration(0L).setListener(null);
        this.slideLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.osna_yellow));

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    private void startStoryViewActivity(int paramID) {
        this.currentID = paramID;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        this.logoWhite.setVisibility(View.VISIBLE);
        this.helpWhite.setVisibility(View.VISIBLE);
        this.logoBlack.animate().alpha(0.0F).setDuration(200L).setListener(null);
        this.helpBlack.animate().alpha(0.0F).setDuration(200L).setListener(null);

        this.categoryButton.setVisibility(View.INVISIBLE);
        this.verticalViewPager.animate().alpha(0.0F).setDuration(200L).setListener(null);

        this.slideLayout.setVisibility(View.VISIBLE);
        this.slideLayout.animate().alpha(1.0F).setDuration(500L).setInterpolator(new DecelerateInterpolator(2.0F)).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator paramAnonymousAnimator) {
                Intent storyIntent = new Intent(MainActivity.this, StoryViewActivity.class);
                storyIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                storyIntent.putExtra("CURRENT_ID", MainActivity.this.currentID);
                MainActivity.this.startActivityForResult(storyIntent, 0);
            }
        });
    }


    private void startCategoryViewActivity(String paramCategory) {
        this.currentCategory = paramCategory;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        this.logoBlack.animate().alpha(0.0F).setDuration(0L).setListener(null);
        this.helpBlack.animate().alpha(0.0F).setDuration(0L).setListener(null);

        this.storyButton.setVisibility(View.INVISIBLE);
        this.categoryLayout.animate().alpha(0.0F).setDuration(0L).setListener(null);

        final Intent categoryIntent = new Intent(MainActivity.this, CategoryViewActivity.class);
        categoryIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        categoryIntent.putExtra("CURRENT_CATEGORY", this.currentCategory);

        if ( this.currentAPIVersion >= android.os.Build.VERSION_CODES.LOLLIPOP ) {
            MainActivity.this.startActivityForResult(categoryIntent, 0);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        else {
            switch(this.currentCategory) {
                case "#FOOD":
                    this.slideLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.category_food_light));
                    break;

                case "#DAILY":
                    this.slideLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.category_daily_light));
                    break;

                case "#DOMITORY":
                    this.slideLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.category_domitory_light));
                    break;

                case "#UNIVERSITY":
                    this.slideLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.category_university_light));
                    break;

                case "#TRAVEL":
                    this.slideLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.category_travel_light));
                    break;

                case "#LEISURE":
                    this.slideLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.category_leisure_light));
                    break;

                default:
                    break;
            }

            this.slideLayout.setVisibility(View.VISIBLE);
            this.slideLayout.animate().alpha(1.0F).setDuration(500L).setInterpolator(new DecelerateInterpolator(2.0F)).setListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator paramAnonymousAnimator) {
                    MainActivity.this.startActivityForResult(categoryIntent, 0);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            });
        }
    }


    public void onActivityResult(int paramRequestCode, int paramResultCode, Intent paramIntent) {
        super.onActivityResult(paramRequestCode, paramResultCode, paramIntent);

        for (  ;  ;  ) {
            if ( paramResultCode == this.RESULT_OK ) {
                resetComponents();
                return;
            }
        }
    }


    public void onBackPressed() {
        if ( System.currentTimeMillis() > (this.backKeyPressedTime + 2000L) ) {
            this.backKeyPressedTime = System.currentTimeMillis();
            this.toast = Toast.makeText(MainActivity.this, "\'이전\' 버튼을 한 번 더 누르면 앱이 종료 됩니다.", Toast.LENGTH_SHORT);
            this.toast.show();
            return;
        }
        else if ( System.currentTimeMillis() <= (this.backKeyPressedTime + 2000L) ) {
            this.toast.cancel();
            finish();
            Process.killProcess(Process.myPid());
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.currentAPIVersion = android.os.Build.VERSION.SDK_INT;

        this.loadingAnimation = AnimationUtils.loadAnimation(this, R.anim.loading);

        ColorMatrix localColorMatrix = new ColorMatrix();
        localColorMatrix.setSaturation(0.0F);
        this.grayScaleFilter = new ColorMatrixColorFilter(localColorMatrix);

        this.displayImageOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.empty_frame).showImageForEmptyUri(R.drawable.empty_frame).showImageOnFail(R.drawable.empty_frame).cacheInMemory(false).cacheOnDisk(true).considerExifParams(false).bitmapConfig(Bitmap.Config.RGB_565).build();

        this.logoWhite = (ImageView)findViewById(R.id.logo_white);
        this.helpWhite = (ImageView)findViewById(R.id.help_white);
        this.logoBlack = (ImageButton)findViewById(R.id.logo_black);
        this.helpBlack = (ImageButton)findViewById(R.id.help_black);

        this.logoWhite.setVisibility(View.INVISIBLE);
        this.helpWhite.setVisibility(View.INVISIBLE);

        this.logoBlack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                ViewAnimator.animate(paramAnonymousView).tada().duration(1000L).start();
            }
        });

        this.helpBlack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                MainActivity.this.showHelp();
            }
        });

        this.studentTotal = DataHandler.getInstance().getData().size();

        for ( int i = 0;  i < this.studentTotal;  i++ ) {
            this.isSelectedStudent.add(Boolean.FALSE);
        }

        this.dpWidth = this.stageWidth / this.density;
        this.dpHeight = this.stageHeight / this.density;

        this.aPageTotal = this.xNum * this.yNum;
        this.pageTotal = ((int)Math.ceil((float)this.studentTotal / (float)this.aPageTotal));

        this.ImageHeight = Math.round(((float)this.dpHeight - 50.0F) / this.yNum * this.density);

        int k = (int)(70.0D * this.density);
        this.rowHH = ((int)Math.floor((this.stageHeight - k) / this.yNum));

        DataHandler.getInstance().setDimension(this.dpHeight);

        if ( this.dpWidth <= 400.0D ) {
            DataHandler.getInstance().setScreenSize(this.SCREEN_SIZE_SMALL);
        }
        else if ( this.dpHeight > 900.0D ) {
            DataHandler.getInstance().setScreenSize(this.SCREEN_SIZE_LARGE);
        }

        categoryLayout = (CircleLayout)findViewById(R.id.category_layout);
        categoryLayout.setVisibility(View.INVISIBLE);

        this.storyButton = (ImageButton)findViewById(R.id.ic_story);
        this.categoryButton = (ImageButton)findViewById(R.id.ic_category);

        this.storyButton.setVisibility(View.INVISIBLE);
        this.categoryButton.setVisibility(View.VISIBLE);

        this.isCurrentMode = STORY_MODE;

        this.storyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                MainActivity.this.storyButton.setVisibility(View.INVISIBLE);
                MainActivity.this.categoryButton.setVisibility(View.VISIBLE);

                MainActivity.this.verticalViewPager.setVisibility(View.VISIBLE);
                MainActivity.this.categoryLayout.setVisibility(View.INVISIBLE);

                ViewAnimator.animate(MainActivity.this.verticalViewPager).fadeIn().duration(750L).start();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                }, 750L);
                MainActivity.this.isCurrentMode = STORY_MODE;
            }
        });

        this.categoryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                MainActivity.this.storyButton.setVisibility(View.VISIBLE);
                MainActivity.this.categoryButton.setVisibility(View.INVISIBLE);

                MainActivity.this.verticalViewPager.setVisibility(View.INVISIBLE);
                MainActivity.this.categoryLayout.setVisibility(View.VISIBLE);

                ViewAnimator.animate(MainActivity.this.categoryLayout).rollIn().duration(750L).interpolator(new DecelerateInterpolator(2.0F)).start();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                }, 750L);
                MainActivity.this.isCurrentMode = CATEGORY_MODE;
            }
        });

        this.viewRevealAnimator = (ViewRevealAnimator)findViewById(R.id.reveal_animator);

        this.categoryList.add((ImageButton)findViewById(R.id.category_food));
        this.categoryList.add((ImageButton)findViewById(R.id.category_daily));
        this.categoryList.add((ImageButton)findViewById(R.id.category_domitory));
        this.categoryList.add((ImageButton)findViewById(R.id.category_university));
        this.categoryList.add((ImageButton)findViewById(R.id.category_travel));
        this.categoryList.add((ImageButton)findViewById(R.id.category_leisure));

        this.categoryTags.add("#FOOD");
        this.categoryTags.add("#DAILY");
        this.categoryTags.add("#DOMITORY");
        this.categoryTags.add("#UNIVERSITY");
        this.categoryTags.add("#TRAVEL");
        this.categoryTags.add("#LEISURE");

        if ( DataHandler.getInstance().getScreenSize() == this.SCREEN_SIZE_SMALL ) {
            for ( int i = 0;  i < this.categoryList.size();  i++ ) {
                this.categoryList.get(i).animate().scaleX(0.65F).scaleY(0.65F).setDuration(0L).setListener(null);
            }
        }

        final Point centerPoint = new Point((int)Math.round(this.stageWidth / 2.0D), (int)Math.round(this.stageHeight / 2.0D));

        for ( int i = 0;  i < this.categoryList.size();  i++ ) {
            final int tempInt = i;
            this.categoryList.get(tempInt).setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    if ( MainActivity.this.currentAPIVersion >= android.os.Build.VERSION_CODES.LOLLIPOP ) {
                        MainActivity.this.viewRevealAnimator.setAnimationDuration(1000);
                        MainActivity.this.viewRevealAnimator.setDisplayedChild(tempInt + 1, true, centerPoint);

                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                MainActivity.this.startCategoryViewActivity(MainActivity.this.categoryTags.get(tempInt));
                            }
                        }, 1000L);
                    }
                    else {
                        MainActivity.this.startCategoryViewActivity(MainActivity.this.categoryTags.get(tempInt));
                    }
                }
            });
        }

        this.verticalViewPager = (VerticalViewPager)findViewById(R.id.vertical_viewpager);
        this.verticalViewPager.setAdapter(new ImagePagerAdapter());
        this.verticalViewPager.setCurrentItem(0);

        this.slideLayout = (RelativeLayout)findViewById(R.id.slide_layout);
        this.slideLayout.setVisibility(View.INVISIBLE);
        this.slideLayout.animate().alpha(0.0F).setDuration(0L).setListener(null);

        this.verticalScrollbarHeight = (int)Math.ceil((this.stageHeight / (double)this.pageTotal));
        this.verticalScrollbar = (RelativeLayout)findViewById(R.id.vertical_scrollbar);
        this.verticalScrollbar.setLayoutParams(new RelativeLayout.LayoutParams(this.VERTICAL_SCROLLBAR_WIDTH, this.verticalScrollbarHeight));
        this.verticalScrollbar.setX(this.stageWidth - this.VERTICAL_SCROLLBAR_TRACK_WIDTH);
        this.verticalScrollbar.setVisibility(View.INVISIBLE);

        VerticalViewPager localVerticalViewPager1 = this.verticalViewPager;
        ViewPager.OnPageChangeListener localOnPageChangeListener1 = new ViewPager.OnPageChangeListener() {
            final float thresholdOffset = 0.5F;
            final int thresholdOffsetPixels = 1;
            boolean scrollStarted;
            boolean checkDirection;

            public void onPageScrollStateChanged(int paramAnonymousInt) {
                if ( (!scrollStarted) && (paramAnonymousInt == ViewPager.SCROLL_STATE_DRAGGING) ) {
                    MainActivity.this.verticalScrollbar.animate().alpha(1.0F).setDuration(0L).setListener(null);
                    scrollStarted = true;
                    checkDirection = true;
                }
                else if  ( paramAnonymousInt == ViewPager.SCROLL_STATE_SETTLING ) {
                    MainActivity.this.verticalScrollbar.animate().alpha(1.0F).setDuration(0L).setListener(null);
                    scrollStarted = false;
                }
                else if ( paramAnonymousInt == ViewPager.SCROLL_STATE_IDLE ) {
                    MainActivity.this.verticalScrollbar.animate().alpha(0.0F).setDuration(0L).setListener(null);
                    scrollStarted = false;
                }
            }

            public void onPageScrolled(int paramPosition, float paramPositionOffset, int paramPositionOffsetPixels) {
                MainActivity.this.verticalScrollbar.animate().alpha(1.0F).y((paramPositionOffset * MainActivity.this.verticalScrollbarHeight) + (paramPosition * MainActivity.this.verticalScrollbarHeight)).setDuration(0L).setListener(null);

                if ( checkDirection ) {
                    if ( (thresholdOffset > paramPositionOffset) && (paramPositionOffsetPixels > thresholdOffsetPixels) ) {
                        MainActivity.this.verticalScrollbar.setVisibility(View.VISIBLE);
                    }
                    else {
                        MainActivity.this.verticalScrollbar.setVisibility(View.VISIBLE);
                    }
                    checkDirection = false;
                }
            }

            public void onPageSelected(int paramAnonymousInt) { }
        };
        localVerticalViewPager1.setOnPageChangeListener(localOnPageChangeListener1);

        return;
    }


    public class ImageAdapter extends BaseAdapter {
        private int addPage;
        private int totalItems;

        ImageAdapter(int paramInt) {
            this.addPage = (paramInt * MainActivity.this.aPageTotal);
            this.totalItems = MainActivity.this.aPageTotal;

            if ( (this.addPage + MainActivity.this.aPageTotal) > MainActivity.this.studentTotal ) {
                this.totalItems = (MainActivity.this.studentTotal - this.addPage);
            }
        }

        public int getCount() {
            return this.totalItems;
        }

        public Object getItem(int paramInt) {
            return DataHandler.getInstance().getData().get(paramInt);
        }

        public long getItemId(int paramInt) {
            return paramInt;
        }

        public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
            View localView = paramView;
            final MainActivity.ViewHolder localViewHolder;

            if (localView == null) {
                localViewHolder = new MainActivity.ViewHolder();
                localView = MainActivity.this.getLayoutInflater().inflate(R.layout.item_image_grid, paramViewGroup, false);

                assert (localView != null);
                localViewHolder.photoView = (KenBurnsView)localView.findViewById(R.id.photo);
                localViewHolder.progressBar = (ProgressBar)localView.findViewById(R.id.progress);
                localViewHolder.frameNameText = (TextView)localView.findViewById(R.id.student_name);
                localViewHolder.frameLeft = (RelativeLayout)localView.findViewById(R.id.frame_left);
                localViewHolder.frameRight = (RelativeLayout)localView.findViewById(R.id.frame_right);
                localViewHolder.frameTop = (RelativeLayout)localView.findViewById(R.id.frame_top);
                localViewHolder.frameBottom = (RelativeLayout)localView.findViewById(R.id.frame_bottom);

                localView.setTag(localViewHolder);
            }
            else {
                localViewHolder = (ViewHolder)localView.getTag();
            }

            localViewHolder.photoView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MainActivity.this.ImageHeight));
            localViewHolder.photoView.setY((int)(MainActivity.this.rowHH - MainActivity.this.ImageHeight));

            final int frameSideWidth = 44;
            final int frameTopBottomHeight = 50;

            final double widthRatio = MainActivity.this.dpWidth / 504.0D;
            final double heightRatio = ((MainActivity.this.dpHeight - 50.0D) / 2) / 413.0D;

            final int frameX = (int)(frameSideWidth * MainActivity.this.density * widthRatio);
            final int frameY = (int)(frameTopBottomHeight * MainActivity.this.density * heightRatio);

            localViewHolder.frameLeft.setLayoutParams(new RelativeLayout.LayoutParams(frameX, MainActivity.this.ImageHeight));

            localViewHolder.frameRight.setLayoutParams(new RelativeLayout.LayoutParams(frameX, MainActivity.this.ImageHeight));
            localViewHolder.frameRight.setX(MainActivity.this.stageWidth - MainActivity.this.VERTICAL_SCROLLBAR_TRACK_WIDTH - frameX);

            localViewHolder.frameTop.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, frameY));

            localViewHolder.frameBottom.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, frameY));
            localViewHolder.frameBottom.setY(MainActivity.this.ImageHeight - frameY);

            localViewHolder.progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.osna_yellow), PorterDuff.Mode.SRC_IN);
            localViewHolder.progressBar.startAnimation(MainActivity.this.loadingAnimation);

            localViewHolder.frameNameText.setX(frameX);
            localViewHolder.frameNameText.setY(MainActivity.this.ImageHeight - (int) (80 * MainActivity.this.density * heightRatio));

            localViewHolder.frameNameText.setText(((DataGetterSetters)DataHandler.getInstance().getData().get(paramInt + this.addPage)).getEnglishName());

            if ( DataHandler.getInstance().getScreenSize() == MainActivity.this.SCREEN_SIZE_LARGE ) {
                localViewHolder.frameNameText.setTextSize(18.0F);
            }

            final int selectedStudentID = paramInt + this.addPage;

            MainActivity.this.imageLoader.displayImage(((DataGetterSetters)DataHandler.getInstance().getData().get(paramInt + this.addPage)).getRepresentativePhoto(), localViewHolder.photoView, MainActivity.this.displayImageOptions, new SimpleImageLoadingListener() {
                public void onLoadingComplete(String paramAnonymousString, View paramAnonymousView, Bitmap paramAnonymousBitmap) {
                    localViewHolder.progressBar.clearAnimation();
                    localViewHolder.progressBar.setVisibility(View.GONE);
                    localViewHolder.photoView.resume();
                }

                public void onLoadingFailed(String paramAnonymousString, View paramAnonymousView, FailReason paramAnonymousFailReason) {
                    localViewHolder.progressBar.clearAnimation();
                    localViewHolder.progressBar.setVisibility(View.GONE);
                    localViewHolder.photoView.pause();
                }

                public void onLoadingStarted(String paramAnonymousString, View paramAnonymousView) {
                    localViewHolder.progressBar.setProgress(0);
                    localViewHolder.progressBar.setVisibility(View.GONE);

                    if ( isSelectedStudent.get(selectedStudentID) != Boolean.TRUE ) {
                        localViewHolder.photoView.setColorFilter(MainActivity.this.grayScaleFilter);
                    }
                }
            }, new ImageLoadingProgressListener() {
                public void onProgressUpdate(String paramAnonymousString, View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2) {
                    localViewHolder.progressBar.setProgress(Math.round(100.0F * paramAnonymousInt1 / paramAnonymousInt2));
                }
            });

            return localView;
        }
    }


    private class ImagePagerAdapter extends PagerAdapter {
        private LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        private int myPosition;

        ImagePagerAdapter() {}

        public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject) {
            paramViewGroup.removeView((View)paramObject);
        }

        public int getCount() {
            return MainActivity.this.pageTotal;
        }

        public Object instantiateItem(ViewGroup paramViewGroup, int paramInt) {
            View localView = this.inflater.inflate(R.layout.item_image_pager, paramViewGroup, false);

            assert(localView != null);

            paramViewGroup.addView(localView, 0);

            MainActivity.this.gridview = (GridView)MainActivity.this.findViewById(R.id.gridview);

            MainActivity.this.gridview.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent) {
                    if ( paramAnonymousMotionEvent.getAction() == MotionEvent.ACTION_MOVE ) {
                        for ( boolean bool = true;  ; bool = false ) {
                            return bool;
                        }
                    }
                    return false;
                }
            });

            MainActivity.this.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                    KenBurnsView selectedView = (KenBurnsView)paramAnonymousView.findViewById(R.id.photo);
                    selectedView.clearColorFilter();

                    int selectedStudentID = paramAnonymousInt + (MainActivity.this.verticalViewPager.getCurrentItem() * MainActivity.this.aPageTotal);
                    isSelectedStudent.set(selectedStudentID, Boolean.TRUE);

                    for (  ;  ;  ) {
                        MainActivity.this.startStoryViewActivity(selectedStudentID);
                        return;
                    }
                }
            });

            this.myPosition = paramInt;
            MainActivity.this.gridview.setAdapter(new MainActivity.ImageAdapter(this.myPosition));

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

    static class ViewHolder {
        KenBurnsView photoView = null;
        ProgressBar progressBar = null;
        TextView frameNameText = null;
        RelativeLayout frameLeft = null;
        RelativeLayout frameRight = null;
        RelativeLayout frameTop = null;
        RelativeLayout frameBottom = null;
    }
}
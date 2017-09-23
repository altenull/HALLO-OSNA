package com.altenull.hallo_osna;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class CategoryViewActivity extends BaseActivity {
    protected DisplayImageOptions displayImageOptions;
    protected ArrayList<Integer> savedPosition = new ArrayList();
    protected ArrayList<View> savedViews = new ArrayList();

    private ViewPager viewPager;
    private String CURRENT_CATEGORY;
    private int photoTotal;

    private int CURRENT_COLOR;
    private int CURRENT_COLOR_LIGHT;

    private ImageButton categorySymbol;

    private TextView photoTitle;
    private TextView photoComment;
    private RelativeLayout photoTitleLayout;
    private RelativeLayout photoCommentLayout;

    private RelativeLayout scrollbar;
    private final int SCROLLBAR_TRACK_HEIGHT = 10;
    private final int SCROLLBAR_HEIGHT = 8;
    private int scrollbarWidth;


    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.view_exit);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);

        this.displayImageOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_empty).showImageOnFail(R.drawable.empty_photo).resetViewBeforeLoading(false).cacheInMemory(false).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).build();
        this.loadingAnimation = AnimationUtils.loadAnimation(this, R.anim.loading);

        Intent localIntent = getIntent();
        Bundle localBundle = localIntent.getExtras();
        assert ( localBundle != null );
        setResult(this.RESULT_OK, localIntent);

        this.CURRENT_CATEGORY = localBundle.getString("CURRENT_CATEGORY");
        this.photoTotal = CategoryDataGetterSetters.getSize(this.CURRENT_CATEGORY);

        this.categorySymbol = (ImageButton)findViewById(R.id.category_symbol);

        this.categorySymbol.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                ViewAnimator.animate(CategoryViewActivity.this.categorySymbol).rubber().duration(1000L).start();
            }
        });

        this.scrollbarWidth = (int)Math.ceil(this.stageWidth / (double)this.photoTotal);
        this.scrollbar = (RelativeLayout)findViewById(R.id.category_scrollbar);
        this.scrollbar.setLayoutParams(new RelativeLayout.LayoutParams(this.scrollbarWidth, this.SCROLLBAR_HEIGHT));
        this.scrollbar.setY(this.stageHeight - this.SCROLLBAR_TRACK_HEIGHT);
        this.scrollbar.setVisibility(View.INVISIBLE);

        switch(CURRENT_CATEGORY) {
            case "#FOOD":
                this.CURRENT_COLOR = ContextCompat.getColor(this, R.color.category_food);
                this.CURRENT_COLOR_LIGHT = ContextCompat.getColor(this, R.color.category_food_light);
                this.categorySymbol.setImageResource(R.drawable.category_food_small);
                this.scrollbar.setBackgroundResource(R.drawable.scrollbar_food);
                break;

            case "#DAILY":
                this.CURRENT_COLOR = ContextCompat.getColor(this, R.color.category_daily);
                this.CURRENT_COLOR_LIGHT = ContextCompat.getColor(this, R.color.category_daily_light);
                this.categorySymbol.setImageResource(R.drawable.category_daily_small);
                this.scrollbar.setBackgroundResource(R.drawable.scrollbar_daily);
                break;

            case "#DOMITORY":
                this.CURRENT_COLOR = ContextCompat.getColor(this, R.color.category_domitory);
                this.CURRENT_COLOR_LIGHT = ContextCompat.getColor(this, R.color.category_domitory_light);
                this.categorySymbol.setImageResource(R.drawable.category_domitory_small);
                this.scrollbar.setBackgroundResource(R.drawable.scrollbar_domitory);
                break;

            case "#UNIVERSITY":
                this.CURRENT_COLOR = ContextCompat.getColor(this, R.color.category_university);
                this.CURRENT_COLOR_LIGHT = ContextCompat.getColor(this, R.color.category_university_light);
                this.categorySymbol.setImageResource(R.drawable.category_university_small);
                this.scrollbar.setBackgroundResource(R.drawable.scrollbar_university);
                break;

            case "#TRAVEL":
                this.CURRENT_COLOR = ContextCompat.getColor(this, R.color.category_travel);
                this.CURRENT_COLOR_LIGHT = ContextCompat.getColor(this, R.color.category_travel_light);
                this.categorySymbol.setImageResource(R.drawable.category_travel_small);
                this.scrollbar.setBackgroundResource(R.drawable.scrollbar_travel);
                break;

            case "#LEISURE":
                this.CURRENT_COLOR = ContextCompat.getColor(this, R.color.category_leisure);
                this.CURRENT_COLOR_LIGHT = ContextCompat.getColor(this, R.color.category_leisure_light);
                this.categorySymbol.setImageResource(R.drawable.category_leisure_small);
                this.scrollbar.setBackgroundResource(R.drawable.scrollbar_leisure);
                break;

            default:
                break;
        }

        getWindow().getDecorView().setBackgroundColor(this.CURRENT_COLOR_LIGHT);

        this.photoTitle = (TextView)findViewById(R.id.category_photo_title);
        this.photoComment = (TextView)findViewById(R.id.category_photo_comment);

        this.photoTitle.setText(CategoryDataGetterSetters.getPhotoTitle(this.CURRENT_CATEGORY).get(0));
        this.photoComment.setText(CategoryDataGetterSetters.getPhotoComment(this.CURRENT_CATEGORY).get(0).replace(" ", "\u00A0"));

        this.photoTitle.setShadowLayer(0.6F, 1.0F, 1.0F, this.CURRENT_COLOR);
        this.photoComment.setShadowLayer(0.6F, 1.0F, 1.0F, this.CURRENT_COLOR);

        if (DataHandler.getInstance().getScreenSize() == this.SCREEN_SIZE_LARGE) {
            this.photoTitle.setTextSize(18.0F);
            this.photoComment.setTextSize(16.0F);
        }

        this.photoTitleLayout = (RelativeLayout)findViewById(R.id.category_title_layout);
        this.photoCommentLayout = (RelativeLayout)findViewById(R.id.category_comment_layout);

        this.photoTitleLayout.animate().y(-this.topMovement).setDuration(0L).setListener(null);
        this.photoCommentLayout.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                CategoryViewActivity.this.photoTitleLayout.animate().y(0).setInterpolator(new DecelerateInterpolator(2.0F)).setDuration(500L).setListener(null);
                CategoryViewActivity.this.photoCommentLayout.setVisibility(View.VISIBLE);
                ViewAnimator.animate(CategoryViewActivity.this.photoCommentLayout).newsPaper().duration(500L).start();
            }
        }, 200L);

        this.viewPager = (ViewPager)findViewById(R.id.category_viewpager);

        ViewPager localViewPager1 = this.viewPager;
        ImagePagerAdapter localViewPagerModule1 = new ImagePagerAdapter(this.CURRENT_CATEGORY);
        localViewPager1.setAdapter(localViewPagerModule1);
        this.viewPager.setCurrentItem(0, true);

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
                CategoryViewActivity.this.photoTitle.setText(CategoryDataGetterSetters.getPhotoTitle(CategoryViewActivity.this.CURRENT_CATEGORY).get(paramAnonymousInt));
                CategoryViewActivity.this.photoComment.setText(CategoryDataGetterSetters.getPhotoComment(CategoryViewActivity.this.CURRENT_CATEGORY).get(paramAnonymousInt).replace(" ", "\u00A0"));
            }
        };
        localViewPager3.addOnPageChangeListener(localViewPagerModule3);

        ViewPager localViewPager4 = this.viewPager;
        ViewPager.OnPageChangeListener localViewPagerModule4 = new ViewPager.OnPageChangeListener() {
            final float thresholdOffset = 0.5F;
            final int thresholdOffsetPixels = 1;
            boolean scrollStarted;
            boolean checkDirection;

            public void onPageScrollStateChanged(int paramAnonymousInt) {
                if ( (!scrollStarted) && (paramAnonymousInt == ViewPager.SCROLL_STATE_DRAGGING) ) {
                    CategoryViewActivity.this.scrollbar.animate().alpha(1.0F).setDuration(0L).setListener(null);
                    scrollStarted = true;
                    checkDirection = true;
                }
                else if  ( paramAnonymousInt == ViewPager.SCROLL_STATE_SETTLING ) {
                    CategoryViewActivity.this.scrollbar.animate().alpha(1.0F).setDuration(0L).setListener(null);
                    scrollStarted = false;
                }
                else if ( paramAnonymousInt == ViewPager.SCROLL_STATE_IDLE ) {
                    CategoryViewActivity.this.scrollbar.animate().alpha(0.0F).setDuration(0L).setListener(null);
                    scrollStarted = false;
                }
            }

            public void onPageScrolled(int paramPosition, float paramPositionOffset, int paramPositionOffsetPixels) {
                CategoryViewActivity.this.scrollbar.animate().alpha(1.0F).x((paramPositionOffset * CategoryViewActivity.this.scrollbarWidth) + (paramPosition * CategoryViewActivity.this.scrollbarWidth)).setDuration(0L).setListener(null);

                if ( checkDirection ) {
                    if ( (thresholdOffset > paramPositionOffset) && (paramPositionOffsetPixels > thresholdOffsetPixels) ) {
                        CategoryViewActivity.this.scrollbar.setVisibility(View.VISIBLE);
                    }
                    else {
                        CategoryViewActivity.this.scrollbar.setVisibility(View.VISIBLE);
                    }
                    checkDirection = false;
                }
            }
            public void onPageSelected(int paramAnonymousInt) { }
        };
        localViewPager4.addOnPageChangeListener(localViewPagerModule4);
    }


    private class ImagePagerAdapter extends PagerAdapter {
        private String currentTag;
        private LayoutInflater inflater;


        ImagePagerAdapter(String paramString) {
            this.currentTag = paramString;
            this.inflater = CategoryViewActivity.this.getLayoutInflater();
        }


        public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject) {
            int destroyIndex = CategoryViewActivity.this.savedPosition.indexOf(Integer.valueOf(paramInt));
            CategoryViewActivity.this.savedPosition.remove(destroyIndex);
            CategoryViewActivity.this.savedViews.remove(destroyIndex);
            paramViewGroup.removeView((View)paramObject);
        }


        public int getCount() { return CategoryDataGetterSetters.getSize(this.currentTag); }


        public Object instantiateItem(ViewGroup paramViewGroup, int paramInt) {
            View localView = this.inflater.inflate(R.layout.item_image_view, paramViewGroup, false);
            assert ( localView != null );
            ImageView localImageView = (ImageView)localView.findViewById(R.id.photoview);
            final ProgressBar localProgressBar = (ProgressBar)localView.findViewById(R.id.progressview);
            localProgressBar.getIndeterminateDrawable().setColorFilter(CategoryViewActivity.this.CURRENT_COLOR, PorterDuff.Mode.SRC_IN);
            localProgressBar.startAnimation(CategoryViewActivity.this.loadingAnimation);
            CategoryViewActivity.this.savedViews.add(localView);
            CategoryViewActivity.this.savedPosition.add(Integer.valueOf(paramInt));

            localView.animate().scaleX((float)1.0D).scaleY((float)1.0D).setDuration(0L).setListener(null);

            CategoryViewActivity.this.imageLoader.displayImage((String)CategoryDataGetterSetters.getPhoto(this.currentTag).get(paramInt), localImageView, CategoryViewActivity.this.displayImageOptions, new SimpleImageLoadingListener() {
                public void onLoadingComplete(String paramAnonymousString, View paramAnonymousView, Bitmap paramAnonymousBitmap) {
                    localProgressBar.clearAnimation();
                    localProgressBar.setVisibility(View.GONE);
                }

                public void onLoadingFailed(String paramAnonymousString, View paramAnonymousView, FailReason paramAnonymousFailReason) {
                    FailReason.FailType localFailType = paramAnonymousFailReason.getType();
                    Object localObject = null;

                    switch(localFailType) {
                        case IO_ERROR:
                            localObject = "네트워크 연결이 불가능합니다.";
                            break;

                        case DECODING_ERROR:
                            localObject = "이미지 디코딩에 실패했습니다.";
                            break;

                        case NETWORK_DENIED:
                            localObject = "네트워크 연결이 불가능합니다.";
                            break;

                        case OUT_OF_MEMORY:
                            localObject = "사용할 수 있는 메모리가 부족합니다.";
                            break;

                        case UNKNOWN:
                            localObject = "알 수 없는 에러가 발생했습니다.";
                            break;

                        default:
                            break;
                    }

                    Toast.makeText(CategoryViewActivity.this, (CharSequence)localObject, Toast.LENGTH_LONG).show();
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
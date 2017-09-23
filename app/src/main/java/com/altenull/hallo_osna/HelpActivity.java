package com.altenull.hallo_osna;


import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.github.florent37.viewanimator.ViewAnimator;


public class HelpActivity extends BaseActivity {
    private ImageButton helpLogo;
    private ImageButton helpSymbol;
    private TextView helpText1;
    private TextView helpText2;
    private TextView helpText3;
    private TextView helpText4;


    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.help_exit);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        overridePendingTransition(R.anim.help_enter, R.anim.hold);

        this.helpLogo = (ImageButton)findViewById(R.id.help_logo);
        this.helpSymbol = (ImageButton)findViewById(R.id.help_symbol);

        this.helpLogo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                ViewAnimator.animate(paramAnonymousView).tada().duration(1000L).start();
            }
        });

        this.helpSymbol.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                ViewAnimator.animate(paramAnonymousView).rubber().duration(1000L).start();
            }
        });

        this.helpText1 = (TextView)findViewById(R.id.help_text_1);
        this.helpText2 = (TextView)findViewById(R.id.help_text_2);
        this.helpText3 = (TextView)findViewById(R.id.help_text_3);
        this.helpText4 = (TextView)findViewById(R.id.help_text_4);

        this.helpText1.setText(("저희는 독일의 아름다운 도시 Osnabrück에서 교환학생으로 공부했습니다. 비록 몇 개월 되지 않는 짧은 시간이었지만, Osnabrück에서 보낸 시간은 눈부시게 아름답고 행복한 시간이었습니다." +
                "\n모든 것이 획일화되고 경쟁적인 한국 사회를 벗어나 다른 세상을 경험해본 것은 삶에 중요한 터닝포인트가 되었습니다." +
                "\n저마다의 방식대로 교환학생을 보낸 친구들의 이야기를 들어보세요. 그리고 정신없는 한국에서 미처 챙기지 못했던 자신의 목소리에 귀를 기울여 보세요." +
                "\n훗날 Osnabrück를 떠올릴 때, 입가에 미소가 번질 수 있는 교환학생 생활을 보내기 바랍니다.").replace(" ", "\u00A0"));

        StringBuilder participantsStringBuilder = new StringBuilder("                       ");
        for ( int i = (DataHandler.getInstance().getData().size() - 3);  i >= 0;  i-- ) {
            participantsStringBuilder.append(((DataGetterSetters)DataHandler.getInstance().getData().get(i)).getName() + "     ");
        }
        this.helpText4.setText(participantsStringBuilder.toString());
        this.helpText4.setSingleLine(true);
        this.helpText4.setSelected(true);

        if ( DataHandler.getInstance().getScreenSize() == this.SCREEN_SIZE_SMALL ) {
            ViewGroup.MarginLayoutParams helpLogoMarginLayoutParams = (ViewGroup.MarginLayoutParams)this.helpLogo.getLayoutParams();
            helpLogoMarginLayoutParams.setMargins((int)(20.0F * this.density), 0, 0, 0);
            this.helpLogo.setLayoutParams(helpLogoMarginLayoutParams);

            ViewGroup.MarginLayoutParams helpMarginLayoutParams1 = (ViewGroup.MarginLayoutParams)this.helpText1.getLayoutParams();
            helpMarginLayoutParams1.setMargins((int)(30.0F * this.density), (int)(20.0F * this.density), (int)(30.0F * this.density), 0);
            this.helpText1.setLayoutParams(helpMarginLayoutParams1);

            ViewGroup.MarginLayoutParams helpMarginLayoutParams2 = (ViewGroup.MarginLayoutParams)this.helpText2.getLayoutParams();
            helpMarginLayoutParams2.setMargins((int)(30.0F * this.density), (int)(16.0F * this.density), (int)(30.0F * this.density), 0);
            this.helpText2.setLayoutParams(helpMarginLayoutParams2);
        }

        if (DataHandler.getInstance().getScreenSize() == this.SCREEN_SIZE_LARGE) {
            this.helpText1.setTextSize(16.0F);
            this.helpText2.setTextSize(14.0F);
            this.helpText3.setTextSize(18.0F);
            this.helpText4.setTextSize(16.0F);
        }
    }
}
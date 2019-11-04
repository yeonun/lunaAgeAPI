package com.example.eventapi.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eventapi.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
        StrictMode.enableDefaults();

        TextView status1 = (TextView) getView().findViewById(R.id.result);

        boolean initem = false, inlunAge = false, insolDay = false, insolMonth = false;
        boolean insolWeek = false, insolYear = false;

        String lunAge = null, solDay = null, solMonth = null, solWeek = null, solYear = null;

        int year=0, day=0, month=0;

        //  for(day=1;day>31;day++) {
        year = 2019;
        month = 11;
        day = 01;

        String year2 = String.valueOf(year);
        String month2 = String.valueOf(month);
        String day2 = String.valueOf(day);
        if (day2.length()==1){
            day2 = "0"+day2;
        }
        Log.d("day2", day2);


            try {
                URL url = new URL("http://apis.data.go.kr/B090041/openapi/service/LunPhInfoService/getLunPhInfo?"
                        + "solYear=" + year2 + "&solMonth=" + month2 + "&solDay=" + day2
                        + "&ServiceKey=hymoYIRJxsamG6osmfNoX0WJgNOg0ND7Eyb0X5uoon1kQo6dVJYfMoqGbNIqNmUuguFXTPaZ7TP1bkvyeq6tgQ%3D%3D"
                );

                XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parserCreator.newPullParser();
                parser.setInput(url.openStream(), null);

                int parserEvent = parser.getEventType();
                System.out.println("파싱시작");

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                            if (parser.getName().equals("lunAge")) {
                                inlunAge = true;
                            }
                            if (parser.getName().equals("solDay")) {
                                insolDay = true;
                            }
                            if (parser.getName().equals("solMonth")) {
                                insolMonth = true;
                            }
                            if (parser.getName().equals("solWeek")) {
                                insolWeek = true;
                            }
                            if (parser.getName().equals("solYear")) {
                                insolYear = true;
                            }
                            if (parser.getName().equals("message")) { //message 태그를 만나면 에러 출력
                                status1.setText(status1.getText() + "에러");
                                //여기에 에러코드에 따라 다른 메세지를 출력
                            }
                            break;

                        case XmlPullParser.TEXT:
                            if (inlunAge) {
                                lunAge = parser.getText();
                                inlunAge = false;
                            }
                            if (insolDay) {
                                solDay = parser.getText();
                                insolDay = false;
                            }
                            if (insolMonth) {
                                solMonth = parser.getText();
                                insolMonth = false;
                            }
                            if (insolWeek) {
                                solWeek = parser.getText();
                                insolWeek = false;
                            }
                            if (insolYear) {
                                solYear = parser.getText();
                                insolYear = false;
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("item")) {
                                status1.setText(status1.getText() + "월령 : " + lunAge + "\n 일: " + solDay + "\n 월 : " + solMonth
                                        + "\n 요일 : " + solWeek + "\n 연도 : " + solYear + "\n");
                                initem = false;
                            }
                            break;
                    }
                    parserEvent = parser.next();
                }
            } catch (Exception e) {
                status1.setText("에러 남.. 그냥 집가라");
            }
        }
  //  }
}

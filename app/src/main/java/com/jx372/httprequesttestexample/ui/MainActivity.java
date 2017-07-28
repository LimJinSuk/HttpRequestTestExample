package com.jx372.httprequesttestexample.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.GsonBuilder;
import com.jx372.httprequesttestexample.R;
import com.jx372.httprequesttestexample.domain.Guestbook;
import com.jx372.httprequesttestexample.network.JSONResult;
import com.jx372.httprequesttestexample.network.SafeAsyncTask;

import java.io.BufferedReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onFetchGuestbookClick(View view){
        new FetchGuestbookListAsycTask().execute();
    }

    /*통신 결과를 담을 Result 클래스*/
    private class JSONResultFatchGuestbookList extends JSONResult<List<Guestbook>>{
    }




    /*통신 내부 클래스  API 하나당 하나씩*/
    private class FetchGuestbookListAsycTask extends SafeAsyncTask<List<Guestbook>>{

        @Override
        public List<Guestbook> call() throws Exception {

            //#1 연결
            String url = "http://http://192.168.1.39:8088/mysite03/guestbook/api/list";
            HttpRequest request = HttpRequest.get(url);

            //"name=abcd & no=1" GET방식(default)
            //request.contentType(HttpRequest.CONTENT_TYPE_FORM);
            //"{name:abcd, no:1}" POST방식
            //request.contentType(HttpRequest.CONTENT_TYPE_JSON); //data보낼때 JSON type정의

            request.accept(HttpRequest.CONTENT_TYPE_JSON);

            request.connectTimeout(3000); //3초후 연결안되면 예외발생
            request.readTimeout(3000); //읽는데 3초안으로 아닐경우 예외


            //#2 요청
            int responseCode = request.code(); //응답코드


            //#3 응답처리
            if(responseCode != HttpURLConnection.HTTP_OK){
                //오류 처리
                throw new RuntimeException("Http Response Error : " + responseCode);
            }

            /*
            BufferedReader br = request.bufferedReader(); //라인단위로 읽어 스트링으로 뽑으려고
            String json = "";
            String line = null;

            while( (line = br.readLine() ) != null){
                json += line;
            }
            br.close();
            */


            //#3-2 GSON을 사용한 객체 생성
            Reader reader = request.bufferedReader();//GSON에게 스트림을 던져주면 알아서 뽑아냄
            JSONResultFatchGuestbookList jsonResult =
                    new GsonBuilder().
                    create().
                    fromJson(reader, JSONResultFatchGuestbookList.class);

            //#5 결과 에러 체크
            if("fail".equals(jsonResult.getResult())){
                throw new RuntimeException(jsonResult.getMessage());
            }

            return jsonResult.getData();
        }


        //call()이 돌면서 예외발생시에 모두 여기로 옴
        @Override
        protected void onException(Exception e) throws RuntimeException {

            super.onException(e);
        }


        @Override
        protected void onSuccess(List<Guestbook> list) throws Exception { //json 받아서 들어온 string s
            super.onSuccess(list);

            for(Guestbook guestbook : list){
                // / 결과 처리
                System.out.println(guestbook);
            }
        }
    }

}

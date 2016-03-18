package com.bocai.boc_tongxing.myrxjavaexample.open_android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bocai.boc_tongxing.myrxjavaexample.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rx.Observable;

/**
 * @Description https://yongjhih.gitbooks.io/feed/content/RxJava.html
 * Created by EthanCo on 2016/3/18.
 */
public class RxJavaSampleActivity extends AppCompatActivity {
    public static final String TAG = "ethanco";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_android);

        Observable.just("").subscribe(s -> Log.i(TAG, "s:" + s),
                e -> Log.e(TAG, "error:" + e.getMessage()));

        //Observable.just("").subscribe(M.S(s->{}));
    }


    /**
     * 有效解决重复的 loop 增进效能，维持同个 loop
     */
    //假设我们有一万名使用者 List<User> users，其中有五千名女性使用者。
    List<Integer> getAgeList(List<User> users) {
        List<Integer> ageList = new ArrayList<>();

        for (User user : users) {
            ageList.add(user.getAge());
        }
        return ageList;
    }

    Observable<Integer> getAgeObs(List<User> users) {
        return Observable.from(users).map(user -> user.getAge());
    }

    List<Integer> getAgeListByRx(List<User> users) {
        return getAgeObs(users).toList().toBlocking().single();
    }

    //列出女性使用者
    List<User> getFemaleList(List<User> users) {
        List<User> femaleList = users;
        Iterator<User> it = users.iterator();
        while (it.hasNext()) {
            User u = it.next();
            if (u.getGender() != User.FEMALE)
                it.remove();
        }

        return femaleList;
    }

    Observable<User> getFemaleObs(List<User> users) {
        return Observable.from(users).filter(user -> user.getGender() == User.FEMALE);
    }

    List<User> getFemaleListByRx(List<User> users) {
        return getFemaleObs(users).toList().toBlocking().single();
    }

    //组合一下就可以列出女性使用者年龄
    List<Integer> getFemaleAgeList(List<User> users) {
        return getAgeList(getFemaleListByRx(users));
    }

    /*发现原本的写法有个瑕疵，就是会先绕完一万使用者，找出来五千名女性后，为了询问年纪，只好再绕一次这五千名女性。
    * 可以第一次找出女性使用者时，就顺便问一下年纪吗？
    * 为了避免重复回圈，你可改变写法，以沿用 loop */
    Observable<Integer> getFemaleAgeObs(List<User> users) {
        return Observable.from(users)
                .filter(user -> user.getGender() == User.FEMALE)
                .map(user1 -> user1.getAge());
    }

    List<Integer> getFemaleAgeListByRx(List<User> users) {
        return getFemaleAgeObs(users).toList().toBlocking().single();
    }

    /**
     * 提前打断回圈的能力，避免不必要的过滤与转换
     */
    //列出Top10女性使用者
    List<User> getFemaleList(List<User> users, int limit) {
        return getFemaleList(users).subList(0, limit); //会绕完万名使用者，找出所有女性使用者，再分割前十名
    }

    Observable<User> getFemaleListByRx(List<User> users, int limit) {
        return getFemaleObs(users).take(limit); //Observable 会聪明地搜集到第十名女性使用者就马上停止
    }

    //列出Top10女性使用者年龄
    List<Integer> getFemaleAgeList(List<User> users, int limit) {
        return Observable.from(users)
                .filter(user -> user.getGender() == User.FEMALE)
                .take(limit)
                .map(user -> user.getAge())
                .toList().toBlocking().single(); // 如果你坚持一定要传递 List
    }

    List<Integer> getFemaleAgeList2(List<User> users, int limit) {
        return getFemaleAgeObs(getFemaleObs(Observable.from(users)))
                .take(10)
                .toList().toBlocking().single();
    }

    Observable<User> getFemaleObs(Observable<User> userObs) {
        return userObs.filter(user -> user.getGender() == User.FEMALE);
    }

    Observable<Integer> getFemaleAgeObs(Observable<User> userObs) {
        return userObs.map(user -> user.getAge());
    }

    /**拉平 callback 增加易读性*/
    /*void login(Activity activity, LoginListenr loginListener) {
        loginFacebook(activity, fbUser -> {
            getFbProfile(fbUser, fbProfile -> {
                loginParse(fbProfile, parseUser -> {
                    getParseProfile(fbProfile, parseProfile -> {
                        loginListener.onLogin(parseProfile);
                    });
                });
            });
        });
    }

    void login(Activity activity, LoginListener loginListener) {
        Observable.just(activity)
                .flatMap(activity -> loginFacebook(activity))
                .flatMap(fbUser -> getFbProfile(fbUser))
                .flatMap(fbProfile -> loginParse(fbProfile))
                .flatMap(parseUser -> getParseProfile(parseUser))
                .subscribe(parseProfile -> loginListener.onLogin(parseProfile));
    }

    Observable<FbUser> loginFacebook(Activity activity) {
        return Observable.create(sub -> {
            loginFacebook(activity, fbUser -> {
                sub.onNext(fbUser);
                sub.onCompleted();
            });
        });
    }*/

    /**
     * 如何导入套用与改变撰写
     */
    //将原本的函式改成 Observable
    File download(String url) {
        File file = new File(url);
        // ...
        return file;
    }

    Observable<File> downLoadObs(String url) {
        return Observable.defer(() -> Observable.just(download(url)));
    }

    //将原来的callback改为Observable
    /*Observable<User> loginWithFaceBook(Activity activity){
        return Observable.create(sub -> {
            login("username","password",new LogInCallback() {
                @Override
                public void done(final User user, ParseException err) {
                    if (err != null) {
                        sub.onError(err);
                    } else {
                        sub.onNext(user);
                        sub.onCompleted();
                    }
                }
            });
        })
    }*/

    /**如何使用 在我们看过一些对照组之后，大致上瞭解未来在使用上会呈现什么样貌。*/

}

class User {
    public static final int FEMALE = 1;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private int age;

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    private int gender;
}

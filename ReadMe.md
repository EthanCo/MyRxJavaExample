# RxJava #
## 创建操作符 ##
### Just ###
将一个或多个对象转换成发射这个或这些对象的一个Observable
### From ###
将一个Iterable, 一个Future, 或者一个数组转换成一个Observable

### Repeat ###
创建一个重复发射指定数据或数据序列的Observable (将一个Observable对象重复发射，可指定其发射的次数)  


> [默认运行在新线程]
	
	Observable.just("data").repeat(5)

### repeatWhen ###
创建一个重复发射指定数据或数据序列的Observable，它依赖于另一个Observable发射的数据  
[对RxJava中.repeatWhen()和.retryWhen()操作符的思考](http://www.qingpingshan.com/rjbc/java/49285.html)
> [默认运行在新线程]

#### 使用.repeatWhen() + .interval()定期轮询数据

 	Observable.just("data").repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
        @Override
        public Observable<?> call(Observable<? extends Void> observable) {
            return Observable.interval(5, TimeUnit.SECONDS);
        }
    }).subscribe(s -> print(s));  

### Create ###
使用一个函数从头创建一个Observable

### defer ###
只有当订阅者订阅才创建Observable；为每个订阅创建一个新的Observable  


> just操作符是在创建Observable就进行了赋值操作，而defer是在订阅者订阅时才创建Observable，此时才进行真正的赋值操作  

### Range ###
创建一个发射指定范围的整数序列的Observable  
从start开始执行count个

### interval ###
创建一个按照给定的时间间隔发射整数序列的Observable  

### timer ###
创建一个在给定的延时之后发射单个数据的Observable

### Empty/Never/Throw
这三个操作符生成的Observable行为非常特殊和受限。测试的时候很有用，有时候也用于结合其它的Observables，或者作为其它需要Observable的操作符的参数。  

Empty  
创建一个不发射任何数据但是正常终止的Observable  
Never  
创建一个不发射数据也不终止的Observable  
Throw  
创建一个不发射数据以一个错误终止的Observable  

## 组合操作 ##
### Merge ###
将两个Observable合并成一个Observable  

	Observable<Integer> odds = Observable.just(1, 3, 5).subscribeOn(someScheduler);
	Observable<Integer> evens = Observable.just(2, 4, 6);

	Observable.merge(odds, evens)
          .subscribe(new Subscriber<Integer>() {
        @Override
        public void onNext(Integer item) {
            System.out.println("Next: " + item);
        }

        @Override
        public void onError(Throwable error) {
            System.err.println("Error: " + error.getMessage());
        }

        @Override
        public void onCompleted() {
            System.out.println("Sequence complete.");
        }
    });

输出  
	
	Next: 1  
	Next: 3  
	Next: 5  
	Next: 2  
	Next: 4  
	Next: 6  

Sequence complete.

### zip ###
通过一个函数将多个Observables的发射物结合到一起，基于这个函数的结果为每个结合体发射单个数据项。  

    List<String> list = new ArrayList<>();
    list.add("item1");
    list.add("item2");
    list.add("item3");

    Observable<String> o1 = Observable.just("h1", "h2");
    Observable<String> o2 = Observable.from(list);
    Observable.zip(o1, o2, new Func2<String, String, String>() {
        @Override
        public String call(String s, String s2) {
            return s + s2; //合并s和s2
        }
    }).subscribe(s -> print(s));

> 输出结果  
> h1item1  
> h2item2

### ZipWith ###
zipWith操作符总是接受两个参数，第一个参数是一个Observable或者一个Iterable。  

	List<String> list = new ArrayList<>();
    list.add("item1");
    list.add("item2");
    list.add("item3");

	Observable<String> o2 = Observable.from(list);
	Observable.just("a1", "a2").zipWith(o2, new Func2<String, String, String>() {
        @Override
        public String call(String s, String s2) {
            return s + s2; //合并s和s2
        }
    }).subscribe(s -> print(s));

> 输出结果  
> h1item1  
> h2item2 
//
//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.LifecycleEventObserver
//import androidx.lifecycle.LifecycleOwner
//import io.reactivex.*
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.schedulers.Schedulers
//
///**
// * Simplify the setup of the scheduler on Android
// *
// * Example:
// * Observable.just("a").withSchedulers()
// *           .subscribe {  }
// *
// * Equivalent to
// *
// * Observable.just("a")
// *           .subscribeOn(Schedulers.io())
// *           .observeOn(AndroidSchedulers.mainThread())
// *           .subscribe {  }
// */
//inline fun <T> Single<T>.withSchedulers(
//    subscribeScheduler: Scheduler = Schedulers.io(),
//    observerScheduler: Scheduler = AndroidSchedulers.mainThread()
//): Single<T> = subscribeOn(subscribeScheduler).observeOn(observerScheduler)
//
//inline fun <T> Observable<T>.withSchedulers(
//    subscribeScheduler: Scheduler = Schedulers.io(),
//    observerScheduler: Scheduler = AndroidSchedulers.mainThread()
//): Observable<T> = subscribeOn(subscribeScheduler).observeOn(observerScheduler)
//
//inline fun Completable.withSchedulers(
//    subscribeScheduler: Scheduler = Schedulers.io(),
//    observerScheduler: Scheduler = AndroidSchedulers.mainThread()
//): Completable = subscribeOn(subscribeScheduler).observeOn(observerScheduler)
//
///**
// * Unsubscribe during the specified life cycle of Lifecycle Owner
// *
// * @param lifecycleOwner LifecycleOwnerIml ex: Activity or Fragment
// * @param event default [Lifecycle.Event.ON_DESTROY]
// *
// * Example:
// *
// * with event:
// * Observable.just("a").withSchedulers()
// *           .auto(lifecycleOwner = activity, event = Lifecycle.Event.ON_PAUSE)
// *           .subscribe {}
// *
// * withOut event:
// * Observable.just("a").withSchedulers()
// *           .auto(fragment)
// *           .subscribe {}
// */
//fun <T> Observable<T>.auto(
//    lifecycleOwner: LifecycleOwner,
//    event: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
//    onDispose: () -> Unit = {}
//): Observable<T> = doOnLifecycle({ dispose ->
//    lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
//        override fun onStateChanged(source: LifecycleOwner, actualEvent: Lifecycle.Event) {
//            if (event == actualEvent && !dispose.isDisposed) {
//                dispose.dispose()
//                onDispose.invoke()
//            }
//        }
//    })
//}, onDispose)
//
///**
// * Unsubscribe during the specified life cycle of Lifecycle Owner
// *
// * @param lifecycleOwner LifecycleOwnerIml ex: Activity or Fragment
// * @param event default [Lifecycle.Event.ON_DESTROY]
// *
// * Example:
// *
// * With event:
// * Observable.just("a").withSchedulers()
// *           .auto(lifecycleOwner = activity, event = Lifecycle.Event.ON_PAUSE)
// *           .subscribe {}
// *
// * WithOut event:
// * Observable.just("a").withSchedulers()
// *           .auto(fragment)
// *           .subscribe {}
// */
//fun <T> Flowable<T>.auto(
//    lifecycleOwner: LifecycleOwner,
//    event: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
//    onCancel: () -> Unit = {}
//): Flowable<T> = doOnLifecycle({
//    lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
//        override fun onStateChanged(source: LifecycleOwner, actualEvent: Lifecycle.Event) {
//            if (event == actualEvent) it.cancel()
//        }
//    })
//}, { }, onCancel)
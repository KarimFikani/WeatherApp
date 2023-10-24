package com.karimfikani.weatherapp.core

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 * This was used to communicate an event that happened on the search fragment back to activity.
 * It can be avoided by having the main weather page as a fragment and the search fragment as a
 * separate fragment (which I already have). The communication between those fragments would be
 * using setFragmentResult & setFragmentResultListener.
 * Another way of communicating that an event happened is using the plugin pattern where we subscribe
 * to search event and once it is emitted, data will get sent out to all subscribers.
 * Event bus is a simplified version of this the only downside is that it is global and might not
 * work for all cases.
 * Since I have limited time, will stick to using a global event bus.
 */
object RxBus {

    private val publisher = PublishSubject.create<Any>()

    fun publish(event: Any) {
        publisher.onNext(event)
    }

    // Listen should return an Observable and not the publisher
    // Using ofType we filter only events that match that class type
    fun <T> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)
}

class RxEvent {
    data class SearchItemClicked(val address: String)
}

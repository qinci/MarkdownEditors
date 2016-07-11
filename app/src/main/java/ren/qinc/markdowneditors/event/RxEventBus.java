/*
 * Copyright 2016. SHENQINCI(沈钦赐)<dev@qinc.me>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ren.qinc.markdowneditors.event;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * 继续RxJava的EventBus
 * from http://nerds.weddingpartyapp.com/tech/2014/12/24/implementing-an-event-bus-with-rxjava-rxbus/
 */
public class RxEventBus {
    public static final RxEventBus bus = new RxEventBus();
    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());

    public static synchronized RxEventBus getInstance() {
        return bus;
    }

    private RxEventBus() {
    }

    public void send(Object event) {
        _bus.onNext(event);
    }

    public Observable<Object> toObserverable() {
        return _bus;
    }

    public boolean hasObservers() {
        return _bus.hasObservers();
    }
}

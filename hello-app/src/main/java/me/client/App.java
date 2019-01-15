package me.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.intendia.rxgwt2.elemento.RxElemento;
import elemental2.dom.*;
import io.reactivex.Observable;
import io.reactivex.Observer;
import org.jboss.gwt.elemento.core.Elements;

import static elemental2.dom.DomGlobal.document;
import static org.jboss.gwt.elemento.core.EventType.mousemove;
import static org.jboss.gwt.elemento.core.EventType.touchmove;


public class App implements EntryPoint {

    @Override
    public void onModuleLoad() {
        HTMLElement el = Elements.div().style("width:655px;text-align:center;background-color: coral;").get();

        Elements.body().add("mouse position:").add(el);


        CheckBox showCoordinates = new CheckBox("Activate coordinates");
        showCoordinates.setValue(false);


        showCoordinates.addClickHandler(new ClickHandler() {

            Observable<RxElemento> mouseMoveCoordinates = new Observable<RxElemento>() {
                @Override
                protected void subscribeActual(Observer<? super RxElemento> observer) {
                    RxElemento.fromEvent(document, mousemove).subscribe(mouseEvent -> el.textContent = mouseEvent.clientX + "," + mouseEvent.clientY);

                }
            };

            Observable<RxElemento> touchMoveCoordinates = new Observable<RxElemento>() {
                @Override
                protected void subscribeActual(Observer<? super RxElemento> observer) {
                    RxElemento.fromEvent(document, touchmove).subscribe(touchEvent -> el.textContent = "Pressed");

                }
            };

            @Override
            public void onClick(ClickEvent event) {

                boolean checked = ((CheckBox) event.getSource()).getValue();
                if (checked) {

                    mouseMoveCoordinates = mouseMoveCoordinates.mergeWith(touchMoveCoordinates);
                    mouseMoveCoordinates.subscribe();

                } else {
                    RxElemento.fromEvent(document, mousemove).subscribe(ev -> el.textContent = "");
                }
            }
        });
        RootPanel.get().add(showCoordinates);
    }

}
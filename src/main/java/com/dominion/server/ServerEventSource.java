package com.dominion.server;

import java.io.IOException;
import org.eclipse.jetty.servlets.EventSource;

public class ServerEventSource implements EventSource {

    private Emitter emitter;

    @Override
    public void onClose() {
        System.out.println("close");
    }

    @Override
    public void onOpen(Emitter emitter) throws IOException {
        this.emitter = emitter;
    }

    public void emitEvent(String eventType, String eventInfo) {
        try {
            System.out.println(eventType + " " + eventInfo);
            this.emitter.event(eventType, eventInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

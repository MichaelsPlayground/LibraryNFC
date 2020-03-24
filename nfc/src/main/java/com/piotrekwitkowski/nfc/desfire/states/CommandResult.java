package com.piotrekwitkowski.nfc.desfire.states;

import com.piotrekwitkowski.nfc.Response;

public class CommandResult {
    private State state;
    private Response response;

    public CommandResult(State state, byte responseCode) {
        this.state = state;
        this.response = new Response(responseCode);
    }

    public CommandResult(State state, byte[] responseBytes) {
        this.state = state;
        this.response = new Response(responseBytes);
    }

    public State getState() {
        return state;
    }

    public Response getResponse() {
        return response;
    }
}

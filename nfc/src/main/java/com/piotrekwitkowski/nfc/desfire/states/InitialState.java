package com.piotrekwitkowski.nfc.desfire.states;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.desfire.aids.AID;
import com.piotrekwitkowski.nfc.desfire.aids.AIDWrongLengthException;
import com.piotrekwitkowski.nfc.desfire.applications.Application;
import com.piotrekwitkowski.nfc.desfire.applications.ApplicationNotFoundException;
import com.piotrekwitkowski.nfc.desfire.applications.Applications;
import com.piotrekwitkowski.nfc.desfire.Command;
import com.piotrekwitkowski.nfc.desfire.Commands;
import com.piotrekwitkowski.nfc.desfire.DESFireException;
import com.piotrekwitkowski.nfc.desfire.ResponseCodes;

public class InitialState extends State {
    private static final String TAG = "InitialState";
    private static Applications applications = new Applications();

    public CommandResult processCommand(Command command) throws DESFireException {
        Log.i(TAG, "processCommand()");

        if (command.getCode() == Commands.SELECT_APPLICATION) {
            return selectApplication(command.getData());
        } else {
            throw new DESFireException("Command not supported in this state.");
        }
    }

    private CommandResult selectApplication(byte[] aid) {
        Log.i(TAG, "selectApplication()");

        try {
            AID aidToSelect = new AID(aid);
            Application application = applications.get(aidToSelect);
            return new CommandResult(new ApplicationSelectedState(application), ResponseCodes.SUCCESS);
        } catch (AIDWrongLengthException ex) {
            return new CommandResult(this, ResponseCodes.LENGTH_ERROR);
        } catch (ApplicationNotFoundException ex) {
            return new CommandResult(this, ResponseCodes.APPLICATION_NOT_FOUND);
        }
    }

}

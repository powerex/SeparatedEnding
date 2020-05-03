package ua.azbest.machine;

import ua.azbest.comunication.Message;
import ua.azbest.comunication.Token;

public interface MachineState {

    void receiveMessage(Message message);
    void sendMessage(Message message) throws UnreachableStateException;
    void receiveToken(Token token);
    void sendToken() throws UnreachableStateException;

}

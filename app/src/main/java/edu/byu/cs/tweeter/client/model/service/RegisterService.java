package edu.byu.cs.tweeter.client.model.service;

import android.widget.EditText;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.RegisterHandler;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterService {

    public interface RegisterObserver {

        void displayRegistered(User registeredUser);

        void displayMessage(String message);
    }


    public void requestRegister(EditText firstName, EditText lastName, EditText alias, EditText password, String imageBytesBase64, RegisterObserver observer) {
        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName.getText().toString(), lastName.getText().toString(),
                alias.getText().toString(), password.getText().toString(), imageBytesBase64, new RegisterHandler(observer));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(registerTask);
    }

}

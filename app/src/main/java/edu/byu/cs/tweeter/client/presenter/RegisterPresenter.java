package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.model.service.RegisterService;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter {

    private View view;
    private RegisterService registerService;

    public void attemptRegister(ImageView imageToUpload, EditText firstName, EditText lastName, EditText alias, EditText password) {
        try {

            validateRegistration(imageToUpload, firstName, lastName, alias, password);

            view.displayRegistering();

            // Convert image to byte array.
            Bitmap image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] imageBytes = bos.toByteArray();

            // Intentionally, Use the java Base64 encoder so it is compatible with M4.
            String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);

            registerService.requestRegister(firstName, lastName, alias, password, imageBytesBase64, new RegisterObserver());

        } catch (Exception e) {
            view.setErrorText(e.getMessage());

        }
    }

    public void validateRegistration(ImageView imageToUpload, EditText firstName, EditText lastName, EditText alias, EditText password) {


        if (firstName.getText().length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.getText().length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (alias.getText().length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.getText().charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.getText().length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.getText().length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (imageToUpload.getDrawable() == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }

//    private UserService userService;

    public interface View {

        void displayRegistering();

        void setErrorText(String message);

        void displayRegistered(User registeredUser);

        void displayMessage(String message);
    }

    public RegisterPresenter(View view)
    {
        this.view = view;

        registerService = new RegisterService();
    }

    public class RegisterObserver implements RegisterService.RegisterObserver {


        @Override
        public void displayRegistered(User registeredUser) {
            view.displayRegistered(registeredUser);

        }

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }
    }
}

package com.example.stackoverphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    private EditText emailEditText, nickEditText, passwordEditText, password2EditText;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.azul));
        }

        signUpButton = findViewById(R.id.signUpButton);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nickEditText = findViewById(R.id.nickEditText);
        password2EditText = findViewById(R.id.passwor2dEditText);



        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (password2EditText.getText().toString().isEmpty() || passwordEditText.getText().toString().isEmpty()
                        || emailEditText.getText().toString().isEmpty() || nickEditText.getText().toString().isEmpty()){
                    Toast.makeText(Register.this, "Complete todos los datos", Toast.LENGTH_LONG).show();
                }else if(!password2EditText.getText().toString().equals(passwordEditText.getText().toString())) {
                    passwordEditText.setError("las contraseñas no coinciden ");
                    passwordEditText.requestFocus();
                }else{
                    FirebaseAuth.getInstance()
                            .createUserWithEmailAndPassword(emailEditText.getText().toString(),passwordEditText.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        //añadir usuario a la bbdd

                                        Usuarios user = new Usuarios(emailEditText.getText().toString(), nickEditText.getText().toString(),
                                                0,0,0);

                                        System.out.println("se crea usuario");
                                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                                        DatabaseReference dbr = db.getReference(Usuarios.class.getSimpleName());
                                        dbr.push().setValue(user).addOnCompleteListener(suc ->
                                        {
                                            System.out.println("bien");
                                        }).addOnFailureListener(er ->
                                        {
                                            System.out.println(er.getMessage());
                                            System.out.println("nop");
                                        });

                                        Intent intent = new Intent(Register.this, Login.class);
                                        startActivity(intent);
                                    }else{
                                        String error =((FirebaseAuthException) task.getException()).getErrorCode();
                                        dameToastdeerror(error);
                                        System.out.println(error);
                                        }

                                    }

                            });

                }

            }
        });
    }


    // este metodo gestiona todos los posibles errores a la hora de autenticarse con Firebase
    // se trata el error y se devuelve un mensaje personalizado
    //https://www.youtube.com/watch?v=aVyKywyFOzM
    private void dameToastdeerror(String error) {

        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(Register.this, "El formato del token personalizado es incorrecto. Por favor revise la documentación", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(Register.this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(Register.this, "La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(Register.this, "La dirección de correo electrónico está mal formada.", Toast.LENGTH_LONG).show();
                emailEditText.setError("La dirección de correo electrónico está mal formada.");
                emailEditText.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(Register.this, "La contraseña no es válida o el usuario no tiene contraseña.", Toast.LENGTH_LONG).show();
                passwordEditText.setError("la contraseña es incorrecta ");
                passwordEditText.requestFocus();
                passwordEditText.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(Register.this, "Las credenciales proporcionadas no corresponden al usuario que inició sesión anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(Register.this,"Esta operación es sensible y requiere autenticación reciente. Inicie sesión nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(Register.this, "Ya existe una cuenta con la misma dirección de correo electrónico pero diferentes credenciales de inicio de sesión. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(Register.this, "La dirección de correo electrónico ya está siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                emailEditText.setError("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                emailEditText.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(Register.this, "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(Register.this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(Register.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(Register.this, "No hay ningún registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(Register.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(Register.this, "Esta operación no está permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(Register.this, "La contraseña proporcionada no es válida..", Toast.LENGTH_LONG).show();
                passwordEditText.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                passwordEditText.requestFocus();
                break;

        }

    }
}
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { UserLoginRequest } from 'src/app/models/user/userLoginRequest/user-login-request';
import { LoginService } from 'src/app/services/login/login.service';

@Component({
  selector: 'app-pantalla-login',
  templateUrl: './pantalla-login.component.html',
  styleUrls: ['./pantalla-login.component.css']
})
export class PantallaLoginComponent implements OnInit {

  userLoginForm!: FormGroup;
  formClass!: UserLoginRequest;
  loginSubscription: Subscription | undefined;

  constructor(private readonly fb: FormBuilder
    , private loginService: LoginService
    , private router: Router){}

    formValidation = this.fb.group({
      nombreUsuario: ['', [Validators.required]],
      contrasenya: ['', [Validators.required]]
    })

    ngOnInit(): void {
      this.userLoginForm = this.formValidation;
    }

    onSubmit(): void {
      this.formClass = this.userLoginForm.value;
      this.loginSubscription = this.loginService.userLogin(this.formClass).subscribe({
        next: (response) => {
          if (response == "login_succesful") {
            console.log('Login successful', response);
            sessionStorage.setItem("nombreUsuario", this.userLoginForm.value.nombreUsuario);
            this.loginService.setNombreUsuario(this.userLoginForm.value.nombreUsuario).subscribe(
              (response) => {
                console.log('Response:', response);
              },
              (error) => {
                console.error('Error:', error);
              }
            );
            this.router.navigate(['/archivos']);
          } else if (response == "password_incorrect") {
            alert("Error en login: " + response);
          } else if (response == "user_does_not_exist") {
            alert("Error en login: " + response);
          }
        },
        error: (err) => {
          console.error('Login error', err);
          alert("Error en login");
          this.router.navigate(['/home']);
        }
      }
    );
    }
}

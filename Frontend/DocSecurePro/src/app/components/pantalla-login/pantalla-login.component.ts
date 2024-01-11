import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { UserLoginRequest } from 'src/app/models/user/userLoginRequest/user-login-request';
import { LoginService } from 'src/app/services/login/login.service';

@Component({
  selector: 'app-pantalla-login',
  templateUrl: './pantalla-login.component.html',
  styleUrls: ['./pantalla-login.component.css']
})
export class PantallaLoginComponent implements OnInit{

  userLoginForm!: FormGroup;
  formClass!: UserLoginRequest;
  loginSubscription: Subscription | undefined;

  constructor(private readonly fb: FormBuilder
    , private loginService: LoginService
    ){}

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
          console.log('Login successful', response);
        },
        error: (err) => {
          console.error('Login error', err);
          // Handle the error accordingly
        }
      }
    );
    }
}

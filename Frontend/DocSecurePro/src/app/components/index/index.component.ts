import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { UserRegisterRequest } from 'src/app/models/user/user-register-request';
import { LoginService } from 'src/app/services/login/login.service';


@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})

export class IndexComponent {
  userRegisterForm!: FormGroup;
  formClass!: UserRegisterRequest;
  registerSubscription: Subscription | undefined;

  cargos = ['Seleccione un rol de usuario:', 'SuperAdmin', 'Directivo', 'Representante']

  constructor(private readonly fb: FormBuilder
    , private loginService: LoginService
    , private router: Router) {}

    formValidation = this.fb.group({
      nombreUsuario: ['', [Validators.required]],
      contrasenya: ['', [Validators.required]],
      correo: ['', [Validators.required]],
      cargo: ['', [Validators.required]]
    })

    ngOnInit(): void {
      this.userRegisterForm = this.formValidation;
    }

    onSubmit(): void {
      this.formClass = this.userRegisterForm.value;
      this.registerSubscription = this.loginService.userRegister(this.formClass).subscribe({
        next: (response) => {
          if (response == "user_creation_succesful") {
            console.log('Insert successful', response);
            this.router.navigate(['/archivos']);
          } else if (response == "user_creation_unsuccesful") {
            alert("Error en registro: " + response);
            this.router.navigate(['/home']);
          } else if (response == "user_does_not_exist") {
            alert("Error en registro: " + response);
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

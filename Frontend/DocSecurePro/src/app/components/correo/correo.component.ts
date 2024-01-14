import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { MailRequest } from 'src/app/models/mail-request';
import { LoginService } from 'src/app/services/login/login.service';

@Component({
  selector: 'app-correo',
  templateUrl: './correo.component.html',
  styleUrls: ['./correo.component.css']
})
export class CorreoComponent implements OnInit {

  mailSubscription: Subscription | undefined;
  mailForm!: FormGroup;
  formClass!: MailRequest;

  constructor(private readonly fb: FormBuilder
    , private loginService: LoginService
    , private router: Router){}

  formValidation = this.fb.group({
    recipient: ['', [Validators.required]],
    subject: ['', [Validators.required]],
    message: ['', [Validators.required]]
  });

  ngOnInit(): void {
    this.mailSubscription = this.loginService.checkSession().subscribe({
      next: (response) => {
        if (response === 'user_logged') {
          console.log('Usuario autenticado');
        } else {
          // Usuario no está autenticado
          console.log('Usuario no autenticado');
          this.router.navigate(['/home']);
        }
      },
      error: (err) => {
        console.error('Error al verificar la sesión', err);
        this.router.navigate(['/home']);
      }
    });
    this.mailForm = this.formValidation;
  }

  onSubmit(): void {
    this.formClass = this.mailForm.value;
      this.mailSubscription = this.loginService.sendMail(this.formClass).subscribe({
        next: (response) => {
          if (response == "mail_sent_succesfully") {
            console.log('Email sent succesfully.', response);
            this.router.navigate(['/archivos']);
          } else {
            console.error('Error sending mail', response);
            alert('Error sending mail');
            this.router.navigate(['/archivos']);
          }
        },
        error: (err) => {
          console.error('Error sending mail', err);
          alert('Error sending mail');
          this.router.navigate(['/archivos']);
        }
      }
    );
  }

}

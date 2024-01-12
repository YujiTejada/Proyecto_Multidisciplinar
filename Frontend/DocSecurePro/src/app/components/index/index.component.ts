import { Component } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})

export class IndexComponent {
  emailFormControl = new FormControl('', [Validators.required, Validators.email]);
  nombreFormControl = new FormControl('', Validators.required);
  apellidosFormControl = new FormControl('', Validators.required);
  passwordFormControl = new FormControl('', Validators.required);
}
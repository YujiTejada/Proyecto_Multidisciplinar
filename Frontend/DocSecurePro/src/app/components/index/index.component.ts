import { Component } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslateConfigService } from 'src/app/services/translate/translate-config.service';
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

  constructor(public router: Router,
    ){}


}
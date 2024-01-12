import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateConfigService } from 'src/app/services/translate/translate-config.service';


@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent {
  constructor (public router: Router,
    private translateConfigService: TranslateConfigService){}


  changeLenguage(leng: any){
    this.translateConfigService.changeLanguaje(leng);
  }


}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { enviroment } from 'src/app/env/enviroment';
import { Observable } from 'rxjs';
import { UserLoginRequest } from 'src/app/models/user/userLoginRequest/user-login-request';
import { UserRegisterRequest } from 'src/app/models/user/user-register-request';
import { MailRequest } from 'src/app/models/mail-request';


@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient) { }

  urlApi = enviroment.baseURL;

  public userLogin(userLoginRequest: UserLoginRequest): Observable<any> {
    const loginUrl = `${this.urlApi}/users/login`;
    return this.http.post(loginUrl, userLoginRequest, {withCredentials: true, responseType: 'text'});
  }

  public userRegister(UserRegisterRequest: UserRegisterRequest): Observable<any> {
    const loginUrl = `${this.urlApi}/users/insert`;
    return this.http.post(loginUrl, UserRegisterRequest, {withCredentials: true, responseType: 'text'});
  }

  public checkSession(): Observable<any>{
    const loginUrl = `${this.urlApi}/users/checkSession`;
    return this.http.get(loginUrl, {withCredentials: true, responseType: 'text'});
  }

  public sendMail(mailRequest: MailRequest): Observable<any> {
    const loginUrl = `${this.urlApi}/mail/sendMail`;
    return this.http.post(loginUrl, mailRequest, {withCredentials: true, responseType: 'text'});
  }

  public setNombreUsuario(nombreUsuario: string): Observable<any> {
    const loginUrl = `http://localhost:8080/upload-user/${nombreUsuario}`;
    console.log(loginUrl);
    return this.http.post(loginUrl, {})
  }

}

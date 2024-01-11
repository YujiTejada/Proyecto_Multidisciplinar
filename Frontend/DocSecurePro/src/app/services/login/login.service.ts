import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { enviroment } from 'src/app/env/enviroment';
import { Observable } from 'rxjs';
import { UserLoginRequest } from 'src/app/models/user/userLoginRequest/user-login-request';


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

  public checkSession(): Observable<any>{
    const loginUrl = `${this.urlApi}/users/checkSession`;
    return this.http.get(loginUrl, {withCredentials: true, responseType: 'text'});
  }
}
